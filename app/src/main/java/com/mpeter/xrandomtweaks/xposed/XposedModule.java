package com.mpeter.xrandomtweaks.xposed;

import android.app.AndroidAppHelper;
import android.content.res.XModuleResources;
import android.os.Debug;

import com.mpeter.xrandomtweaks.App;
import com.mpeter.xrandomtweaks.BuildConfig;
import com.mpeter.xrandomtweaks.dummy.android.app.ActivityThread;
import com.mpeter.xrandomtweaks.dummy.android.app.IApplicationThread;

import de.robv.android.xposed.XC_MethodHook;
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
    private static XSharedPreferences mEnabledPackages;

    private static boolean initialized = false;
    private static boolean isTempRes = true;
    private static final boolean earlyDebug = true;

    public static String getLogtag(Class clazz) {
        return PACKAGE.substring(PACKAGE.lastIndexOf("."), PACKAGE.length()) +"/" + clazz.getSimpleName() + " ";
    }

    public static void init(XC_LoadPackage.LoadPackageParam loadPackageParam){
        if (initialized) {
            Timber.tag(LOG_TAG).d("already initialized in %s", loadPackageParam.packageName);
            return;
        }
        Timber.tag(LOG_TAG).d("initializing in %s", loadPackageParam.packageName);

        setSharedPrefs(new XSharedPreferences(PACKAGE));
        setupEnabledPackages(new XSharedPreferences(PACKAGE, App.ENABLED_PACKAGES_PREF_FILE));
        setResources(XModuleResources.createInstance(mModulePath, null));
        new ModuleSettings();

        initialized = true;
    }

    public static void setModulePath(String modulePath){
        mModulePath = modulePath;
    }

    public static String getModulePath() {
        return mModulePath;
    }

    public static void setResources(XModuleResources resources) {
        if (resources == null){
            Timber.tag(LOG_TAG).w("incoming resources is null in %s", CurrentApp.getPackageName());
            return;
        } else if (!isTempRes) {
            Timber.tag(LOG_TAG).w("Resources has been already set. stored res: " + mResources.toString() + ", incoming res: " + resources.toString() + ", packageName: " + CurrentApp.getPackageName());
            return;
        } else if (resources.getDisplayMetrics().widthPixels == 0) {
            Timber.tag(LOG_TAG).w("Resources are now temporary");
            isTempRes = true;
        } else {
            Timber.tag(LOG_TAG).d("Resources now contains metrics and config");
            isTempRes = false;
        }

        mResources = resources;
    }

    public static boolean needsResources(){
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

    private static void setupEnabledPackages(XSharedPreferences sharedPrefs){
        if (mEnabledPackages != null) Timber.tag(LOG_TAG).w("sharedPrefs has been already set");
        else if (sharedPrefs == null) Timber.tag(LOG_TAG).e("sharedPrefs is null");
        else mEnabledPackages = sharedPrefs;
    }

    public static XSharedPreferences getSharedPrefs() {
        return mSharedPrefs;
    }

    public static XSharedPreferences getEnabledPackages(){
        return mEnabledPackages;
    }

    public static void handleBindApplication(XC_LoadPackage.LoadPackageParam loadPackageParam){
        XposedHelpers.findAndHookMethod("android.app.ActivityThread", loadPackageParam.classLoader, "handleBindApplication", "android.app.ActivityThread.AppBindData", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (!earlyDebug || !param.method.getClass().getPackage().getName().startsWith(SupportedPackages.Package.PACKAGE_MIUI_HOME.getPackageName()))
                    return;

                Timber.tag(LOG_TAG).d("preparing early debug");

                ActivityThread.AppBindData.xsetDebugMode(IApplicationThread.DEBUG_WAIT);

                Debug.changeDebugPort(8100);

                Timber.tag(LOG_TAG).d("early debug prepared");

                Debug.waitForDebugger();

                Timber.tag(LOG_TAG).d("early debug finished");
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                CurrentApp.getApplicationContext().setValue(AndroidAppHelper.currentApplication().getApplicationContext());
                Timber.tag(LOG_TAG).d("AppContext is from %s", CurrentApp.getApplicationContext().getValue().getPackageName());
            }
        });
    }

    public static boolean isModuleEnabled(){
        return false;
    }
}
