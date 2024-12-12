package namedEntities.heuristics;

import java.text.Normalizer;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CapitalizedWordHeuristic extends GenericHeuristic {

    public CapitalizedWordHeuristic() {
        super("CapitalizedWordHeuristic", "This heuristic will search for words that start with a capital letter and identify them as named entities.");
        List<String> validKeys = new ArrayList<>(Arrays.asList("capitalizedwordheuristic", "cw"));
        this.addValidKeys(validKeys);
    }

    @Override
    public List<String> extractCandidates(String text) {
        List<String> candidates = new ArrayList<>();

        text = text.replaceAll("[-+.^:,\"]", "");
        text = Normalizer.normalize(text, Normalizer.Form.NFD);
        text = text.replaceAll("\\p{M}", "");

        Pattern pattern = Pattern.compile("[A-Z][a-z]+(?:\\s[A-Z][a-z]+)*");

        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            candidates.add(matcher.group());
        }
        return candidates;
    }
}
