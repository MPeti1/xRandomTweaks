package com.mpeter.xrandomtweaks.xposed.xRandomTweaks;

import com.mpeter.xrandomtweaks.xposed.HookedApp;
import com.mpeter.xrandomtweaks.xposed.SupportedPackages;
import com.mpeter.xrandomtweaks.xposed.XposedModule;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class XRandomTweaksHooks extends HookedApp {
    public XRandomTweaksHooks() {
        super(XRandomTweaksHooks.class, SupportedPackages.Package.PACKAGE_SELF);
    }

    @Override
    public void initHooks(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod(XposedModule.class.getCanonicalName(), loadPackageParam.classLoader, "isModuleEnabled", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(true);
            }
        });
    }
}
