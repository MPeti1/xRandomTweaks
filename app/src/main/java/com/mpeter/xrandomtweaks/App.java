package com.mpeter.xrandomtweaks;

import android.app.Application;

import com.mpeter.xrandomtweaks.foresttools.Forester;

public class App extends Application {
    public static final String XSETTINGS_PREF_FILE = "xsettings";

    @Override
    public void onCreate() {
        Forester.forestate();
        super.onCreate();
    }
}
