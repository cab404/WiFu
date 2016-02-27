package com.cab404.wifu.modules;

import com.cab404.wifu.core.WifiLoginModule;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Module for Beeline WiFi free networks
 *
 * @author cab404
 */
public class BeelineFreeConnect implements WifiLoginModule {

    @Override
    public boolean handle(WifiContextInfo info, Log log) throws IOException {

        String payload = "lang=ru&screen=normal&url=ya.ru&mode=partner";

        // Requesting
        HttpURLConnection login =
                (HttpURLConnection) new URL("https://startwifi.beeline.ru/status")
                        .openConnection();
        login.setRequestMethod("POST");
        login.setDefaultUseCaches(false);
        login.setUseCaches(false);
        login.setDoOutput(true);
        login.setDoInput(true);

        // Regular params
        login.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        login.setRequestProperty("Content-Length", payload.length() + "");

        PrintWriter writer = new PrintWriter(login.getOutputStream());
        writer.write(payload);
        writer.flush();

        login.connect();

        // IMPORTANT, BECAUSE URLCONNECTIONS SUCKS >:|
        // No, srsly, that forces wait for request to finish,
        // and prevents input stream errors
        login.getResponseCode();

        return true;
    }

    @Override
    public long repeatDelay() {
        return TimeUnit.SECONDS.toMillis(10);
    }

    @Override
    public int canHandle(WifiContextInfo info) {
        return "Beeline_WiFi_FREE".equalsIgnoreCase(info.ssid()) ? 100 : 0;
    }
}
