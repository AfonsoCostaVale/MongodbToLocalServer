package config;

import mongodb.collector.MongodbCloudCollectorData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

public class ConfigManager {
    public static final String DEFAULTFILENAME = "conf.ini";

    private static final String[] COMMENT =
            {
                    "//Nome do utilizador da base de dados\n",
                    "//User a usar na base de dados\n",
                    "//Nome da database para o mongo conectar\n",
                    "//Ip a usar\n",
                    "//Porta a usar para efetuar a configuração\n",
                    "//Password a usar para entrar no mongodb\n",
                    "//Nome das coleções a clonar, separadas por \";\"\n",
                    "//Modo de clonagem a usar, pode ser mqtt ou direct\n",
                    "//AutoStart if you want the program to automatically start importing with the current configs, can be \"on\" or \"off\"\n",
                    "//MQTTbroker is the ip of the mqttbroker that is going to be used to transfer data when mqtt option is enabled\n",
                    "//MQTTQOS is the Quality of service used in the mqtt connection\n",
                    "//MQTTTOPIC is the topic used in the mqtt connection\n",
                    "//LOCALMONGODBNAME is the name used for the local mongoDatabase\n",
                    "//YEARDATEFORMONGOCLONE is the year from which documents start getting cloned from the cloud \n",
                    "//MONTHDATEFORMONGOCLONE is the month from which documents start getting cloned from the cloud \n",
                    "//DAYDATEFORMONGOCLONE is the day from which documents start getting cloned from the cloud \n",
                    "//HOURDATEFORMONGOCLONE is the hour from which documents start getting cloned from the cloud \n",
            };

    public String getFilename() {
        return filename;
    }

    private final String filename;

    public ConfigManager(String filename) {
        this.filename = filename;
    }

