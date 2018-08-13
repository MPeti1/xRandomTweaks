package com.mpeter.xrandomtweaks.xposed;

import android.content.pm.ApplicationInfo;
import android.content.res.XResources;

import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import timber.log.Timber;

public class CurrentApp {
    public static final String LOG_TAG = XposedModule.getLogtag(CurrentApp.class);

    private static ClassLoader mClassLoader;
    private static String mPackageName;
    private static ApplicationInfo mAppInfo;
    private static XSharedPreferences mSharedPrefs;
    private static XResources mResources;

    private static boolean initialized = false;

    public synchronized static void init(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (initialized) Timber.tag(LOG_TAG).w("init has been already called");
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

    public static void initResources(XC_InitPackageResources.InitPackageResourcesParam resparam) {
        if (mResources != null)
            Timber.tag(LOG_TAG).w("Resources has been already set. stored res: %s, incoming res: %s, in package %s\nmaybe %s loads external resources?",
                mResources.getPackageName(),
                resparam.packageName,
                getPackageName(),
                getPackageName()
        );

        else if (resparam.res == null) Timber.tag(LOG_TAG).e("Resources is null");
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
        if (classLoader == null) Timber.tag(LOG_TAG).e("classLoader is null in loadPackageParam");
        else mClassLoader = classLoader;
    }

    private static void setPackageName(String packageName) {
        if (packageName == null || packageName.isEmpty())
            Timber.tag(LOG_TAG).e("packageName is null or empty in loadPackageParam");
        else mPackageName = packageName;
    }

    private static void setAppInfo(ApplicationInfo appInfo) {
        if (appInfo == null) Timber.tag(LOG_TAG).e("appInfo in loadPackageParam is null");
        else if (!appInfo.packageName.equals(getPackageName()))
            Timber.tag(LOG_TAG).w("appInfo (%s) belongs to an other app (%s)", appInfo.packageName, getPackageName());
        else mAppInfo = appInfo;
    }

    private static void setSharedPrefs(XSharedPreferences sharedPrefs) {
        if (sharedPrefs == null) Timber.tag(LOG_TAG).e("sharedPrefs is null");
        else mSharedPrefs = sharedPrefs;
    }
}
