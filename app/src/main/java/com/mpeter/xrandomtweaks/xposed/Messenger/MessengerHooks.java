package com.mpeter.xrandomtweaks.xposed.Messenger;

import com.mpeter.xrandomtweaks.xposed.HookedApp;
import com.mpeter.xrandomtweaks.xposed.SupportedPackages;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MessengerHooks extends HookedApp {

    public MessengerHooks() {
        super(MessengerHooks.class, SupportedPackages.Package.PACKAGE_FB_MESSENGER);
    }

    @Override
    public void initHooks(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (!isEnabled(true)) return;

        new SoccerViewHooks().initHooks(loadPackageParam);
    }

    @Override
    public void unHookAll() {

    }
}
