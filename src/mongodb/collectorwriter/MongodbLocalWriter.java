package mongodb.collectorwriter;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongodbLocalWriter {
    private MongoClient mongoClient;
    private MongoDatabase db;
    private MongoCollection<Document> collection;

    protected MongodbLocalWriter(){
        mongoClient = new MongoClient( "localhost" , 27017 );
        db = mongoClient.getDatabase("sid");
        collection = null;
    }

    protected void write(Document doc){
        collection.insertOne(doc);
        System.out.println("Entry added in collection "+ collection.getNamespace());
    }

    protected void setCollection(String collection){
        this.collection = db.getCollection(collection);
    }

}
