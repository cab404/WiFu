package com.cab404.wifu.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

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
        // We don't need no stinkin' intents!
        // (no, srsly, there's no bssid in there)
        Butler.getInstance().onNetworkStatusUpdate(context);
    }
}
