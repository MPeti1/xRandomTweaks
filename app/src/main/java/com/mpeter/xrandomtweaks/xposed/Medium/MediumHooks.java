package com.mpeter.xrandomtweaks.xposed.Medium;

import com.mpeter.xrandomtweaks.xposed.HookedApp;
import com.mpeter.xrandomtweaks.xposed.ModuleSettings;
import com.mpeter.xrandomtweaks.xposed.SupportedPackages;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import timber.log.Timber;

public class MediumHooks extends HookedApp{
    private static boolean hooksEnabled;
    private static boolean youReadALot;
    private static int unlocksRemaining;

    public MediumHooks() {
        super(MediumHooks.class, SupportedPackages.Package.PACKAGE_MEDIUM);
    }

    @Override
    public void initHooks(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (!hooksEnabled && !ModuleSettings.preloadDisabledHooks()) return;
        if (youReadALot || ModuleSettings.preloadDisabledHooks()){
            hookUnlimitedAccess(loadPackageParam);
        }
    }

    private void hookUnlimitedAccess(XC_LoadPackage.LoadPackageParam loadPackageParam){
        XposedHelpers.findAndHookConstructor("com.medium.android.common.generated.MeteringProtos.MeteringInfo", loadPackageParam.classLoader, "com.medium.android.common.generated.MeteringProtos.MeteringInfo.Builder", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (!youReadALot) return;
                XposedHelpers.setIntField(param.args[0], "unlocksRemaining", unlocksRemaining);
                Timber.tag(LOG_TAG).d("unlocksremaining is now %d", XposedHelpers.getIntField(param.args[0], "unlocksRemaining"));
                super.beforeHookedMethod(param);
            }
        });

        XposedHelpers.findAndHookMethod("com.medium.android.common.post.Posts", loadPackageParam.classLoader, "canPresent", "com.medium.android.common.generated.PostProtos.Post", "com.google.common.base.Optional", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }
        });
    }

    public static void setHooksEnabled(boolean hooksEnabled) {
        MediumHooks.hooksEnabled = hooksEnabled;
    }

    public static void setYouReadALot(boolean youReadALot) {
        MediumHooks.youReadALot = youReadALot;
    }

    public static void setUnlocksRemaining(int unlocksRemaining) {
        MediumHooks.unlocksRemaining = unlocksRemaining;
    }
}
