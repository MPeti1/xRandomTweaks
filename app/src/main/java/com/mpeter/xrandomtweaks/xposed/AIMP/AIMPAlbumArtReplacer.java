package com.mpeter.xrandomtweaks.xposed.AIMP;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.mpeter.xrandomtweaks.App;
import com.mpeter.xrandomtweaks.xposed.HookedFeature;
import com.mpeter.xrandomtweaks.xposed.SupportedPackages;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.mpeter.xrandomtweaks.xposed.AIMP.AIMPConstants.AIMP_LOGO_ENCODED;

public class AIMPAlbumArtReplacer extends HookedFeature {
    AIMPAlbumArtReplacer() {
        super(AIMPAlbumArtReplacer.class, SupportedPackages.Package.PACKAGE_AIMP, App.FEATURE_TAG_AIMP_ALBUMART_REPLCAE);
    }

    @Override
    public void initHooks(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (!isEnabled()) return;

        XposedHelpers.findAndHookMethod("android.support.v4.media.MediaMetadataCompat$Builder", loadPackageParam.classLoader, "putBitmap", String.class, Bitmap.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                byte[] albumartData = Base64.decode(AIMP_LOGO_ENCODED, 0);
                Bitmap albumArt = BitmapFactory.decodeByteArray(albumartData, 0, albumartData.length);

                param.args[1] = albumArt;
                XposedBridge.log(LOG_TAG + "albumart set");
            }
        });
    }

    @Override
    public void unHookAll() {

    }
}
