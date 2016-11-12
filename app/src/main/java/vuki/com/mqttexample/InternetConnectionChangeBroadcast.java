package vuki.com.mqttexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mvukosav on 9.11.2016..
 * Copyright by @ MqttExample
 */
public class InternetConnectionChangeBroadcast extends BroadcastReceiver {

    protected final IntentFilter defaultIntentFilter = new IntentFilter( ConnectivityManager.CONNECTIVITY_ACTION );
    private List<OnInternetChangeListener> onInternetChangeListeners = new ArrayList<>();
    private static InternetConnectionChangeBroadcast instance;

    private InternetConnectionChangeBroadcast() {
    }

    public static InternetConnectionChangeBroadcast getInstance() {
        if( instance == null ) {
            instance = new InternetConnectionChangeBroadcast();
        }
        return instance;
    }

    @Override
    public void onReceive( Context context, Intent intent ) {

        if( isConnected( context ) ) {
            notifyConnectionChange( true );
        } else {
            notifyConnectionChange( false );
        }
    }

    private void notifyConnectionChange( boolean hasInternet ) {
        for( OnInternetChangeListener listener : onInternetChangeListeners ) {
            if( hasInternet ) {
                listener.onConnected();
            } else {
                listener.onConnectionLost();
            }
        }
    }

    public boolean isConnected( Context context ) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public void addListener( Context context, OnInternetChangeListener listener, @Nullable IntentFilter intentFilter ) {
        if( intentFilter == null ) {
            intentFilter = defaultIntentFilter;
        }
        context.registerReceiver( this, intentFilter );
        onInternetChangeListeners.add( listener );
    }

    public void removeListener( Context context, OnInternetChangeListener listener ) {
        context.unregisterReceiver( this );
        onInternetChangeListeners.remove( listener );
    }

    interface OnInternetChangeListener {
        void onConnected();

        void onConnectionLost();
    }

}