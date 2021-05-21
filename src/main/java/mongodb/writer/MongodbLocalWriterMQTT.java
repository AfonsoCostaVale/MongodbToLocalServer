package mongodb.writer;

import com.mongodb.client.FindIterable;
import mongodb.collector.MongodbCloudCollectorData;
import mqtt.GeneralMqttVariables;
import mqtt.MQTTWriter;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;

public class MongodbLocalWriterMQTT extends MongodbLocalWriter {

    private final MQTTWriter mqttWriter;

    public void disconnect() throws MqttException {
        mqttWriter.disconnect();
    }

    public MongodbLocalWriterMQTT(String collection, MongoCollection<Document> collectionToRead, MQTTWriter mqttWriter, MongodbCloudCollectorData data) {
        localMongoClient = new MongoClient("localhost", 27017);
        localDB = localMongoClient.getDatabase(data.getLocalmongodbname());
        this.collectionToWrite = localDB.getCollection(collection);
        this.collectionToRead = collectionToRead;
        collectionName = collectionToRead.getNamespace().getFullName();
        this.mqttWriter = mqttWriter;
        this.data = data;
        this.setName("Thread MQTT-"+collection);
    }

    public void run() {
        try {
            System.out.println(this.getName() + " Started writing in " + collectionToWrite.getNamespace().getFullName());

            BasicDBObject dbQuerry= new BasicDBObject();
            dbQuerry.put("Data", new BasicDBObject("$gt",  data.getDateString()));
            FindIterable<Document> documents = collectionToRead.find(dbQuerry);

            for (Document entry : documents) {
                if (!this.isInterrupted()) {
                    try {
                        write(entry);
                        mqttWriter.sendMessage(entry.toString(), GeneralMqttVariables.TOPIC);
                    } catch (MongoWriteException e) {
                        //if (e.getError().getCategory() == ErrorCategory.DUPLICATE_KEY) {
                        //    System.out.println("Found Duplicate");
                        //}
                    }
                }else{
                    break;
                }
            }
            //mqttWriter.disconnect();
            enterCheckMode();

        } catch (MongoInterruptedException e) {
            e.printStackTrace();
        } catch (MongoTimeoutException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            //e.printStackTrace();
        } finally {
            try {
                mqttWriter.disconnect();
            } catch (MqttException e) {
                //e.printStackTrace();
                System.out.println("Problemas a desconectar o mqttwritter!");
            }
        }

    }

    protected void enterCheckMode(){

        System.out.println("Entered check mode " + collectionToWrite.getNamespace().getFullName());
        while (!Thread.currentThread().isInterrupted()) {
            try {
                FindIterable<Document> documentsToWrite = collectionToRead.find(MongodbCloudCollectorData.getLastMinuteDBQuery());
                for (Document doc:documentsToWrite) {
                    if (doc != null){
                        write(doc);
                        mqttWriter.sendMessage(doc.toString(), GeneralMqttVariables.TOPIC);
                        //System.out.println("Added " + collectionToWrite.getNamespace().getFullName());
                    }
                }
            } catch (MongoWriteException e) {
                //if (e.getError().getCategory() == ErrorCategory.DUPLICATE_KEY) {
                    //System.out.println("Found Duplicate");
                //}
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (MqttException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException interruptedException) {
                //interruptedException.printStackTrace();

            }
        }
        System.out.println("Exiting checkMode");

    }

}
