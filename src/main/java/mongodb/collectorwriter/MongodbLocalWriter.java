package mongodb.collectorwriter;

import MQTT.GeneralMqttVariables;
import MQTT.MQTTWriter;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;

public class MongodbLocalWriter extends Thread {
    private final MongoClient localMongoClient;
    private final MongoDatabase localDB;
    private final MongoCollection<Document> collectionToWrite;
    private final MongoCollection<Document> collectionToRead;
    private final String collectionName;
    private final MQTTWriter mqttWriter;

    protected MongodbLocalWriter(String collection, MongoCollection<Document> collectionToRead,MQTTWriter mqttWriter) {
        localMongoClient = new MongoClient("localhost", 27017);
        localDB = localMongoClient.getDatabase("sid");
        this.collectionToWrite = localDB.getCollection(collection);
        this.collectionToRead = collectionToRead;
        collectionName = collectionToRead.getNamespace().getFullName();
        this.mqttWriter = mqttWriter;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void run() {
        try {
            System.out.println("Started writing in " + collectionToWrite.getNamespace().getFullName());


            for (Document entry : collectionToRead.find().skip((int) collectionToWrite.count())) {
                try {
                    write(entry);
                    mqttWriter.sendMessage(entry.toString(),GeneralMqttVariables.QOS,GeneralMqttVariables.TOPIC);
                } catch (MongoWriteException e) {
                    if (e.getError().getCategory() == ErrorCategory.DUPLICATE_KEY) {
                        System.out.println("Found Duplicate");
                    }
                }
            }
            mqttWriter.disconnect();

            try {
                enterCheckMode();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        } catch (MongoInterruptedException e) {

        } catch (MongoTimeoutException e) {

        } catch (MqttException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void enterCheckMode() throws MqttException {

        System.out.println("Entered check mode " + collectionToWrite.getNamespace().getFullName());
        MQTTWriter mqttWriter= new MQTTWriter(GeneralMqttVariables.BROKER,GeneralMqttVariables.CLIENT_ID,GeneralMqttVariables.PERSISTENCE);
        mqttWriter.connect();
        while (true) {
            try {
                Document documentToWrite = collectionToRead.find().skip((int) collectionToWrite.count()).first();
                write(documentToWrite);
                mqttWriter.sendMessage(documentToWrite.toString(),GeneralMqttVariables.QOS,GeneralMqttVariables.TOPIC);
                System.out.println("Added " + collectionToWrite.getNamespace().getFullName());
            } catch (MongoWriteException e) {
                if (e.getError().getCategory() == ErrorCategory.DUPLICATE_KEY) {
                    System.out.println("Found Duplicate");
                }
            } catch (IllegalArgumentException e) {

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    protected void write(Document doc) {
        collectionToWrite.insertOne(doc);

    }

}
