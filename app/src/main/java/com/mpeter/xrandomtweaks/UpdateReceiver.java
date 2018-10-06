package com.mpeter.xrandomtweaks;

import android.app.AndroidAppHelper;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.SystemClock;
import android.support.v7.preference.PreferenceManager;

import org.threeten.bp.Instant;
import org.threeten.bp.format.DateTimeFormatter;

import timber.log.Timber;

public class UpdateReceiver extends BroadcastReceiver {
    private static Instant lastUpdateTime = null;
    private static SharedPreferences sharedPreferences;
    private static Resources r;

    private static void init(Context context){
        if (r == null) r = context.getResources();
        if (sharedPreferences == null)
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (lastUpdateTime == null)
            lastUpdateTime = Instant.parse(sharedPreferences.getString(r.getString(R.string.last_update_time), DateTimeFormatter.ISO_INSTANT.format(Instant.EPOCH)));
    }

    private static void init(){
        init(AndroidAppHelper.currentApplication().getApplicationContext());
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        init(context);

        Timber.d("Intent received: %s", intent.getAction());

        setLastUpdateTime(Instant.now(), context);
    }

    private static void setLastUpdateTime(Instant lastUpdateTime, Context context) {
        sharedPreferences
                .edit()
                .putString(
                        context.getResources().getString(R.string.last_update_time),
                        DateTimeFormatter.ISO_INSTANT.format(lastUpdateTime)
                ).apply();

        UpdateReceiver.lastUpdateTime = lastUpdateTime;
    }

    public static Instant getLastUpdateTime(){
        init();
        return lastUpdateTime;
    }

    public static boolean isUpdatedSinceBoot(){
        init();
        Instant bootTime = Instant.now().minusMillis(SystemClock.elapsedRealtime());
        return lastUpdateTime.isAfter(bootTime);
    }
}
