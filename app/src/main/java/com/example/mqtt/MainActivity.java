package com.example.mqtt;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "myTag";
    Button btn;
    TextView textview;
    MqttAndroidClient client;
    String topic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        btn=(Button) findViewById(R.id.button);
        String clientId = MqttClient.generateClientId();
        topic="test/H2polito";
         client =
                new MqttAndroidClient(this.getApplicationContext(), "tcp://broker.hivemq.com:1883",
                        clientId);

        btn.setOnClickListener(view -> {
            connectX();
        });

    }

    private void connectX() {
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "onSuccess");
                    sub();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "onFailure");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void sub(){
    try{
       client.subscribe(topic,0);
       client.setCallback(new MqttCallback() {
           @Override
           public void connectionLost(Throwable cause) {
               //log
           }

           @Override
           public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d(TAG, "message:"+ new String(message.getPayload()));
           }

           @Override
           public void deliveryComplete(IMqttDeliveryToken token) {
                //toast
           }
       });
    }catch(MqttException e){
        e.printStackTrace();
    }
    }
}