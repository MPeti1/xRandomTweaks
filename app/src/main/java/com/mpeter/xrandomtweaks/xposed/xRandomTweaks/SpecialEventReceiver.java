package com.mpeter.xrandomtweaks.xposed.xRandomTweaks;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.mpeter.xrandomtweaks.xposed.EggInc.EggIncConstants;
import com.mpeter.xrandomtweaks.xposed.EggInc.EggIncHooks;
import com.mpeter.xrandomtweaks.xposed.SupportedPackages;
import com.mpeter.xrandomtweaks.xposed.XposedModule;

import java.util.Timer;
import java.util.TimerTask;

import de.robv.android.xposed.XposedHelpers;
import timber.log.Timber;

public class SpecialEventReceiver extends BroadcastReceiver {
    public static final String LOG_TAG = XposedModule.getLogtag(SpecialEventReceiver.class);
    public static final String ACTION_EXIT_APP = "com.mpeter.xrandomtweaks.ACTION_EXIT_APP";
    public static final String ACTION_EGGINC_START_AR = "com.mpeter.xrandomtweaks.ACTION_EGGINC_START_AR";
    public static final int AR_DELAY = 5000;

    public static final String[] ACTIONS_ALL = new String[]{
            ACTION_EXIT_APP,
            ACTION_EGGINC_START_AR
    };

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
            case ACTION_EGGINC_START_AR:
                if (!context.getPackageName().equals(SupportedPackages.Package.PACKAGE_EGGINC.getPackageName()))
                    return;

                ComponentName eggIncComponent = new ComponentName(
                        SupportedPackages.Package.PACKAGE_EGGINC.getPackageName(),
                        EggIncConstants.CLASS_EggIncActivity);

                Intent startEggInc = new Intent().setComponent(eggIncComponent);
                startEggInc.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                if (startEggInc.resolveActivity(context.getPackageManager()).getClassName().equals(EggIncConstants.CLASS_EggIncActivity)){
                    new Timer().schedule(new StartARMode(), AR_DELAY);
                    Toast.makeText(context.getApplicationContext(), "Starting AR mode in " + AR_DELAY + " milliseconds", Toast.LENGTH_SHORT).show();

                    context.startActivity(startEggInc);
                } else Timber.tag(LOG_TAG).e("EggIncActivity can't be resolved with ComponentName %s", eggIncComponent.toString());
                break;
        }

        Timber.tag(LOG_TAG).d("Finished action %s in %s", intent.getAction(), context.getPackageName());
    }

    class StartARMode extends TimerTask{
        @Override
        public void run() {
            Timber.tag(LOG_TAG).d("Running task StartARMode");

            XposedHelpers.callMethod(
                    EggIncHooks.getEggIncActivity().get(),
                    "requestInitAR"
            );

            Timber.tag(LOG_TAG).d("Task StartARMode half done");

            XposedHelpers.callMethod(
                    EggIncHooks.getEggIncActivity().get(),
                    "requestARSessionStart"
            );

            Timber.tag(LOG_TAG).d("Task StartARMode finished");
        }
    }
}