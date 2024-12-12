package feed;

public class Article {
    private String title;
    private String link;
    private String description;
    private String pubDate;

    public Article(String title, String link, String description, String pubDate){
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void print() {
        System.out.println("\nTitle: " + title + "\n");
        System.out.println("Description: " + (description != "" ? description : "None.") + "\n");
        System.out.println("Link: " + link + "\n");
        System.out.println("Publication Date: " + pubDate + "\n");
        System.out.println("*******************************************************************************");
    }
}