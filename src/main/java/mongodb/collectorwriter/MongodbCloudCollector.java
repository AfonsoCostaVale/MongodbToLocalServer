package mongodb.collectorwriter;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;

public class MongodbCloudCollector extends Thread {

    private MongodbCloudCollectorData data;
    private ArrayList<MongodbLocalWriter> writers;

    public MongodbCloudCollector(MongodbCloudCollectorData data) {
        this.data = data;
        writers = new ArrayList<>();
    }

    public boolean removeWriter(String collectionName) {
        for (MongodbLocalWriter writer : writers) {
            if(writer.getCollectionName().equals(collectionName)){
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
            e.printStackTrace();
            System.out.println("Could not write collections to local server");
        }
    }

    private MongoClient createClient() {
        MongoCredential credential = MongoCredential.createCredential(data.getUser(), data.getDatabaseUser(), data.getPassword());
        MongoClient mongoClient = new MongoClient(new ServerAddress(data.getIp(), data.getPort()), Arrays.asList(credential));
        return mongoClient;
    }

    private void writeInfo(MongoDatabase db) throws InterruptedException {

        for (String collection : data.getCollections()) {
            MongoCollection<Document> table = db.getCollection(collection);
            MongodbLocalWriter mongodbLocalWriter = new MongodbLocalWriter(collection, table);
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
