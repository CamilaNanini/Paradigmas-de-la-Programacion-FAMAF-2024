package utils;

import java.util.List;

public class FeedsData {
    private String label;
    private String url;
    private String type;

    public FeedsData(String label, String url, String type) {
        this.label = label;
        this.url = url;
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }

    static public String getUrlFromLabel(List<FeedsData> feedsList, String searchedLabel) {
        String foundUrl = null;

        for (int i = 0; i < feedsList.size(); i++) {
            FeedsData elem = feedsList.get(i);

            if (elem.label.equals(searchedLabel)) {
                foundUrl = feedsList.get(i).getUrl();
            }
        }

        return foundUrl;
    }

    public void print() {
        System.out.println("Feed: " + label);
        System.out.println("URL: " + url);
        System.out.println("Type: " + type);
    }
}