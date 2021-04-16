package MQTT;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTWriter {
    private static String topic = "MQTT Examples";
    private static String content = "Message from MqttPublishSample";
    private static int qos = 2;
    private static String broker = "tcp://mqtt.eclipse.org:1883";
    private static String clientId = "JavaSample";
    private static MemoryPersistence persistence = new MemoryPersistence();
    MqttClient sampleClient;
    MqttConnectOptions connOpts;

    public MQTTWriter(String broker, String clientID, MqttClientPersistence persistence) throws MqttException {
        sampleClient = new MqttClient(broker, clientID, persistence);
        connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
    }

    public static void main(String[] args) {
        try {
            MQTTWriter writer = new MQTTWriter("broker.mqttdashboard.com", clientId, persistence);
            writer.connect();
            writer.sendMessage("boas pessoal", 2, "SID-test");
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
        System.out.println("Connecting to broker: " + broker);
        sampleClient.connect(connOpts);
        System.out.println("Connected");
    }

    public void sendMessage(String content, int qos, String topic) throws MqttException {
        System.out.println("Publishing message: " + content);

        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(qos);
        sampleClient.publish(topic, message);

        System.out.println("Message published");
    }

    public void disconnect() throws MqttException {
        sampleClient.disconnect();
        System.out.println("Disconnected");

    }
}
