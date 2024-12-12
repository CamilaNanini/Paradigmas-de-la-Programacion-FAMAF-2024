package utils;

public class Config {
    private boolean printFeed = false;
    private String feedKey;
    private boolean printHelp;
    private String heuristic;
    private String statsFormat;

    public Config(boolean printFeed, String feedKey, boolean printHelp, String heuristic, String statsFormat) {
        this.printFeed = printFeed;
        this.feedKey = feedKey;
        this.printHelp = printHelp;
        this.heuristic = heuristic;
        this.statsFormat = statsFormat;
    }

    public boolean getPrintFeed() {
        return printFeed;
    }

    public String getFeedKey() {
        return feedKey;
    }

    public boolean getHelp() {
        return printHelp;
    }

    public String getHeuristic() {
        return heuristic;
    }

    public String getStatsFormat() {
        return statsFormat;
    }
}
