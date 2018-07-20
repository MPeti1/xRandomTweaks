package com.mpeter.xrandomtweaks;

import android.app.Application;

import com.mpeter.xrandomtweaks.foresttools.Forester;

public class App extends Application {
    public static final String ENABLED_PACKAGES_PREF_FILE = "enabled_packages";

    @Override
    public void onCreate() {
        Forester.forestate();
        super.onCreate();
    }
}
