package com.mpeter.xrandomtweaks.xposed;

import android.content.res.XModuleResources;
import android.text.TextUtils;
import android.util.Log;

import com.mpeter.xrandomtweaks.BuildConfig;

import de.robv.android.xposed.XSharedPreferences;

public class XposedModule {
    public static final String LOG_TAG = getLogtag(XposedModule.class);
    public static final String PACKAGE = BuildConfig.APPLICATION_ID;
    private static String mModulePath;
    private static XModuleResources mResources;
    private static XSharedPreferences mSharedPrefs;

    public static String getLogtag(Class clazz) {
        return PACKAGE.substring(PACKAGE.lastIndexOf("."), PACKAGE.length() - 1) +"/" + clazz.getSimpleName() + ": ";
    }

    public static void setModulePath(String modulePath) {
        if (mModulePath != null) Log.w(LOG_TAG, "modulePath has been already set");
        else if (modulePath != null && !TextUtils.isEmpty(modulePath)) {
            mModulePath = modulePath;
            setSharedPrefs(new XSharedPreferences(PACKAGE));
        }
        else Log.w(LOG_TAG, "modulePath is null or empty");
    }

    public static String getModulePath() {
        return mModulePath;
    }

    public static void setResoucres(XModuleResources resoucres) {
        if (mResources != null) Log.w(LOG_TAG, "Resources has been already set");
        else if (resoucres == null) Log.e(LOG_TAG, "Resources is null");
        else mResources = resoucres;
    }

    public static XModuleResources getResources() {
        return mResources;
    }

    public static void setSharedPrefs(XSharedPreferences sharedPrefs) {
        if (mSharedPrefs != null) Log.w(LOG_TAG, "SharedPrefs has been already set");
        else if (sharedPrefs == null) Log.e(LOG_TAG, "SharedPrefs is null");
        else XposedModule.mSharedPrefs = sharedPrefs;
    }

    public static XSharedPreferences getSharedPrefs() {
        return mSharedPrefs;
    }

    public static boolean isModuleEnabled(){
        return false;
    }
}
