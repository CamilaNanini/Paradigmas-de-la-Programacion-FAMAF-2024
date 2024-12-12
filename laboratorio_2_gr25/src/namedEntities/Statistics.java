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

    private HashMap<String, HashMap<String, Integer>> ByCategory(List<NamedEntity> entitiesList) {
        // Recorrer la lista de entidades nombradas dada, y agregar por categoria al hash manteninendo la cuenta.
        // Devuelve un hash con claves categoria, y como valor una lista que tiene la entidad que pertenece a la categoria y cuantas veces aparece la entidad.
        HashMap<String, HashMap<String, Integer>> result = new HashMap<>();

        for (NamedEntity entity : entitiesList) {
            String entityCategory = entity.getCategory();
            String entityName = entity.getName();
            if(result.containsKey(entityCategory)){ // Si el objeto pertenece a una categoria ya agregada, tengo que checkear si ya esta el objeto y si esta sumar 1, si no agregarlo

                HashMap<String, Integer> categoryHash = (HashMap<String, Integer>) result.get(entityCategory);
                if (categoryHash.containsKey(entityName)){ // Si ya estaba la entidad en la categoria, solo sumo una aparicion
                    categoryHash.put(entityName, categoryHash.get(entityName) + 1);
                }
                else{ // Si la entidad aparece por primera vez la categoria no lo tendra en su diccionario
                    categoryHash.put(entityName, 1);
                } // Esto en realidad nuca sucede porque si una entidad tiene una categoria nueva, tampoco existe la categoria en el hash grande

                // 'Postear' el hash de la categoria actualizada al hash grande
                result.put(entityCategory, categoryHash);
            }
            else {
                                    //NEW FOUND POWEEEEER 
                HashMap<String,Integer> newFoundCategory = new HashMap<String, Integer>();

                newFoundCategory.put(entityName,1);

                result.put(entityCategory, newFoundCategory);
            }
        }

        return result;
    }

    private HashMap<String, HashMap<String, Integer>> ByTopic(List<NamedEntity> entitiesList) {
        // Recorrer la lista de entidades nombradas dada, y agregar por categoria al hash manteninendo la cuenta.
        // Devuelve un hash con claves topico, y como valor una lista que tiene la entidad que pertenece a la topico y cuantas veces aparece la entidad.
        HashMap<String, HashMap<String, Integer>> result = new HashMap<>();
        
        for (NamedEntity entity : entitiesList) {
            String entityName = entity.getName();
            for (String entityTopic: entity.getTopics() )
            {
                if(result.containsKey(entityTopic)){ // Si el objeto pertenece a una categoria ya agregada, tengo que checkear si ya esta el objeto y si esta sumar 1, si no agregarlo
                    
                    HashMap<String, Integer> topicHash = (HashMap<String, Integer>) result.get(entityTopic);
                    if (topicHash.containsKey(entityName)){ // Si ya estaba la entidad en la categoria, solo sumo una aparicion
                        topicHash.put(entityName, topicHash.get(entityName) + 1);
                    }
                    else{ // Si la entidad aparece por primera vez la categoria no lo tendra en su diccionario
                        topicHash.put(entityName, 1);
                    } // Esto en realidad nuca sucede porque si una entidad tiene una categoria nueva, tampoco existe la categoria en el hash grande
                    
                    // 'Postear' el hash de la categoria actualizada al hash grande
                    result.put(entityTopic, topicHash);
                }
                else {
                    //NEW FOUND POWEEEEER (PD: Sabemos que quedó este comentario)
                    HashMap<String,Integer> newFoundTopic = new HashMap<String, Integer>();
                    
                    newFoundTopic.put(entityName,1);
                    
                    result.put(entityTopic, newFoundTopic);
                }
            }
        }

        return result;
    }


    public void getStatistics(List<NamedEntity> namedEntitiesList, String format) throws InvalidStatFormatExeption {
        if (format.equals("cat")){
            HashMap<String, HashMap<String, Integer>> categoryHash= ByCategory(namedEntitiesList);
            Set<String> categories = categoryHash.keySet(); // Specify the type of the keyset
            for (String category : categories){
                System.out.println("CATEGORY: " + category);
                HashMap<String,Integer> entities = categoryHash.get(category);
                Set<String> entityNames = entities.keySet();
                for(String entityName : entityNames){
                    System.out.println("    "+entityName+" ("+ entities.get(entityName) +")");
                }
            }
        }
        else if (format.equals("topic")){
            HashMap<String, HashMap<String, Integer>> topicHash= ByTopic(namedEntitiesList);
            Set<String> topics = topicHash.keySet(); // Specify the type of the keyset
            for (String topic : topics){
                System.out.println("TOPIC: " + topic);
                HashMap<String,Integer> entities = topicHash.get(topic);
                Set<String> entityNames = entities.keySet();
                for(String entityName : entityNames){
                    System.out.println("    "+entityName+" ("+ entities.get(entityName) +")");
                }
            }
        }
        else{
            throw new InvalidStatFormatExeption("There is no implemented format called \"" + format + "\"");
        }
    }
}