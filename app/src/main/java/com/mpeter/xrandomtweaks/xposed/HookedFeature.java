package com.mpeter.xrandomtweaks.xposed;

public abstract class HookedFeature extends HookedApp {
    public final String FEATURE_TAG;

    public HookedFeature(Class clazz, SupportedPackages.Package pkg, String FEATURE_TAG) {
        super(clazz, pkg);

        this.FEATURE_TAG = PACKAGE + "_" + FEATURE_TAG;
    }
}
