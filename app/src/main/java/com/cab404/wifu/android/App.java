package com.cab404.wifu.android;

import android.app.Application;

import com.cab404.wifu.modules.MetroConnect;
import com.cab404.wifu.modules.MgupiConnect;

import java.util.List;

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
        Butler.getInstance().initialize(this);

        final List<Plugin> plugins = PluginManager.getInstance().getPlugins();
        Plugin mgupi = new Plugin();
        mgupi.module = new MgupiConnect();
        mgupi.name = "MGUPI-WiFi";
        plugins.add(mgupi);

        Plugin metro = new Plugin();
        metro.module = new MetroConnect();
        metro.name = "MosMetro_Free";
        plugins.add(metro);

    }
}
