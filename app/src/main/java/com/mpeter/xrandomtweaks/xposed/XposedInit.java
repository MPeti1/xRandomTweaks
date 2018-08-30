package com.mpeter.xrandomtweaks.xposed;

import android.content.res.XModuleResources;

import com.mpeter.xrandomtweaks.foresttools.Forester;
import com.mpeter.xrandomtweaks.xposed.AIMP.AIMPHooks;
import com.mpeter.xrandomtweaks.xposed.EggInc.EggIncHooks;
import com.mpeter.xrandomtweaks.xposed.FlashFire.FlashFireHooks;
import com.mpeter.xrandomtweaks.xposed.GBoard.GBoardHooks;
import com.mpeter.xrandomtweaks.xposed.Messenger.MessengerHooks;
import com.mpeter.xrandomtweaks.xposed.MiuiHome.MiuiHomeHooks;
import com.mpeter.xrandomtweaks.xposed.xRandomTweaks.XRandomTweaksHooks;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class XposedInit implements IXposedHookLoadPackage, IXposedHookInitPackageResources, IXposedHookZygoteInit {
    public static final String LOG_TAG = XposedModule.getLogtag(XposedInit.class);
    public static XC_LoadPackage.LoadPackageParam firstLpparam;

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        XposedModule.setModulePath(startupParam.modulePath);
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        SupportedPackages.Package pkg = SupportedPackages.Package.forString(lpparam.packageName);

        if (pkg == null) return;

        firstLpparam = lpparam;

        Forester.forestate();

        XposedModule.init(lpparam);
        CurrentApp.init(lpparam);

        switch (pkg) {
            case PACKAGE_SELF:
                new XRandomTweaksHooks().initHooks(lpparam);
                break;
            case PACKAGE_FB_MESSENGER:
                new MessengerHooks().initHooks(lpparam);
                break;
            case PACKAGE_EGGINC:
                new EggIncHooks().initHooks(lpparam);
                break;
            case PACKAGE_AIMP:
                new AIMPHooks().initHooks(lpparam);
                break;
            case PACKAGE_GBOARD:
                new GBoardHooks().initHooks(lpparam);
                break;
            case PACKAGE_MIUI_HOME:
                new MiuiHomeHooks().initHooks(lpparam);
                break;
            case PACKAGE_FLASHFIRE:
                new FlashFireHooks().initHooks(lpparam);
                break;

            default:
                throw new IllegalStateException(LOG_TAG + "Switch missing a case for a supported package: " + pkg.getPackageName());
        }
    }

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
        SupportedPackages.Package pkg = SupportedPackages.Package.forString(resparam.packageName);

        if (pkg == null) return;

        if (XposedModule.needsResources())
            XposedModule.setResources(XModuleResources.createInstance(XposedModule.getModulePath(), resparam.res));

        switch (pkg) {
            case PACKAGE_SELF:
            case PACKAGE_MIUI_HOME:
            case PACKAGE_FB_MESSENGER:
            case PACKAGE_EGGINC:
            case PACKAGE_AIMP:
            case PACKAGE_GBOARD:
                CurrentApp.initResources(resparam);
                break;
            default:
                break;
        }
    }
}