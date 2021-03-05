package mongodb.collectorwriter;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongodbLocalWriter extends Thread{
    private MongoClient mongoClient;
    private MongoDatabase db;
    private MongoCollection<Document> collectionToWrite;
    private MongoCollection<Document> collectionToRead;

    protected MongodbLocalWriter(String collection, MongoCollection<Document> collectionToRead){
        mongoClient = new MongoClient( "localhost" , 27017 );
        db = mongoClient.getDatabase("sid");
        this.collectionToWrite = db.getCollection(collection);
        this.collectionToRead = collectionToRead;
    }

    public void run() {
        System.out.println("Started writing in "+ collectionToWrite.getNamespace().getFullName());

        for(Document entry : collectionToRead.find()) {
            write(entry);
        }

        System.out.println("Finished writing in "+ collectionToWrite.getNamespace().getFullName());
    }

    protected void write(Document doc){
        collectionToWrite.insertOne(doc);
    }

}
