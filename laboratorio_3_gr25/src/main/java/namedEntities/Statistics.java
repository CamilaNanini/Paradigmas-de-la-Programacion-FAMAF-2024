package namedEntities;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import namedEntities.entityClasses.NamedEntity;

class InvalidStatFormatExeption extends Exception {
    public InvalidStatFormatExeption(String errorMessage) {
        super(errorMessage);
    }
}

public class Statistics {

    private HashMap<String, HashMap<String, Integer>> groupEntities (List<NamedEntity> entitiesList, String cat_or_topic) {
        // Recorrer la lista de entidades nombradas dada, y agregar por categoría al hash manteninendo la cuenta.
        // Devuelve un hash con claves categoría, y como valor una lista que tiene la entidad que pertenece a la categoría y cuántas veces aparece la entidad.
        HashMap<String, HashMap<String, Integer>> result = new HashMap<>();

        for (NamedEntity entity : entitiesList) {
            String entityCategory = entity.getCategory();
            String entityName = entity.getName();
            // Si el objeto pertenece a una categoría ya agregada, tengo que checkear si ya está el objeto y si está sumar 1, si no agregarlo
            if(cat_or_topic.equals("cat")){
                if(result.containsKey(entityCategory)){

                    HashMap<String, Integer> categoryHash = (HashMap<String, Integer>) result.get(entityCategory);
                    if (categoryHash.containsKey(entityName)){ // Si ya estaba la entidad en la categorìa, solo sumo una aparición
                        categoryHash.put(entityName, categoryHash.get(entityName) + 1);
                    }
                    else{ // Si la entidad aparece por primera vez la categoría no lo tendrá en su diccionario
                        categoryHash.put(entityName, 1);
                    }

                // Esto en realidad nuca sucede porque si una entidad tiene una categoría nueva, tampoco existe la categoria en el hash grande
                // 'Postear' el hash de la categoría actualizada al hash grande
                    result.put(entityCategory, categoryHash);
                }
                else {
                    HashMap<String,Integer> newFoundCategory = new HashMap<>();
                    newFoundCategory.put(entityName,1);
                    result.put(entityCategory, newFoundCategory);
                }
            }else if (cat_or_topic.equals("topic")) { //Pensada de manera similar a la anterior
                for (String entityTopic: entity.getTopics() ){
                    if(result.containsKey(entityTopic)){ 
                        HashMap<String, Integer> topicHash = (HashMap<String, Integer>) result.get(entityTopic);
                        if (topicHash.containsKey(entityName)){ 
                            topicHash.put(entityName, topicHash.get(entityName) + 1);
                        }
                        else{ 
                            topicHash.put(entityName, 1);
                        } 
                        result.put(entityTopic, topicHash);
                    }
                    else {
                        HashMap<String,Integer> newFoundTopic = new HashMap<>();
                        newFoundTopic.put(entityName,1);
                        result.put(entityTopic, newFoundTopic);
                    }
                }
            }
        }
        return result;
    }

    public void getStatistics(List<NamedEntity> namedEntitiesList, String format) throws InvalidStatFormatExeption {
        if(!format.equals("cat") && !format.equals("topic")){
            throw new InvalidStatFormatExeption("There is no implemented format called \"" + format + "\"");
        }
        else{
            String to_print = "";
            to_print += format.equals("cat") ? "CATEGORY: " : "TOPIC: ";
            HashMap<String, HashMap<String, Integer>> actualHash =groupEntities(namedEntitiesList,format);
            Set<String> categories_or_topics = actualHash.keySet();
            for (String helper : categories_or_topics){
                System.out.println(to_print + helper);
                HashMap<String,Integer> entities = actualHash.get(helper);
                Set<String> entityNames = entities.keySet();
                for(String entityName : entityNames){
                    System.out.println("    "+entityName+" ("+ entities.get(entityName) +")");
                }
            }
        }
    }
}