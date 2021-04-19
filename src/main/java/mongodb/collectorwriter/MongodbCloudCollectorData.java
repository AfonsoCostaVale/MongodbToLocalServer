package mongodb.collectorwriter;

import java.util.*;

public class MongodbCloudCollectorData {


    public static final MongodbCloudCollectorData DEFAULTCOLLECTORDATA = new MongodbCloudCollectorData(
            "aluno",
            "admin",
            "sid2021",
            "194.210.86.10",
            27017,
            new char[]{'a', 'l', 'u', 'n', 'o'},
            new String[]{"sensorh1", "sensorh2", "sensorl1", "sensorl2", "sensort1", "sensort2"}
    );

    private String user;
    private String databaseUser;
    private String database;
    private String ip;
    private int port;
    private char[] password;
    private String[] collections;

    public MongodbCloudCollectorData(String user,
                                     String databaseUser,
                                     String database,
                                     String ip,
                                     int port,
                                     char[] password,
                                     String[] collections) {
        this.user = user;
        this.databaseUser = databaseUser;
        this.database = database;
        this.ip = ip;
        this.port = port;
        this.password = password;
        this.collections = collections;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }

    public void setDatabaseUser(String databaseUser) {
        this.databaseUser = databaseUser;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public String[] getCollections() {
        return collections;
    }

    public void setCollections(String[] collections) {
        this.collections = collections;
    }


    //To change Config Settings
    public boolean changeSetting(String setting2Change, List<String> newValue) {
        String[] newArrayValue = newValue.toArray(new String[0]);
        String newSingleValue = "";
        for(String value: newValue) {
            newSingleValue+=value;
            newSingleValue+=" ";
        }
        newSingleValue=newSingleValue.substring(0,newSingleValue.length()-1);
        //O switch case nao ta a igualar as strings, se em caso de houver uma resolução a isso trocar de volta para o switch case
        if(setting2Change.toLowerCase(Locale.ROOT).equals("user"))  setUser(newSingleValue);
        else if(setting2Change.toLowerCase(Locale.ROOT).equals("databaseuser"))  setDatabaseUser(newSingleValue);
        else if(setting2Change.toLowerCase(Locale.ROOT).equals("database"))  setDatabase(newSingleValue);
        else if(setting2Change.toLowerCase(Locale.ROOT).equals("ip"))  setIp(newSingleValue);
        else if(setting2Change.toLowerCase(Locale.ROOT).equals("port"))  setPort(Integer.parseInt(newSingleValue));
        else if(setting2Change.toLowerCase(Locale.ROOT).equals("password")) {
            char[] passChar = newSingleValue.toCharArray();
            setPassword(passChar);
        }
        else if(setting2Change.equals("collections"))  setCollections(newArrayValue);
        else {
            System.out.println("Parametro de configuração desconhecido");
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MongodbCloudCollectorData{" +
                ", user='" + user + '\'' +
                ", databaseUser='" + databaseUser + '\'' +
                ", database='" + database + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", password=" + Arrays.toString(password) +
                ", collections=" + Arrays.toString(collections) +
                '}';
    }
}
