package com.mpeter.xrandomtweaks.xposed;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public interface HookedApp {
    void initHooks(XC_LoadPackage.LoadPackageParam loadPackageParam);
    void unHookAll();
}
