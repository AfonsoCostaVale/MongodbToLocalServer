package mongodb.writer;

import mqtt.GeneralMqttVariables;
import mqtt.MQTTWriter;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;

public class MongodbLocalWriterMQTT extends MongodbLocalWriter {

    private final MQTTWriter mqttWriter;

    public MongodbLocalWriterMQTT(String collection, MongoCollection<Document> collectionToRead, MQTTWriter mqttWriter) {
        localMongoClient = new MongoClient("localhost", 27017);
        localDB = localMongoClient.getDatabase("sid");
        this.collectionToWrite = localDB.getCollection(collection);
        this.collectionToRead = collectionToRead;
        collectionName = collectionToRead.getNamespace().getFullName();
        this.mqttWriter = mqttWriter;
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
            e.printStackTrace();
        } catch (MongoTimeoutException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void enterCheckMode() throws MqttException {

        System.out.println("Entered check mode " + collectionToWrite.getNamespace().getFullName());
        while (true) {
            try {
                Document documentToWrite = collectionToRead.find().skip((int) collectionToWrite.count()).first();
                write(documentToWrite);
               // mqttWriter.sendMessage(documentToWrite.toString(),GeneralMqttVariables.QOS,GeneralMqttVariables.TOPIC);
                System.out.println("Added " + collectionToWrite.getNamespace().getFullName());
            } catch (MongoWriteException e) {
                if (e.getError().getCategory() == ErrorCategory.DUPLICATE_KEY) {
                    System.out.println("Found Duplicate");
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

    }

}
