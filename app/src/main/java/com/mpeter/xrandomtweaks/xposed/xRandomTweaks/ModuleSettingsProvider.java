package com.mpeter.xrandomtweaks.xposed.xRandomTweaks;

import com.crossbowffs.remotepreferences.RemotePreferenceProvider;
import com.mpeter.xrandomtweaks.App;
import com.mpeter.xrandomtweaks.BuildConfig;
import com.mpeter.xrandomtweaks.xposed.XposedModule;

import timber.log.Timber;

public class ModuleSettingsProvider extends RemotePreferenceProvider {
    public static final String LOG_TAG = XposedModule.getLogtag(ModuleSettingsProvider.class);
    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".settings";

    public ModuleSettingsProvider() {
        super(AUTHORITY, new String[]{App.XSETTINGS_PREF_FILE});
        Timber.tag(LOG_TAG).d("initialized");
    }

    @Override
    protected boolean checkAccess(String prefName, String prefKey, boolean write) {
        return false;
    }
}
