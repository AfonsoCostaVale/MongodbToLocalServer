package mongodb.collector;

import com.mongodb.BasicDBObject;
import config.ConfigParams;
import mqtt.GeneralMqttVariables;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private String mqttbroker;
    private int mqttqos;
    private String mqtttopic;
    private String localmongodbname;
    private int yeardateformongoclone;
    private int monthdateformongoclone;
    private int daydateformongoclone;
    private int hourdateformongoclone;
    public static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd 'at' HH:mm:ss 'GMT'");



    public MongodbCloudCollectorData(String user,
                                     String databaseUser,
                                     String database,
                                     String ip,
                                     int port,
                                     char[] password,
                                     String[] collections,
                                     String clone_mode,
                                     String autostart,
                                     String mqttbroker,
                                     int mqttQOS,
                                     String mqtttopic,
                                     String localmongodbname,
                                     int yeardateformongoclone,
                                     int monthdateformongoclone,
                                     int daydateformongoclone,
                                     int hourdateformongoclone
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
        this.mqttbroker = mqttbroker;
        this.mqttqos = mqttQOS;
        this.mqtttopic = mqtttopic;
        this.localmongodbname = localmongodbname;
        this.yeardateformongoclone = yeardateformongoclone;
        this.monthdateformongoclone = monthdateformongoclone;
        this.daydateformongoclone = daydateformongoclone;
        this.hourdateformongoclone = hourdateformongoclone;
    }
