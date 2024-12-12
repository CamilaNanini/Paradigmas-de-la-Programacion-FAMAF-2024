package namedEntities.heuristics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GenericHeuristic {
    private String name;
    private String description;
    private HashSet<String> validKeys;

    public GenericHeuristic(String name, String description) {
        this.name = name;
        this.description = description;
        validKeys = new HashSet<String>();
        validKeys.add(name);
    }

    public List<String> extractCandidates(String text) {
        List<String> namedEntity = new ArrayList<String>();
        return namedEntity;
    }

    public void addValidKeys(List<String> validKeys) {
        this.validKeys.addAll(validKeys);
    }

    public HashSet<String> getValidKeys() {
        return new HashSet<String>(validKeys);
    }

    public Boolean isValidKey(String key) {
        return validKeys.contains(key);
    }

    public String getName() {
        return this.name;
    }    

    public String getDescription() {
        return this.description;
    }
}
