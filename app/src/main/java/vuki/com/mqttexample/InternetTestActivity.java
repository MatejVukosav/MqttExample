package vuki.com.mqttexample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import vuki.com.mqttexample.databinding.ActivityInternetTestBinding;

public class InternetTestActivity extends AppCompatActivity implements InternetConnectionChangeBroadcast.OnInternetChangeListener {

    ActivityInternetTestBinding binding;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        binding = DataBindingUtil.setContentView( this, R.layout.activity_internet_test );

        InternetConnectionChangeBroadcast.getInstance().addListener( this, this, null );

    }

    @Override
    protected void onDestroy() {
        InternetConnectionChangeBroadcast.getInstance().removeListener( this, this );
        super.onDestroy();
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
