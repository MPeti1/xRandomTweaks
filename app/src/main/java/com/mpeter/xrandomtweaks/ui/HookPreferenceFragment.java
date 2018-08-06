package com.mpeter.xrandomtweaks.ui;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;

import com.mpeter.xrandomtweaks.App;
import com.mpeter.xrandomtweaks.BuildConfig;
import com.mpeter.xrandomtweaks.R;
import com.mpeter.xrandomtweaks.xposed.SupportedPackages;

public class HookPreferenceFragment extends PreferenceFragmentCompat{
    public static final String EXTRA_PACKAGE_NAME = "package_name";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        Bundle bundle = getArguments();
        if (bundle == null) throw new IllegalArgumentException("preference fragment cannot be opened without arguments");

        final SupportedPackages.Package pkg = SupportedPackages.Package.forString(bundle.getString(EXTRA_PACKAGE_NAME));

        Context context = getActivity();

        PreferenceScreen preferenceScreen = getPreferenceManager().createPreferenceScreen(context);
        setPreferenceScreen(preferenceScreen);
        getPreferenceManager().setSharedPreferencesName(App.ENABLED_PACKAGES_PREF_FILE);

        TypedValue tValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.preferenceTheme, tValue, true);
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, tValue.resourceId);

        PreferenceCategory preferenceCategory = new PreferenceCategory(contextThemeWrapper);
        preferenceScreen.addPreference(preferenceCategory);

        Resources r = getResources();

        switch (pkg){
            case PACKAGE_SELF:
                break;
            case PACKAGE_MIUI_HOME:
                SwitchPreferenceCompat layoutHeightFix = new SwitchPreferenceCompat(contextThemeWrapper);
                layoutHeightFix.setTitle("Fix layout height");
                layoutHeightFix.setSummary("Otherwise if you set the dpi under 270, the 5. row would overflow behind the favourites bar");
                layoutHeightFix.setKey(getString(R.string.miuihome_fix_heightgap));
                layoutHeightFix.setDefaultValue(r.getBoolean(R.bool.miuihome_fix_heightgap_def));
                preferenceCategory.addPreference(layoutHeightFix);
                preferenceCategory.setTitle("MIUI Home settings");
                break;
            case PACKAGE_FB_MESSENGER:
                SwitchPreferenceCompat cheatSoccer = new SwitchPreferenceCompat(contextThemeWrapper);
                cheatSoccer.setTitle("Cheat soccer score");
                cheatSoccer.setKey(getString(R.string.messenger_cheat_soccer));
                cheatSoccer.setDefaultValue(r.getBoolean(R.bool.messenger_cheat_soccer_def));

                EditTextPreference cheatedScore = new EditTextPreference(contextThemeWrapper); //TODO: csak számok
                cheatedScore.setTitle("Custom score");
                cheatedScore.setKey(getString(R.string.messenger_soccer_score));
                cheatedScore.setDefaultValue(r.getInteger(R.integer.messenger_soccer_score_def));

                preferenceCategory.addPreference(cheatSoccer);
                preferenceCategory.addPreference(cheatedScore);
                preferenceCategory.setTitle("Messenger settings");
                break;
            case PACKAGE_EGGINC:
                SwitchPreferenceCompat preventMusic = new SwitchPreferenceCompat(contextThemeWrapper);
                preventMusic.setTitle("Prevent music from starting");
                preventMusic.setSummary("Otherwise music will start for a moment at startup even if you turned it off");
                preventMusic.setKey(getString(R.string.egginc_prevent_music));
                preventMusic.setDefaultValue(r.getBoolean(R.bool.egginc_prevent_music_def));

                preferenceCategory.addPreference(preventMusic);
                preferenceCategory.setTitle("EggInc setings");

                if (BuildConfig.DEBUG){
                    SwitchPreferenceCompat skipAds = new SwitchPreferenceCompat(contextThemeWrapper);
                    skipAds.setTitle("Skip ads immediately");
                    skipAds.setKey(getString(R.string.egginc_skip_ads));
                    skipAds.setDefaultValue(r.getBoolean(R.bool.egginc_skip_ads_def));

                    preferenceCategory.addPreference(skipAds);
                }
                break;
            case PACKAGE_AIMP:
                SwitchPreferenceCompat replaceAlbumart = new SwitchPreferenceCompat(contextThemeWrapper);
                replaceAlbumart.setTitle("Replace album art on lock screen to the good old AIMP logo");
                replaceAlbumart.setKey(getString(R.string.aimp_replace_albumart));
                replaceAlbumart.setSummary("I liked it, and maybe I'm not alone. Some versions ago this was the default (except if the song had it\'s own album art)");
                replaceAlbumart.setDefaultValue(r.getBoolean(R.bool.aimp_replace_albumart_def));

                SwitchPreferenceCompat restartOnLongpress = new SwitchPreferenceCompat(contextThemeWrapper);
                restartOnLongpress.setTitle("Restart music on long press");
                restartOnLongpress.setSummary("of play/pause button of headset. Some versions ago this was the default");
                restartOnLongpress.setKey(getString(R.string.aimp_restart_on_longpress));
                restartOnLongpress.setDefaultValue(r.getBoolean(R.bool.aimp_restart_on_longpress_def));

                preferenceCategory.addPreference(replaceAlbumart);
                preferenceCategory.addPreference(restartOnLongpress);
                preferenceCategory.setTitle("AIMP settings");
                break;
            case PACKAGE_GBOARD:
                SwitchPreferenceCompat oldCornerRounds = new SwitchPreferenceCompat(contextThemeWrapper);
                oldCornerRounds.setTitle("Change button corner rounds to be small");
                oldCornerRounds.setSummary("as in older versions, if you hate that curly ui sh*t");

                EditTextPreference cornerRoundMetric = new EditTextPreference(contextThemeWrapper); //TODO: csak számok
                cornerRoundMetric.setTitle("Change button corner rounds to this size");
                cornerRoundMetric.setSummary("It's default is 3");
                cornerRoundMetric.setKey(getString(R.string.gboard_custom_round_corner_dip));
                cornerRoundMetric.setDefaultValue(r.getInteger(R.integer.gboard_custom_round_corner_dip_def));

                preferenceCategory.addPreference(oldCornerRounds);
                preferenceCategory.addPreference(cornerRoundMetric);
                preferenceCategory.setTitle("GBoard settings");
                break;
        }
    }
}
