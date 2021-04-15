import mongodb.collectorwriter.MongodbCloudCollector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TerminalController{

    private MongodbCloudCollector mongodbCloudCollector;

    public TerminalController(MongodbCloudCollector mongodbCloudCollector){
        this.mongodbCloudCollector = mongodbCloudCollector;
    }

    public void launch() throws InterruptedException {
        System.out.println("Programa feito no âmbito na disciplina de PSID pelo grupo 12.\n" +
                "Programa de terminal para gerir e sincronizar as importações do mongodb cloud para o local.\n" +
                "Escrever \"ajuda\" para ver os comandos disponiveis.");

        String inputTerminal = "";
        while(!inputTerminal.equals("sair")){
            try {
                System.out.print(">>> ");
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                inputTerminal = reader.readLine();
                handleInputTerminal(inputTerminal);
            } catch (IOException e) {
                System.out.println("Erro no input da informação.");
            }
        }

        if(mongodbCloudCollector.isAlive()){
            mongodbCloudCollector.killWriters();
            mongodbCloudCollector.join();
        }

        System.out.println("A sair do programa.");
    }

    private void handleInputTerminal(String inputTerminal) throws IOException {
        switch (inputTerminal){
            case "sair":
            case "": {
                break;
            }
            case "ajuda":{
                writeComandList();
                break;
            }
            case "importar":{
                System.out.println("A inciar importação e sincronização.");
                mongodbCloudCollector.start();
                break;
            }
            default:{
                System.out.println(inputTerminal +" é um comando não reconhecido, por favor digitar \"ajuda\" para saber a lista de comandos.");
            }
        }
    }

    private void writeComandList(){
        System.out.println("Lista de comandos:\n" +
                "\"importar\" - Para importar e sincronizar as mongodb locais com o cloud.\n"
                + "\"sair\" - Para sair do programa.");
    }


}