import config.ConfigManager;
import mongodb.collectorwriter.MongodbCloudCollector;
import mongodb.collectorwriter.MongodbCloudCollector.MongodbCloudCollectorData;

import java.io.FileNotFoundException;

public class Main {
     public static void main(String[] args) {
         MongodbCloudCollectorData data = new MongodbCloudCollectorData(
                 "aluno",
                 "admin",
                 "sid2021",
                 "194.210.86.10",
                 27017,
                 new char[]{'a', 'l', 'u', 'n', 'o'},
                 new String[]{"sensorh1", "sensorh2", "sensorl1", "sensorl2", "sensort1", "sensort2"}
         );

         //ConfigManager.writeToFile("conf.ini",data);
         try {
             MongodbCloudCollectorData readdata = ConfigManager.readFromFile("conf.ini");
             ConfigManager.writeToFile("conf.ini",readdata);

         } catch (FileNotFoundException e) {
             e.printStackTrace();
         }

         MongodbCloudCollector collector =new MongodbCloudCollector(data);
         TerminalController terminalController = new TerminalController(collector);
         terminalController.launch();
    }
}
