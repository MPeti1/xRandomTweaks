package com.mpeter.xrandomtweaks.xposed.EggInc;

import com.mpeter.xrandomtweaks.xposed.HookedApp;
import com.mpeter.xrandomtweaks.xposed.ModuleSettings;
import com.mpeter.xrandomtweaks.xposed.SupportedPackages;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import timber.log.Timber;

import static com.mpeter.xrandomtweaks.xposed.EggInc.EggIncConstants.CLASS_EggIncActivity;
import static com.mpeter.xrandomtweaks.xposed.EggInc.EggIncConstants.METHOD_initAdPlatforms;
import static com.mpeter.xrandomtweaks.xposed.EggInc.EggIncConstants.METHOD_playAdColonyVideoAd;
import static com.mpeter.xrandomtweaks.xposed.EggInc.EggIncConstants.METHOD_playChartboostVideoAd;
import static com.mpeter.xrandomtweaks.xposed.EggInc.EggIncConstants.METHOD_playMusic;
import static com.mpeter.xrandomtweaks.xposed.EggInc.EggIncConstants.METHOD_playUnityVideoAd;
import static com.mpeter.xrandomtweaks.xposed.EggInc.EggIncConstants.METHOD_playVungleVideoAd;
import static com.mpeter.xrandomtweaks.xposed.EggInc.EggIncConstants.METHOD_videoAdViewComplete;

public class EggIncHooks extends HookedApp {
    private static boolean hooksEnabled;
    private static boolean preventMusic;
    private static boolean preventAdLoad;
    private static boolean skipAds;

    public EggIncHooks() {
        super(EggIncHooks.class, SupportedPackages.Package.PACKAGE_EGGINC);
    }

    @Override
    public void initHooks(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (!hooksEnabled && !ModuleSettings.preloadDisabledHooks()) return;

        if (preventMusic || ModuleSettings.preloadDisabledHooks())
            hookPreventMusic(loadPackageParam);

        if (skipAds || ModuleSettings.preloadDisabledHooks())
            hookSkipAds(loadPackageParam);

        if (preventAdLoad || ModuleSettings.preloadDisabledHooks()) {
            hookPreventInitAdPlatforms(loadPackageParam);
            if (!skipAds) hookSkipAds(loadPackageParam);
        }
    }

    private void hookPreventMusic(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod(CLASS_EggIncActivity, loadPackageParam.classLoader, METHOD_playMusic, String.class, boolean.class, float.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (hooksEnabled && preventMusic)
                    param.setResult(null);
            }
        });
    }

    private void hookSkipAds(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod(CLASS_EggIncActivity, loadPackageParam.classLoader, METHOD_playVungleVideoAd, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (hooksEnabled && skipAds) {
                    param.setResult(true);
                    videoAdViewComplete(param);
                }
            }
        });

        XposedHelpers.findAndHookMethod(CLASS_EggIncActivity, loadPackageParam.classLoader, METHOD_playAdColonyVideoAd, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (hooksEnabled && skipAds) {
                    param.setResult(true);
                    videoAdViewComplete(param);
                }
            }
        });

        XposedHelpers.findAndHookMethod(CLASS_EggIncActivity, loadPackageParam.classLoader, METHOD_playChartboostVideoAd, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (hooksEnabled && skipAds) {
                    param.setResult(true);
                    videoAdViewComplete(param);
                }
            }
        });

        XposedHelpers.findAndHookMethod(CLASS_EggIncActivity, loadPackageParam.classLoader, METHOD_playUnityVideoAd, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (hooksEnabled && skipAds) {
                    param.setResult(true);
                    videoAdViewComplete(param);
                }
            }
        });
    }

    private void hookPreventInitAdPlatforms(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod(CLASS_EggIncActivity, loadPackageParam.classLoader, METHOD_initAdPlatforms, XC_MethodReplacement.DO_NOTHING);
    }

    private void videoAdViewComplete(XC_MethodHook.MethodHookParam param) {
        XposedHelpers.callMethod(param.thisObject, METHOD_videoAdViewComplete, true);
        Timber.tag(LOG_TAG).i("Skipped an ad");
    }

    public static void setHooksEnabled(boolean hooksEnabled) {
        EggIncHooks.hooksEnabled = hooksEnabled;
    }

    public static void setPreventMusic(boolean preventMusic) {
        EggIncHooks.preventMusic = preventMusic;
    }

    public static void setSkipAds(boolean skipAds) {
        EggIncHooks.skipAds = skipAds;
    }

    public static void setPreventAdLoad(boolean preventAdLoad) {
        EggIncHooks.preventAdLoad = preventAdLoad;
    }
}
