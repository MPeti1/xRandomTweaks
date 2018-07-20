package com.mpeter.xrandomtweaks.xposed.Messenger;

import com.mpeter.xrandomtweaks.xposed.HookedApp;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MessengerHooks implements HookedApp {
    @Override
    public void initHooks(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        new MessengerDebugHooks().initHooks(loadPackageParam);
        new SoccerViewHooks().initHooks(loadPackageParam);
    }

    @Override
    public void unHookAll() {

    }
}
