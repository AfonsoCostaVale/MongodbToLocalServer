import config.ConfigManager;
import mongodb.collectorwriter.MongodbCloudCollector;
import mongodb.collectorwriter.MongodbCloudCollector.MongodbCloudCollectorData;

import java.io.FileNotFoundException;

import static config.ConfigManager.DEFUALTFILENAME;
import static mongodb.collectorwriter.MongodbCloudCollector.MongodbCloudCollectorData.DEFAULTCOLLECTORDATA;

public class Main {
     public static void main(String[] args) {

         MongodbCloudCollector collector =new MongodbCloudCollector(DEFAULTCOLLECTORDATA);
         TerminalController terminalController = new TerminalController(collector);

         //start TerminalController;
    }
}
