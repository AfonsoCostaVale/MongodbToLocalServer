package mongodb.writer;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.eclipse.paho.client.mqttv3.MqttException;
import sql.CulturaDB;
import util.Average;
import util.Pair;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class MongodbLocalWriterDirect extends MongodbLocalWriter {

    private final Connection connection;

    private int sensorID = 0;
    private Pair<Double,Average> lastMedicoes;


    public MongodbLocalWriterDirect(String collection, MongoCollection<Document> collectionToRead, Connection connection) {
        localMongoClient = new MongoClient("localhost", 27017);
        localDB = localMongoClient.getDatabase("sid");
        this.collectionToWrite = localDB.getCollection(collection);
        this.collectionToRead = collectionToRead;
        collectionName = collectionToRead.getNamespace().getFullName();
        this.connection = connection;
    }

    public void run() {
        try {
            System.out.println("Started writing in " + collectionToWrite.getNamespace().getFullName());
            boolean first=true;
            for (Document entry : collectionToRead.find().skip((int) collectionToWrite.count())) {
                try {
                    if(first) {
                        sensorID = CulturaDB.getSensorId(this.connection,entry.toString());
                        lastMedicoes = new Pair<>(null,new Average(sensorID));
                        first = false;
                    }
                    write(entry);
                    CulturaDB.insertMedicao(entry.toString(), this.connection);
                    handlePredictedValue();
                } catch (MongoWriteException e) {
                    if (e.getError().getCategory() == ErrorCategory.DUPLICATE_KEY) {
                        System.out.println("Found Duplicate");
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
                enterCheckMode();


        } catch (MongoInterruptedException e) {
            e.printStackTrace();
        } catch (MongoTimeoutException e) {
            e.printStackTrace();
        }
    }

    protected void enterCheckMode(){

        System.out.println("Entered check mode " + collectionToWrite.getNamespace().getFullName());
        while (true) {
            try {
                Document documentToWrite = collectionToRead.find().skip((int) collectionToWrite.count()).first();
                write(documentToWrite);
                // mqttWriter.sendMessage(documentToWrite.toString(),GeneralMqttVariables.QOS,GeneralMqttVariables.TOPIC);
                //System.out.println("Added " + collectionToWrite.getNamespace().getFullName());
            } catch (IllegalArgumentException e) {

            }
        }

    }

    private void handlePredictedValue() throws SQLException {
        if(CulturaDB.getLastMedicaoWithId(this.connection,sensorID).isEmpty()) { return;}

        //Calculate change percentage of each medicao and adding value to list

        double newLeitura = Double.parseDouble(CulturaDB.getLastMedicaoWithId(this.connection,sensorID).get(3));
        if(newLeitura > 0 && newLeitura < 0.01) { newLeitura=0.01; }
        if(newLeitura < 0 && newLeitura > -0.01) { newLeitura=-0.01; }
        if(newLeitura == 0) { newLeitura = 0.01; }

        if(lastMedicoes.getB().getSize() > 1){
            lastMedicoes.getB().putValue((newLeitura - lastMedicoes.getA()) * 100 / newLeitura);
            if(lastMedicoes.getB().getSize() >= 10) {
                double predictedValue = (newLeitura + ((lastMedicoes.getB().getAverage()/100) * newLeitura));

                //Predicted Value is sent back to backend to be decided if an alerta is sent or not
                ArrayList<String> medicaoForPredicted = CulturaDB.getLastMedicaoWithId(this.connection,sensorID);
                medicaoForPredicted.add(String.valueOf(predictedValue));
                CulturaDB.checkForAlerta(this.connection,medicaoForPredicted,true);
                System.out.println("Added Predicted: " + predictedValue);
            }
        } else if(lastMedicoes.getA() != null ){
            lastMedicoes.getB().putValue((lastMedicoes.getA()-newLeitura) * 100 / newLeitura);
        }
        lastMedicoes.setA(newLeitura);
    }
}
