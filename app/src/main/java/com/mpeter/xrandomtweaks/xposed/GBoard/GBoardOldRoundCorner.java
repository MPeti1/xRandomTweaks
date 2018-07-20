package com.mpeter.xrandomtweaks.xposed.GBoard;

import android.graphics.Paint;
import android.graphics.RectF;
import android.util.TypedValue;

import com.mpeter.xrandomtweaks.App;
import com.mpeter.xrandomtweaks.xposed.HookedFeature;
import com.mpeter.xrandomtweaks.xposed.SupportedPackages;
import com.mpeter.xrandomtweaks.xposed.XposedModule;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class GBoardOldRoundCorner extends HookedFeature {
    public static final String LOG_TAG = XposedModule.getLogtag(GBoardHooks.class);
    public static final float ROUND_CORNER_DIP = 3f; //nagyjából ennyi (3f) volt a mértéke a régi verziókban
    private static float ROUND_CORNER = 0f; //automatikus, a fentit állítsd, ne ezt.

    public GBoardOldRoundCorner() {
        super(GBoardOldRoundCorner.class, SupportedPackages.Package.PACKAGE_GBOARD, App.FEATURE_TAG_GBOARD_OLD_ROUNDCORNER);
    }

    @Override
    public void initHooks(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (!isEnabled()) return;

        XposedHelpers.findAndHookMethod("android.graphics.Canvas", loadPackageParam.classLoader, "drawRoundRect", float.class, float.class, float.class, float.class, float.class, float.class, Paint.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                initRoundCorner();

                param.args[4] = ROUND_CORNER;
                param.args[5] = ROUND_CORNER;
            }
        });

        XposedHelpers.findAndHookMethod("android.graphics.Canvas", loadPackageParam.classLoader, "drawRoundRect", RectF.class, float.class, float.class, Paint.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                initRoundCorner();

                param.args[1] = ROUND_CORNER;
                param.args[2] = ROUND_CORNER;
            }
        });
    }

    private void initRoundCorner(){
        if (ROUND_CORNER == 0f){
            if (XposedModule.getResources() != null) {
                ROUND_CORNER = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ROUND_CORNER_DIP, XposedModule.getResources().getDisplayMetrics());
            } else XposedBridge.log(LOG_TAG + "modres nem elérhető");
        }
    }

    @Override
    public void unHookAll() {

    }
}
