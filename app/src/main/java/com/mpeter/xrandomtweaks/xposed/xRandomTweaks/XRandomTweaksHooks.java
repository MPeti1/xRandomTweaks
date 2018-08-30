package com.mpeter.xrandomtweaks.xposed.xRandomTweaks;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Binder;

import com.mpeter.xrandomtweaks.App;
import com.mpeter.xrandomtweaks.R;
import com.mpeter.xrandomtweaks.utils.TimeUtils;
import com.mpeter.xrandomtweaks.xposed.HookedApp;
import com.mpeter.xrandomtweaks.xposed.SupportedPackages;
import com.mpeter.xrandomtweaks.xposed.XposedModule;

import org.threeten.bp.Duration;
import org.threeten.bp.Instant;

import java.util.Objects;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import timber.log.Timber;

public class XRandomTweaksHooks extends HookedApp {
    public static final String LOG_TAG = XposedModule.getLogtag(XRandomTweaksHooks.class);

    public XRandomTweaksHooks() {
        super(XRandomTweaksHooks.class, SupportedPackages.Package.PACKAGE_SELF);
    }

    @Override
    public void initHooks(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod(XposedModule.class.getCanonicalName(), loadPackageParam.classLoader, "isModuleEnabled", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(true);
            }
        });

        XposedHelpers.findAndHookMethod(ModuleSettingsProvider.class.getCanonicalName(), loadPackageParam.classLoader, "checkAccess", String.class, String.class, boolean.class, new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                Instant s = Instant.now();
                Instant e;

                final String prefName = (String) param.args[0];
                final String prefKey = (String) param.args[1];
                final boolean write = (boolean) param.args[2];

                if (write) return false;

                //original
                String callingPackage = ((ThreadLocal<String>) XposedHelpers.getObjectField(param.thisObject, "mCallingPackage")).get();
                int callingUid = Binder.getCallingUid();

                //by uid
                String[] packagesForCallingUid = XposedModule.getSystemContext().getPackageManager().getPackagesForUid(callingUid);
                String callingPackagesByUid = "";
                for (int i = 0; i < packagesForCallingUid.length; i++) {
                    callingPackagesByUid += packagesForCallingUid[i];
                    if (packagesForCallingUid.length - 1 != i) callingPackagesByUid += ", ";
                }

                Timber.tag(LOG_TAG).d("Calling context is %s (uid %d:%s), trying to access %s in %s",
                        callingPackage,
                        callingUid, callingPackagesByUid,
                        prefKey, prefName
                );

                if (!Objects.equals(callingPackage, packagesForCallingUid[0]) && !Objects.equals(callingPackage, "android"))
                    Timber.tag(LOG_TAG).w("Warning! calling package doesn't match with uid identity. calling package: %s, uid identity: %s",
                            callingPackage,
                            callingPackagesByUid
                    );


                boolean permit = prefKey.startsWith(packagesForCallingUid[0]);
                if (!permit)
                    permit = checkAccessExclusive(packagesForCallingUid, prefName, prefKey);

                e = Instant.now();

                Duration duration = Duration.between(s, e);
                Timber.tag(LOG_TAG).d(TimeUtils.getDurationString("checkAccess", duration));

                return permit;
            }
        });

        XposedHelpers.findAndHookMethod("android.app.ApplicationPackageManager", loadPackageParam.classLoader, "getResourcesForApplication", ApplicationInfo.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Resources r = (Resources) param.getResult();
                super.afterHookedMethod(param);
            }
        });

        XposedHelpers.findAndHookMethod("android.app.ApplicationPackageManager", loadPackageParam.classLoader, "putCachedIcon", "android.app.ApplicationPackageManager.ResourceName", Drawable.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Drawable drawable = (Drawable) param.args[1];
                super.beforeHookedMethod(param);
            }
        });

        XposedHelpers.findAndHookMethod("android.app.ApplicationPackageManager", loadPackageParam.classLoader, "getDrawable", String.class, int.class, ApplicationInfo.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Drawable drawable = (Drawable) param.getResult();
                super.afterHookedMethod(param);
            }
        });

        XposedBridge.hookMethod(
                XposedHelpers.findMethodBestMatch(
                        XposedHelpers.findClass(
                                "android.app.MiuiThemeHelper",
                                loadPackageParam.classLoader),
                        "getDrawable"),
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Drawable drawable = (Drawable) param.getResult();
                        super.afterHookedMethod(param);
                    }
                });

        XposedHelpers.findAndHookMethod(PackageItemInfo.class.getCanonicalName(), loadPackageParam.classLoader, "loadIcon", PackageManager.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Drawable drawable = (Drawable) param.getResult();
                super.afterHookedMethod(param);
            }
        });
    }

    private static boolean checkAccessExclusive(String[] packagesForCallingUid, String prefName, String prefKey) {
        Resources r = XposedModule.getResources();
        boolean permitFile = false;
        boolean permitKey = false;
        String[] permittedFiles = null;
        int[] permittedKeys = null;

        SupportedPackages.Package pkg = SupportedPackages.Package.forString(packagesForCallingUid[0]);
        if (pkg == null) return false;
        switch (pkg) {
            case PACKAGE_SELF:
                break;
            case PACKAGE_MIUI_HOME:
                permittedFiles = new String[]{App.XSETTINGS_PREF_FILE};
                permittedKeys = new int[]{
                        R.string.preload_disabled_hooks
                };
                break;
            case PACKAGE_FB_MESSENGER:
                permittedFiles = new String[]{App.XSETTINGS_PREF_FILE};
                permittedKeys = new int[]{
                        R.string.preload_disabled_hooks
                };
                break;
            case PACKAGE_EGGINC:
                permittedFiles = new String[]{App.XSETTINGS_PREF_FILE};
                permittedKeys = new int[]{
                        R.string.preload_disabled_hooks
                };
                break;
            case PACKAGE_AIMP:
                permittedFiles = new String[]{App.XSETTINGS_PREF_FILE};
                permittedKeys = new int[]{
                        R.string.preload_disabled_hooks
                };
                break;
            case PACKAGE_GBOARD:
                permittedFiles = new String[]{App.XSETTINGS_PREF_FILE};
                permittedKeys = new int[]{
                        R.string.preload_disabled_hooks
                };
                break;

            case PACKAGE_FLASHFIRE:
                permittedFiles = new String[]{App.XSETTINGS_PREF_FILE};
                permittedKeys = new int[]{
                        R.string.preload_disabled_hooks
                };
                break;

            default:
                Timber.tag(LOG_TAG).wtf("No such app: %s", pkg.getPackageName());
                break;
        }

        for (String permittedFile : permittedFiles) {
            if (permittedFile.equals(prefName)) {
                permitFile = true;
                break;
            }
        }

        for (int permittedKey : permittedKeys) {
            if (r.getString(permittedKey).equals(prefKey)) {
                permitKey = true;
                break;
            }
        }

        return permitFile && permitKey;
    }
}
