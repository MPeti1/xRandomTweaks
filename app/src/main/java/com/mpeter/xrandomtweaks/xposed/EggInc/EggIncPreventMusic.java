package com.mpeter.xrandomtweaks.xposed.EggInc;

import com.mpeter.xrandomtweaks.App;
import com.mpeter.xrandomtweaks.xposed.HookedFeature;
import com.mpeter.xrandomtweaks.xposed.SupportedPackages;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.mpeter.xrandomtweaks.xposed.EggInc.EggIncConstants.CLASS_EggIncActivity;
import static com.mpeter.xrandomtweaks.xposed.EggInc.EggIncConstants.METHOD_playMusic;

public class EggIncPreventMusic extends HookedFeature {
    EggIncPreventMusic() {
        super(EggIncPreventMusic.class, SupportedPackages.Package.PACKAGE_EGGINC, App.FEATURE_TAG_EGGINC_PREVENT_MUSIC);
    }

    @Override
    public void initHooks(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (!isEnabled()) return;

        XposedHelpers.findAndHookMethod(CLASS_EggIncActivity, loadPackageParam.classLoader, METHOD_playMusic, String.class, boolean.class, float.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(null);
            }
        });
    }

    @Override
    public void unHookAll() {

    }
}
