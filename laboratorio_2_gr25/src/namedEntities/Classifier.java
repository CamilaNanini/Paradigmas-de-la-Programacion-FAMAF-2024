package namedEntities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import namedEntities.entityClasses.NamedEntity;
import utils.JSONParser;
import java.text.Normalizer;

public class Classifier {
    // List of classified named entities
    // List<NamedEntity> classified;

    // Returns a raw list of classified named entities (possibly repeated), based on the dictionary
    public List<NamedEntity> rawClassify (List<String> entititesAsStrings) {
        List<NamedEntity> entityDictionary = new ArrayList<NamedEntity>();
        try {
            entityDictionary = JSONParser.parseJsonDictionaryData("src/data/dictionary.json"); // Contiene los posibles matches a las keywords encontradas
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        List<NamedEntity> classifiedEntities = new ArrayList<NamedEntity>();
        // Iterar sobre la lista de entidades como string y comparar con el diccionario

        for (String entityAsString : entititesAsStrings ){
            NamedEntity lookup = EntityInDict(entityAsString,entityDictionary);
            if (lookup != null){ //Es decir si halle una coincidencia en el diccionario la tengo en lookup
                classifiedEntities.add(lookup);
            }
            else{ //Si no existia la entidad en el diccionario
                List<String> unmatchedList = Arrays.asList("OTHER");
                NamedEntity unclassifiedEntity = new NamedEntity(entityAsString,"OTHER",unmatchedList,unmatchedList);
                classifiedEntities.add(unclassifiedEntity);
            }
        }
        return classifiedEntities;
    }

    private NamedEntity EntityInDict(String entityAsString , List<NamedEntity> entityDictionary){ // Si encuentra la entidad nombrada en el diccionario, devuelve la coincidencia
        NamedEntity match = null;
        for (NamedEntity dictDefinition : entityDictionary){
            List<String> keywords = dictDefinition.getKeywords();
            if(findMatch(entityAsString,keywords)) { //Si la entidad como string coincide con alguna de las keywords de una entidad nombrada del diccionario
                match = dictDefinition;
                break; // Una vez hallado un match no sigo buscando entre el resto de entidades del diccionario
            }
        }
        return match;
    }

    private Boolean findMatch(String entityAsString ,List <String> keywords){
        Boolean result = false;
        for (String keyword : keywords){
            String normalizedKeyword = Normalizer.normalize(keyword,Normalizer.Form.NFD).replaceAll("\\p{M}", "");
            if (entityAsString.equals(normalizedKeyword)){ // Si hay match
                result = true;
                break;
            }
        }
        return result;
    }
}
