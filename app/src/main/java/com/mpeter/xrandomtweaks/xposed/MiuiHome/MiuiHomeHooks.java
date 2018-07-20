package com.mpeter.xrandomtweaks.xposed.MiuiHome;

import com.mpeter.xrandomtweaks.xposed.HookedApp;
import com.mpeter.xrandomtweaks.xposed.MiuiHome.com.miui.home.launcher.DeviceConfig;
import com.mpeter.xrandomtweaks.xposed.SupportedPackages;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.mpeter.xrandomtweaks.xposed.MiuiHome.MiuiHomeConstants.CLASS_CellLayout;
import static com.mpeter.xrandomtweaks.xposed.MiuiHome.MiuiHomeConstants.METHOD_onMeasure;

public class MiuiHomeHooks extends HookedApp {
    public MiuiHomeHooks() {
        super(MiuiHomeHooks.class, SupportedPackages.Package.PACKAGE_MIUI_HOME);
    }

    @Override
    public void initHooks(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (!isEnabled(true)) return;

        new MiuiHomeHeightGapFix().initHooks(loadPackageParam);

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
    }

    @Override
    public void unHookAll() {
    }
}