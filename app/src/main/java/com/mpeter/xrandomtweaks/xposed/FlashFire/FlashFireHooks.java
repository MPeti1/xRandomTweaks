package com.mpeter.xrandomtweaks.xposed.FlashFire;

import com.mpeter.xrandomtweaks.xposed.HookedApp;
import com.mpeter.xrandomtweaks.xposed.ModuleSettings;
import com.mpeter.xrandomtweaks.xposed.SupportedPackages;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class FlashFireHooks extends HookedApp {
    static boolean enableHooks = false;
    static boolean hookProReal = false;

    public FlashFireHooks() {
        super(FlashFireHooks.class, SupportedPackages.Package.PACKAGE_FLASHFIRE);
    }

    @Override
    public void initHooks(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (!enableHooks && !ModuleSettings.preloadDisabledHooks()) return;

        if (hookProReal || ModuleSettings.preloadDisabledHooks()){
            hookProReal(loadPackageParam);
        }

        hookLogEnable(loadPackageParam);
    }

    private void hookProReal(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod("eu.chainfire.flash.ui.misc.InAppPurchases", loadPackageParam.classLoader, "getOrders", String.class, boolean.class, new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                if (!hookProReal) return param.getResult();

                Class Order = XposedHelpers.findClass("eu.chainfire.flash.ui.misc.InAppPurchases.Order", loadPackageParam.classLoader);
                return Array.newInstance(Order, 1);

            }
        });
    }

    private void hookLogEnable(XC_LoadPackage.LoadPackageParam loadPackageParam){
        Class Logger = XposedHelpers.findClass("eu.chainfire.librootjava.Logger", loadPackageParam.classLoader);
        Method[] methods = Logger.getDeclaredMethods();
        for (Method method : methods) {
            XposedBridge.hookMethod(method, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    XposedHelpers.setStaticBooleanField(Logger, "log", true);
                }
            });
        }
    }

    public static void setEnableHooks(boolean enableHooks) {
        FlashFireHooks.enableHooks = enableHooks;
    }

    public static void setHookProReal(boolean hookProReal) {
        FlashFireHooks.hookProReal = hookProReal;
    }
}
