package com.cab404.wifu.modules;

import com.cab404.wifu.core.WifiLoginModule;
import com.cab404.wifu.core.NetUtils;
import com.cab404.wifu.util.SU;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Well, sorry for no comments here!
 * Still you can send me your question to me@cab404.ru!
 * <p/>
 * Created at 06:37 on 29/11/15
 *
 * @author cab404
 */
public class MetroConnect implements WifiLoginModule {

    @Override
    public boolean handle(WifiContextInfo info, Log log) throws IOException {
        // Fetching redirect page
        HttpURLConnection o = (HttpURLConnection) new URL("http://ya.ru/404").openConnection();
        o.setUseCaches(false);
        o.setDefaultUseCaches(false);
        o.connect();
        o.getResponseCode();

        final String location = o.getHeaderField("Location");
        if (location == null) return false;

        URL base = new URL(location);

        // Fetching main page with csrf code
        HttpURLConnection main_page = (HttpURLConnection) base.openConnection();
        main_page.addRequestProperty("Host", "login.wi-fi.ru");
        main_page.addRequestProperty("Cookie", "device=desktop");
        main_page.connect();

        // Because URLConnections are retarded
        try {
            main_page.getInputStream();
        } catch (IOException e) {
            main_page.getErrorStream();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(main_page.getErrorStream()));
//        String url = null;
        String payload = null;
        String next;

        while ((next = reader.readLine()) != null){
            if (next.contains("csrf.sign"))
                payload = next;
//            if (next.contains("meta http-equiv=\"refresh\""))
//                url = next;
            if (payload != null/* && url != null*/)
                break;
        }

        if (payload == null) throw new RuntimeException("CSRF not found!");
//        if (url == null) throw new RuntimeException("URL not found!");

        // Some assembly required, may (and will) break on template changes
        payload = "promogoto=&IDButton=Confirm"
                + payload
                .replace("</form>", "")
                .replace("\"/>", "")
                .replace("<input type=\"hidden\" name=\"", "&")
                .replace("\" value=\"", "=")
                .trim();


//        url = SU.sub(url, "URL=", "\"");
//        String pre = "https://login.wi-fi.ru/am/UI/Login" + url;
//        String post = "http://login.wi-fi.ru/am/UI/Login" + url;

//        // Now checking out cookies
//        String cookie = String.valueOf(NetUtils.buildCookies(main_page));
//        cookie = "device=desktop; " + cookie;

        // Requesting
        HttpURLConnection login = (HttpURLConnection) new URL(location).openConnection();
        login.setRequestMethod("POST");
        login.setDefaultUseCaches(false);
        login.setUseCaches(false);
        login.setDoOutput(true);
        login.setDoInput(true);

        login.setRequestProperty("Host", "login.wi-fi.ru");
        login.setRequestProperty("Referer", location);
        login.setRequestProperty("Accept", "*/*");
        login.setRequestProperty("Cookie", "device=desktop");
        login.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        login.setRequestProperty("Content-Length", payload.length() + "");
        System.out.println(payload.length());
        // And payload
        PrintWriter writer = new PrintWriter(login.getOutputStream());
        writer.write(payload);
        writer.flush();
        login.connect();

        // IMPORTANT, BECAUSE URLCONNECTIONS SUCKS >:|
        // No, srsly, that forces wait for request to finish,
        // and prevents input stream errors

        try {
            login.getResponseCode();
            login.getInputStream();
        } catch (IOException e) {
            login.getErrorStream();
        }

        reader = new BufferedReader(new InputStreamReader(login.getInputStream()));
        while ((next = reader.readLine()) != null)
            if (next.contains("<title>"))
                System.out.println(next);

        return true;
    }

    @Override
    public long repeatDelay() {
        return -1;
    }

    @Override
    public int canHandle(WifiContextInfo info) {
        return "MosMetro_Free".equals(info.ssid()) ? 100 : 0;
    }
}
