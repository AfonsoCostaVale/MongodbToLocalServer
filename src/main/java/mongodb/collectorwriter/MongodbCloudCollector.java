package mongodb.collectorwriter;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;

public class MongodbCloudCollector {

    final static String user = "aluno";
    final static String databaseUsers = "admin";
    final static String database = "sid2021";
    final static String ip = "194.210.86.10";
    final static int port = 27017;
    final static char[] password = {'a', 'l', 'u', 'n', 'o'};
    final static String[] collections = {"sensorh1", "sensorh2", "sensorl1", "sensorl2", "sensort1", "sensort2"};
    private MongodbCloudCollectorData data;

    public MongodbCloudCollector(MongodbCloudCollectorData data) {
        this.data = data;
    }

    public static void collect() {
        try {
            writeInfo(createClient().getDatabase(database));
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("Could not write collections to local server");
        }
    }

    private static MongoClient createClient() {
        MongoCredential credential = MongoCredential.createCredential(user, databaseUsers, password);
        MongoClient mongoClient = new MongoClient(new ServerAddress(ip, port), Arrays.asList(credential));
        return mongoClient;
    }

    private static void writeInfo(MongoDatabase db) throws InterruptedException {

        ArrayList<MongodbLocalWriter> writers = new ArrayList<>();

        //   System.out.println("Started writing collections");

        for (String collection : collections) {
            MongoCollection<Document> table = db.getCollection(collection);
            MongodbLocalWriter mongodbLocalWriter = new MongodbLocalWriter(collection, table);
            writers.add(mongodbLocalWriter);
            mongodbLocalWriter.start();
        }

      /*  for(MongodbLocalWriter mongodbLocalWriter: writers){
            mongodbLocalWriter.join();
        }

        System.out.println("Finished writing collections");*/

    }

    public MongodbCloudCollectorData getData() {
        return data;
    }

    public void setData(MongodbCloudCollectorData data) {
        this.data = data;
    }

    public static class MongodbCloudCollectorData {
        public static final String USER = "user";
        public static final String DATABASEUSERS = "databaseUsers";
        public static final String DATABASE = "database";
        public static final String IP = "ip";
        public static final String PORT = "port";
        public static final String PASSWORD = "password";
        public static final String COLLECTIONS = "collections";
        private String user;
        private String databaseUsers;
        private String database;
        private String ip;
        private int port;
        private char[] password;
        private String[] collections;

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getDatabaseUsers() {
            return databaseUsers;
        }

        public void setDatabaseUsers(String databaseUsers) {
            this.databaseUsers = databaseUsers;
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
