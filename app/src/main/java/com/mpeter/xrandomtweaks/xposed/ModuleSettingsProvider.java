package com.mpeter.xrandomtweaks.xposed;

import com.crossbowffs.remotepreferences.RemotePreferenceProvider;
import com.mpeter.xrandomtweaks.App;
import com.mpeter.xrandomtweaks.BuildConfig;

public class ModuleSettingsProvider extends RemotePreferenceProvider {
    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".settings";

    public ModuleSettingsProvider() {
        super(AUTHORITY, new String[]{App.ENABLED_PACKAGES_PREF_FILE});
    }
}
