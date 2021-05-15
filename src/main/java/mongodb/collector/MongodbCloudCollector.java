package mongodb.collector;

import mongodb.writer.MongodbLocalWriter;
import mongodb.writer.MongodbLocalWriterMQTT;
import mqtt.GeneralMqttVariables;
import mqtt.MQTTWriter;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.ArrayList;
import java.util.Collections;

public class MongodbCloudCollector extends Thread {

    private MongodbCloudCollectorData data;
    private ArrayList<MongodbLocalWriter> writers;

    public MongodbCloudCollector(MongodbCloudCollectorData data) {
        this.data = data;
        writers = new ArrayList<>();
    }

    public boolean removeWriter(String collectionName) {
        for (MongodbLocalWriter writer : writers) {
            if (writer.getCollectionName().equals(collectionName)) {
                writer.interrupt();
                return true;
            }

        }

        return false;
    }

    public void run() {
        collect();
    }

    private void collect() {
        try {
            writeInfo(createClient().getDatabase(getData().getDatabase()));
        } catch (InterruptedException e) {
            //e.printStackTrace();
            System.out.println("Could not write collections to local server");
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

    private MongoClient createClient() {
        MongoCredential credential = MongoCredential.createCredential(data.getUser(), data.getDatabaseUser(), data.getPassword());
        return new MongoClient(new ServerAddress(data.getIp(), data.getPort()), Collections.singletonList(credential));
    }

    private void writeInfo(MongoDatabase db) throws InterruptedException, MqttException {

        MQTTWriter mqttWriter= new MQTTWriter(GeneralMqttVariables.BROKER,GeneralMqttVariables.CLIENT_ID,GeneralMqttVariables.PERSISTENCE);
        mqttWriter.connect();
        for (String collection : data.getCollections()) {
            MongoCollection<Document> table = db.getCollection(collection);
            MongodbLocalWriter mongodbLocalWriter = new MongodbLocalWriterMQTT(collection, table,mqttWriter);
            writers.add(mongodbLocalWriter);
            mongodbLocalWriter.start();
        }

        for (MongodbLocalWriter mongodbLocalWriter : writers) {
            mongodbLocalWriter.join();
        }

    }

    public MongodbCloudCollectorData getData() {
        return data;
    }

    public void setData(MongodbCloudCollectorData data) {
        this.data = data;
    }

    public void killWriters() {
        for (MongodbLocalWriter mongodbLocalWriter : writers) {
            mongodbLocalWriter.interrupt();
        }
    }

}
