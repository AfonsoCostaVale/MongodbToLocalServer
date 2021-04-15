package mongodb.collectorwriter;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;

public class MongodbCloudCollector extends Thread{

    private MongodbCloudCollectorData data;
    private ArrayList<MongodbLocalWriter> writers;

    public MongodbCloudCollector(MongodbCloudCollectorData data) {
        this.data = data;
        writers = new ArrayList<>();
    }

    public void run(){
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

        for(MongodbLocalWriter mongodbLocalWriter: writers){
            mongodbLocalWriter.join();
        }

    }

    public MongodbCloudCollectorData getData() {
        return data;
    }

    public void setData(MongodbCloudCollectorData data) {
        this.data = data;
    }

    public static class MongodbCloudCollectorData {
        public static final String USER = "user";
        public static final String DATABASEUSER = "databaseUsers";
        public static final String DATABASE = "database";
        public static final String IP = "ip";
        public static final String PORT = "port";
        public static final String PASSWORD = "password";
        public static final String COLLECTIONS = "collections";
        private String user;
        private String databaseUser;
        private String database;
        private String ip;
        private int port;
        private char[] password;
        private String[] collections;

        public MongodbCloudCollectorData() {}
        public MongodbCloudCollectorData(String user,
                                         String databaseUser,
                                         String database,
                                         String ip,
                                         int port,
                                         char[] password,
                                         String[] collections) {
            this.user = user;
            this.databaseUser = databaseUser;
            this.database = database;
            this.ip = ip;
            this.port = port;
            this.password = password;
            this.collections = collections;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getDatabaseUser() {
            return databaseUser;
        }

        public void setDatabaseUser(String databaseUser) {
            this.databaseUser = databaseUser;
        }

        public String getDatabase() {
            return database;
        }

        public void setDatabase(String database) {
            this.database = database;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public char[] getPassword() {
            return password;
        }

        public void setPassword(char[] password) {
            this.password = password;
        }

        public String[] getCollections() {
            return collections;
        }

        public void setCollections(String[] collections) {
            this.collections = collections;
        }

    }

}
