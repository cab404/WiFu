package com.cab404.wifu.android;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.cab404.wifu.R;
import com.cab404.wifu.base.WifiLoginModule;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * «Oatmeal, sir.»
 * <p/>
 * Basically, that's central coordination center of everything.
 * It receives network status changes, as well as performing initialization.
 *
 * @author cab404
 */
public class Butler {

    private static Butler ourInstance = new Butler();

    public static Butler getInstance() {
        return ourInstance;
    }

    private Butler() {
    }

    private static final String TAG = "Butler";
    private Timer scheduled = new Timer(TAG, true);
    private TimerTask scheduledRepeater;

    private synchronized void replaceRepeater(final WifiLoginModule module, final WifiLoginModule.WifiContextInfo info, final WifiLoginModule.Log log) {
        if (scheduledRepeater != null) {
            Log.v(TAG, "Cancelling previous log-in task");
            scheduledRepeater.cancel();
        }
        scheduledRepeater = new TimerTask() {
            @Override
            public void run() {
                Log.v(TAG, "Repeat delay has passed, relaunching ");
                try {
                    module.handle(info, log);
                } catch (Exception e) {
                    Log.e(TAG, "Module " + module.getClass() + " had failed with exception", e);
                }
            }
        };
        scheduled.schedule(
                scheduledRepeater,
                module.repeatDelay(),
                module.repeatDelay()
        );
    }


    public void onNetworkStatusUpdate(final Context ctx) {
        WifiManager manager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);

        if (manager.getConnectionInfo().getNetworkId() == -1) {
            if (scheduledRepeater != null) {
                Log.v(TAG, "Disconnected from network, cancelling previous login task.");
                scheduledRepeater.cancel();
                scheduledRepeater = null;
            }
        } else {

            final WifiLoginModule.WifiContextInfo info = new AndroidWifiInfo(manager.getConnectionInfo());
            final Handler handler = new Handler(Looper.getMainLooper());

            final List<WifiLoginModule> ways = new LinkedList<>();
            for (WifiLoginModule way : ModuleRegistry.getInstance().getModules())
                if (way.canHandle(info) > 0)
                    ways.add(way);

            if (ways.isEmpty()) {
                Log.v(TAG, "New connection detected, but either it's open, " +
                        "or we've not equipped properly to deal with it.");
                return;
            }
            // Sorting in descending order
            Collections.sort(ways, new Comparator<WifiLoginModule>() {
                @Override
                public int compare(WifiLoginModule wk1, WifiLoginModule wk2) {
                    return wk2.canHandle(info) - wk1.canHandle(info);
                }
            });

            final WifiLog wifiLog = new WifiLog();

            // Trying
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.v(TAG, "Starting dispatching thread for " + info.ssid());
                    Log.v(TAG, "Got " + ways.size() + " module(s) to try");

                    boolean handle = false;
                    WifiLoginModule module = null;
                    while (!ways.isEmpty()) {
                        final WifiLoginModule top = ways.remove(0);
                        Log.v(TAG, "Checking " + top.getClass());
                        try {
                            if (handle = (module = top).handle(info, wifiLog)) break;
                        } catch (Exception e) {
                            Log.e(TAG, "Module " + top.getClass() + " had failed with exception", e);
                        }
                    }
                    if (module == null) {
                        Log.v(TAG, "No module found for handling " + info.ssid() + ", assuming that is fine.");
                        return;
                    }

                    if (handle)
                        Log.v(TAG, "Success, proceeding with " + module.getClass());
                    else
                        Log.v(TAG, "All modules we tried have failed");


                    final String houston = handle ?
                            ctx.getString(R.string.logged_in, info.ssid())
                            :
                            ctx.getString(R.string.failure, info.ssid());


                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ctx, houston, Toast.LENGTH_SHORT).show();
                        }
                    });

                    if (handle && module.repeatDelay() > 0) {
                        Log.v(TAG, "Module asked to relog later, added timer task, delay is " + module.repeatDelay() + "ms");
                        replaceRepeater(module, info, wifiLog);
                    }

                }
            }).start();
        }
    }

    public void initialize(Context ctx) {
        final File file = new File(Environment.getExternalStorageDirectory(), "WiFu Plugins");
        file.mkdir();
        ModuleRegistry.getInstance().loadModule(file.listFiles());
    }
}
