package com.mpeter.xrandomtweaks.xposed.xRandomTweaks;

import com.mpeter.xrandomtweaks.xposed.HookedApp;
import com.mpeter.xrandomtweaks.xposed.XposedModule;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import timber.log.Timber;

public class XRandomTweaksHooks implements HookedApp {
    public static final String LOG_TAG = XposedModule.getLogtag(XRandomTweaksHooks.class);
    public static boolean isActive = false;

    @Override
    public void initHooks(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (isActive) {
            Timber.w("%salready initialized", LOG_TAG);
            return;
        }

        XposedHelpers.findAndHookMethod(XposedModule.class.getCanonicalName(), loadPackageParam.classLoader, "isModuleEnabled", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(true);
            }
        });

        isActive = true;
    }

    @Override
    public void unHookAll() {

    }
}
