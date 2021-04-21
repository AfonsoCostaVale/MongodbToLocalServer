package config;

public enum ConfigParams {

    USER("user"),
    DATABASEUSERS("databaseusers"),
    DATABASE("database"),
    IP("ip"),
    PORT("port"),
    PASSWORD("password"),
    COLLECTIONS("collections");

    private final String label;


    ConfigParams(String label) {
        this.label = label;
    }

    public final String getLabel() {
        return this.label;
    }

    @Override
    public String toString() {
        return label;
    }
}
