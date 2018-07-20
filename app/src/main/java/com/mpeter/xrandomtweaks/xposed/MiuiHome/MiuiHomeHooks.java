package com.mpeter.xrandomtweaks.xposed.MiuiHome;

import android.content.res.XResources;

import com.mpeter.xrandomtweaks.xposed.CurrentApp;
import com.mpeter.xrandomtweaks.xposed.HookedApp;
import com.mpeter.xrandomtweaks.xposed.MiuiHome.com.miui.home.launcher.DeviceConfig;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MiuiHomeHooks extends MiuiHomeConstants implements HookedApp {
    public static final String COMPONENT_NAME = MiuiHomeHooks.class.getSimpleName() + ": ";

    static boolean asd = true; //false
    static boolean asd2 = true;
    static int asd1 = 0;

    @Override
    public void initHooks(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
        /*XposedHelpers.findAndHookMethod(CLASS_DeviceConfig, loadPackageParam.classLoader, METHOD_getWidgetCellPaddingTop, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(0);
            }
        });*/

        XposedHelpers.findAndHookMethod(CLASS_CellLayout, loadPackageParam.classLoader, METHOD_onMeasure, int.class, int.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                int widgetWorkingHeight = DeviceConfig.getWidgetWorkingHeight();
                int mWidgetCellHeight = XposedHelpers.getIntField(param.thisObject, "mWidgetCellHeight");
                int mVCells = XposedHelpers.getIntField(param.thisObject, "mVCells");
                int widgetCellPaddingTop = DeviceConfig.getWidgetCellPaddingTop();
                int cellWorkingHeight = DeviceConfig.getCellWorkingHeight();
                int cellHeight = XposedHelpers.getIntField(param.thisObject, "mCellHeight"); //tényleges cell height
                int mCellPaddingTop = XposedHelpers.getIntField(param.thisObject, "mCellPaddingTop"); //workspace padding top
                //valójában csak az utolsó 2 számít

                //screentype 2 autocelssize
                int cellPaddingTop_1 = (widgetWorkingHeight - (mWidgetCellHeight * mVCells)) / 2;

                //screentype 2 normalcellsize
                int cellPaddingTop_2 = widgetCellPaddingTop + ((widgetWorkingHeight - (mWidgetCellHeight * mVCells)) / 2);

                //screentype x
                int heightGap = Math.round(((float) (cellWorkingHeight - (cellHeight * mVCells))) / (((float) mVCells) - 1.0f));
            }
        });

        XposedHelpers.findAndHookMethod(CL_SUBCLASS_LayoutParams, loadPackageParam.classLoader, METHOD_setup, int.class, int.class, int.class, int.class, int.class, int.class, int.class, int.class, int.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
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

    @Override
    public void unHookAll() {
    }
}