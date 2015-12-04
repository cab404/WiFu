package com.cab404.wifu.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Butler does all the dirty work.
 *
 * @author cab404
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // We don't need no stinkin' intents!
        // (no, srsly, there's no bssid in there)
        Butler.getInstance().onNetworkStatusUpdate(context);
    }
}
