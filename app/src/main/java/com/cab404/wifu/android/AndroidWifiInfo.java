package com.cab404.wifu.android;

import android.net.wifi.WifiInfo;

import java.io.PrintWriter;

import com.cab404.wifu.base.WifiLoginModule;

/**
 * Well, sorry for no comments here!
 * Still you can send me your question to me@cab404.ru!
 * <p/>
 * Created at 11:28 on 01/12/15
 *
 * @author cab404
 */
class AndroidWifiInfo implements WifiLoginModule.WifiContextInfo {

    final PrintWriter log = new PrintWriter(System.out);
    private String bssid;
    private String ssid;
    private String mac;

    public AndroidWifiInfo(String bssid, String ssid, String mac) {
        this.bssid = bssid;
        this.ssid = ssid;
        this.mac = mac;
    }

    public AndroidWifiInfo(WifiInfo connectionInfo) {
        this.bssid = connectionInfo.getBSSID();
        this.ssid = connectionInfo.getSSID();
        if (ssid.contains("\"")) // SSID string specification in android is rather strange :\
            ssid = ssid().substring(1, ssid.length() - 1);
        this.mac = connectionInfo.getMacAddress();
    }

    @Override
    public String bssid() {
        return bssid;
    }

    @Override
    public String ssid() {
        return ssid;
    }

    @Override
    public String mac() {
        return mac;
    }

}
