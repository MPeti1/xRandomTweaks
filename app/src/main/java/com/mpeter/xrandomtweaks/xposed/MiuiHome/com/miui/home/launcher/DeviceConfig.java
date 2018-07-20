package com.mpeter.xrandomtweaks.xposed.MiuiHome.com.miui.home.launcher;

import android.content.res.XResources;

import com.mpeter.xrandomtweaks.xposed.CurrentApp;
import com.mpeter.xrandomtweaks.xposed.XposedModule;

import java.lang.reflect.Method;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static com.mpeter.xrandomtweaks.xposed.MiuiHome.MiuiHomeConstants.FIELD_mCellWorkingHeight;

public class DeviceConfig {
    public static final String LOG_TAG = XposedModule.getLogtag(DeviceConfig.class);

    private static String mAutoCellSize = "config_enable_auto_cellsize";

    public static final Class CLASS = XposedHelpers.findClass("com.miui.home.launcher.DeviceConfig", CurrentApp.getClassLoader());

    class Fields {
        public static final String mWidgetCellPaddingTop = "mWidgetCellPaddingTop";
        public static final String mWidgetWorkingHeight = "mWidgetWorkingHeight";
    }

    public static int getWidgetCellPaddingTop() {
        return XposedHelpers.getStaticIntField(CLASS, Fields.mWidgetCellPaddingTop);
    }

    public static int getWidgetWorkingHeight() {
        return XposedHelpers.getStaticIntField(CLASS, Fields.mWidgetWorkingHeight);
    }

    public static boolean isAutoCellSize() {
        XResources resources = CurrentApp.getResources();
        return resources.getBoolean(resources.getIdentifier(mAutoCellSize, "bool", CurrentApp.getPackageName()));
    }

    public static int getCellWorkingHeight() {
        return XposedHelpers.getStaticIntField(CLASS, FIELD_mCellWorkingHeight);
    }

    public static int getStatusBarHeight() {
        return XposedHelpers.getStaticIntField(CLASS, "mStatusBarHeight");
    }

    public static int getWorkspaceCellPaddingTop() {
        return (int) XposedHelpers.callStaticMethod(CLASS, "getWorkspaceCellPaddingTop");
    }

    public static int getWorkspaceCellPaddingBottom() {
        Method[] methods = CLASS.getMethods();
        String log = methods.length + " methods:\n";
        for (int i = 0; i < methods.length; i++) {
            log += methods[i].getName() + "\n";
        }

        XposedBridge.log(log);
        return (int) XposedHelpers.callStaticMethod(CLASS, "getWorkspaceCellPaddingBottom");
    }

    public static int getScreenHeight() {
        return XposedHelpers.getStaticIntField(CLASS, "mScreenHeight");
    }

    public static int getCellCountY() {
        return (int) XposedHelpers.callStaticMethod(CLASS, "getCellCountY");
    }
}
