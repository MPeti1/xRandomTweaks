package com.mpeter.xrandomtweaks.xposed;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.XModuleResources;

import com.mpeter.xrandomtweaks.R;
import com.mpeter.xrandomtweaks.xposed.AIMP.AIMPHooks;
import com.mpeter.xrandomtweaks.xposed.EggInc.EggIncHooks;
import com.mpeter.xrandomtweaks.xposed.GBoard.GBoardHooks;
import com.mpeter.xrandomtweaks.xposed.Messenger.MessengerHooks;
import com.mpeter.xrandomtweaks.xposed.MiuiHome.MiuiHomeHooks;

import java.util.Iterator;
import java.util.Map;

import timber.log.Timber;

public class ModuleSettings implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static boolean preloadDisabledHooks;

    public static final String LOG_TAG = XposedModule.getLogtag(ModuleSettings.class);

    public ModuleSettings() {
        SharedPreferences sharedPrefs = XposedModule.getXSettings();
        XModuleResources r = XposedModule.getResources();

        setPreloadDisabledHooks(sharedPrefs.getBoolean(r.getString(R.string.preload_disabled_hooks_key), false));

        AIMPHooks.setHooksEnabled(sharedPrefs.getBoolean(r.getString(R.string.aimp_hooks_enabled), false));
        AIMPHooks.setReplaceAlbumArt(sharedPrefs.getBoolean(r.getString(R.string.aimp_replace_albumart), false));
        AIMPHooks.setRestartOnLongpause(sharedPrefs.getBoolean(r.getString(R.string.aimp_restart_on_longpress), false));

        EggIncHooks.setHooksEnabled(sharedPrefs.getBoolean(r.getString(R.string.egginc_hooks_enabled), false));
        EggIncHooks.setPreventMusic(sharedPrefs.getBoolean(r.getString(R.string.egginc_prevent_music), false));
        EggIncHooks.setSkipAds(sharedPrefs.getBoolean(r.getString(R.string.egginc_skip_ads), false));

        GBoardHooks.setHooksEnabled(sharedPrefs.getBoolean(r.getString(R.string.gboard_hooks_enabled), false));
        GBoardHooks.setUseCustomRoundCorner(sharedPrefs.getBoolean(r.getString(R.string.gboard_use_custom_round_corner), false));
        GBoardHooks.setCustomRoundCornerDip(sharedPrefs.getFloat(r.getString(R.string.gboard_custom_round_corner_dip), GBoardHooks.ROUND_CORNER_DIP));

        MessengerHooks.setHooksEnabled(sharedPrefs.getBoolean(r.getString(R.string.messenger_hooks_enabled), false));
        MessengerHooks.setCheatInSoccer(sharedPrefs.getBoolean(r.getString(R.string.messenger_cheat_soccer), false));
        MessengerHooks.setSoccerScoreToCheat(Integer.valueOf(sharedPrefs.getString(r.getString(R.string.messenger_soccer_score), String.valueOf(0))));

        MiuiHomeHooks.setHooksEnabled(sharedPrefs.getBoolean(r.getString(R.string.miuihome_hooks_enabled), false));
        MiuiHomeHooks.setFixHeightGap(sharedPrefs.getBoolean(r.getString(R.string.miuihome_fix_heightgap), false));

        Timber.tag(LOG_TAG).i("miuihome hooks enabled %s, miuihome heightgapfix enabled %s",
                sharedPrefs.getBoolean(r.getString(R.string.miuihome_hooks_enabled), false),
                sharedPrefs.getBoolean(r.getString(R.string.miuihome_fix_heightgap), false));

        sharedPrefs.registerOnSharedPreferenceChangeListener(this);

        StringBuilder log = new StringBuilder();
        Map<String, ?> prefs = sharedPrefs.getAll();
        Iterator<? extends Map.Entry<String, ?>> entries = prefs.entrySet().iterator();
        while (entries.hasNext()){
            Map.Entry<String, ?> entry = entries.next();
            log.append(entry.getKey()).append(": ").append(entry.getValue());
            if (entries.hasNext()) log.append("\n");
        }
        Timber.tag(LOG_TAG).i(log.toString());
    }

    public static boolean preloadDisabledHooks() {
        return preloadDisabledHooks;
    }

    private static void setPreloadDisabledHooks(boolean preloadDisabledHooks) {
        ModuleSettings.preloadDisabledHooks = preloadDisabledHooks;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String packageName = key.substring(0, key.indexOf("_"));
        SupportedPackages.Package pkg = SupportedPackages.Package.forString(packageName);
        Resources r = XposedModule.getResources();

        assert pkg != null;
        switch (pkg){
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

            default:
                Timber.tag(LOG_TAG).e("No such app: %s", pkg.getPackageName());
                break;
        }
    }
}
