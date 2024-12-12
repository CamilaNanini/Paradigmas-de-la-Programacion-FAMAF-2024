package namedEntities.entityClasses;
import java.util.List;

public class NamedPerson extends NamedEntity {
    // Atributos de las entidades de la clase persona
    private Integer age;
    private String nationality;

    public NamedPerson(String name, String category, List<String> topics, List<String> keywords, Integer age, String nationality){
        super(name,category,topics,keywords);
        this.age = age;
        this.nationality = nationality;
    }

    public Integer getAge(){
        return age;
    }
    public String getNationality(){
        return nationality;
    }
}


