package com.mpeter.xrandomtweaks.dummy.android.app;

import android.app.AndroidAppHelper;

import de.robv.android.xposed.XposedHelpers;

public class ActivityThread {
    public static final class AppBindData{
        public static final Class<?> CLASS = XposedHelpers.findClass("android.app.ActivityThread.AppBindData", AndroidAppHelper.currentApplication().getClassLoader());
        public static final String int_debugMode = "debugMode";

        public static void xsetDebugMode(int mode){
            XposedHelpers.setStaticIntField(CLASS, "debugMode", mode);
        }
    }

    private class ApplicationThread extends ApplicationThreadNative{

    }
}
