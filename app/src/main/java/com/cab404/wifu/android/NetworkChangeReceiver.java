package com.cab404.wifu.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.cab404.wifu.R;
import com.cab404.wifu.base.WifiLoginModule;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Well, sorry for no comments here!
 * Still you can send me your question to me@cab404.ru!
 * <p/>
 * Created at 09:58 on 26/11/15
 *
 * @author cab404
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    Timer scheduled = new Timer("WifiLogin", true);
    TimerTask scheduledRepeater;

    synchronized void replaceRepeater(final WifiLoginModule module, final WifiLoginModule.WifiContextInfo info, final WifiLoginModule.Log log) {
        if (scheduledRepeater != null)
            scheduledRepeater.cancel();

        scheduled.schedule(new TimerTask() {
                               @Override
                               public void run() {
                                   module.handle(info, log);
                               }
                           },
                module.repeatDelay(),
                module.repeatDelay()
        );
    }


    @Override
    public void onReceive(final Context context, Intent intent) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (manager.getConnectionInfo().getSSID() != null && manager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {

            final WifiLoginModule.WifiContextInfo info = new AndroidWifiInfo(manager.getConnectionInfo());
            final Handler handler = new Handler(Looper.getMainLooper());

            final List<WifiLoginModule> ways = new LinkedList<>();
            for (WifiLoginModule way : ModuleRegistry.getInstance().getModules())
                if (way.canHandle(info) > 0)
                    ways.add(way);

            if (ways.isEmpty()) return;

            // Sorting in descending order
            Collections.sort(ways, new Comparator<WifiLoginModule>() {
                @Override
                public int compare(WifiLoginModule wk1, WifiLoginModule wk2) {
                    return wk2.canHandle(info) - wk1.canHandle(info);
                }
            });


            final Log log = new Log();

            // Trying
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean handle = false;
                    WifiLoginModule module = null;
                    while (!ways.isEmpty())
                        if (handle = (module = ways.remove(0)).handle(info, log)) break;

                    final String houston = handle ?
                            context.getString(R.string.logged_in, info.ssid())
                            :
                            context.getString(R.string.failure, info.ssid());

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, houston, Toast.LENGTH_SHORT).show();
                        }
                    });

                    if (handle && module.repeatDelay() > 0)
                        replaceRepeater(module, info, log);

                }
            }).start();
        }
    }
}
