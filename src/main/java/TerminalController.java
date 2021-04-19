import config.ConfigManager;
import mongodb.collectorwriter.MongodbCloudCollector;
import mongodb.collectorwriter.MongodbCloudCollectorData;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class TerminalController{

    private static final String SAIR = "sair";
    private static final String AJUDA = "ajuda";
    private static final String IMPORTAR = "importar";
    private static final String PARAR = "parar";
    private static final String COMANDONAORECONHECIDO = "é um comando não reconhecido, por favor digitar \"ajuda\" para saber a lista de comandos.";
    private static final String ERRO_NO_INPUT_DA_INFORMAÇÃO = "Erro no input da informação.";
    private static final String COPYRIGHT = "Programa feito no âmbito na disciplina de PSID pelo grupo 12.\n" +
            "Programa de terminal para gerir e sincronizar as importações do mongodb cloud para o local.\n" +
            "Escrever \"ajuda\" para ver os comandos disponiveis.";

    private MongodbCloudCollector mongodbCloudCollector;
    private MongodbCloudCollectorData mongodbCloudCollectorData;

    public TerminalController(MongodbCloudCollector mongodbCloudCollector) throws FileNotFoundException {
        this.mongodbCloudCollector = mongodbCloudCollector;
        mongodbCloudCollectorData = ConfigManager.readFromFile();
    }

    public void launch() throws InterruptedException {
        System.out.println(COPYRIGHT);

        String inputTerminal = "";
        while(!inputTerminal.equals(SAIR)){
            try {
                System.out.print(">>> ");
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                inputTerminal = reader.readLine().trim();
                handleInputTerminal(inputTerminal);
            } catch (IOException e) {
                System.out.println(ERRO_NO_INPUT_DA_INFORMAÇÃO);
            }
        }

        if(mongodbCloudCollector.isAlive()){
            mongodbCloudCollector.killWriters();
            mongodbCloudCollector.join();
        }

        System.out.println("A sair do programa.");
    }

    private void handleInputTerminal(String inputTerminal) throws IOException {
        //String[] inputParts = inputTerminal.split(":");
        //String command = inputParts[0].trim().toLowerCase(Locale.ROOT); //alterar
        //String[] commandArgs={""};
        /*
        boolean hasArgs=false;
        if(inputParts.length > 1){
            commandArgs=inputParts;
            commandArgs=inputParts[1].trim().split(" ");
            hasArgs =true;
        }
        */

        //alterar user manuel
        LinkedList<String> inputParts = new LinkedList<>(Arrays.asList( inputTerminal.split("-")));
        String command = inputParts.pop();
        LinkedList<String> commandArgs=inputParts;
        //alterar -collections sensorh1 sensorh2
        switch (command){
            case SAIR:
            case "": {
                break;
            }
            case AJUDA:{
                writeComandList();
                break;
            }
            case IMPORTAR:{
                System.out.println("A inciar importação e sincronização.");
                mongodbCloudCollector.start();
                break;
            } case PARAR: {
                pararCloner(commandArgs);
                break;
            }default:{
                System.out.println(command + " " + COMANDONAORECONHECIDO);
            }
        }
    }

    private void pararCloner(List<String> commandArgs) {
        if (commandArgs.isEmpty()) {
            for (String collection : commandArgs) {
                collection = collection.trim();
                if (mongodbCloudCollector.removeWriter(collection)) {
                    System.out.println("Parado com sucessos" + collection + " foi parada ");
                } else {
                    System.out.println(collection + " não foi encontrado e não pode ser removido");
                }
            }
        } else {
            System.out.println("Não inseriu nenhuma coleção para parar ");
        }
    }

    private void alterarConfig(List<String> alterarNewValue) {
        String configCategory = alterarNewValue.remove(0).toLowerCase(Locale.ROOT);
        mongodbCloudCollectorData.changeSetting(configCategory,alterarNewValue);
    }


    private void writeComandList(){
        System.out.println("Lista de comandos:\n" +
                "\""+IMPORTAR+"\" - Para importar e sincronizar as mongodb locais com o cloud.\n"+
                "\""+PARAR+"\": seguido do  nome das coleções a parar (separadas por espaços)- Para parar de clonar uma ou mais coleções.\n"+
                "\""+SAIR+"\" - Para sair do programa.\n"+
                "\""+ALTERAR+"\": Alterar parametros da configuração.");

    }


}