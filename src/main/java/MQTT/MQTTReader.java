package MQTT;

import org.eclipse.paho.client.mqttv3.*;

import static MQTT.GeneralMqttVariables.*;

public class MQTTReader {

    MqttClient sampleClient;
    MqttConnectOptions connOpts;

    public MQTTReader(String broker, String clientID, MqttClientPersistence persistence) throws MqttException {
        sampleClient = new MqttClient(broker, clientID, persistence);
        connOpts = new MqttConnectOptions();
        connOpts.setAutomaticReconnect(true);
        connOpts.setCleanSession(true);
    }

    public static void main(String[] args) {
        try {
            MQTTReader reader = new MQTTReader(BROKER, CLIENT_ID, PERSISTENCE);
            reader.connect();
            reader.subscribe();
            reader.unsubscribe();
            reader.disconnect();
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


    public void subscribe() throws MqttException {
        System.out.println("Subscribing to broker: " + BROKER);
        sampleClient.subscribe(TOPIC,QOS,(s, mqttMessage) -> System.out.println(mqttMessage));

        System.out.println("Subscribed");
    }

    private void unsubscribe() throws MqttException {
        sampleClient.unsubscribe(TOPIC);
    }


    public void disconnect() throws MqttException {
        sampleClient.disconnect();
        System.out.println("Disconnected");

    }

}
