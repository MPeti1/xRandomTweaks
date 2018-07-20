package com.mpeter.xrandomtweaks.xposed;

import android.content.res.XModuleResources;
import android.os.Environment;

import com.mpeter.xrandomtweaks.App;
import com.mpeter.xrandomtweaks.BuildConfig;

import de.robv.android.xposed.XSharedPreferences;
import timber.log.Timber;

public class XposedModule {
    public static final String LOG_TAG = getLogtag(XposedModule.class);
    public static final String PACKAGE = BuildConfig.APPLICATION_ID;
    private static final String MODULE_PATH = Environment.getDataDirectory().getPath() + "/data/" + PACKAGE;
    private static XModuleResources mResources;
    private static XSharedPreferences mSharedPrefs;
    private static XSharedPreferences mEnabledPackages;

    private static boolean initialized = false;

    public static String getLogtag(Class clazz) {
        return PACKAGE.substring(PACKAGE.lastIndexOf("."), PACKAGE.length()) +"/" + clazz.getSimpleName() + " ";
    }

    public static void init(){
        if (initialized) {
            Timber.tag(LOG_TAG).d("already initialized");
            return;
        }
        Timber.tag(LOG_TAG).d("initializing");

        setSharedPrefs(new XSharedPreferences(PACKAGE));
        setupEnabledPackages(new XSharedPreferences(PACKAGE, App.ENABLED_PACKAGES_PREF_FILE));

        initialized = true;

        Timber.tag(LOG_TAG).d("initialized. " + String.valueOf(mSharedPrefs == null) + String.valueOf(mEnabledPackages == null));
    }

    public static String getModulePath() {
        return MODULE_PATH;
    }

    public static void setResoucres(XModuleResources resoucres) {
        if (mResources != null) Timber.tag(LOG_TAG).w("Resources has been already set. stored res: %s$1, incoming res: %s$2", mResources.toString(), resoucres.toString());
        else if (resoucres == null) Timber.tag(LOG_TAG).e("Resources is null");
        else mResources = resoucres;
    }

    public static XModuleResources getResources() {
        return mResources;
    }

    private static void setSharedPrefs(XSharedPreferences sharedPrefs) {
        if (mSharedPrefs != null) Timber.tag(LOG_TAG).w("sharedPrefs has been already set");
        else if (sharedPrefs == null) Timber.tag(LOG_TAG).e("sharedPrefs is null");
        else mSharedPrefs = sharedPrefs;
    }

    private static void setupEnabledPackages(XSharedPreferences sharedPrefs){
        if (mSharedPrefs != null) Timber.tag(LOG_TAG).w("sharedPrefs has been already set");
        else if (sharedPrefs == null) Timber.tag(LOG_TAG).e("sharedPrefs is null");
        else mEnabledPackages = sharedPrefs;
    }

    public static XSharedPreferences getSharedPrefs() {
        return mSharedPrefs;
    }

    public static XSharedPreferences getEnabledPackages(){
        return mEnabledPackages;
    }

    public static boolean isModuleEnabled(){
        return false;
    }
}
