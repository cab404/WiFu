package com.cab404.wifu.android;

import android.util.Log;

import com.cab404.wifu.core.WifiLoginModule;
import com.cab404.wifu.util.LogManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Well, sorry for no comments here!
 * Still you can send me your question to me@cab404.ru!
 * <p/>
 * Created at 14:42 on 04/12/15
 *
 * @author cab404
 */
public class AndroidLogManager implements LogManager {

    private static AndroidLogManager instance;
    private File dir;

    public static AndroidLogManager getInstance() {
        return instance;
    }

    public static void init(File dir){
        instance = new AndroidLogManager(dir);
    }

    private AndroidLogManager(File dir) {
        this.dir = dir;
    }

    public class FileLog implements WifiLoginModule.Log {
        private PrintStream in;
        private String tag;

        public FileLog(String tag, File file) throws FileNotFoundException {
            this.tag = tag;
            in = new PrintStream(new FileOutputStream(file, true), true);
        }
        
        String ts(){
            return SimpleDateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(Calendar.getInstance().getTime());
        }

        @Override
        public synchronized void e(Throwable t, String message) {
            Log.e(tag, message, t);
            in.println(ts() + " [E] " + message);
            t.printStackTrace(in);
            t.printStackTrace(System.out);
            in.flush();
        }

        @Override
        public synchronized void v(String message) {
            Log.v(tag, message);
            in.println(ts() + " [V] " + message);
        }

        @Override
        public synchronized void alert(String message) {
            Log.i(tag, message);
            in.println(ts() + " [A] " + message);
            in.flush();
        }
    }

    Map<String, FileLog> logs = new HashMap<>();

    public WifiLoginModule.Log generateLog(String tag) {
        try {
        if (logs.containsKey(tag)) return logs.get(tag);
            FileLog nlog = new FileLog(tag, new File(dir, tag + ".txt"));
            logs.put(tag, nlog);
            return nlog;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Cannot create log!", e);
        }
    }

}
