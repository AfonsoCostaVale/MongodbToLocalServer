package mqtt;

import org.eclipse.paho.client.mqttv3.*;

import java.io.*;

import static mqtt.GeneralMqttVariables.*;

public class MQTTWriter {

    private IMqttClient client;
    private MqttConnectOptions connOpts;

    public MQTTWriter(String broker, String clientID, MqttClientPersistence persistence) throws MqttException {
        client = new MqttClient(broker, clientID, persistence);
        connOpts = new MqttConnectOptions();
        connOpts.setAutomaticReconnect(true);
        connOpts.setCleanSession(true);
    }

    public void connect() throws MqttException {
        //System.out.println("Connecting to broker: " + BROKER);
        client.connect(connOpts);
        //System.out.println("Connected");
    }

    public void sendMessage(String content, int qos, String topic) throws MqttException, IOException {
        //System.out.println("Publishing message: " + content);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(content);
        oos.flush();
        byte [] data = bos.toByteArray();

        MqttMessage message = new MqttMessage(data);
        message.setQos(qos);
        client.publish(topic, message);

        //System.out.println("Message published");
    }

    public void disconnect() throws MqttException {
        client.disconnect();
        //System.out.println("Disconnected");

    }
}
