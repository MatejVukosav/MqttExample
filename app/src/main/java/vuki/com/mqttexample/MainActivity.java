package vuki.com.mqttexample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import vuki.com.mqttexample.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements InternetConnectionChangeBroadcast.OnInternetChangeListener {
    ActivityMainBinding binding;

    MqttAndroidClient mqttAndroidClient;
    private static final String TAG = "mqttTest";

    private static final String SERVER_URI = "tcp://test.mosquitto.org:1883";
    private static final String CLIENT_ID = "MqttExample";
    //    final String SUBSCRIPTION_TOPIC = "mqttExampleTopic";
    private static final String SUBSCRIPTION_TOPIC = "temperatura";
    private static final String PUBLIS_TOPIC = "mqttExamplePublishTopic";

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        binding = DataBindingUtil.setContentView( this, R.layout.activity_main );

        InternetConnectionChangeBroadcast.getInstance().addListener( this, this, null );

        init();
        initListeners();

        //for smooth transition between pictures
//        binding.appBar.addOnOffsetChangedListener( new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged( AppBarLayout appBarLayout, int verticalOffset ) {
//                //measuring for alpha
//                int toolBarHeight = binding.toolbar.getMeasuredHeight();
//                int appBarHeight = appBarLayout.getMeasuredHeight();
//                Float f = ( ( ( (float) appBarHeight - toolBarHeight ) + verticalOffset ) / ( (float) appBarHeight - toolBarHeight ) ) * 255;
//                binding.fadingBackdrop.getBackground().setAlpha( 255 - Math.round( f ) );
//            }
//        } );
    }

    @Override
    protected void onDestroy() {
        InternetConnectionChangeBroadcast.getInstance().removeListener( this, this );
        super.onDestroy();
    }

    private void init() {
        setSupportActionBar( binding.toolbar );
        mqttAndroidClient = new MqttAndroidClient( getApplicationContext(), SERVER_URI, CLIENT_ID );
    }

    private void initListeners() {
        mqttAndroidClient.setCallback( new MqttCallbackExtended() {
            @Override
            public void connectComplete( boolean reconnect, String serverURI ) {
                if( reconnect ) {
                    subscribeToTopic();
                    Log.d( TAG, "recconect" );
                }
            }

            @Override
            public void connectionLost( Throwable cause ) {
                Toast.makeText( MainActivity.this, "Connection lost", Toast.LENGTH_SHORT ).show();
            }

            @Override
            public void messageArrived( String topic, MqttMessage message ) throws Exception {
                binding.txtResult.setText( "Topic: " + topic + " Message: " + message.toString() );
            }

            @Override
            public void deliveryComplete( IMqttDeliveryToken token ) {

            }
        } );
//        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
//        mqttConnectOptions.setCleanSession( false );

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession( false );
        mqttConnectOptions.setAutomaticReconnect( true );
        try {
            mqttAndroidClient.connect( mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess( IMqttToken asyncActionToken ) {
                    subscribeToTopic();
                    Log.d( TAG, "Success subscription to topic" );
                }

                @Override
                public void onFailure( IMqttToken asyncActionToken, Throwable exception ) {
                    Toast.makeText( MainActivity.this, "On failure connection", Toast.LENGTH_SHORT ).show();

                }
            } );
        } catch( MqttException e ) {
            e.printStackTrace();
        }

        binding.btnSend.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                binding.txtResult.setText( "" );
                publishMessage();
            }
        } );

        binding.btnSubscribe.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                subscribeToTopic();
            }
        } );
    }

    public void subscribeToTopic() {
        try {
            mqttAndroidClient.subscribe( SUBSCRIPTION_TOPIC, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess( IMqttToken asyncActionToken ) {
                    Log.d( TAG, "Success subscription to topic" );
                    binding.btnSubscribe.setText( "Subscribed to topic: " + SUBSCRIPTION_TOPIC );
                }

                @Override
                public void onFailure( IMqttToken asyncActionToken, Throwable exception ) {
                    Log.d( TAG, "Failure subscription to topic" );
                    binding.btnSubscribe.setText( "Subscribe to topic" );

                }
            } );
        } catch( MqttException e ) {
            e.printStackTrace();
        }
    }

    public void publishMessage() {
        MqttMessage message = new MqttMessage();
        String temperature = binding.editTemperature.getText().toString();
        message.setPayload( temperature.getBytes() );
        try {
            mqttAndroidClient.publish( PUBLIS_TOPIC, message );
        } catch( MqttException e ) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected() {
        binding.txtInternetStatus.setText( "Online" );
    }

    @Override
    public void onConnectionLost() {
        binding.txtInternetStatus.setText( "Offline" );

    }
}
