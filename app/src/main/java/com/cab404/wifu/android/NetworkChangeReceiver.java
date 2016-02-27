package com.cab404.wifu.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;

import com.cab404.wifu.util.Butler;

/**
 * Butler does all the dirty work.
 *
 * @author cab404
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.TYPE_WIFI != intent.getIntExtra(ConnectivityManager.EXTRA_NETWORK_TYPE, 0))
            return;
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        // We don't need no stinkin' intents!
        // (no, srsly, there's no bssid in there)
        Butler.getInstance().onNetworkConnect(new AndroidWifiInfo(manager.getConnectionInfo()));
    }
}
