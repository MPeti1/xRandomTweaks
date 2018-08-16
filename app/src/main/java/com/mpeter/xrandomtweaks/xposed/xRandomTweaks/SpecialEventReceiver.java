package com.mpeter.xrandomtweaks.xposed.xRandomTweaks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mpeter.xrandomtweaks.xposed.XposedModule;

import timber.log.Timber;

public class SpecialEventReceiver extends BroadcastReceiver {
    public static final String LOG_TAG = XposedModule.getLogtag(SpecialEventReceiver.class);
    public static final String ACTION_EXIT_APP = "com.mpeter.xrandomtweaks.ACTION_EXIT_APP";

    public SpecialEventReceiver() {
        super();

        Timber.tag(LOG_TAG).d("initialized");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == null || intent.getAction().isEmpty()){
            Timber.tag(LOG_TAG).e("received broadcast without action");
        }

        Timber.tag(LOG_TAG).d("received broadcast with action %s", intent.getAction());

        switch (intent.getAction()){
            case ACTION_EXIT_APP:
                System.exit(0);
                break;
        }
    }
}