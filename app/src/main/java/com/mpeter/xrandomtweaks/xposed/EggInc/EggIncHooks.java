package com.mpeter.xrandomtweaks.xposed.EggInc;

import com.mpeter.xrandomtweaks.xposed.HookedApp;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class EggIncHooks implements HookedApp {
    @Override
    public void initHooks(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        new EggIncActivityHooks().initHooks(loadPackageParam);
    }

    @Override
    public void unHookAll() {

    }
}
