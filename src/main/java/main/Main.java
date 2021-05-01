package main;

import java.io.FileNotFoundException;

import static config.ConfigManager.DEFAULTFILENAME;

public class Main {
    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        //MongodbCloudCollector collector =new MongodbCloudCollector(DEFAULTCOLLECTORDATA);
        TerminalController terminalController = new TerminalController(DEFAULTFILENAME);
        terminalController.launch();
    }

    /*TODO
        Arranjar maneira de não aparecer os erros (Ajudar a ver)
        CommandLine mais extensa(com mais comandos e edição de parametros)
        Problema se fizermos DROP a database sid o programa spamma Added sid.sensort1 infinitamente
    */
}
