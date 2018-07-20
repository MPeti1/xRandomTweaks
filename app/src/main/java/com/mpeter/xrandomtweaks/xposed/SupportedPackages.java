package com.mpeter.xrandomtweaks.xposed;

import com.mpeter.xrandomtweaks.BuildConfig;

import java.util.ArrayList;

public class SupportedPackages {
    public enum Package {
        PACKAGE_SELF(BuildConfig.APPLICATION_ID),
        PACKAGE_MIUI_HOME("com.miui.home"),
        PACKAGE_FB_MESSENGER("com.facebook.orca"),
        PACKAGE_EGGINC("com.auxbrain.egginc"),
        PACKAGE_AIMP("com.aimp.player"),
        PACKAGE_GBOARD("com.google.android.inputmethod.latin");

        private final String packageName;

        Package(String s) {
            packageName = s;
        }

        public String getPackageName() {
            return packageName;
        }

        public static Package forString(String packageName) {
            for (Package pkg : Package.values()) {
                if (packageName.equalsIgnoreCase(pkg.packageName))
                    return pkg;
            }

            return null;
        }
    }

    public static ArrayList<String> getPackages() {
        ArrayList<String> packages = new ArrayList<>();

        for (int i = 0; i < Package.values().length; i++) {
            packages.add(Package.values()[i].getPackageName());
        }

        packages.remove(Package.PACKAGE_SELF.getPackageName());

        return packages;
    }
}
