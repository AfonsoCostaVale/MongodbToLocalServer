package config;

import mongodb.collectorwriter.MongodbCloudCollector;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import static mongodb.collectorwriter.MongodbCloudCollector.MongodbCloudCollectorData.*;

public class ConfigManager {
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
            myWriter.write(USER+"="+toWrite.getUser() + "\n");
            myWriter.write(COMMENT[1]);
            myWriter.write(DATABASEUSER+"="+toWrite.getDatabaseUser() + "\n");
            myWriter.write(COMMENT[2]);
            myWriter.write(DATABASE+"="+toWrite.getDatabase() + "\n");
            myWriter.write(COMMENT[3]);
            myWriter.write(IP+"="+toWrite.getIp() + "\n");
            myWriter.write(COMMENT[4]);
            myWriter.write(PORT+"="+toWrite.getPort() + "\n");
            myWriter.write(COMMENT[5]);
            String tempCharacter ="";
            for (Character character  :toWrite.getPassword() ) {
                tempCharacter+=(character);
            }
            myWriter.write(PASSWORD+"="+ tempCharacter + "\n");
            myWriter.write(COMMENT[6]);

            String tempCollections="";
            for (String collection  :toWrite.getCollections() ) {
                tempCollections+=(collection+";");
            }
            tempCollections =tempCollections.substring(0,tempCollections.length()-1);
            myWriter.write(COLLECTIONS+"="+tempCollections+"\n");

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

    public static MongodbCloudCollector.MongodbCloudCollectorData readFromFile(String filename) throws NumberFormatException, IllegalArgumentException {
        MongodbCloudCollector.MongodbCloudCollectorData data = new MongodbCloudCollector.MongodbCloudCollectorData();
        Scanner scanner = new Scanner(filename);
        String line;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            if (!line.startsWith("//")) {
                String[] lineContent = line.split("=");
                lineContent[1]=lineContent[1].trim().replace("\n","");
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
        /*
        try (
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn))
        {
            output = (ArrayList<String>)in.readObject();

        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        */
        return data;
    }

    //TODO
    public static void clearConfigFile(String filename) throws IOException {
        new FileWriter(filename, false).close();

    }
}
