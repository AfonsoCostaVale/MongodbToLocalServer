package config;

import mongodb.collectorwriter.MongodbCloudCollector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static mongodb.collectorwriter.MongodbCloudCollector.MongodbCloudCollectorData.*;

public class ConfigManager {
    public static final String DEFUALTFILENAME = "conf.ini";
    private static final String[] COMMENT =
            {
                    "//Nome do utilizador da base de dados\n",
                    "//User a usar na base de dados\n",
                    "//Nome da database para o mongo conectar\n",
                    "//Ip a usar\n",
                    "//Porta a usar para efetuar a configuração\n",
                    "//Password a usar para entrar no mongodb\n",
                    "//Nome das coleções a clonar, separadas por \";\"\n"
            };

    public static void writeToFile(String filename, MongodbCloudCollector.MongodbCloudCollectorData toWrite) {
        try (FileWriter myWriter = new FileWriter(filename)) {
            clearConfigFile(filename);
            myWriter.write(COMMENT[0]);
            myWriter.write(USER + "=" + toWrite.getUser() + "\n");
            myWriter.write(COMMENT[1]);
            myWriter.write(DATABASEUSER + "=" + toWrite.getDatabaseUser() + "\n");
            myWriter.write(COMMENT[2]);
            myWriter.write(DATABASE + "=" + toWrite.getDatabase() + "\n");
            myWriter.write(COMMENT[3]);
            myWriter.write(IP + "=" + toWrite.getIp() + "\n");
            myWriter.write(COMMENT[4]);
            myWriter.write(PORT + "=" + toWrite.getPort() + "\n");
            myWriter.write(COMMENT[5]);
            String tempCharacter = "";
            for (Character character : toWrite.getPassword()) {
                tempCharacter += (character);
            }
            myWriter.write(PASSWORD + "=" + tempCharacter + "\n");
            myWriter.write(COMMENT[6]);

            String tempCollections = "";
            for (String collection : toWrite.getCollections()) {
                tempCollections += (collection + ";");
            }
            tempCollections = tempCollections.substring(0, tempCollections.length() - 1);
            myWriter.write(COLLECTIONS + "=" + tempCollections + "\n");

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

    private static void createConfigFile() {
        writeToFile(DEFUALTFILENAME, DEFAULTCOLLECTORDATA);

    }

    public static MongodbCloudCollector.MongodbCloudCollectorData readFromFile(String filename) throws NumberFormatException, IllegalArgumentException, FileNotFoundException {
        MongodbCloudCollector.MongodbCloudCollectorData data = DEFAULTCOLLECTORDATA;
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
                    switch (lineContent[0]) {
                        case USER:
                            data.setUser(lineContent[1]);
                            break;
                        case DATABASEUSER:
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
                        default:
                            throw new IllegalArgumentException();
                    }

                }

            }

            return data;
        } catch (FileNotFoundException e) {
            createConfigFile();
            return DEFAULTCOLLECTORDATA;
        }
    }

    //TODO
    public static void clearConfigFile(String filename) throws IOException {
        new FileWriter(filename, false).close();

    }
}
