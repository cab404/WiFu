package com.cab404.wifu.android;

import android.app.Activity;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.TextView;

import com.cab404.wifu.R;
import com.cab404.wifu.util.Butler;
import com.cab404.wifu.util.Plugin;
import com.cab404.wifu.util.PluginManager;

/**
 * Well, sorry for no comments here!
 * Still you can send me your question to me@cab404.ru!
 * <p/>
 * Created at 15:13 on 19/11/15
 *
 * @author cab404
 */
public class WifiControllerActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
        String text = ((TextView) findViewById(R.id.text)).getText().toString();
        for (Plugin plugin : PluginManager.getInstance().getPlugins())
            text += '\n' + plugin.name;
        ((TextView) findViewById(R.id.text)).setText(text);

    }
}
