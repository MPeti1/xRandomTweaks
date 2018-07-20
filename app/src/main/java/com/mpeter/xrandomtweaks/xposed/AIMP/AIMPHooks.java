package com.mpeter.xrandomtweaks.xposed.AIMP;

import com.mpeter.xrandomtweaks.xposed.HookedApp;
import com.mpeter.xrandomtweaks.xposed.SupportedPackages;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class AIMPHooks extends HookedApp {
    public AIMPHooks() {
        super(AIMPHooks.class, SupportedPackages.Package.PACKAGE_AIMP);
    }

    @Override
    public void initHooks(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (!isEnabled(true)) return;

        new AIMPAlbumArtReplacer().initHooks(loadPackageParam);
        new AIMPRestartOnLongpause().initHooks(loadPackageParam);
    }

    @Override
    public void unHookAll() {

    }
}
