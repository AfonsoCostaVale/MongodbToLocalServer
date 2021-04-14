package config;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    public static void writeToFile(String filename, List<String> toWrite){
        try (
            FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);)
        {

            out.writeObject(toWrite);

        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }
    public static List<String> readFromFile(String filename){
        List<String> output = new ArrayList<>();
        try (
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn))
        {



        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return output;

    }
}
