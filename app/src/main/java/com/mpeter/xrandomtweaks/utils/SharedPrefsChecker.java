package com.mpeter.xrandomtweaks.utils;

import de.robv.android.xposed.XSharedPreferences;

public class SharedPrefsChecker {
    public static boolean getBoolean(XSharedPreferences sharedPrefers, String key) {
        boolean unsure;

        unsure = sharedPrefers.getBoolean(key, true);

        if (unsure && !sharedPrefers.getBoolean(key, false)){
            throw new IllegalArgumentException(key + " doesn not exist in this sharedprefs instance");
        } else return unsure;
    }
}