/**
 * Constructor with the default values for the config data
 * */
    public MongodbCloudCollectorData() {
        this.user = "aluno";
        this.databaseUser = "admin";
        this.database = "sid2021";
        this.ip = "194.210.86.10";
        this.port = 27017;
        this.password = new char[]{'a', 'l', 'u', 'n', 'o'};
        this.collections = new String[]{"sensorh1", "sensorh2", "sensorl1", "sensorl2", "sensort1", "sensort2"};
        this.clone_mode = "mqtt";
        this.autostart = "off";
        this.mqttbroker = GeneralMqttVariables.BROKER;
        this.mqttqos = GeneralMqttVariables.QOS;
        this.mqtttopic = GeneralMqttVariables.TOPIC;
        this.localmongodbname = "sid";

        LocalDateTime date = LocalDateTime.now();
        this.yeardateformongoclone = date.getYear();
        this.monthdateformongoclone =date.getMonthValue();
        this.daydateformongoclone = 0;
        hourdateformongoclone=0;
    }

    public String       getUser() {                                             return user;                                            }
    public void         setUser(String user) {                                  this.user = user;                                       }
    public String       getDatabaseUser() {                                     return databaseUser;                                    }
    public void         setDatabaseUser(String databaseUser) {                  this.databaseUser = databaseUser;                       }
    public String       getDatabase() {                                         return database;                                        }
    public void         setDatabase(String database) {                          this.database = database;                               }
    public String       getIp() {                                               return ip;                                              }
    public void         setIp(String ip) {                                      this.ip = ip;                                           }
    public int          getPort() {                                             return port;                                            }
    public void         setPort(int port) {                                     this.port = port;                                       }
    public char[]       getPassword() {                                         return password;                                        }
    public void         setPassword(char[] password) {                          this.password = password;                               }
    public String[]     getCollections() {                                      return collections;                                     }
    public void         setCollections(String[] collections) {                  this.collections = collections;                         }
    public String       getClone_mode() {                                       return clone_mode;                                      }
    public void         setClone_mode(String clone_mode) {                      this.clone_mode = clone_mode;                           }
    public String       getAutostart() {                                        return autostart;                                       }
    public void         setAutostart(String autostart) {                        this.autostart = autostart;                             }
    public String       getMqttbroker() {                                       return mqttbroker;                                      }
    public void         setMqttbroker(String mqttbroker) {                      this.mqttbroker = mqttbroker;                           }
    public int          getMqttqos() {                                          return mqttqos;                                         }
    public void         setMqttqos(int mqttqos) {                               this.mqttqos = mqttqos;                                 }
    public String       getMqtttopic() {                                        return mqtttopic;                                       }
    public void         setMqtttopic(String mqtttopic) {                        this.mqtttopic = mqtttopic;                             }
    public String       getLocalmongodbname() {                                 return localmongodbname;                                }
    public void         setLocalmongodbname(String localmongodbname) {          this.localmongodbname = localmongodbname;               }
    public int          getYeardateformongoclone() {                            return yeardateformongoclone;                           }
    public void         setYeardateformongoclone(int yeardateformongoclone) {   this.yeardateformongoclone = yeardateformongoclone;     }
    public int          getMonthdateformongoclone() {                           return monthdateformongoclone;                          }
    public void         setMonthdateformongoclone(int monthdateformongoclone) { this.monthdateformongoclone = monthdateformongoclone;   }
    public int          getDaydateformongoclone() {                             return daydateformongoclone;                            }
    public void         setDaydateformongoclone(int hourdateformongoclone) {    this.daydateformongoclone = hourdateformongoclone;     }
    public int          getHourdateformongoclone() {                            return hourdateformongoclone;                           }
    public void         setHourdateformongoclone(int hourdateformongoclone) {   this.hourdateformongoclone = hourdateformongoclone;     }

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
            if(setting2Change.equalsIgnoreCase(ConfigParams.USER.getLabel()))
                setUser(newSingleValue);
            else if(setting2Change.equalsIgnoreCase(ConfigParams.DATABASEUSERS.getLabel()))
                setDatabaseUser(newSingleValue);
            else if(setting2Change.equalsIgnoreCase(ConfigParams.DATABASE.getLabel()))
                setDatabase(newSingleValue);
            else if(setting2Change.equalsIgnoreCase(ConfigParams.IP.getLabel()))
                setIp(newSingleValue);
            else if(setting2Change.equalsIgnoreCase(ConfigParams.PORT.getLabel()))  {
                try{
                    setPort(Integer.parseInt(newSingleValue));
                } catch (NumberFormatException e) {
                    System.out.println("Não foi dado um número para "+ ConfigParams.PORT.getLabel());
                    return false;
                }
            }
            else if(setting2Change.equalsIgnoreCase(ConfigParams.PASSWORD.getLabel())) {
                char[] passChar = newSingleValue.toCharArray();
                setPassword(passChar);
            }
            else if(setting2Change.equalsIgnoreCase(ConfigParams.COLLECTIONS.getLabel()))
                setCollections(newArrayValue);
            else if(setting2Change.equalsIgnoreCase(ConfigParams.CLONEMODE.getLabel()))
                setClone_mode(newSingleValue);
            else if(setting2Change.equalsIgnoreCase(ConfigParams.AUTOSTART.getLabel()))
                setAutostart(newSingleValue);
            else if(setting2Change.equalsIgnoreCase(ConfigParams.MQTTBROKER.getLabel()))
                setAutostart(newSingleValue);
            else if(setting2Change.equalsIgnoreCase(ConfigParams.MQTTQOS.getLabel())) {
                try{
                    setMqttqos(Integer.parseInt(newSingleValue));
                } catch (NumberFormatException e) {
                    System.out.println("Não foi dado um número para "+ ConfigParams.MQTTQOS.getLabel());
                    return false;
                }
            } else if(setting2Change.equalsIgnoreCase(ConfigParams.MQTTTOPIC.getLabel())){
                setMqtttopic(newSingleValue);
            } else if(setting2Change.equalsIgnoreCase(ConfigParams.LOCALMONGODBNAME.getLabel())){
                setLocalmongodbname(newSingleValue);
            } else if(setting2Change.equalsIgnoreCase(ConfigParams.YEARDATEFORMONGOCLONE.getLabel())){
                try{
                setYeardateformongoclone(Integer.parseInt(newSingleValue));
                } catch (NumberFormatException e) {
                    System.out.println("Não foi dado um número para "+ ConfigParams.YEARDATEFORMONGOCLONE.getLabel());
                    return false;
                }
            } else if(setting2Change.equalsIgnoreCase(ConfigParams.MONTHDATEFORMONGOCLONE.getLabel())){
                try{
                setMonthdateformongoclone(Integer.parseInt(newSingleValue));
                } catch (NumberFormatException e) {
                    System.out.println("Não foi dado um número para "+ ConfigParams.MONTHDATEFORMONGOCLONE.getLabel());
                    return false;
                }
            } else if(setting2Change.equalsIgnoreCase(ConfigParams.DAYDATEFORMONGOCLONE.getLabel())){
                try{
                setDaydateformongoclone(Integer.parseInt(newSingleValue));
                } catch (NumberFormatException e) {
                    System.out.println("Não foi dado um número para "+ ConfigParams.DAYDATEFORMONGOCLONE.getLabel());
                    return false;
                }
            } else if(setting2Change.equalsIgnoreCase(ConfigParams.HOURDATEFORMONGOCLONE.getLabel())){
                try{
                setHourdateformongoclone(Integer.parseInt(newSingleValue));
                } catch (NumberFormatException e) {
                    System.out.println("Não foi dado um número para "+ ConfigParams.HOURDATEFORMONGOCLONE.getLabel());
                    return false;
                }
            }else{
                System.out.println("Parametro de configuração desconhecido");
                return false;
            }
            return true;
        }
        System.out.println("Valor vazio");
        return false;
    }

    public String getDateString() {
        String month="";
        int monthdateformongoclone = getMonthdateformongoclone();

        if(monthdateformongoclone < 10){
            month +="0"+ monthdateformongoclone;
        }else{
            month += monthdateformongoclone;
        }

        String day="";
        int daydateformongoclone = getDaydateformongoclone();

        if(daydateformongoclone < 10){
            day +="0"+ daydateformongoclone;
        }else{
            day += daydateformongoclone;
        }
        String hour="";
        if(hourdateformongoclone < 10){
            day +="0"+ hourdateformongoclone;
        }else{
            day += hourdateformongoclone;
        }

        return getYeardateformongoclone() + "-" + month + "-" + day + " at " + hour + ":00:00 GMT";
    }

    public static BasicDBObject getLastMinuteDBQuery() {
        BasicDBObject dbQuerry= new BasicDBObject();
        LocalDateTime dateTimeNow = LocalDateTime.now();
        dbQuerry.put("Data", new BasicDBObject("$gt", java.time.LocalDate.now() + " at " + dateTimeNow.getHour() + ":" +dateTimeNow.getMinute() + ":00 GMT"));
        return dbQuerry;
    }

    @Override
    public String toString() {
        String spacer = "    ";
        return "MongodbCloudCollectorData{" +
                "\n  " + spacer + ConfigParams.USER                     +"='" + user                            + '\'' +
                "\n  " + spacer + ConfigParams.DATABASEUSERS            +"='" + databaseUser                    + '\'' +
                "\n  " + spacer + ConfigParams.DATABASE                 +"='" + database                        + '\'' +
                "\n  " + spacer + ConfigParams.IP                       +"='" + ip                              + '\'' +
                "\n  " + spacer + ConfigParams.PORT                     +"='" + port                            + '\'' +
                "\n  " + spacer + ConfigParams.PASSWORD                 +"='" + Arrays.toString(password)       + '\'' +
                "\n  " + spacer + ConfigParams.COLLECTIONS              +"='" + Arrays.toString(collections)    + '\'' +
                "\n  " + spacer + ConfigParams.CLONEMODE                +"='" + clone_mode                      + '\'' +
                "\n  " + spacer + ConfigParams.AUTOSTART                +"='" + autostart                       + '\'' +
                "\n  " + spacer + ConfigParams.MQTTBROKER               +"='" + mqttbroker                      + '\'' +
                "\n  " + spacer + ConfigParams.MQTTQOS                  +"='" + mqttqos                         + '\'' +
                "\n  " + spacer + ConfigParams.MQTTTOPIC                +"='" + mqtttopic                       + '\'' +
                "\n  " + spacer + ConfigParams.LOCALMONGODBNAME         +"='" + localmongodbname                + '\'' +
                "\n  " + spacer + ConfigParams.YEARDATEFORMONGOCLONE    +"='" + yeardateformongoclone           + '\'' +
                "\n  " + spacer + ConfigParams.MONTHDATEFORMONGOCLONE   +"='" + monthdateformongoclone          + '\'' +
                "\n  " + spacer + ConfigParams.DAYDATEFORMONGOCLONE     +"='" + daydateformongoclone            + '\'' +
                "\n  " + spacer + ConfigParams.HOURDATEFORMONGOCLONE    +"='" + hourdateformongoclone           + '\'' +

                "\n}";
    }


}
