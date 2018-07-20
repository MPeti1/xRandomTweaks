package com.mpeter.xrandomtweaks;

import android.app.Application;

import com.mpeter.xrandomtweaks.foresttools.Forester;
import com.mpeter.xrandomtweaks.xposed.SupportedPackages;

public class App extends Application {
    public static final String ENABLED_PACKAGES_PREF_FILE = "enabled_packages";

    public static final String FEATURE_TAG_EGGINC_PREVENT_MUSIC = SupportedPackages.Package.PACKAGE_EGGINC + ".prevent-music";
    public static final String FEATURE_TAG_EGGINC_SKIP_ADS = SupportedPackages.Package.PACKAGE_EGGINC + ".skip-ads";
    public static final String FEATURE_TAG_MESSENGER_SOCCER_CHEAT = SupportedPackages.Package.PACKAGE_FB_MESSENGER + ".soccer-cheat";

    @Override
    public void onCreate() {
        Forester.forestate();
        super.onCreate();
    }
}
