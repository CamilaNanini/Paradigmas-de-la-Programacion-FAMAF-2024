package namedEntities.entityClasses;
import java.util.ArrayList;
import java.util.List;

public class NamedEntity {
    private String name;
    private String category;
    private List<String> topics; // Porque una entidad está etiquetada con uno o más tópicos
    private List<String> keywords;

    public NamedEntity(String name, String category, List<String> topics, List<String> keywords) {
        this.name = name;
        this.category = category;
        this.topics = topics;
        this.keywords = keywords;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public List<String> getTopics() {
        return new ArrayList<String>(topics);
    }

    public List<String> getKeywords() {
        return new ArrayList<String>(keywords);
    }

    public Object getAttributes() {
        System.out.println("WARNING: This method is not implemented for the NamedEntity class.");
        return null;
    }

    public String printEntity() {
        return "NamedEntity{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", topics=" + topics +
                ", keywords=" + keywords +
                '}';
    }

}