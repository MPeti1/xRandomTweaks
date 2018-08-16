package com.mpeter.xrandomtweaks.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.preference.PreferenceManager;

import com.mpeter.xrandomtweaks.R;
import com.mpeter.xrandomtweaks.xposed.XposedModule;

import timber.log.Timber;

/**
 * Created by masap on 2018. 03. 05..
 */

public class ResourceUtils {
    public static final String LOG_TAG = XposedModule.getLogtag(ResourceUtils.class);

    public static ColorStateList getColorStatelist(int themeColor){
        // FOR NAVIGATION VIEW ITEM TEXT COLOR
        int[][] state = new int[][] {
                new int[] {-android.R.attr.state_enabled}, // disabled
                new int[] {android.R.attr.state_enabled},  // enabled
                new int[] {-android.R.attr.state_checked}, // unchecked
                new int[] { android.R.attr.state_pressed}  // pressed

        };

        int[] color = new int[] {
                themeColor,
                themeColor,
                themeColor,
                themeColor
        };

        return new ColorStateList(state, color);
    }

    public static TypedArray getAttrs(Context context, int... ids){
        return context.obtainStyledAttributes(ids);
    }

    public static int[] getAttrResID(Context context, int... ids){
        int[] resultsids = new int[ids.length];
        for (int i = 0; i < resultsids.length; i++) {
            resultsids[i] = getAttrs(context, ids).getResourceId(i, 0);
        }
        return resultsids;
    }

    public static int getAttrResID(Context context, int id){
        return getAttrs(context, id).getResourceId(0, 0);
    }

    public static void setupTheme(Context context){
        Resources r = context.getResources();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String themeName = sharedPrefs.getString(r.getString(R.string.app_theme_key), r.getString(R.string.app_theme_light));

        if (themeName.equals(r.getString(R.string.app_theme_light)))
            context.setTheme(R.style.AppTheme);
        else if (themeName.equals(r.getString(R.string.app_theme_dark)))
            context.setTheme(R.style.AppThemeDark);
        else Timber.tag(LOG_TAG).wtf("invalid theme name: %s", themeName);
    }
}
