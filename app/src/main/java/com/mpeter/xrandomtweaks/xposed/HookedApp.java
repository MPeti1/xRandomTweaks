package com.mpeter.xrandomtweaks.xposed;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public abstract class HookedApp {
    public final String LOG_TAG;
    public final String PACKAGE;

    public HookedApp(Class clazz, SupportedPackages.Package pkg) {
        LOG_TAG = XposedModule.getLogtag(clazz);
        PACKAGE = pkg.getPackageName();
    }

    public abstract void initHooks(XC_LoadPackage.LoadPackageParam loadPackageParam);
}
