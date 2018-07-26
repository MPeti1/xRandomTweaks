package com.mpeter.xrandomtweaks.xposed.MiuiHome;

import android.content.res.XResources;

import com.mpeter.xrandomtweaks.xposed.CurrentApp;
import com.mpeter.xrandomtweaks.xposed.HookedApp;
import com.mpeter.xrandomtweaks.xposed.MiuiHome.com.miui.home.launcher.DeviceConfig;
import com.mpeter.xrandomtweaks.xposed.ModuleSettings;
import com.mpeter.xrandomtweaks.xposed.SupportedPackages;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.mpeter.xrandomtweaks.xposed.MiuiHome.MiuiHomeConstants.CL_SUBCLASS_LayoutParams;
import static com.mpeter.xrandomtweaks.xposed.MiuiHome.MiuiHomeConstants.METHOD_setup;

public class MiuiHomeHooks extends HookedApp {
    private static boolean hooksEnabled;
    private static boolean fixHeightGap;

    public MiuiHomeHooks() {
        super(MiuiHomeHooks.class, SupportedPackages.Package.PACKAGE_MIUI_HOME);
    }

    @Override
    public void initHooks(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (!hooksEnabled) return;

        if (fixHeightGap || ModuleSettings.preloadDisabledHooks())
            hookHeightGapFix(loadPackageParam);
    }

    private void hookHeightGapFix(XC_LoadPackage.LoadPackageParam loadPackageParam){
        XposedHelpers.findAndHookMethod(CL_SUBCLASS_LayoutParams, loadPackageParam.classLoader, METHOD_setup, int.class, int.class, int.class, int.class, int.class, int.class, int.class, int.class, int.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (!hooksEnabled || !fixHeightGap) return;

                int cellX = 0, cellY = 1, spanX = 2, spanY = 3, cellWidth = 4, cellHeight = 5, widthGap = 6, heightGap = 7, hStartPadding = 8, vStartPadding = 9,
                        screenHeight = DeviceConfig.getScreenHeight(),
                        availableHeight,
                        hotSeatHeight,
                        cellCountY = DeviceConfig.getCellCountY();

                XResources r = CurrentApp.getResources();

                hotSeatHeight = r.getDimensionPixelSize(r.getIdentifier("hotseats_height", "dimen", CurrentApp.getPackageName()));
                availableHeight = screenHeight - (int) param.args[vStartPadding] - hotSeatHeight;

                param.args[heightGap] = (availableHeight / cellCountY) - ((int) param.args[cellHeight]);

//                param.args[7] = 0;
            }
        });
    }

    public static void setHooksEnabled(boolean hooksEnabled) {
        MiuiHomeHooks.hooksEnabled = hooksEnabled;
    }

    public static void setFixHeightGap(boolean fixHeightGap) {
        MiuiHomeHooks.fixHeightGap = fixHeightGap;
    }
}