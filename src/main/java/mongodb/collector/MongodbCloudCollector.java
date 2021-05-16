package mongodb.collector;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import mongodb.writer.MongodbLocalWriter;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public abstract class MongodbCloudCollector extends Thread{

    protected MongodbCloudCollectorData data;
    protected ArrayList<MongodbLocalWriter> writers;

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

    protected void collect() {
        try {
            writeInfo(createClient().getDatabase(getData().getDatabase()));
        } catch (InterruptedException e) {
            //e.printStackTrace();
            System.out.println("Could not write collections to local server");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    protected abstract void writeInfo(MongoDatabase database) throws InterruptedException, SQLException;

    protected MongoClient createClient() {
        MongoCredential credential = MongoCredential.createCredential(data.getUser(), data.getDatabaseUser(), data.getPassword());
        return new MongoClient(new ServerAddress(data.getIp(), data.getPort()), Collections.singletonList(credential));
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
