package mongodb.collector;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import mongodb.writer.MongodbLocalWriter;
import mongodb.writer.MongodbLocalWriterDirect;
import mongodb.writer.MongodbLocalWriterMQTT;
import mqtt.GeneralMqttVariables;
import mqtt.MQTTWriter;
import org.bson.Document;
import org.eclipse.paho.client.mqttv3.MqttException;
import sql.CulturaDB;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class MongodbCloudCollectorMQTT extends MongodbCloudCollector{

    public MongodbCloudCollectorMQTT(MongodbCloudCollectorData data) {
        this.data = data;
        writers = new ArrayList<>();
    }

    @Override
    protected void writeInfo(MongoDatabase db) throws InterruptedException, SQLException {
        Connection connection = CulturaDB.getLocalConnection();
        MQTTWriter mqttWriter= null;
        try {
            mqttWriter = new MQTTWriter(GeneralMqttVariables.BROKER,GeneralMqttVariables.CLIENT_ID,GeneralMqttVariables.PERSISTENCE);
            mqttWriter.connect();
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
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
}
