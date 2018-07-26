package com.mpeter.xrandomtweaks.xposed.AIMP;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.mpeter.xrandomtweaks.xposed.HookedApp;
import com.mpeter.xrandomtweaks.xposed.ModuleSettings;
import com.mpeter.xrandomtweaks.xposed.SupportedPackages;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.mpeter.xrandomtweaks.xposed.AIMP.AIMPConstants.AIMP_LOGO_ENCODED;

public class AIMPHooks extends HookedApp {
    private static boolean hooksEnabled;
    private static boolean replaceAlbumArt;
    private static boolean restartOnLongpause;

    public AIMPHooks() {
        super(AIMPHooks.class, SupportedPackages.Package.PACKAGE_AIMP);
    }

    @Override
    public void initHooks(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (!hooksEnabled) return;

        if (replaceAlbumArt || ModuleSettings.preloadDisabledHooks()){
            hookReplaceAlbumart(loadPackageParam);
        }

        if (restartOnLongpause || ModuleSettings.preloadDisabledHooks()){
           hookRestartlongPause(loadPackageParam);
        }
    }

    private void hookReplaceAlbumart(XC_LoadPackage.LoadPackageParam loadPackageParam){
        XposedHelpers.findAndHookMethod("android.support.v4.media.MediaMetadataCompat$Builder", loadPackageParam.classLoader, "putBitmap", String.class, Bitmap.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (!hooksEnabled || !replaceAlbumArt)
                    return;

                byte[] albumartData = Base64.decode(AIMP_LOGO_ENCODED, 0);
                Bitmap albumArt = BitmapFactory.decodeByteArray(albumartData, 0, albumartData.length);

                param.args[1] = albumArt;
                XposedBridge.log(LOG_TAG + "albumart set");
            }
        });
    }

    private void hookRestartlongPause(XC_LoadPackage.LoadPackageParam loadPackageParam){
        XposedHelpers.findAndHookMethod("com.aimp.player.service.helpers.MediaButtonHandler", loadPackageParam.classLoader, "getActionByKeyCode", int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (!hooksEnabled || !restartOnLongpause)
                    return;

                Class AppService = XposedHelpers.findClass("com.aimp.player.service.AppService", loadPackageParam.classLoader);

                String fieldActionStop = "ACTION_STOP";

                if ((int) param.args[0] == 86) {
                    param.setResult(XposedHelpers.getStaticObjectField(AppService, fieldActionStop));
                    XposedBridge.log(LOG_TAG + "action set to STOP from PAUSE");
                }
            }
        });
    }

    public static void setHooksEnabled(boolean hooksEnabled) {
        AIMPHooks.hooksEnabled = hooksEnabled;
    }

    public static void setReplaceAlbumArt(boolean replaceAlbumArt) {
        AIMPHooks.replaceAlbumArt = replaceAlbumArt;
    }

    public static void setRestartOnLongpause(boolean restartOnLongpause) {
        AIMPHooks.restartOnLongpause = restartOnLongpause;
    }
}
