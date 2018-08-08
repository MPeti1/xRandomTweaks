package com.mpeter.xrandomtweaks.ui;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;

import com.mpeter.xrandomtweaks.App;
import com.mpeter.xrandomtweaks.BuildConfig;
import com.mpeter.xrandomtweaks.IntNumberEditTextPreference;
import com.mpeter.xrandomtweaks.LongNumberEditTextPreference;
import com.mpeter.xrandomtweaks.R;
import com.mpeter.xrandomtweaks.xposed.SupportedPackages;
import com.takisoft.fix.support.v7.preference.PreferenceCategory;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

public class HookPreferenceFragment extends PreferenceFragmentCompat {
    public static final String EXTRA_PACKAGE_NAME = "package_name";

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        Bundle bundle = getArguments();
        if (bundle == null)
            throw new IllegalArgumentException("preference fragment cannot be opened without arguments");

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

        switch (pkg) {
            case PACKAGE_SELF:
                break;
            case PACKAGE_MIUI_HOME:
                SwitchPreferenceCompat layoutHeightFix = new SwitchPreferenceCompat(contextThemeWrapper);
                layoutHeightFix.setTitle(getString(R.string.xsettings_com_miui_home_fix_layout_height_title));
                layoutHeightFix.setSummary(getString(R.string.xsettings_com_miui_home_fix_layout_height_sumary));
                layoutHeightFix.setKey(getString(R.string.miuihome_fix_heightgap));
                layoutHeightFix.setDefaultValue(r.getBoolean(R.bool.miuihome_fix_heightgap_def));

                preferenceCategory.addPreference(layoutHeightFix);
                preferenceCategory.setTitle(getString(R.string.xsettings_com_miui_home_title));

                break;
            case PACKAGE_FB_MESSENGER:
                SwitchPreferenceCompat cheatSoccer = new SwitchPreferenceCompat(contextThemeWrapper);
                cheatSoccer.setTitle(getString(R.string.xsettings_com_facebook_orca_cheat_soccer_score_title));
                cheatSoccer.setKey(getString(R.string.messenger_cheat_soccer));
                cheatSoccer.setDefaultValue(r.getBoolean(R.bool.messenger_cheat_soccer_def));

                IntNumberEditTextPreference cheatedScore = new IntNumberEditTextPreference(contextThemeWrapper);
                cheatedScore.setTitle(getString(R.string.xsettings_com_facebook_orca_custom_score_title));
                cheatedScore.setKey(getString(R.string.messenger_soccer_score));
                cheatedScore.setDefaultValue(String.valueOf(r.getInteger(R.integer.messenger_soccer_score_def)));

                preferenceCategory.addPreference(cheatSoccer);
                preferenceCategory.addPreference(cheatedScore);
                preferenceCategory.setTitle(getString(R.string.xsettings_com_facebook_orca_title));
                break;
            case PACKAGE_EGGINC:
                SwitchPreferenceCompat preventMusic = new SwitchPreferenceCompat(contextThemeWrapper);
                preventMusic.setTitle(getString(R.string.xsettings_com_auxbrain_egginc_prevent_music_title));
                preventMusic.setSummary(getString(R.string.xsettings_com_auxbrain_egginc_prevent_music_summary));
                preventMusic.setKey(getString(R.string.egginc_prevent_music));
                preventMusic.setDefaultValue(r.getBoolean(R.bool.egginc_prevent_music_def));

                preferenceCategory.addPreference(preventMusic);
                preferenceCategory.setTitle(getString(R.string.xsettings_com_auxbrain_egginc_title));

                if (BuildConfig.DEBUG) {
                    SwitchPreferenceCompat skipAds = new SwitchPreferenceCompat(contextThemeWrapper);
                    skipAds.setTitle(getString(R.string.xsettings_com_auxbrain_egginc_skip_ads_title));
                    skipAds.setKey(getString(R.string.egginc_skip_ads));
                    skipAds.setDefaultValue(r.getBoolean(R.bool.egginc_skip_ads_def));

                    preferenceCategory.addPreference(skipAds);
                }
                break;
            case PACKAGE_AIMP:
                SwitchPreferenceCompat replaceAlbumart = new SwitchPreferenceCompat(contextThemeWrapper);
                replaceAlbumart.setTitle(getString(R.string.xsettings_com_aimp_player_replace_albumart_title));
                replaceAlbumart.setKey(getString(R.string.aimp_replace_albumart));
                replaceAlbumart.setSummary(getString(R.string.xsettings_com_aimp_player_replace_albumart_summary));
                replaceAlbumart.setDefaultValue(r.getBoolean(R.bool.aimp_replace_albumart_def));

                SwitchPreferenceCompat restartOnLongpress = new SwitchPreferenceCompat(contextThemeWrapper);
                restartOnLongpress.setTitle(getString(R.string.xsettings_com_aimp_player_restart_on_longpress_title));
                restartOnLongpress.setSummary(getString(R.string.xsettings_com_aimp_player_restart_on_longpress_summary));
                restartOnLongpress.setKey(getString(R.string.aimp_restart_on_longpress));
                restartOnLongpress.setDefaultValue(r.getBoolean(R.bool.aimp_restart_on_longpress_def));

                preferenceCategory.addPreference(replaceAlbumart);
                preferenceCategory.addPreference(restartOnLongpress);
                preferenceCategory.setTitle(getString(R.string.xsettings_com_aimp_player_title));
                break;
            case PACKAGE_GBOARD:
                SwitchPreferenceCompat oldCornerRounds = new SwitchPreferenceCompat(contextThemeWrapper);
                oldCornerRounds.setTitle(getString(R.string.xsettings_com_google_android_inputmethod_latin_old_corner_rounds_title));
                oldCornerRounds.setSummary(getString(R.string.xsettings_com_google_android_inputmethod_latin_old_corner_rounds_summary));
                oldCornerRounds.setKey(r.getString(R.string.gboard_use_custom_round_corner));
                oldCornerRounds.setDefaultValue(r.getBoolean(R.bool.gboard_use_custom_round_corner_def));

                /*EditTextPreference cornerRoundMetric = new EditTextPreference(contextThemeWrapper); //TODO: csak számok
                cornerRoundMetric.setTitle(getString(R.string.xsettings_com_google_android_inputmethod_latin_corner_round_metric_title));
                cornerRoundMetric.setSummary(getString(R.string.xsettings_com_google_android_inputmethod_latin_corner_round_metric_summary));
                cornerRoundMetric.setKey(getString(R.string.gboard_custom_round_corner_dip));
                cornerRoundMetric.setDefaultValue(String.valueOf(r.getInteger(R.integer.gboard_custom_round_corner_dip_def)));

                EditText editText = cornerRoundMetric.getEditText();
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                editText.setFilters(new InputFilter[]{(charSequence, i, i1, spanned, i2, i3) -> {
                    float cornerRoundSize = Float.parseFloat(charSequence.toString());

                    if (cornerRoundSize < 0) {
                        Toast.makeText(getContext(), "Invalid number. ", Toast.LENGTH_SHORT).show();
                        return null;
                    }
                    charSequence = charSequence.subSequence(1, charSequence.length());
                    return charSequence;
                }});*/

                LongNumberEditTextPreference preference = new LongNumberEditTextPreference(contextThemeWrapper);
                preference.setTitle(getString(R.string.xsettings_com_google_android_inputmethod_latin_corner_round_metric_title));
                preference.setSummary(getString(R.string.xsettings_com_google_android_inputmethod_latin_corner_round_metric_summary));
                preference.setKey(getString(R.string.gboard_custom_round_corner_dip));
                preference.setDefaultValue(String.valueOf(r.getInteger(R.integer.gboard_custom_round_corner_dip_def)));

                preferenceCategory.addPreference(oldCornerRounds);
//                preferenceCategory.addPreference(cornerRoundMetric);
                preferenceCategory.addPreference(preference);
                preferenceCategory.setTitle(getString(R.string.xsettings_com_google_android_inputmethod_latin_title));
                break;
        }
    }
}
