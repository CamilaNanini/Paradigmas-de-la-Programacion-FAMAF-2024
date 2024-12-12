package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import namedEntities.heuristics.Heuristics;

class InvalidInputsException extends Exception {
    public InvalidInputsException(String errorMessage) {
        super(errorMessage);
    }
}

public class UserInterface {

    private HashMap<String, String> optionDict;

    private List<Option> options;

    public UserInterface() {
        options = new ArrayList<Option>();
        options.add(new Option("-h", "--help", false));
        options.add(new Option("-f", "--feed", true));
        options.add(new Option("-ne", "--named-entity", true));
        options.add(new Option("-pf", "--print-feed", false));
        options.add(new Option("-sf", "--stats-format", true));

        optionDict = new HashMap<String, String>();
    }

    public static void printHelp(List<FeedsData> feedsDataArray) {
        System.out.println("Usage: make run ARGS=\"[OPTION]\"");
        System.out.println("Options:");
        System.out.println("  -h, --help: Show this help message and exit");
        System.out.println("  -f, --feed <feedKey>:                Fetch and process the feed with");
        System.out.println("                                       the specified key");
        System.out.println("                                       Available feed keys are: ");
        for (FeedsData feedData : feedsDataArray) {
            System.out.println("                                       " + feedData.getLabel());
        }
        System.out.println("  -ne, --named-entity <heuristicName>: Use the specified heuristic to extract");
        System.out.println("                                       named entities");
        System.out.println("                                       Available heuristic names are (<name>: <description>): ");
        Heuristics.printHeurNames();
        System.out.println("  -pf, --print-feed:                   Print the fetched feed");
        System.out.println("  -sf, --stats-format <format>:        Print the stats in the specified format");
        System.out.println("                                       Available formats are: ");
        System.out.println("                                       cat: Category-wise stats");
        System.out.println("                                       topic: Topic-wise stats");
    }

    public Config handleInput(String[] args) throws InvalidInputsException {
        try {
            Boolean optionMatch = false;
            for (Integer i = 0; i < args.length; i++) {
                optionMatch = false;
                for (Option option : options) {
                    if (option.getName().equals(args[i]) || option.getLongName().equals(args[i])) {
                        optionMatch = true;
                        if (!option.takesArg()) {
                            optionDict.put(option.getName(), null);
                        } else {
                            if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                                optionDict.put(option.getName(), args[i + 1]);
                                i++;
                            } else if (args[i].equals("-sf")) {
                                // -sf can take an argument or not (so takes default value "cat"), 
                                // according to our interpretation.
                                optionDict.put(option.getName(), null);
                            } else {
                                throw new InvalidInputsException("Invalid inputs");
                            }
                        }
                        break; // If option matches any, we dont check if the rest of Options matches
                    }
                }
                if (!optionMatch) { // If args[i] doesnt match any valid option excepts
                    throw new InvalidInputsException("Invalid inputs");
                }
            }
        } catch (InvalidInputsException e) {
            System.out.println(e.getMessage());
            throw new InvalidInputsException("Invalid inputs");
        } catch (Exception e) {
            System.out.println("UserInterface: Algo salió mal. " + e.getMessage());
            System.exit(1);
        }

        Boolean printFeed = optionDict.containsKey("-pf");
        String feedKey = optionDict.get("-f");
        Boolean getHelp = optionDict.containsKey("-h");
        String heuristic = optionDict.get("-ne");
        String statFormat = optionDict.get("-sf");

        // Default value if It is not specified
        if (statFormat == null) {
            statFormat = "cat";
        }

        if (heuristic == null) {
            printFeed = true;
        }

        return new Config(printFeed, feedKey, getHelp, heuristic, statFormat);
    }
}
