package com.mpeter.xrandomtweaks.xposed;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.XModuleResources;

import com.crossbowffs.remotepreferences.RemotePreferences;
import com.mpeter.xrandomtweaks.App;
import com.mpeter.xrandomtweaks.BuildConfig;

import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import timber.log.Timber;

public class XposedModule {
    public static final String LOG_TAG = getLogtag(XposedModule.class);
    public static final String PACKAGE = BuildConfig.APPLICATION_ID;
    private static String mModulePath = null;
    private static XModuleResources mResources;
    private static XSharedPreferences mSharedPrefs;
    private static SharedPreferences mXSettings;
    private static Context systemContext;

    private static boolean initialized = false;
    private static boolean isTempRes = true;
    private static final boolean earlyDebug = true;

    public static String getLogtag(Class clazz) {
        return PACKAGE.substring(PACKAGE.lastIndexOf("."), PACKAGE.length()) + "/" + clazz.getSimpleName() + " ";
    }

    public static void init(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (initialized) {
            Timber.tag(LOG_TAG).d("already initialized in %s", loadPackageParam.packageName);
            return;
        }
        Timber.tag(LOG_TAG).d("initializing in %s", loadPackageParam.packageName);

        setSharedPrefs(new XSharedPreferences(PACKAGE));
        setResources(XModuleResources.createInstance(mModulePath, null));
        setupXSettings(loadPackageParam);

        initialized = true;
    }

    public static void setModulePath(String modulePath) {
        mModulePath = modulePath;
    }

    public static String getModulePath() {
        return mModulePath;
    }

    public static void setResources(XModuleResources resources) {
        if (resources == null) {
            Timber.tag(LOG_TAG).w("incoming resources is null in %s", CurrentApp.getPackageName());
            return;
        } else if (!isTempRes) {
            Timber.tag(LOG_TAG).w("Resources has been already set. stored res: " + mResources.toString() + ", incoming res: " + resources.toString() + ", packageName: " + CurrentApp.getPackageName());
            return;
        } else if (resources.getDisplayMetrics().widthPixels == 0) {
            Timber.tag(LOG_TAG).d("Resources are now temporary");
            isTempRes = true;
        } else {
            Timber.tag(LOG_TAG).d("Resources now contains metrics and config");
            isTempRes = false;
        }

        mResources = resources;
    }

    public static boolean needsResources() {
        return isTempRes;
    }

    public static XModuleResources getResources() {
        return mResources;
    }

    private static void setSharedPrefs(XSharedPreferences sharedPrefs) {
        if (mSharedPrefs != null) Timber.tag(LOG_TAG).w("sharedPrefs has been already set");
        else if (sharedPrefs == null) Timber.tag(LOG_TAG).e("sharedPrefs is null");
        else mSharedPrefs = sharedPrefs;
    }

    private static void setupXSettings(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        /*if (mEnabledPackages != null) Timber.tag(LOG_TAG).w("sharedPrefs has been already set");
        else if (sharedPrefs == null) Timber.tag(LOG_TAG).e("sharedPrefs is null");
        else mEnabledPackages = sharedPrefs;*/

       /* XposedHelpers.findAndHookMethod("android.app.ActivityThread", loadPackageParam.classLoader, "handleBindApplication", "android.app.ActivityThread.AppBindData", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);

                *//*Application application = (Application) XposedHelpers.callMethod(
                        XposedHelpers.getObjectField(param.args[0], "info"),
                        "makeApplication",
                        false,
                        null
                );

                XposedHelpers.callMethod(
                        param.thisObject,
                        "installContentProviders",
                        application,
                        XposedHelpers.getObjectField(param.args[0], "providers");
                );*//*

                new ModuleSettings();
            }
        });*/

        systemContext = (Context) XposedHelpers.callMethod(
                XposedHelpers.callStaticMethod(
                        XposedHelpers.findClass("android.app.ActivityThread", loadPackageParam.classLoader),
                        "currentActivityThread"),
                "getSystemContext"
        );

        mXSettings = new RemotePreferences(systemContext, ModuleSettingsProvider.AUTHORITY, App.ENABLED_PACKAGES_PREF_FILE);

        new ModuleSettings();
    }

    public static XSharedPreferences getSharedPrefs() {
        return mSharedPrefs;
    }

    public static SharedPreferences getXSettings() {
        return mXSettings;
    }

    public static boolean isModuleEnabled() {
        return false;
    }
}
