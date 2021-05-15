package mongodb.collector;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MongodbCloudCollectorData implements Serializable {

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

    public MongodbCloudCollectorData() {
        this.user = "aluno";
        this.databaseUser = "admin";
        this.database = "sid2021";
        this.ip = "194.210.86.10";
        this.port = 27017;
        this.password = new char[]{'a', 'l', 'u', 'n', 'o'};
        this.collections = new String[]{"sensorh1", "sensorh2", "sensorl1", "sensorl2", "sensort1", "sensort2"};
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
        if(!newValue.isEmpty()){
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
            else if(setting2Change.toLowerCase(Locale.ROOT).equals("port"))  {
                try{
                    setPort(Integer.parseInt(newSingleValue));
                } catch (NumberFormatException e) {
                    System.out.println("Não foi dado um número");
                    return false;
                }
            }
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
        System.out.println("Valor vazio");
        return false;
    }

    @Override
    public String toString() {
        String spacer = "    ";
        return "MongodbCloudCollectorData{" +
                "\n  " + spacer + "  user='" + user + '\'' +
                "\n  " + spacer + "  databaseUser='" + databaseUser + '\'' +
                "\n  " + spacer + "  database='" + database + '\'' +
                "\n  " + spacer + "  ip='" + ip + '\'' +
                "\n  " + spacer + "  port=" + port +
                "\n  " + spacer + "  password=" + Arrays.toString(password) +
                "\n  " + spacer + "  collections=" + Arrays.toString(collections) +
                "\n}";
    }
}
