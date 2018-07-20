package com.mpeter.xrandomtweaks.xposed;

import timber.log.Timber;

public abstract class HookedFeature extends HookedApp {
    public final String FEATURE_TAG;

    public HookedFeature(Class clazz, SupportedPackages.Package pkg, String FEATURE_TAG) {
        super(clazz, pkg);

        this.FEATURE_TAG = PACKAGE + "_" + FEATURE_TAG;
    }

    @Override
    protected boolean isEnabled() {
        return isEnabled(true);
    }

    @Override
    protected boolean isEnabled(boolean def) {
        if (super.isEnabled(def) && XposedModule.getEnabledPackages().getBoolean(FEATURE_TAG, def)){
            Timber.tag(LOG_TAG).d("%s hooks are enabled", FEATURE_TAG);
            return true;

        } else {
            Timber.tag(LOG_TAG).d("%s hooks are disabled", FEATURE_TAG);
            return false;
        }
    }
}
