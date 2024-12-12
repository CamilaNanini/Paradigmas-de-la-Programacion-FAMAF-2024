package namedEntities.heuristics;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// La siguiente heurística debe ser:
// Las palabras que estén totalmente escritas con mayúsculas se consideran entidades nombradas
// Esto lo suponemos porque los acrónimos del estilo de U.N.C los pasamos a UNC

public class AcronymWordHeuristic extends GenericHeuristic {

    public AcronymWordHeuristic() {
        super("AcronymWordHeuristic", "This heuristic is responsible for searching for all acronyms and identifying them as named entities.");
        List<String> validKeys = new ArrayList<>(Arrays.asList("acronymwordheuristic", "ac"));
        this.addValidKeys(validKeys);
    }

    @Override
    public List<String> extractCandidates(String text) {
        List<String> namedEntities = new ArrayList<>();

        //Mantengo esto al igual que la otra heuristica porque es mejor tener un texto "limpio"
        text = text.replaceAll("[-+.^:,\"]", "");
        text = Normalizer.normalize(text, Normalizer.Form.NFD);
        text = text.replaceAll("\\p{M}", ""); 

        Pattern pattern = Pattern.compile("([A-Z]){2}[A-Z]*");

        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            namedEntities.add(matcher.group());
        }
        return namedEntities;
    }    
}