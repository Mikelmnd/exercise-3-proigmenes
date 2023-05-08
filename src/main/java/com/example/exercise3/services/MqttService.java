package com.example.exercise3.services;

import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
public class MqttService implements MqttCallback {
    MqttClient myClient;
    MqttConnectOptions connOpt;
    static final String M2MIO_THING = UUID.randomUUID().toString();
    static final String BROKER_URL = "tcp://test.mosquitto.org:1883";
    static final Boolean subscriber = false;
    static final Boolean publisher = true;
    private Random rnd = new Random();
    private static final Logger log = LoggerFactory.getLogger(MqttService.class);
    public static final String TOPIC = "grupatras/lab/engine/message";
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public void connectionLost(Throwable t) {
        log.info("Connection lost!");
// code to reconnect to the broker would go here if desired
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {
        log.info("\n");
        log.info("-------------------------------------------------");
        log.info("| Topic:" + topic);
        log.info("| Message: " + new String(message.getPayload()));
        log.info("-------------------------------------------------");
        log.info("\n");
    }

    public void runClient() {
        String clientID = M2MIO_THING;
        connOpt = new MqttConnectOptions();
        connOpt.setCleanSession(true);
        connOpt.setKeepAliveInterval(30);
        try {
            myClient = new MqttClient(BROKER_URL, clientID);
            myClient.setCallback(this);
            myClient.connect(connOpt);
        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        log.info("Connected to " + BROKER_URL);
        String myTopic = TOPIC;
        MqttTopic topic = myClient.getTopic(myTopic);
        if (subscriber) {
            try {
                int subQoS = 0;
                myClient.subscribe(myTopic, subQoS);
                if (!publisher) {
                    while (true) {
                        Thread.sleep(1000);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (publisher) {
            double temp = 80 + rnd.nextDouble() * 20.0;
            String val = this.message;
            String pubMsg = "{\"Message\":" + val + "}";
            int pubQoS = 0;
            MqttMessage message = new MqttMessage(pubMsg.getBytes());
            message.setQos(pubQoS);
            message.setRetained(false);

            log.info("Publishing to topic \"" + topic + "\" qos " + pubQoS + "\" value " + val);
            MqttDeliveryToken token = null;
            try {

                token = topic.publish(message);

                token.waitForCompletion();
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        try {

            if (subscriber) {
                Thread.sleep(5000);
            }
            myClient.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
