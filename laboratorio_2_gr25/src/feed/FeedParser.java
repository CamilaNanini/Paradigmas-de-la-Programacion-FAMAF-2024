package feed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

class NullUrlException extends Exception{
    public NullUrlException(String errorMessage) {
        super(errorMessage);
    }
}

public class FeedParser {
    private static Document document;
    
    public static List<Article> parseXML(String xmlData) {
        List<Article> articles = new ArrayList<>();
        InputSource xmlSource;
        
        try {
            // Creating an input source that the builder can read
            xmlSource = new InputSource(new StringReader(xmlData));

            // Instantiating the DocumentBuilderFactory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            // Creating a DocumentBuilder
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Parsing the proper document
            document = builder.parse(xmlSource);
            NodeList list = document.getElementsByTagName("item");
            
            for (int i = 0; i < list.getLength() ; i++) {
                String title = "";
                String description = "";
                String link = "";
                String pubDate = "";
                Node node = list.item(i);
                NodeList children = node.getChildNodes();

                for (int j = 0 ; j < children.getLength() ; j++) {
                    Node child = children.item(j);
                    
                    if (child.getNodeName().equals("title")) {
                        title += child.getTextContent();
                    } else if (child.getNodeName().equals("description")){
                        description += child.getTextContent();
                    } else if (child.getNodeName().equals("link")){
                        link += child.getTextContent();
                    } else if (child.getNodeName().equals("pubDate")) {
                        pubDate = child.getTextContent();
                    }
                }

                Article art = new Article(title, link, description, pubDate);
                articles.add(art);
            }
        } catch (Exception e) {
            System.err.println("An exception happend while parsing the xml:");
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        
        return articles;
    }

    public static String fetchFeed(String feedURL) throws MalformedURLException, IOException, Exception {

        @SuppressWarnings("deprecation")
        URL url = new URL(feedURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("User-agent", "Korōra 🐧");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        int status = connection.getResponseCode();
        if (status != 200) {
            throw new Exception("HTTP error code: " + status);
        } else {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            connection.disconnect();
            return content.toString();
        }

    }
    
    public static List<Article> fetchAndParse(String feedURL) throws NullUrlException {
        List<Article> feedArticles = null;
        if (feedURL == null) {
            throw new NullUrlException("Received null Url");
        }

        try {
            String xmlData = FeedParser.fetchFeed(feedURL);
            feedArticles = FeedParser.parseXML(xmlData);
        } catch (Exception e) {
            System.out.println("Something went wrong while trying to fetch the requested feed from the requested url:");
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    
        return feedArticles;
    }

    public static List<List<Article>> fetchAll(List<String> urls) {
        List<List<Article>> feedsArticles = new ArrayList<List<Article>>();

        urls.forEach((url) -> {
            try {
                feedsArticles.add(fetchAndParse(url));
            } catch (NullUrlException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        });

        return feedsArticles;
    }

}
