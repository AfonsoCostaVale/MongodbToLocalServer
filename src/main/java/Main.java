import mongodb.collectorwriter.MongodbCloudCollector;

import java.io.FileNotFoundException;

import static mongodb.collectorwriter.MongodbCloudCollectorData.DEFAULTCOLLECTORDATA;

public class Main {
    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        MongodbCloudCollector collector =new MongodbCloudCollector(DEFAULTCOLLECTORDATA);
        TerminalController terminalController = new TerminalController(collector);
        terminalController.launch();
    }

    /*TODO
        Arranjar maneira de não aparecer os erros (Ajudar a ver)
        CommandLine mais extensa(com mais comandos e edição de parametros)
    */
}
