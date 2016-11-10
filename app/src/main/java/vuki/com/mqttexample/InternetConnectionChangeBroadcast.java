package vuki.com.mqttexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mvukosav on 9.11.2016..
 * Copyright by @ MqttExample
 */
public class InternetConnectionChangeBroadcast extends BroadcastReceiver {

    private static List<OnInternetChangeListener> onInternetChangeListeners = new ArrayList<>();

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

    public void addListener( OnInternetChangeListener listener ) {
        onInternetChangeListeners.add( listener );
    }

    public void removeListener( OnInternetChangeListener listener ) {
        onInternetChangeListeners.remove( listener );
    }

    interface OnInternetChangeListener {
        void onConnected();

        void onConnectionLost();
    }

}