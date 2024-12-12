package namedEntities.entityClasses;

import java.sql.Date;
import java.util.List;

public class NamedEvent extends NamedEntity {
    // Atributos propios de las entidades evento
    private Date date; // Capaz sea peligroso usar esa libreria por el tema de versiones
    private String location;

    public NamedEvent(String name, String category, List<String> topics, List<String> keywords, Date date, String location) {
        super(name, category, topics, keywords);
        this.date = date;
        this.location = location;
    }

    public Date getDate() {
        return date;
    }
    public String getLocation() {
        return location;
    }
}
