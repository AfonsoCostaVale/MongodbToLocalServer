package mongodb.writer;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.eclipse.paho.client.mqttv3.MqttException;

public abstract class MongodbLocalWriter extends Thread {
    protected MongoClient localMongoClient;
    protected MongoDatabase localDB;
    protected MongoCollection<Document> collectionToWrite;
    protected MongoCollection<Document> collectionToRead;
    protected String collectionName;

    public abstract void run();

    protected abstract void enterCheckMode() throws MqttException;

    public String getCollectionName() {
        return collectionName;
    }

    protected void write(Document doc) {
        collectionToWrite.insertOne(doc);

    }

}
