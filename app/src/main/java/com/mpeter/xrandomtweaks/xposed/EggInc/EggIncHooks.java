package com.mpeter.xrandomtweaks.xposed.EggInc;

import com.mpeter.xrandomtweaks.xposed.HookedApp;
import com.mpeter.xrandomtweaks.xposed.SupportedPackages;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class EggIncHooks extends HookedApp {
    public static final String PACKAGE = SupportedPackages.Package.PACKAGE_EGGINC.getPackageName();

    public EggIncHooks() {
        super(EggIncHooks.class, SupportedPackages.Package.PACKAGE_EGGINC);
    }

    @Override
    public void initHooks(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (!isEnabled(true)) return;

        new EggIncActivityHooks().initHooks(loadPackageParam);
    }

    @Override
    public void unHookAll() {

    }
}
