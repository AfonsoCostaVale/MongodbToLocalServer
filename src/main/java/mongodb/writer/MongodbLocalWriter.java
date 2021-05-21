package mongodb.writer;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import mongodb.collector.MongodbCloudCollectorData;
import org.bson.Document;

public abstract class MongodbLocalWriter extends Thread {
    protected MongoClient localMongoClient;
    protected MongoDatabase localDB;
    protected MongoCollection<Document> collectionToWrite;
    protected MongoCollection<Document> collectionToRead;
    protected String collectionName;
    protected MongodbCloudCollectorData data;

    public abstract void run();

    protected abstract void enterCheckMode();

    public String getCollectionName() {
        return collectionName;
    }

    protected void write(Document doc) {
        collectionToWrite.insertOne(doc);

    }

}
