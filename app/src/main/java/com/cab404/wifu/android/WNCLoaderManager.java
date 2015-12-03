package com.cab404.wifu.android;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import com.cab404.wifu.base.WifiLoginModule;

/**
 * Well, sorry for no comments here!
 * Still you can send me your question to me@cab404.ru!
 * <p/>
 * Created at 12:14 on 26/11/15
 *
 * @author cab404
 */
public class WNCLoaderManager {

    private URLClassLoader urlClassLoader;

    public static class ManagedNetworkController {
        WifiLoginModule instance;
        String className;
        String meta;
    }

    public void setSrc(File... files) throws ClassNotFoundException, MalformedURLException {
        URL[] urls = new URL[files.length];
        for (int i = 0; i < files.length; i++)
            urls[i] = new URL("file://" + files[i].getAbsolutePath());
        urlClassLoader = new URLClassLoader(urls);
    }

    void setSrc(String className){

    }

}
