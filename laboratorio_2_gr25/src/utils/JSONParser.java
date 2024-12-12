package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import namedEntities.entityClasses.*;

public class JSONParser {

    static public List<FeedsData> parseJsonFeedsData(String jsonFilePath) throws IOException {
        String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
        List<FeedsData> feedsList = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(jsonData);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String label = jsonObject.getString("label");
            String url = jsonObject.getString("url");
            String type = jsonObject.getString("type");
            feedsList.add(new FeedsData(label, url, type));
        }
        return feedsList;
    }

    static public List<NamedEntity> parseJsonDictionaryData (String jsonFilePath) throws IOException {
        String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
        List<NamedEntity> entitieslList = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(jsonData);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String label = jsonObject.getString("label");
            String category = jsonObject.getString("Category"); 
            //Parte fea
            JSONArray topicArray = jsonObject.getJSONArray("Topics");
            List<String> topics = new ArrayList<String>();
            
            // Surrounding with try to avoid JSON (related with String casting but not directly IO) posible exceptions
            try {
                for (int j = 0 ; j < topicArray.length() ; j++){
                    topics.add(topicArray.getString(j));
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
                throw new IOException();
            }

            JSONArray keywordsArray = jsonObject.getJSONArray("keywords");
            List<String> keywords = new ArrayList<String>();
            for (int j = 0 ; j < topicArray.length() ; j++){
                keywords.add(keywordsArray.getString(j));
            }

            // aca deberia estar implementada la extraccion de atributos de la entidad
            // pero es para el punto estrella , asi que por ahora todos los atributos son seteados null, 0 en caso de double pq java es lloron
            switch (category) {
                case "PERSON":
                    //Integer age = jsonObject.getInt("age");
                    entitieslList.add(new NamedPerson(label, category, topics, keywords, null, null));
                    break;
                case "LOCATION":
                    //Double latitude = jsonObject.getDouble("latitude");
                    //Double longitude = jsonObject.getDouble("longitude");
                    entitieslList.add(new NamedLocation(label, category, topics, keywords, 0, 0));
                    break;
                case "ORGANIZATION":
                    //String type = jsonObject.getString("type");
                    entitieslList.add(new NamedOrganization(label, category, topics, keywords,null));
                    break;
                case "EVENT":
                    //Date date = jsonObject.getDate("date");
                    //String location = jsonObject.getString("location");
                    entitieslList.add(new NamedEvent(label, category, topics, keywords,null, null));
                    break;
                default:
                    entitieslList.add(new NamedEntity(label, category, topics, keywords));
                    break;
            }
        }

        return entitieslList;
    }
}
