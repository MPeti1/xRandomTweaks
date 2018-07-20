package com.mpeter.xrandomtweaks.xposed;

import android.app.AndroidAppHelper;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.XResources;

import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import timber.log.Timber;

public class CurrentApp {
    public static final String LOG_TAG = XposedModule.getLogtag(CurrentApp.class);
    public static int scounter = 0;

    private static ClassLoader sClassLoader;
    private static Context sAppContext; //ha problémás akkor máshonnan kell megszerezni
    private static String sPackageName;
    private static ApplicationInfo sAppInfo;

    private static XSharedPreferences sSharedPrefs;
    private static XResources sResources;

    private static boolean initialized = false;

    public synchronized static void init(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (initialized) Timber.w("%sinit has been already called", LOG_TAG);

        setClassLoader(loadPackageParam.classLoader);
        setPackageName(loadPackageParam.packageName);
        setAppInfo(loadPackageParam.appInfo);
        setSharedPrefs(new XSharedPreferences(loadPackageParam.packageName));

//        initContextGetterHook();

        initialized = true;
    }

    public static ClassLoader getClassLoader() {
        if (sClassLoader != null) return sClassLoader;
        else
            throw new NullPointerException("ClassLoader is null. You should init it in handleLoadPackage");
    }

    public static String getPackageName() {
        return sPackageName;
    }

    public static ApplicationInfo getAppInfo() {
        return sAppInfo;
    }

    public static Context getAppContext() {
        if (sAppContext != null) return sAppContext;
        sAppContext = AndroidAppHelper.currentApplication().getApplicationContext();

        if (sAppContext != null) return sAppContext;
        throw new NullPointerException("appContext is null. You should init it in handleLoadPackage");
    }

    /*private static void initContextGetterHook() {
        XposedHelpers.findAndHookMethod(Application.class.getCanonicalName(), sClassLoader, "onCreate", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

                sAppContext = (Context) param.args[0];
            }
        });
    }*/

    public static void initResources(XC_InitPackageResources.InitPackageResourcesParam resparam) {
        if (sResources != null) Timber.w("%s$1Resources has been already set. stored res: %s$2, incoming res: %s$3", LOG_TAG, sResources.getPackageName(), resparam.packageName);
        else if (resparam.res == null) Timber.e("%sResources is null", LOG_TAG);
        else sResources = resparam.res;
    }

    public static XResources getResources() {
        return sResources;
    }

    public static XSharedPreferences getSharedPrefs() {
        return sSharedPrefs;
    }

    private static void setClassLoader(ClassLoader classLoader) {
        if (classLoader == null) Timber.w("%sclassLoader is null in loadPackageParam", LOG_TAG);
        else sClassLoader = classLoader;
    }

    private static void setPackageName(String packageName) {
        if (packageName == null || packageName.isEmpty())
            Timber.w("%spackageName is null or empty in loadPackageParam", LOG_TAG);
        else sPackageName = packageName;
    }

    private static void setAppInfo(ApplicationInfo appInfo) {
        if (appInfo == null) Timber.w("%sappInfo in loadPackageParam is null", LOG_TAG);
        else if (!appInfo.packageName.equals(getPackageName()))
            Timber.w(LOG_TAG + "appInfo (" + appInfo.packageName + ") belongs to an other app (" + getPackageName() + ")");
        else sAppInfo = appInfo;
    }

    private static void setSharedPrefs(XSharedPreferences sharedPrefs) {
        if (sharedPrefs == null) Timber.w("%ssharedPrefs is null", LOG_TAG);
        else sSharedPrefs = sharedPrefs;
    }
}
