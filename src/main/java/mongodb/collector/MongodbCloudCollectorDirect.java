package mongodb.collector;

import mongodb.writer.MongodbLocalWriter;
import mongodb.writer.MongodbLocalWriterDirect;
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
import sql.CulturaDB;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class MongodbCloudCollectorDirect extends MongodbCloudCollector {

    public MongodbCloudCollectorDirect(MongodbCloudCollectorData data) {
        this.data = data;
        writers = new ArrayList<>();
    }

    @Override
    protected void writeInfo(MongoDatabase db) throws InterruptedException, SQLException {
        Connection connection = CulturaDB.getLocalConnection();
//        MQTTWriter mqttWriter= new MQTTWriter(GeneralMqttVariables.BROKER,GeneralMqttVariables.CLIENT_ID,GeneralMqttVariables.PERSISTENCE);
//        mqttWriter.connect();
        for (String collection : data.getCollections()) {
            MongoCollection<Document> table = db.getCollection(collection);

//          MongodbLocalWriter mongodbLocalWriter = new MongodbLocalWriterMQTT(collection, table,mqttWriter);
            MongodbLocalWriter mongodbLocalWriter = new MongodbLocalWriterDirect(collection, table, connection);
            writers.add(mongodbLocalWriter);
            mongodbLocalWriter.start();
        }

        for (MongodbLocalWriter mongodbLocalWriter : writers) {
            mongodbLocalWriter.join();
        }

    }


}
