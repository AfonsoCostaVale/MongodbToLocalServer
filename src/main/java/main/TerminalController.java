package main;

import config.ConfigManager;
import mongodb.collector.MongodbCloudCollector;
import mongodb.collector.MongodbCloudCollectorDirect;
import mongodb.collector.MongodbCloudCollectorData;
import mongodb.collector.MongodbCloudCollectorMQTT;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class TerminalController{

    private static final String IMPORTAR = "importar";
    private static final String RESTARTCOLLETCTOR = "restartColletctor";
    private static final String PARAR = "parar";
    private static final String PARARTUDO = "parartudo";
    private static final String ALTERAR = "alterar";
    private static final String SHOWCONF = "mostrarconf";
    private static final String SHOWDEFAULT = "showdefault";
    private static final String RESETCONF = "reporconf";
    private static final String SAVE = "salvar";
    private static final String SAIR = "sair";
    private static final String AJUDA = "ajuda";

    private static final String COMANDONAORECONHECIDO = "é um comando não reconhecido, por favor digitar \"ajuda\" para saber a lista de comandos.";
    private static final String ERRO_NO_INPUT_DA_INFORMACAO = "Erro no input da informação.";
    private static final String COPYRIGHT = "Programa feito no âmbito na disciplina de PSID pelo grupo 12.\n" +
            "Programa de terminal para gerir e sincronizar as importações do mongodb cloud para o local.\n" +
            "Escrever \"ajuda\" para ver os comandos disponiveis.";

    public static final String DIRECT = "direct";
    public static final String MQTT = "mqtt";

    public static final String CURSOR = ">>> ";

    private MongodbCloudCollector mongodbCloudCollector;
    private MongodbCloudCollectorData mongodbCloudCollectorData;
    private ConfigManager configManager;
    private boolean isImport=false;
    //public main.TerminalController(String filename,MongodbCloudCollector mongodbCloudCollector) throws FileNotFoundException {
    public TerminalController(String filename) throws FileNotFoundException {
        this.configManager = new ConfigManager(filename);
        mongodbCloudCollectorData = configManager.readFromFile();
        //directOrMqtt();

    }

    private void directOrMqtt() throws FileNotFoundException {
        String inputTerminal = "";
        Boolean cycle = true;
        do {
            System.out.println("Deseja correr o programa em modo \"MQTT\" ou \"Direct\"?");
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                inputTerminal = reader.readLine().trim();
                System.out.println(inputTerminal);

                if (inputTerminal.equalsIgnoreCase("mqtt")){
                    this.mongodbCloudCollector = new MongodbCloudCollectorMQTT(mongodbCloudCollectorData);
                    cycle=false;
                }else if(inputTerminal.equalsIgnoreCase("direct")){
                    this.mongodbCloudCollector = new MongodbCloudCollectorDirect(mongodbCloudCollectorData);
                    cycle=false;
                }
            } catch (IOException e) {
                System.out.println(ERRO_NO_INPUT_DA_INFORMACAO);
            }
        } while (cycle);
    }

    public void launch() throws InterruptedException {
        System.out.println(COPYRIGHT);

        String autostart = mongodbCloudCollectorData.getAutostart();
        if (autostart.equalsIgnoreCase("on")) {
            dispatchedImport();
        }

        String inputTerminal = "";
        while(!inputTerminal.equals(SAIR)){
            try {
                System.out.print(CURSOR);
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                inputTerminal = reader.readLine().trim();
                handleInputTerminal(inputTerminal);
            } catch (IOException ioException) {
                System.out.println(ERRO_NO_INPUT_DA_INFORMACAO);
            }catch(Exception exception){
                System.out.println("Erro inesperado");
            }
        }

        if(mongodbCloudCollector.isAlive()){
            mongodbCloudCollector.killWriters();
            mongodbCloudCollector.join();
        }

        System.out.println("A sair do programa.");
    }

    private void handleInputTerminal(String inputTerminal) throws IOException {
        LinkedList<String> inputParts = new LinkedList<>(Arrays.asList(inputTerminal.split("-")));
        String command = inputParts.pop().trim();
        dispatcher(command, inputParts);
    }

    private void dispatcher(String command, LinkedList<String> commandArgs) {
        switch (command){
            case SAIR:
            case "": {
                break;
            }case AJUDA:{
                dispatchedAjuda();
                break;
            }case IMPORTAR:{
                dispatchedImport();
                break;
            }case RESTARTCOLLETCTOR:{
                dispatchedRestart();
                break;
            } case PARAR: {
                dispatchedParar(commandArgs);
                break;
            }case PARARTUDO: {
                dispatchedPararALL();
                break;
            } case ALTERAR: {
                dispatchedAlterar(commandArgs);
                break;
            } case SHOWCONF: {
                dispatchedShowconf();
                break;
            } case SHOWDEFAULT: {
                dispatchedShowDefault();
                break;
            } case RESETCONF: {
                dispatchedReset();
                break;
            } case SAVE: {
                dispatchedSave();
                break;
            }default:{
                System.out.println(command + " " + COMANDONAORECONHECIDO);
            }
        }
    }

    private void dispatchedRestart() {
        dispatchedPararALL();
        dispatchedImport();
    }

    private void dispatchedReset() {
        this.mongodbCloudCollectorData= new MongodbCloudCollectorData();
        System.out.println("Novas configurações:\n"+this.mongodbCloudCollectorData);
    }

    private void dispatchedShowDefault() {
        System.out.println(new MongodbCloudCollectorData());
    }

    private void dispatchedSave() {
        configManager.writeToFile(this.mongodbCloudCollectorData);
        System.out.println("Configurações salvas em " + configManager.getFilename());
    }

    private void dispatchedShowconf() {
        System.out.println(mongodbCloudCollectorData);
    }

    private void dispatchedImport() {
        if (!isImport) {
            System.out.println("A inciar importação e sincronização.");
            isImport = true;
            String clone_mode = mongodbCloudCollectorData.getClone_mode();

            if(clone_mode.equalsIgnoreCase(MQTT)){
                this.mongodbCloudCollector = new MongodbCloudCollectorMQTT(mongodbCloudCollectorData);
            }else if(clone_mode.equalsIgnoreCase(DIRECT)){
                this.mongodbCloudCollector = new MongodbCloudCollectorDirect(mongodbCloudCollectorData);
            }
            mongodbCloudCollector.start();
        } else {
            System.out.println("A importação já está a ser executada, por favor pare tudo para voltar a iniciar a importação.");
        }
    }

    private void dispatchedParar(List<String> commandArgs) {
        if (!commandArgs.isEmpty()) {
            for (String collection : commandArgs) {
                collection = collection.trim();
                if (mongodbCloudCollector.removeWriter(collection)) {
                    System.out.println("Parado com sucesso" + collection + " foi parada.");
                } else {
                    System.out.println(collection + " não foi encontrado e não pode ser removido.");
                }
            }
        } else {
            System.out.println("Não inseriu nenhuma coleção para parar!");
        }
    }
    private void dispatchedPararALL() {
        mongodbCloudCollector.killWriters();
        isImport = false;
        System.out.println("Todos os cloners foram parados com sucesso.");
    }

    private void dispatchedAlterar(List<String> alterarNewValue) {
        for (String changeParam: alterarNewValue) {
            LinkedList<String> args =  new LinkedList<>(Arrays.asList(changeParam.split(" ")));
            String field = args.pop();

            if(mongodbCloudCollectorData.changeSetting(field,args)) {
                System.out.println("Foi alterado o campo: " + field);
            }
        }
    }

    private void dispatchedAjuda(){
        System.out.println("Lista de comandos:\n"+
                "\""+IMPORTAR           +"\": Importar as mongodb locais com o cloud.\n"                                                     +
                "\""+RESTARTCOLLETCTOR  +"\": Efetua o restart das improtações(fecha e volta a abrir com as novas configurações).\n"         +
                "\""+PARAR              +"\": Parar de clonar uma ou mais coleções. exemplo:\" parar -sensorh1 sensorh2\".\n"                +
                "\""+PARARTUDO          +"\": Parar de clonar TODAS as coleções. exemplo:\" parartudo\".\n"                                  +
                "\""+ALTERAR            +"\": Alterar parametros da configuração. exemplo: \"alterar -user manuel -password manuel2021\".\n" +
                "\""+SHOWCONF           +"\": Mostrar configurações atuais.\n"                                                               +
                "\""+SHOWDEFAULT        +"\": Mostrar configurações default.\n"                                                              +
                "\""+RESETCONF          +"\": Carregar as configurações default\n"                                                           +
                "\""+SAVE               +"\": Salvar as configurações atuais.\n"                                                             +
                "\""+SAIR               +"\": Sair do programa.\n"                                                                           +
                "\""+AJUDA              +"\": Mostrar os comandos do programa.\n"                                                                           +
                "Para as novas configurações tomarem efeito sugerimos parar o programa e voltar a abrir"
        );

    }


}