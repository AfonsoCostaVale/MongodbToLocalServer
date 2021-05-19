package mongodb.collector;

import config.ConfigParams;

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
    private String clone_mode;
    private String autostart;

    public MongodbCloudCollectorData(String user,
                                     String databaseUser,
                                     String database,
                                     String ip,
                                     int port,
                                     char[] password,
                                     String[] collections,
                                     String clone_mode,
                                     String autostart
    ) {
        this.user = user;
        this.databaseUser = databaseUser;
        this.database = database;
        this.ip = ip;
        this.port = port;
        this.password = password;
        this.collections = collections;
        this.clone_mode = clone_mode;
        this.autostart = autostart;
    }

    public MongodbCloudCollectorData() {
        this.user = "aluno";
        this.databaseUser = "admin";
        this.database = "sid2021";
        this.ip = "194.210.86.10";
        this.port = 27017;
        this.password = new char[]{'a', 'l', 'u', 'n', 'o'};
        this.collections = new String[]{"sensorh1", "sensorh2", "sensorl1", "sensorl2", "sensort1", "sensort2"};
        this.clone_mode = "mqtt";
        this.autostart = "on";
    }

    public String getUser() {                           return user;                      }
    public void setUser(String user) {                  this.user = user;                 }
    public String getDatabaseUser() {                   return databaseUser;              }
    public void setDatabaseUser(String databaseUser) {  this.databaseUser = databaseUser; }
    public String getDatabase() {                       return database;                  }
    public void setDatabase(String database) {          this.database = database;         }
    public String getIp() {                             return ip;                        }
    public void setIp(String ip) {                      this.ip = ip;                     }
    public int getPort() {                              return port;                      }
    public void setPort(int port) {                     this.port = port;                 }
    public char[] getPassword() {                       return password;                  }
    public void setPassword(char[] password) {          this.password = password;         }
    public String[] getCollections() {                  return collections;               }
    public void setCollections(String[] collections) {  this.collections = collections;   }
    public String getClone_mode() {                     return clone_mode;                }
    public void setClone_mode(String clone_mode) {      this.clone_mode = clone_mode;     }
    public String getAutostart() {                      return autostart;                 }
    public void setAutostart(String autostart) {        this.autostart = autostart;       }

    /**
     * Method To change Config Settings
     */
    public boolean changeSetting(String setting2Change, List<String> newValue) {
        String[] newArrayValue = newValue.toArray(new String[0]);
        String newSingleValue = "";
        if(!newValue.isEmpty()){
            for(String value: newValue) {
                newSingleValue+=value;
                newSingleValue+=" ";
            }
            newSingleValue = newSingleValue.substring(0,newSingleValue.length()-1);

            //O switch case nao ta a igualar as strings, se em caso de houver uma resolução a isso trocar de volta para o switch case

            setting2Change = setting2Change.toLowerCase(Locale.ROOT);
            if(setting2Change.equals(ConfigParams.USER.getLabel()))  setUser(newSingleValue);
            else if(setting2Change.equals(ConfigParams.DATABASEUSERS.getLabel()))  setDatabaseUser(newSingleValue);
            else if(setting2Change.equals(ConfigParams.DATABASE.getLabel()))  setDatabase(newSingleValue);
            else if(setting2Change.equals(ConfigParams.IP.getLabel()))  setIp(newSingleValue);
            else if(setting2Change.equals(ConfigParams.PORT.getLabel()))  {
                try{
                    setPort(Integer.parseInt(newSingleValue));
                } catch (NumberFormatException e) {
                    System.out.println("Não foi dado um número");
                    return false;
                }
            }
            else if(setting2Change.equals(ConfigParams.PASSWORD.getLabel())) {
                char[] passChar = newSingleValue.toCharArray();
                setPassword(passChar);
            }
            else if(setting2Change.equals(ConfigParams.COLLECTIONS.getLabel()))  setCollections(newArrayValue);
            else if(setting2Change.equals(ConfigParams.CLONEMODE.getLabel()))  setClone_mode(newSingleValue);
            else if(setting2Change.equals(ConfigParams.AUTOSTART.getLabel()))  setAutostart(newSingleValue);
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
                "\n  " + spacer + ConfigParams.USER           +"='" + user                          + '\'' +
                "\n  " + spacer + ConfigParams.DATABASEUSERS  +"='" + databaseUser                  + '\'' +
                "\n  " + spacer + ConfigParams.DATABASE       +"='" + database                      + '\'' +
                "\n  " + spacer + ConfigParams.IP             +"='" + ip                            + '\'' +
                "\n  " + spacer + ConfigParams.PORT           +"='" + port                          + '\'' +
                "\n  " + spacer + ConfigParams.PASSWORD       +"='" + Arrays.toString(password)     + '\'' +
                "\n  " + spacer + ConfigParams.COLLECTIONS    +"='" + Arrays.toString(collections)  + '\'' +
                "\n  " + spacer + ConfigParams.CLONEMODE      +"='" + clone_mode                    + '\'' +
                "\n  " + spacer + ConfigParams.AUTOSTART      +"='" + autostart                     + '\'' +
                "\n}";
    }


}
