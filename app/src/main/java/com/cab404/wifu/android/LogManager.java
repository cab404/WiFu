package com.cab404.wifu.android;

import com.cab404.wifu.base.WifiLoginModule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * Well, sorry for no comments here!
 * Still you can send me your question to me@cab404.ru!
 * <p/>
 * Created at 14:42 on 04/12/15
 *
 * @author cab404
 */
public class LogManager {

    public class FileLog implements WifiLoginModule.Log {
        private final File file;
        PrintStream in;

        public FileLog(File file) throws FileNotFoundException {
            this.file = file;
            in = new PrintStream(file);
        }


        @Override
        public synchronized void e(Throwable t, String message) {
            in.println("[E] " + message);
            t.printStackTrace(in);
        }

        @Override
        public synchronized void v(String message) {
            in.println("[V] " + message);
        }

        @Override
        public synchronized void alert(String message) {
            in.println("[A] " + message);
            // TODO: Add toast here
        }
    }

    public void generateLog(File file){

    }

}
