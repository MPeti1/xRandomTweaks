package com.mpeter.xrandomtweaks.xposed;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import timber.log.Timber;

public abstract class HookedApp {
    public final String LOG_TAG;
    public final String PACKAGE;

    public HookedApp(Class clazz, SupportedPackages.Package pkg) {
        LOG_TAG = XposedModule.getLogtag(clazz);
        PACKAGE = pkg.getPackageName();
    }

    public abstract void initHooks(XC_LoadPackage.LoadPackageParam loadPackageParam);
    public abstract void unHookAll();

    protected boolean isEnabled(boolean def){
        if (XposedModule.getEnabledPackages().getBoolean(PACKAGE, def)){
            Timber.tag(LOG_TAG).d("%s hooks are enabled", PACKAGE);
            return true;
        } else {
            Timber.tag(LOG_TAG).d("%s hooks are disabled", PACKAGE);
            return false;
        }
    }
}
