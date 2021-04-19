package config;

public  enum ConfigParams {

         USER("user"),
                DATABASEUSER ("databaseUsers"),
                DATABASE ("database"),
                IP ("ip"),
                PORT ("port"),
                PASSWORD ("password"),
                COLLECTIONS ("collections");

        private String label;


        public final String getLabel(){
            return this.label;
        }
        private ConfigParams(String label){
            this.label = label;
        }

    @Override
    public String toString() {
        return label;
    }
}
