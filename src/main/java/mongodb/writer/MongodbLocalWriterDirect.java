package mongodb.writer;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.eclipse.paho.client.mqttv3.MqttException;
import sql.CulturaDB;

import java.sql.Connection;
import java.sql.SQLException;

public class MongodbLocalWriterDirect extends MongodbLocalWriter {

    private final Connection connection;

    public MongodbLocalWriterDirect(String collection, MongoCollection<Document> collectionToRead, Connection connection) {
        localMongoClient = new MongoClient("localhost", 27017);
        localDB = localMongoClient.getDatabase("sid");
        this.collectionToWrite = localDB.getCollection(collection);
        this.collectionToRead = collectionToRead;
        collectionName = collectionToRead.getNamespace().getFullName();
        this.connection = connection;
    }

    public void run() {
        try {
            System.out.println("Started writing in " + collectionToWrite.getNamespace().getFullName());

            for (Document entry : collectionToRead.find().skip((int) collectionToWrite.count())) {
                try {
                    write(entry);
                    CulturaDB.insertMedicao(entry.toString(), this.connection);
                } catch (MongoWriteException e) {
                    if (e.getError().getCategory() == ErrorCategory.DUPLICATE_KEY) {
                        System.out.println("Found Duplicate");
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
                enterCheckMode();


        } catch (MongoInterruptedException e) {
            e.printStackTrace();
        } catch (MongoTimeoutException e) {
            e.printStackTrace();
        }
    }

    protected void enterCheckMode(){

        System.out.println("Entered check mode " + collectionToWrite.getNamespace().getFullName());
        while (true) {
            try {
                Document documentToWrite = collectionToRead.find().skip((int) collectionToWrite.count()).first();
                write(documentToWrite);
                // mqttWriter.sendMessage(documentToWrite.toString(),GeneralMqttVariables.QOS,GeneralMqttVariables.TOPIC);
                System.out.println("Added " + collectionToWrite.getNamespace().getFullName());
            } catch (IllegalArgumentException e) {

            }
        }

    }

}
