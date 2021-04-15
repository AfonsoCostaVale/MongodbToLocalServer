import mongodb.collectorwriter.MongodbCloudCollector;

import static mongodb.collectorwriter.MongodbCloudCollector.MongodbCloudCollectorData.DEFAULTCOLLECTORDATA;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        MongodbCloudCollector collector =new MongodbCloudCollector(DEFAULTCOLLECTORDATA);
        TerminalController terminalController = new TerminalController(collector);
        terminalController.launch();
    }
}
