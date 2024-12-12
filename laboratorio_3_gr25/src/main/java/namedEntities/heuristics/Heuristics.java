package namedEntities.heuristics;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class InvalidHeuristicExeption extends Exception {
    public InvalidHeuristicExeption(String errorMessage) {
        super(errorMessage);
    }
}

abstract public class Heuristics {
    private static List<GenericHeuristic> heurList = new ArrayList<GenericHeuristic>(
            Arrays.asList(new SpecialWordsHeuristic(), new AcronymWordHeuristic(), new CapitalizedWordHeuristic())
    );

    private static HashMap<String, GenericHeuristic> heurMap = new HashMap<String, GenericHeuristic>() {
        {
            for (GenericHeuristic heuristic : heurList) {
                heuristic.getValidKeys().forEach((key) -> put(key, heuristic));
            }
        }
    };

    public static Boolean isValidHeuristic(String name) {
        Boolean validity = heurMap.containsKey(name);

        if (!validity) {
            for (GenericHeuristic heur : heurMap.values()) {
                validity = validity || heur.isValidKey(name);
            }
        }

        return validity;
    }

    public static void printHeurNames() {
        String description = " Generic Description";
        String name = " Generic Name";
        String spacing = "                                           ";

        for (GenericHeuristic heuristic : heurList) {
            name = heuristic.getName();
            description = heuristic.getDescription();
            printTrimmedLine(name + ": " + description, spacing, 80);
            printTrimmedLine("Synonyms: " + heuristic.getValidKeys(), spacing, 80);
            System.out.println();
        }
    }

    private static void printTrimmedLine(String text, String spacing, Integer sliceLength) {
        List<String> toPrint = new ArrayList<String>();
        Integer length = text.length();

        for (Integer i = 0; i < length; i += sliceLength) {
            toPrint.add(text.substring(i, (i + sliceLength <= length ? i + sliceLength : length)));
        }

        toPrint.forEach((line) -> System.out.println(spacing + line));
    }

    public static List<String> runHeuristic(String synonym, String text) throws InvalidHeuristicExeption {
        if (isValidHeuristic(synonym)) {
            return heurMap.get(synonym).extractCandidates(text);
        } else {
            throw new InvalidHeuristicExeption(
                "The desired heuristic \"" + synonym + "\" does not exist"
            );
        }
    }
}
