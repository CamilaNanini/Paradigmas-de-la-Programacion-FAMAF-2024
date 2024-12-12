package namedEntities.entityClasses;
import java.util.List;

public class NamedLocation extends NamedEntity{
    // Atributos propios de las locaciones
    private double latitude;
    private double longitude;

    public NamedLocation(String name, String category, List<String> topics, List<String> keywords,double latitude, double longitude){
        super(name, category, topics, keywords);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude(){
        return latitude;
    }
    public double getLongitude(){
        return longitude;
    }
}
