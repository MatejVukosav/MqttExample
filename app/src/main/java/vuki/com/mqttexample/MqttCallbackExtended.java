package vuki.com.mqttexample;

import org.eclipse.paho.client.mqttv3.MqttCallback;

/**
 * Created by mvukosav on 9.11.2016..
 * Copyright by @ MqttExample
 */
public interface MqttCallbackExtended extends MqttCallback {

    /**
     * Called when the connection to the server is completed successfully.
     * @param reconnect If true, the connection was the result of automatic reconnect.
     * @param serverURI The server URI that the connection was made to.
     */
    public void connectComplete(boolean reconnect, String serverURI);

}
