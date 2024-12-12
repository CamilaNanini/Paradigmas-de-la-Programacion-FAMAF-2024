package namedEntities.entityClasses;

import java.util.List;

public class NamedOrganization extends NamedEntity{
    // Atributos propios de las organizaciones
    private String organizationType; //SA, SRL, ONG ,etc


    public NamedOrganization(String name, String category, List<String> topics, List<String> keywords, String organizationType){
        super(name, category, topics, keywords);
        this.organizationType = organizationType;
    }

    public String getOrganizationType() {
        return organizationType;
    }
}