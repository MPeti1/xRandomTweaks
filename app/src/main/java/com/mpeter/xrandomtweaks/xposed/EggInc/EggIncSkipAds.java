package com.mpeter.xrandomtweaks.xposed.EggInc;

import com.mpeter.xrandomtweaks.App;
import com.mpeter.xrandomtweaks.xposed.HookedFeature;
import com.mpeter.xrandomtweaks.xposed.SupportedPackages;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.mpeter.xrandomtweaks.xposed.EggInc.EggIncConstants.CLASS_EggIncActivity;
import static com.mpeter.xrandomtweaks.xposed.EggInc.EggIncConstants.METHOD_playAdColonyVideoAd;
import static com.mpeter.xrandomtweaks.xposed.EggInc.EggIncConstants.METHOD_playChartboostVideoAd;
import static com.mpeter.xrandomtweaks.xposed.EggInc.EggIncConstants.METHOD_playUnityVideoAd;
import static com.mpeter.xrandomtweaks.xposed.EggInc.EggIncConstants.METHOD_playVungleVideoAd;
import static com.mpeter.xrandomtweaks.xposed.EggInc.EggIncConstants.METHOD_videoAdViewComplete;

public class EggIncSkipAds extends HookedFeature {
    EggIncSkipAds() {
        super(EggIncSkipAds.class, SupportedPackages.Package.PACKAGE_EGGINC, App.FEATURE_TAG_EGGINC_SKIP_ADS);
    }

    @Override
    public void initHooks(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (!isEnabled()) return;

        XposedHelpers.findAndHookMethod(CLASS_EggIncActivity, loadPackageParam.classLoader, METHOD_playVungleVideoAd, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(true);
                videoAdViewComplete(param);
            }
        });

        XposedHelpers.findAndHookMethod(CLASS_EggIncActivity, loadPackageParam.classLoader, METHOD_playAdColonyVideoAd, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(true);
                videoAdViewComplete(param);
            }
        });

        XposedHelpers.findAndHookMethod(CLASS_EggIncActivity, loadPackageParam.classLoader, METHOD_playChartboostVideoAd, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(true);
                videoAdViewComplete(param);
            }
        });

        XposedHelpers.findAndHookMethod(CLASS_EggIncActivity, loadPackageParam.classLoader, METHOD_playUnityVideoAd, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(true);
                videoAdViewComplete(param);
            }
        });
    }

    private void videoAdViewComplete(XC_MethodHook.MethodHookParam param){
        XposedHelpers.callMethod(param.thisObject, METHOD_videoAdViewComplete, true);
    }

    @Override
    public void unHookAll() {

    }
}
