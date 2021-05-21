package main;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.logging.LogManager;

import static config.ConfigManager.DEFAULTFILENAME;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        //MongodbCloudCollector collector =new MongodbCloudCollector(DEFAULTCOLLECTORDATA);
        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb.driver").setLevel(Level.OFF);




        TerminalController terminalController = null;
        try {
            terminalController = new TerminalController(DEFAULTFILENAME);
            terminalController.launch();
        } catch (FileNotFoundException e) {
            System.out.println("Ficheiro de configs não foi encontrado\nExiting");
        }
    }

    /*TODO
        Arranjar maneira de não aparecer os erros (Ajudar a ver)
        CommandLine mais extensa(com mais comandos e edição de parametros)
        Problema se fizermos DROP a database sid o programa spamma Added sid.sensort1 infinitamente
    */
}