    public static void writeToFile(String filename, MongodbCloudCollectorData dataToWrite) {
        try (FileWriter myWriter = new FileWriter(filename)) {
            clearConfigFile(filename);
            myWriter.write(COMMENT[0]);
            myWriter.write(ConfigParams.USER + "=" + dataToWrite.getUser() + "\n");
            myWriter.write(COMMENT[1]);
            myWriter.write(ConfigParams.DATABASEUSERS + "=" + dataToWrite.getDatabaseUser() + "\n");
            myWriter.write(COMMENT[2]);
            myWriter.write(ConfigParams.DATABASE + "=" + dataToWrite.getDatabase() + "\n");
            myWriter.write(COMMENT[3]);
            myWriter.write(ConfigParams.IP + "=" + dataToWrite.getIp() + "\n");
            myWriter.write(COMMENT[4]);
            myWriter.write(ConfigParams.PORT + "=" + dataToWrite.getPort() + "\n");
            myWriter.write(COMMENT[5]);
            String tempCharacter = "";
            for (Character character : dataToWrite.getPassword()) {
                tempCharacter += (character);
            }
            myWriter.write(ConfigParams.PASSWORD + "=" + tempCharacter + "\n");
            myWriter.write(COMMENT[6]);

            String tempCollections = "";
            for (String collection : dataToWrite.getCollections()) {
                tempCollections += (collection + ";");
            }
            tempCollections = tempCollections.substring(0, tempCollections.length() - 1);
            myWriter.write(ConfigParams.COLLECTIONS + "=" + tempCollections + "\n");
            myWriter.write(COMMENT[7]);
            myWriter.write(ConfigParams.CLONEMODE + "=" + dataToWrite.getClone_mode() + "\n");
            myWriter.write(COMMENT[8]);
            myWriter.write(ConfigParams.AUTOSTART + "=" + dataToWrite.getAutostart() + "\n");
            myWriter.write(COMMENT[9]);
            myWriter.write(ConfigParams.MQTTBROKER + "=" + dataToWrite.getMqttbroker() + "\n");
            myWriter.write(COMMENT[10]);
            myWriter.write(ConfigParams.MQTTQOS + "=" + dataToWrite.getMqttqos() + "\n");
            myWriter.write(COMMENT[11]);
            myWriter.write(ConfigParams.MQTTTOPIC + "=" + dataToWrite.getMqtttopic() + "\n");
            myWriter.write(COMMENT[12]);
            myWriter.write(ConfigParams.LOCALMONGODBNAME + "=" + dataToWrite.getLocalmongodbname() + "\n");
            myWriter.write(COMMENT[13]);
            myWriter.write(ConfigParams.YEARDATEFORMONGOCLONE + "=" + dataToWrite.getYeardateformongoclone() + "\n");
            myWriter.write(COMMENT[14]);
            myWriter.write(ConfigParams.MONTHDATEFORMONGOCLONE + "=" + dataToWrite.getMonthdateformongoclone() + "\n");
            myWriter.write(COMMENT[15]);
            myWriter.write(ConfigParams.DAYDATEFORMONGOCLONE + "=" + dataToWrite.getDaydateformongoclone() + "\n");
            myWriter.write(COMMENT[16]);
            myWriter.write(ConfigParams.HOURDATEFORMONGOCLONE + "=" + dataToWrite.getHourdateformongoclone() + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }

        /*try (
            FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);)
        {
            out.writeObject(toWrite);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        */

    }

    public static MongodbCloudCollectorData readFromFile(String filename) throws IllegalArgumentException, FileNotFoundException {
        MongodbCloudCollectorData data = new MongodbCloudCollectorData();
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(filename));

            String line = "";
            while (scanner.hasNextLine()) {
                line = scanner.nextLine().trim();
                if (!line.startsWith("//") && !line.equals(filename)) {
                    String[] lineContent = line.split("=");
                    boolean isEmpty = (lineContent.length == 1);
                    if (!isEmpty) {
                        lineContent[1] = lineContent[1].trim().replace("\n", "");
                    } else {
                        continue;
                    }
                    switch (ConfigParams.valueOf(lineContent[0].toUpperCase(Locale.ROOT))) {
                        case USER:
                            data.setUser(lineContent[1]);
                            break;
                        case DATABASEUSERS:
                            data.setDatabaseUser(lineContent[1]);
                            break;
                        case DATABASE:
                            data.setDatabase(lineContent[1]);
                            break;
                        case IP:
                            data.setIp(lineContent[1]);
                            break;
                        case PORT:
                            data.setPort(Integer.parseInt(lineContent[1]));
                            break;
                        case PASSWORD:
                            data.setPassword(lineContent[1].toCharArray());
                            break;
                        case COLLECTIONS:
                            data.setCollections(lineContent[1].split(";"));
                            break;
                        case CLONEMODE:
                            data.setClone_mode(lineContent[1]);
                            break;
                        case AUTOSTART:
                            data.setAutostart(lineContent[1]);
                            break;
                        case MQTTBROKER:
                            data.setMqttbroker(lineContent[1]);
                            break;
                        case MQTTQOS:
                            data.setMqttqos(Integer.parseInt(lineContent[1]));
                            break;
                        case MQTTTOPIC:
                            data.setMqtttopic(lineContent[1]);
                            break;
                        case LOCALMONGODBNAME:
                            data.setLocalmongodbname(lineContent[1]);
                            break;
                        case YEARDATEFORMONGOCLONE:
                            data.setYeardateformongoclone(Integer.parseInt(lineContent[1]));
                            break;
                        case MONTHDATEFORMONGOCLONE:
                            data.setMonthdateformongoclone(Integer.parseInt(lineContent[1]));
                            break;
                        case DAYDATEFORMONGOCLONE:
                            data.setDaydateformongoclone(Integer.parseInt(lineContent[1]));
                            break;
                        case HOURDATEFORMONGOCLONE:
                            data.setHourdateformongoclone(Integer.parseInt(lineContent[1]));
                            break;

                        default:
                            throw new IllegalArgumentException();
                    }

                }

            }

            return data;
        } catch (FileNotFoundException e) {
            createConfigFile(filename);
            return new MongodbCloudCollectorData();
        }
    }

    //TODO
    public static void clearConfigFile(String filename) throws IOException {
        new FileWriter(filename, false).close();
    }

    private static void createConfigFile(String filename) {
        writeToFile(filename, new MongodbCloudCollectorData());
    }

    public void writeToFile(MongodbCloudCollectorData dataToWrite) {
        writeToFile(this.filename, dataToWrite);
    }

    private void createConfigFile() {
        writeToFile(new MongodbCloudCollectorData());
    }

    public MongodbCloudCollectorData readFromFile() throws IllegalArgumentException, FileNotFoundException {
        return readFromFile(this.filename);
    }
}
