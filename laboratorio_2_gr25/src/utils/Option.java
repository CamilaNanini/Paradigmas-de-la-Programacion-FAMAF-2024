package utils;

public class Option {
    private String name;
    private String longName;
    private Boolean takesArg;

    //Toma una flag, su nombre largo, y si toma o no un argumento
    public Option(String name, String longName, Boolean takesArg) {
        this.name = name;
        this.longName = longName;
        this.takesArg = takesArg;
    }

    public String getName() {
        return name;
    }

    public String getLongName() {
        return longName;
    }

    public Boolean takesArg() {
        return takesArg;
    }

}
