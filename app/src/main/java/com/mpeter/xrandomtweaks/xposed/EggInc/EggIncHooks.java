package com.mpeter.xrandomtweaks.xposed.EggInc;

import com.mpeter.xrandomtweaks.xposed.HookedApp;
import com.mpeter.xrandomtweaks.xposed.SupportedPackages;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class EggIncHooks extends HookedApp {
    public EggIncHooks() {
        super(EggIncHooks.class, SupportedPackages.Package.PACKAGE_EGGINC);
    }

    @Override
    public void initHooks(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (!isEnabled()) return;

        new EggIncPreventMusic().initHooks(loadPackageParam);
        new EggIncSkipAds().initHooks(loadPackageParam);
    }

    @Override
    public void unHookAll() {

    }
}
