package com.cab404.wifu.android;

import android.app.Application;

import com.cab404.wifu.modules.BeelineFreeConnect;
import com.cab404.wifu.modules.MetroConnect;
import com.cab404.wifu.modules.MgupiConnect;
import com.cab404.wifu.util.Butler;
import com.cab404.wifu.util.PluginManager;

import java.io.File;
import java.util.List;
import java.util.logging.LogManager;

/**
 * Well, sorry for no comments here!
 * Still you can send me your question to me@cab404.ru!
 * <p/>
 * Created at 11:07 on 04/12/15
 *
 * @author cab404
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        final File logs = new File(getFilesDir(), "logs");
        logs.mkdirs();
        AndroidLogManager.init(logs);

        Butler.getInstance().setLog(AndroidLogManager.getInstance());

        final PluginManager manager = PluginManager.getInstance();

        manager.addModule("MGUPI-WiFi", new MgupiConnect());
        manager.addModule("MosMetro_Free", new MetroConnect());
        manager.addModule("Beeline_WiFi_Free", new BeelineFreeConnect());

    }
}
