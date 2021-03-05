package mongodb.collectorwriter;

import com.mongodb.ErrorCategory;
import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
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

        /*
        TODO
            maybe compare the first ones to check if they deleted the first ones to save space
        */

        for(Document entry : collectionToRead.find().skip((int) collectionToWrite.count())) {
            try{
                write(entry);
            }catch (MongoWriteException e){
                if (e.getError().getCategory() == ErrorCategory.DUPLICATE_KEY) {
                    System.out.println("Found Duplicate");
                }
            }
        }

        System.out.println("Finished writing in "+ collectionToWrite.getNamespace().getFullName());
    }

    protected void write(Document doc){
        collectionToWrite.insertOne(doc);
    }

}
