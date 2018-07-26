package com.mpeter.xrandomtweaks.xposed;

import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.XModuleResources;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.mpeter.xrandomtweaks.BuildConfig;
import com.mpeter.xrandomtweaks.R;
import com.mpeter.xrandomtweaks.xposed.AIMP.AIMPHooks;
import com.mpeter.xrandomtweaks.xposed.EggInc.EggIncHooks;
import com.mpeter.xrandomtweaks.xposed.GBoard.GBoardHooks;
import com.mpeter.xrandomtweaks.xposed.Messenger.MessengerHooks;
import com.mpeter.xrandomtweaks.xposed.MiuiHome.MiuiHomeHooks;

import java.util.Objects;

import de.robv.android.xposed.XSharedPreferences;
import timber.log.Timber;

public class ModuleSettings {
    private static boolean preloadDisabledHooks;

    public static final String LOG_TAG = XposedModule.getLogtag(ModuleSettings.class);

    public ModuleSettings() {
        XSharedPreferences sharedPrefs = XposedModule.getEnabledPackages();
        XModuleResources r = XposedModule.getResources();

        preloadDisabledHooks = sharedPrefs.getBoolean(r.getString(R.string.preload_disabled_hooks_key), false);

        AIMPHooks.setHooksEnabled(sharedPrefs.getBoolean(r.getString(R.string.aimp_hooks_enabled), true));
        AIMPHooks.setReplaceAlbumArt(sharedPrefs.getBoolean(r.getString(R.string.aimp_replace_albumart), true));
        AIMPHooks.setRestartOnLongpause(sharedPrefs.getBoolean(r.getString(R.string.aimp_restart_on_longpress), true));

        EggIncHooks.setHooksEnabled(sharedPrefs.getBoolean(r.getString(R.string.egginc_hooks_enabled), true));
        EggIncHooks.setPreventMusic(sharedPrefs.getBoolean(r.getString(R.string.egginc_prevent_music), true));
        EggIncHooks.setSkipAds(sharedPrefs.getBoolean(r.getString(R.string.egginc_skip_ads), false));

        GBoardHooks.setHooksEnabled(sharedPrefs.getBoolean(r.getString(R.string.gboard_hooks_enabled), true));
        GBoardHooks.setUseCustomRoundCorner(sharedPrefs.getBoolean(r.getString(R.string.gboard_use_custom_round_corner), true));
        GBoardHooks.setCustomRoundCornerDip(sharedPrefs.getFloat(r.getString(R.string.gboard_custom_round_corner_dip), 3f));

        MessengerHooks.setHooksEnabled(sharedPrefs.getBoolean(r.getString(R.string.messenger_hooks_enabled), true));
        MessengerHooks.setCheatInSoccer(sharedPrefs.getBoolean(r.getString(R.string.messenger_cheat_soccer), false));
        MessengerHooks.setSoccerScoreToCheat(sharedPrefs.getInt(r.getString(R.string.messenger_soccer_score), 0));

        MiuiHomeHooks.setHooksEnabled(sharedPrefs.getBoolean(r.getString(R.string.miuihome_hooks_enabled), true));
        MiuiHomeHooks.setFixHeightGap(sharedPrefs.getBoolean(r.getString(R.string.miuihome_fix_heightgap), true));

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addCategory(SettingsChangedReceiver.CATEGORY_SETTINGS_CHANGED);

        CurrentApp.getApplicationContext().observeForever(new Observer<Context>() {
            @Override
            public void onChanged(@Nullable Context context) {
                context.registerReceiver(new SettingsChangedReceiver(), intentFilter);
                CurrentApp.getApplicationContext().removeObserver(this);
            }
        });
    }

    public static boolean preloadDisabledHooks() {
        return preloadDisabledHooks;
    }

    private static void setPreloadDisabledHooks(boolean preloadDisabledHooks) {
        ModuleSettings.preloadDisabledHooks = preloadDisabledHooks;
    }

