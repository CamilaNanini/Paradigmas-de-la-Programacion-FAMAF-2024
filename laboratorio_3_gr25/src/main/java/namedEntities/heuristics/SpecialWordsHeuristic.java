package namedEntities.heuristics;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpecialWordsHeuristic extends GenericHeuristic{

    public SpecialWordsHeuristic() {
        super("SpecialWordsHeuristic", "This heuristic is responsible for searching for named entities that it identifies as being related to certain special words.");
        List<String> validKeys = new ArrayList<>(Arrays.asList("specialwordsheuristic", "sp"));
        this.addValidKeys(validKeys);
    }

    public static boolean hasWord(String[] array, String palabra) {
        for (String elemento : array) {
            if (elemento.equals(palabra)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> extractCandidates(String text) {
        List<String> namedEntities = new ArrayList<String>();
        //La idea es tener algunas palabras claves que tengan a continuacion de ellas a una entidad nombrada
        String[] specialswords = {"presidente","presidenta", "ministro", "ministra","gobernador", "senador", 
        "diputado", "artista", "cantante", "jugador","deportista","empresario","empresa","organizacion","provincia"};
        //Debe haber muchos más casos

        //Mantengo esto al igual que la otra heuristica porque es mejor tener un texto "limpio"
        text = text.replaceAll("[-+.^:,\"]", "");
        text = Normalizer.normalize(text, Normalizer.Form.NFD); //Separar acentos de las letras
        text = text.replaceAll("\\p{M}", ""); //Quitar acentos

        String possibilities = "";
        for (String word : specialswords) {
            possibilities += "|" + word;
        }
        possibilities = possibilities.substring(1);

        Pattern pattern = Pattern.compile("(" + possibilities + "){1} ([A-Z]{1}([a-z]+)){1}( ([A-Z]{1}([a-z]+)))*");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String stringFound = matcher.group();
            String withoutFirst = stringFound.substring(stringFound.trim().indexOf(' ')+1);
            namedEntities.add(withoutFirst);
        }
        return namedEntities;
    }

}
