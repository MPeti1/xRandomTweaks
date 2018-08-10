package com.mpeter.xrandomtweaks.xposed;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.XResources;

import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import timber.log.Timber;

public class CurrentApp {
    public static final String LOG_TAG = XposedModule.getLogtag(CurrentApp.class);
    public static int counter = 0;

    private static ClassLoader mClassLoader;
    private static MutableLiveData<Context> mAppContext = new MutableLiveData<>(); //ha problémás akkor máshonnan kell megszerezni
    private static String mPackageName;
    private static ApplicationInfo mAppInfo;

    private static XSharedPreferences mSharedPrefs;
    private static XResources mResources;

    private static boolean initialized = false;

    public synchronized static void init(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (initialized) Timber.w("%sinit has been already called", LOG_TAG);
        Timber.tag(LOG_TAG).d("initializing in %s", loadPackageParam.packageName);

        setClassLoader(loadPackageParam.classLoader);
        setPackageName(loadPackageParam.packageName);
        setAppInfo(loadPackageParam.appInfo);
        setSharedPrefs(new XSharedPreferences(loadPackageParam.packageName));

        initialized = true;
    }

    public static ClassLoader getClassLoader() {
        if (mClassLoader != null) return mClassLoader;
        else
            throw new NullPointerException("ClassLoader is null. You should init it in handleLoadPackage");
    }

    public static String getPackageName() {
        return mPackageName;
    }

    public static ApplicationInfo getAppInfo() {
        return mAppInfo;
    }

    public static MutableLiveData<Context> getApplicationContext() {
        return mAppContext;
    }

    //bugos ha többször hivatkozol egy adatra, mert azt nézi hogy hányszor hivatkoztál
    //és mennyi adatot adsz neki, és nem azt hogy létezik-e annyi adat ahanyadikra hivatkozol
    @SuppressLint("TimberArgCount")
    public static void initResources(XC_InitPackageResources.InitPackageResourcesParam resparam) {
        if (mResources != null) Timber.tag(LOG_TAG).w("Resources has been already set. stored res: %1$s, incoming res: %2$s, in package %3$s\nmaybe %1$s loads external resources?", mResources.getPackageName(), resparam.packageName, getPackageName());
        else if (resparam.res == null) Timber.e("%sResources is null", LOG_TAG);
        else {
            mResources = resparam.res;
            Timber.tag(LOG_TAG).d("Resources set in %s", getPackageName());
        }
    }

    public static XResources getResources() {
        return mResources;
    }

    public static XSharedPreferences getSharedPrefs() {
        return mSharedPrefs;
    }

    private static void setClassLoader(ClassLoader classLoader) {
        if (classLoader == null) Timber.e("%sclassLoader is null in loadPackageParam", LOG_TAG);
        else mClassLoader = classLoader;
    }

    private static void setPackageName(String packageName) {
        if (packageName == null || packageName.isEmpty())
            Timber.e("%spackageName is null or empty in loadPackageParam", LOG_TAG);
        else mPackageName = packageName;
    }

    private static void setAppInfo(ApplicationInfo appInfo) {
        if (appInfo == null) Timber.e("%sappInfo in loadPackageParam is null", LOG_TAG);
        else if (!appInfo.packageName.equals(getPackageName()))
            Timber.w(LOG_TAG + "appInfo (" + appInfo.packageName + ") belongs to an other app (" + getPackageName() + ")");
        else mAppInfo = appInfo;
    }

    private static void setSharedPrefs(XSharedPreferences sharedPrefs) {
        if (sharedPrefs == null) Timber.e("%ssharedPrefs is null", LOG_TAG);
        else mSharedPrefs = sharedPrefs;
    }
}
