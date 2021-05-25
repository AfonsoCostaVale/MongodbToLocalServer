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

import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.gte;

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
            boolean first = true;
                String currentdbQuerry= MongodbCloudCollectorData.getLastMinuteDBQuery();
            while (!Thread.currentThread().isInterrupted()) {
                //System.out.println(this.getName() + " Started writing in " + collectionToWrite.getNamespace().getFullName());

                FindIterable<Document> documents;

                if (first) {
                    documents = collectionToRead.find(gte("Data",data.getDateString()));
                    first = false;
                } else {
                    documents = collectionToRead.find(gte("Data",currentdbQuerry));
                    currentdbQuerry= MongodbCloudCollectorData.getLastMinuteDBQuery();
                }
                if (!cloneDocuments(documents)) {
                    break;
                }
                //mqttWriter.disconnect();
                //enterCheckMode();
            }

        }catch (Exception e){
            System.out.println(this.getName()+ ERRO_GERAL_CONTACTE_O_SUPORTE);
        } finally {
            try {
                mqttWriter.disconnect();
            } catch (MqttException e) {
                //e.printStackTrace();
                System.out.println(this.getName()+" Problemas a desconectar o mqttwritter!");
            }
        }

    }
    private boolean cloneDocuments(FindIterable<Document> documents) {
        boolean first=true;
        int problems =0;
        for (Document entry : documents) {
            try {
                write(entry);
                mqttWriter.sendMessage(entry.toString(), GeneralMqttVariables.TOPIC);

            } catch (MongoWriteException ignored) {
            } catch ( MqttException throwables) {
                System.out.println(this.getName()+"-"+problems+"ºProblemas with MQTT Connection");
                problems++;
                if(problems ==10){
                    System.out.println(this.getName()+ OBTEVE_10_ERRORS_QUITTING);
                    return false;
                }
                //throwables.printStackTrace();
            }catch(Exception e){
                System.out.println(this.getName()+"General Error- Quitting");
                return false;
            }
        }
        return true;
    }
    /*
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
            }catch (Exception e){
                System.out.println(this.getName()+ ERRO_GERAL_CONTACTE_O_SUPORTE);
            }
        }
        System.out.println("Exiting checkMode");

    }
*/
}
