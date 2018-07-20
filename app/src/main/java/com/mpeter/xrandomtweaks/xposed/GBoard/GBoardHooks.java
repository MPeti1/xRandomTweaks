package com.mpeter.xrandomtweaks.xposed.GBoard;

import com.mpeter.xrandomtweaks.xposed.HookedApp;
import com.mpeter.xrandomtweaks.xposed.SupportedPackages;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class GBoardHooks extends HookedApp {
    public GBoardHooks() {
        super(GBoardHooks.class, SupportedPackages.Package.PACKAGE_GBOARD);
    }

    @Override
    public void initHooks(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (!isEnabled(true)) return;

        new GBoardOldRoundCorner().initHooks(loadPackageParam);
    }

    @Override
    public void unHookAll() {

    }
}
