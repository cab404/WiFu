package com.cab404.wifu.android;

import android.util.Log;

import com.cab404.wifu.base.WifiLoginModule;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Contains methods of connecting to network
 * <p/>
 * Created at 12:19 on 28/11/15
 *
 * @author cab404
 */
public class ModuleRegistry {

    private static final String MKEY_TITLE = "WifuModule-Title";
    private static final String MKEY_VERSION = "WifuModule-Version";
    private static final String MKEY_MODULE_CLASS = "WifuModule-Class";
    private static final String MKEY_UPDATE_URL = "WifuModule-UpdateUrl";
    private static final String MKEY_VERSION_URL = "WifuModule-VersionUrl";
    private static final String DESC_FILE = "description.txt";
    private static ModuleRegistry instance = new ModuleRegistry();

    public static ModuleRegistry getInstance() {
        return instance;
    }

    private final List<WifiLoginModule> modules = new ArrayList<>();

    public List<WifiLoginModule> getModules() {
        return modules;
    }

    public static class Plugin {
        Class<? extends WifiLoginModule> module;
        public String version_url;
        public String update_url;
        public String name;
        public String info;
        public int version;
    }

    public static class ModuleLoadInfo {
        enum LoadStatus {
            SUCCESS,
            FAIL_NO_JAR,
            FAIL_CLASS_NOT_FOUND,
            FAIL_CORRUPTED_MANIFEST,
            FAIL_CLASS_IS_NOT_MODULE,
            FAIL_CLASS_HAS_NO_DEFAULT_CONSTRUCTOR
        }

        LoadStatus status;
        Plugin loaded;
        File from;
    }

    public List<ModuleLoadInfo> loadModule(File... jars) {
        URL[] jar_urls = new URL[jars.length];
        for (int i = 0; i < jars.length; i++)
            try {
                jar_urls[i] = jars[i].toURI().toURL();
            } catch (MalformedURLException e) {
                throw new RuntimeException("Something stupid happened with urls", e);
            }

        final URLClassLoader classLoader = new URLClassLoader(jar_urls);
        final List<ModuleLoadInfo> loadData = new ArrayList<>(jars.length);

        for (File jarFile : jars) {

            ModuleLoadInfo moduleLoadInfo = new ModuleLoadInfo();
            moduleLoadInfo.from = jarFile;
            moduleLoadInfo.status = ModuleLoadInfo.LoadStatus.SUCCESS;

            // Checking if jar is present
            if (!jarFile.exists()) {
                moduleLoadInfo.status = ModuleLoadInfo.LoadStatus.FAIL_NO_JAR;
                continue;
            }

            final JarFile jar;
            final Manifest manifest;
            try {
                manifest = (jar = new JarFile(jarFile)).getManifest();
            } catch (IOException e) {
                Log.e("PluginManager", "Something stupid had happened with jar, skipping it", e);
                continue;
            }

            // Checking if manifest is present
            loadData.add(moduleLoadInfo);
            if (manifest == null) {
                moduleLoadInfo.status = ModuleLoadInfo.LoadStatus.FAIL_CORRUPTED_MANIFEST;
                continue;
            }

            // Checking module class
            final Attributes attrs = manifest.getMainAttributes();
            if (attrs.getValue(MKEY_MODULE_CLASS) == null) {
                moduleLoadInfo.status = ModuleLoadInfo.LoadStatus.FAIL_CORRUPTED_MANIFEST;
                continue;
            }

            Plugin plugin = moduleLoadInfo.loaded = new Plugin();

            // Loading module itself
            try {
                Class moduleClass = classLoader.loadClass(attrs.getValue(MKEY_MODULE_CLASS));
                if (WifiLoginModule.class.isAssignableFrom(moduleClass))
                    //noinspection unchecked
                    plugin.module = moduleClass;
                else {
                    moduleLoadInfo.status = ModuleLoadInfo.LoadStatus.FAIL_CLASS_IS_NOT_MODULE;
                    continue;
                }
                //noinspection unchecked
                moduleClass.getConstructor();
            } catch (ClassNotFoundException e) {
                moduleLoadInfo.status = ModuleLoadInfo.LoadStatus.FAIL_CLASS_NOT_FOUND;
                continue;
            } catch (NoSuchMethodException e) {
                moduleLoadInfo.status = ModuleLoadInfo.LoadStatus.FAIL_CLASS_HAS_NO_DEFAULT_CONSTRUCTOR;
                continue;
            }

            // Checking if version is present and is valid integer
            if (attrs.getValue(MKEY_VERSION) != null)
                try {
                    plugin.version = Integer.parseInt(attrs.getValue(MKEY_VERSION));
                } catch (NumberFormatException e) {
                    moduleLoadInfo.status = ModuleLoadInfo.LoadStatus.FAIL_CORRUPTED_MANIFEST;
                    continue;
                }

            // Copying update sites
            plugin.update_url = attrs.getValue(MKEY_UPDATE_URL);
            plugin.version_url = attrs.getValue(MKEY_VERSION_URL);
            // And title
            plugin.name = attrs.getValue(MKEY_TITLE);

            // Trying to load plugin description
            final JarEntry descriptionFile = jar.getJarEntry(DESC_FILE);
            if (descriptionFile != null) {
                StringBuilder description = new StringBuilder();
                try {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(jar.getInputStream(descriptionFile))
                    );

                    String line;
                    while ((line = reader.readLine()) != null)
                        description.append(line).append('\n');
                    reader.close();
                } catch (IOException e) {
                    Log.e("PluginManager", "Something stupid had happened with description, assuming it's empty", e);
                }

                plugin.info = description.toString();
            }

        }

        return loadData;

    }

}
