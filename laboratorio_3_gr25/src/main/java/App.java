import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.api.java.JavaRDD;

import feed.Article;
import feed.FeedParser;
import namedEntities.Classifier;
import namedEntities.Statistics;
import namedEntities.entityClasses.NamedEntity;
import namedEntities.heuristics.Heuristics;
import utils.Config;
import utils.FeedsData;
import utils.JSONParser;
import utils.UserInterface;

public class App {
    public static void main(String[] args) {
        List<FeedsData> feedsDataArray = new ArrayList<FeedsData>();
        try {
            feedsDataArray = JSONParser.parseJsonFeedsData("src/main/java/data/feeds.json");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        try {
            UserInterface ui = new UserInterface();
            Config config = ui.handleInput(args);
            run(config, feedsDataArray);
        } catch (Exception e) {
            e.printStackTrace();
            UserInterface.printHelp(feedsDataArray);
            System.exit(1);
        }
    }

    private static void run(Config config, List<FeedsData> feedsDataArray) {
        if (feedsDataArray == null || feedsDataArray.size() == 0) {
            System.out.println("No feeds data found");
            return;
        }

        // An array of lists. Each element consist of one feed articles.
        List<List<Article>> allFeedArticles = null;

        if (config.getHelp()) {
            UserInterface.printHelp(feedsDataArray);
            return;
        }

        if (config.getFeedKey() != null) {
            String feedURL = FeedsData.getUrlFromLabel(feedsDataArray, config.getFeedKey());
            try {
                // Initialize the array of list of articles
                allFeedArticles = new ArrayList<List<Article>>();
                // Add the list of articles of the feed into the array
                allFeedArticles.add(FeedParser.fetchAndParse(feedURL));
            } catch (Exception e) {
                e.printStackTrace();
                UserInterface.printHelp(feedsDataArray);
                System.exit(1);
            }
        } else {
            List<String> feedURLS = new ArrayList<String>();
            feedsDataArray.forEach((data) -> feedURLS.add(data.getUrl()));
            // Fetching all feeds and filling the array
            allFeedArticles = FeedParser.fetchAll(feedURLS);
            if (allFeedArticles != null) {
                try (FileWriter file = new FileWriter("src/main/java/data/bigData.txt")) {
                    for (List<Article> feedArticles : allFeedArticles) {
                        for (Article art : feedArticles) {
                            file.write(art.getTitle() + "\n");
                            file.write(art.getDescription() + "\n");
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Failed to write to file: " + e.getMessage());
                }
            }
        }

        if (config.getHeuristic() != null) {
            String heurName = config.getHeuristic();
            // Instead of verifying the validity of the heuristic, We follow the convention
            // (as the first part of the App did) of trying to run the code an catch posible
            // exceptions (so that if something else went wrong, it will be handled).
            try {
                SparkSession spark = SparkSession
                    .builder()
                    .appName("JavaCalculateNamedEntities")
                    .master("spark://127.0.1.1:7077")
                    .getOrCreate();

                // Creates an RDD based on bigData lines (separated by '\n').
                JavaRDD<String> articlesLines = spark.read().textFile("src/main/java/data/bigData.txt").javaRDD();
                // Transforms the articles RDD to a named entities RDD.
                JavaRDD<String> allNamedEntities = articlesLines.flatMap(line -> Heuristics.runHeuristic(heurName, line).iterator());
                    // Tricking the user so that if the heuristic doesn't exist, the name does not
                    // show up
                    System.out.println("Computing named entities using " + heurName);
                    if (config.getStatsFormat() != null) {
                        Statistics stats = new Statistics();
                        Classifier classifier = new Classifier();
                        List<NamedEntity> namedEntitiesList = classifier.distributedRawClassify(allNamedEntities, spark); 
                            if(namedEntitiesList != null)  {
                                stats.getStatistics(namedEntitiesList, config.getStatsFormat());
                            }
                        }
                spark.close();
            } catch (Exception e) {
                e.printStackTrace();
                UserInterface.printHelp(feedsDataArray);
                System.exit(1);
            }
        }
        if (config.getPrintFeed()) {
            System.out.println("Printing feed(s) ");
            if (allFeedArticles != null) {
                allFeedArticles.forEach((feedArts) -> feedArts.forEach((article) -> article.print()));
            }
        }
    }
}