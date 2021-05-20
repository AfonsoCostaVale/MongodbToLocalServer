package mqtt;

import org.eclipse.paho.client.mqttv3.*;

import java.io.*;

public class MQTTWriter {

    private IMqttClient client;
    private MqttConnectOptions connOpts;
    private final int mqttqos;
    private final String mqttTopic;

    public MQTTWriter(String broker, int mqttqos, String mqttTopic, String clientID, MqttClientPersistence persistence) throws MqttException {
        client = new MqttClient(broker, clientID, persistence);
        connOpts = new MqttConnectOptions();
        connOpts.setAutomaticReconnect(true);
        connOpts.setCleanSession(true);
        this.mqttqos = mqttqos ;
        this.mqttTopic = mqttTopic;
    }

    public void connect() throws MqttException {
        //System.out.println("Connecting to broker: " + BROKER);
        client.connect(connOpts);
        //System.out.println("Connected");
    }

    public void sendMessage(String content, String topic) throws MqttException, IOException, InterruptedException{

            //System.out.println("Publishing message: " + content);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(content);
            oos.flush();
            byte [] data = bos.toByteArray();

            MqttMessage message = new MqttMessage(data);
            message.setQos(mqttqos);
            if (!Thread.currentThread().isInterrupted()) {
                client.publish(topic, message);
            }

        //System.out.println("Message published");
    }

    public void disconnect() throws MqttException {
        client.disconnect();
        //System.out.println("Disconnected");

    }
}
