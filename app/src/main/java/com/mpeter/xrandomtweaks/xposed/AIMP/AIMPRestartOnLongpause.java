package com.mpeter.xrandomtweaks.xposed.AIMP;

import com.mpeter.xrandomtweaks.App;
import com.mpeter.xrandomtweaks.xposed.HookedFeature;
import com.mpeter.xrandomtweaks.xposed.SupportedPackages;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class AIMPRestartOnLongpause extends HookedFeature {
    AIMPRestartOnLongpause() {
        super(AIMPRestartOnLongpause.class, SupportedPackages.Package.PACKAGE_AIMP, App.FEATURE_TAG_AIMP_LONGPAUSE_RESTART);
    }

    @Override
    public void initHooks(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (!isEnabled()) return;

        XposedHelpers.findAndHookMethod("com.aimp.player.service.helpers.MediaButtonHandler", loadPackageParam.classLoader, "getActionByKeyCode", int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Class AppService = XposedHelpers.findClass("com.aimp.player.service.AppService", loadPackageParam.classLoader);

                String fieldActionStop = "ACTION_STOP";

                if ((int) param.args[0] == 86) {
                    param.setResult(XposedHelpers.getStaticObjectField(AppService, fieldActionStop));
                    XposedBridge.log(LOG_TAG + "action set to STOP from PAUSE");
                }
            }
        });
    }

    @Override
    public void unHookAll() {

    }
}
