package MQTT;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class GeneralMqttVariables {
    public static final String TOPIC = "SidTest-JoaoAlmeida";
    public static final String CONTENT = "Message from MqttPublishSample";
    public static final int QOS = 2;
    //public static final String broker = "tcp://mqtt.eclipse.org:1883";
    //public static final String broker = "tcp:public.mqtthq.com";
    //public static final String broker = "tcp:broker.mqttdashboard.com:8000";
    public static final String BROKER = "tcp:localhost:8000";
    public static final String CLIENT_ID = MqttClient.generateClientId();
    public static final MemoryPersistence PERSISTENCE = new MemoryPersistence();

}