package com.mpeter.xrandomtweaks.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class ToastUtils {
    @SuppressLint("NewApi")
    public static Toast makeToast(final Context context, final String text, final int duration) {
        /*if (context.getMainLooper().isCurrentThread()){
            final Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
            toast.show();

            final Handler handler = new Handler(context.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast.cancel();
                }
            }, duration);

            return toast;
        }*/

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                final Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                toast.show();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, duration);
            }
        });

        return null;
    }
}
