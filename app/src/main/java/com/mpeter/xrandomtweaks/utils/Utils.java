package com.mpeter.xrandomtweaks.utils;

import android.content.res.ColorStateList;

import de.robv.android.xposed.XposedHelpers;

public class Utils {
    public static ColorStateList getColorStatelist(int themeColor){
        // FOR NAVIGATION VIEW ITEM TEXT COLOR
        int[][] state = new int[][] {
                new int[] {-android.R.attr.state_enabled}, // disabled
                new int[] {android.R.attr.state_enabled},  // enabled
                new int[] {-android.R.attr.state_checked}, // unchecked
                new int[] { android.R.attr.state_pressed}  // pressed

        };

        int[] color = new int[] {
                themeColor,
                themeColor,
                themeColor,
                themeColor
        };

        return new ColorStateList(state, color);
    }

    public static String getCurrentProcessname(){
        Class ActivityThread = XposedHelpers.findClass("ActivityThread", Utils.class.getClassLoader());
        String processName = (String) XposedHelpers.callMethod(ActivityThread, "getProcessName");
        String packageName = (String) XposedHelpers.callMethod(ActivityThread, "getPackageName");

        return String.format("process name: %1$s, package name: %2$s", processName, packageName);
    }
}
