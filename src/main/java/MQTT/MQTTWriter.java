package MQTT;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import mongodb.collectorwriter.MongodbCloudCollectorData;
import org.eclipse.paho.client.mqttv3.*;

import javax.swing.text.Document;
import java.io.*;
import java.util.Scanner;

import static MQTT.GeneralMqttVariables.*;

public class MQTTWriter {

    IMqttClient sampleClient;
    MqttConnectOptions connOpts;

    public MQTTWriter(String broker, String clientID, MqttClientPersistence persistence) throws MqttException {
        sampleClient = new MqttClient(broker, clientID, persistence);
        connOpts = new MqttConnectOptions();
        connOpts.setAutomaticReconnect(true);
        connOpts.setCleanSession(true);

    }

    public static void main(String[] args) throws IOException {
        try {
            MQTTWriter writer = new MQTTWriter(BROKER, CLIENT_ID, PERSISTENCE);
            writer.connect();
            //writer.sendMessage(CONTENT, QOS, TOPIC);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(new MongodbCloudCollectorData().getCollections());
            oos.flush();
            byte [] data = bos.toByteArray();

            MqttMessage message = new MqttMessage(data);
            message.setQos(QOS);
            writer.sampleClient.publish(TOPIC, message);
            System.out.println("message sent:" + message);

            System.out.println("You can start sending your own messages:");
            String input = "";
            while(!input.equals("quit")){
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                input = reader.readLine();
                writer.sendMessage(input, QOS, TOPIC);
            }

            writer.disconnect();
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }

    }

    public void connect() throws MqttException {
        System.out.println("Connecting to broker: " + BROKER);
        sampleClient.connect(connOpts);
        System.out.println("Connected");
    }

    public void sendMessage(String content, int qos, String topic) throws MqttException, IOException {
        System.out.println("Publishing message: " + content);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(content);
        oos.flush();
        byte [] data = bos.toByteArray();

        MqttMessage message = new MqttMessage(data);
        message.setQos(qos);
        sampleClient.publish(topic, message);

        System.out.println("Message published");
    }

    public void disconnect() throws MqttException {
        sampleClient.disconnect();
        System.out.println("Disconnected");

    }
}