    //TODO: lecserélni erre https://github.com/hamsterksu/MultiprocessPreferences
    class SettingsChangedReceiver extends BroadcastReceiver{
        public static final String EXTRA_PENDINGINTENT = BuildConfig.APPLICATION_ID + ".extra-pendingintent";
        public static final String CATEGORY_SETTINGS_CHANGED = BuildConfig.APPLICATION_ID + ".category-settings-changed";
        public static final String ACTION_GENERAL_CHANGED = BuildConfig.APPLICATION_ID + ".action-general-changed";
        public static final String ACTION_AIMP_CHANGED = BuildConfig.APPLICATION_ID + ".action-aimp-changed";
        public static final String ACTION_EGGINC_CHANGED = BuildConfig.APPLICATION_ID + ".action-egginc-changed";
        public static final String ACTION_GBOARD_CHANGED = BuildConfig.APPLICATION_ID + ".action-gboard-changed";
        public static final String ACTION_MESSENGER_CHANGED = BuildConfig.APPLICATION_ID + ".action-messenger-changed";
        public static final String ACTION_MIUIHOME_CHANGED = BuildConfig.APPLICATION_ID + ".action-miuihome-changed";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(EXTRA_PENDINGINTENT)){
                PendingIntent pendingIntent = intent.getParcelableExtra(EXTRA_PENDINGINTENT);
                if (!Objects.equals(pendingIntent.getCreatorPackage(), BuildConfig.APPLICATION_ID))
                    Timber.tag(LOG_TAG).e("An other app tried to modify the behaviour of the module! The app's package is %s", pendingIntent.getCreatorPackage());
            } else Timber.tag(LOG_TAG).e("An other app tried to modify the behaviour of the module! The app has no PendingIntent included, so it's identity is unknown");

            if (intent.getAction() == null || intent.getAction().isEmpty()){
                Timber.tag(LOG_TAG).e("Intent action is null or empty.");
                return;
            } else if (intent.getExtras() == null){
                Timber.tag(LOG_TAG).e("Intent extras not found");
                return;
            }

            XModuleResources r = XposedModule.getResources();
            Bundle e = intent.getExtras();
            String key;

            switch (intent.getAction()){
                case ACTION_GENERAL_CHANGED:
                    key = r.getString(R.string.preload_disabled_hooks_key);
                    if (e.containsKey(key))
                        setPreloadDisabledHooks(e.getBoolean(key));
                    break;

                case ACTION_AIMP_CHANGED:
                    key = r.getString(R.string.aimp_hooks_enabled);
                    if (e.containsKey(key))
                        AIMPHooks.setHooksEnabled(e.getBoolean(key));

                    key = r.getString(R.string.aimp_replace_albumart);
                    if (e.containsKey(key))
                        AIMPHooks.setReplaceAlbumArt(e.getBoolean(key));

                    key = r.getString(R.string.aimp_restart_on_longpress);
                    if (e.containsKey(key))
                        AIMPHooks.setRestartOnLongpause(e.getBoolean(key));
                    break;

                case ACTION_EGGINC_CHANGED:
                    key = r.getString(R.string.egginc_hooks_enabled);
                    if (e.containsKey(key))
                        EggIncHooks.setHooksEnabled(e.getBoolean(key));
                    key = r.getString(R.string.egginc_prevent_music);
                    if (e.containsKey(key))
                        EggIncHooks.setPreventMusic(e.getBoolean(key));
                    key = r.getString(R.string.egginc_skip_ads);
                    if (e.containsKey(key))
                        EggIncHooks.setSkipAds(e.getBoolean(key));
                    break;

                case ACTION_GBOARD_CHANGED:
                    key = r.getString(R.string.gboard_hooks_enabled);
                    if (e.containsKey(key))
                        GBoardHooks.setHooksEnabled(e.getBoolean(key));
                    key = r.getString(R.string.gboard_use_custom_round_corner);
                    if (e.containsKey(key))
                        GBoardHooks.setUseCustomRoundCorner(e.getBoolean(key));
                    key = r.getString(R.string.gboard_custom_round_corner_dip);
                    if (e.containsKey(key))
                        GBoardHooks.setCustomRoundCornerDip(e.getFloat(key));
                    break;

                case ACTION_MESSENGER_CHANGED:
                    key = r.getString(R.string.messenger_hooks_enabled);
                    if (e.containsKey(key))
                        MessengerHooks.setHooksEnabled(e.getBoolean(key));
                    key = r.getString(R.string.messenger_cheat_soccer);
                    if (e.containsKey(key))
                        MessengerHooks.setCheatInSoccer(e.getBoolean(key));
                    key = r.getString(R.string.messenger_soccer_score);
                    if (e.containsKey(key))
                        MessengerHooks.setSoccerScoreToCheat(e.getInt(key));
                    break;

                case ACTION_MIUIHOME_CHANGED:
                    key = r.getString(R.string.miuihome_hooks_enabled);
                    if (e.containsKey(key))
                        MiuiHomeHooks.setHooksEnabled(e.getBoolean(key));
                    key = r.getString(R.string.miuihome_fix_heightgap);
                    if (e.containsKey(key))
                        MiuiHomeHooks.setFixHeightGap(e.getBoolean(key));
                    break;
            }
        }
    }
}
