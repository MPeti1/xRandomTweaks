package com.mpeter.xrandomtweaks.xposed;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.XModuleResources;

import com.mpeter.xrandomtweaks.R;
import com.mpeter.xrandomtweaks.xposed.AIMP.AIMPHooks;
import com.mpeter.xrandomtweaks.xposed.EggInc.EggIncHooks;
import com.mpeter.xrandomtweaks.xposed.FlashFire.FlashFireHooks;
import com.mpeter.xrandomtweaks.xposed.GBoard.GBoardHooks;
import com.mpeter.xrandomtweaks.xposed.Messenger.MessengerHooks;
import com.mpeter.xrandomtweaks.xposed.MiuiHome.MiuiHomeHooks;

import timber.log.Timber;

public class ModuleSettings implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String LOG_TAG = XposedModule.getLogtag(ModuleSettings.class);
    private static ModuleSettings statik;

    private boolean preloadDisabledHooks;
    private SharedPreferences xSettings;

    private ModuleSettings() {
        xSettings = XposedModule.getXSettings();
        XModuleResources r = XposedModule.getResources();

        setPreloadDisabledHooks(xSettings.getBoolean(r.getString(R.string.preload_disabled_hooks), false));

        SupportedPackages.Package pkg = SupportedPackages.Package.forString(XposedInit.firstLpparam.packageName);

        switch (pkg) {
            case PACKAGE_SELF:
                break;
            case PACKAGE_MIUI_HOME:
                MiuiHomeHooks.setHooksEnabled(xSettings.getBoolean(r.getString(R.string.miuihome_hooks_enabled), false));
                MiuiHomeHooks.setFixHeightGap(xSettings.getBoolean(r.getString(R.string.miuihome_fix_heightgap), false));
                break;
            case PACKAGE_FB_MESSENGER:
                MessengerHooks.setHooksEnabled(xSettings.getBoolean(r.getString(R.string.messenger_hooks_enabled), false));
                MessengerHooks.setCheatInSoccer(xSettings.getBoolean(r.getString(R.string.messenger_cheat_soccer), false));
                MessengerHooks.setSoccerScoreToCheat(Integer.valueOf(xSettings.getString(r.getString(R.string.messenger_soccer_score), String.valueOf(0))));
                break;
            case PACKAGE_EGGINC:
                EggIncHooks.setHooksEnabled(xSettings.getBoolean(r.getString(R.string.egginc_hooks_enabled), false));
                EggIncHooks.setPreventMusic(xSettings.getBoolean(r.getString(R.string.egginc_prevent_music), false));
                EggIncHooks.setSkipAds(xSettings.getBoolean(r.getString(R.string.egginc_skip_ads), false));
                EggIncHooks.setPreventAdLoad(xSettings.getBoolean(r.getString(R.string.egginc_prevent_load_ads), false));
                break;
            case PACKAGE_AIMP:
                AIMPHooks.setHooksEnabled(xSettings.getBoolean(r.getString(R.string.aimp_hooks_enabled), false));
                AIMPHooks.setReplaceAlbumArt(xSettings.getBoolean(r.getString(R.string.aimp_replace_albumart), false));
                AIMPHooks.setRestartOnLongpause(xSettings.getBoolean(r.getString(R.string.aimp_restart_on_longpress), false));
                break;
            case PACKAGE_GBOARD:
                GBoardHooks.setHooksEnabled(xSettings.getBoolean(r.getString(R.string.gboard_hooks_enabled), false));
                GBoardHooks.setUseCustomRoundCorner(xSettings.getBoolean(r.getString(R.string.gboard_use_custom_round_corner), false));
                GBoardHooks.setCustomRoundCornerDip(xSettings.getFloat(r.getString(R.string.gboard_custom_round_corner_dip), GBoardHooks.ROUND_CORNER_DIP));
                break;

            case PACKAGE_FLASHFIRE:
                FlashFireHooks.setEnableHooks(xSettings.getBoolean(r.getString(R.string.flashfire_hooks_enabled), false));
                FlashFireHooks.setHookProReal(xSettings.getBoolean(r.getString(R.string.flashfire_hook_proreal), false));
                break;

            default:
                Timber.tag(LOG_TAG).wtf("No such app: %s", pkg.getPackageName());
                break;
        }
    }

    public static ModuleSettings getInstance() {
        if (statik == null)
            statik = new ModuleSettings();

        return statik;
    }

    public void registerXSettingsChangeListener() {
        Timber.tag(LOG_TAG).d("Registering change listener");
        xSettings = XposedModule.getXSettings();
        xSettings.registerOnSharedPreferenceChangeListener(this);
    }

    public static boolean preloadDisabledHooks() {
        return statik.preloadDisabledHooks;
    }

    private void setPreloadDisabledHooks(boolean preloadDisabledHooks) {
        this.preloadDisabledHooks = preloadDisabledHooks;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String packageName = key.substring(0, key.indexOf("_"));
        SupportedPackages.Package pkg = SupportedPackages.Package.forString(packageName);
        Resources r = XposedModule.getResources();

        Timber.tag(LOG_TAG).d("Shared preference change received in package %s, changed key is %s", packageName, key);

        if (pkg == null && packageName.equals("preload"))
            setPreloadDisabledHooks(sharedPreferences.getBoolean(key, false));
        switch (pkg) {
            case PACKAGE_MIUI_HOME:
                if (key.equals(r.getString(R.string.miuihome_hooks_enabled)))
                    MiuiHomeHooks.setHooksEnabled(sharedPreferences.getBoolean(key, false));
                else if (key.equals(r.getString(R.string.miuihome_fix_heightgap)))
                    MiuiHomeHooks.setFixHeightGap(sharedPreferences.getBoolean(key, false));
                else Timber.tag(LOG_TAG).e("No such setting: %s", key);
                break;
            case PACKAGE_FB_MESSENGER:
                if (key.equals(r.getString(R.string.messenger_hooks_enabled)))
                    MessengerHooks.setHooksEnabled(sharedPreferences.getBoolean(key, false));
                else if (key.equals(r.getString(R.string.messenger_cheat_soccer)))
                    MessengerHooks.setCheatInSoccer(sharedPreferences.getBoolean(key, false));
                else if (key.equals(r.getString(R.string.messenger_soccer_score)))
                    MessengerHooks.setSoccerScoreToCheat(sharedPreferences.getInt(key, 0));
                else Timber.tag(LOG_TAG).e("No such setting: %s", key);
                break;
            case PACKAGE_EGGINC:
                if (key.equals(r.getString(R.string.egginc_hooks_enabled)))
                    EggIncHooks.setHooksEnabled(sharedPreferences.getBoolean(key, false));
                else if (key.equals(r.getString(R.string.egginc_prevent_music)))
                    EggIncHooks.setPreventMusic(sharedPreferences.getBoolean(key, false));
                else if (key.equals(r.getString(R.string.egginc_skip_ads)))
                    EggIncHooks.setSkipAds(sharedPreferences.getBoolean(key, false));
                else Timber.tag(LOG_TAG).e("No such setting: %s", key);
                break;
            case PACKAGE_AIMP:
                if (key.equals(r.getString(R.string.aimp_hooks_enabled)))
                    AIMPHooks.setHooksEnabled(sharedPreferences.getBoolean(key, false));
                else if (key.equals(r.getString(R.string.aimp_replace_albumart)))
                    AIMPHooks.setReplaceAlbumArt(sharedPreferences.getBoolean(key, false));
                else if (key.equals(r.getString(R.string.aimp_restart_on_longpress)))
                    AIMPHooks.setRestartOnLongpause(sharedPreferences.getBoolean(key, false));
                else Timber.tag(LOG_TAG).e("No such setting: %s", key);
                break;
            case PACKAGE_GBOARD:
                if (key.equals(r.getString(R.string.gboard_hooks_enabled)))
                    GBoardHooks.setHooksEnabled(sharedPreferences.getBoolean(key, false));
                else if (key.equals(r.getString(R.string.gboard_use_custom_round_corner)))
                    GBoardHooks.setUseCustomRoundCorner(sharedPreferences.getBoolean(key, false));
                else if (key.equals(r.getString(R.string.gboard_custom_round_corner_dip)))
                    GBoardHooks.setCustomRoundCornerDip(sharedPreferences.getFloat(key, GBoardHooks.ROUND_CORNER_DIP));
                else Timber.tag(LOG_TAG).e("No such setting: %s", key);
                break;
            case PACKAGE_FLASHFIRE:
                if (key.equals(r.getString(R.string.flashfire_hooks_enabled)))
                    FlashFireHooks.setEnableHooks(sharedPreferences.getBoolean(key, false));
                else if (key.equals(r.getString(R.string.flashfire_hook_proreal)))
                    FlashFireHooks.setHookProReal(sharedPreferences.getBoolean(key, false));
                else Timber.tag(LOG_TAG).e("No such setting: %s", key);
                break;

            default:
                Timber.tag(LOG_TAG).wtf("No such app: %s", pkg.getPackageName());
                break;
        }
    }
}
