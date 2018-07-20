package com.mpeter.xrandomtweaks.xposed.Messenger;

import android.app.AndroidAppHelper;
import android.content.Context;

import com.mpeter.xrandomtweaks.utils.ToastUtils;
import com.mpeter.xrandomtweaks.xposed.HookedApp;
import com.mpeter.xrandomtweaks.xposed.XposedModule;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class SoccerViewHooks extends MessengerConstants implements HookedApp {
    public static final String LOG_TAG = XposedModule.getLogtag(SoccerViewHooks.class);

    private XSharedPreferences xSharedPrefs = XposedModule.getSharedPrefs();

    @Override
    public void initHooks(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod(CLASS_SoccerView, loadPackageParam.classLoader, METHOD_setDisplayScore, CLASS_SoccerView, int.class, new XC_MethodHook() {
            Context applicationContext;

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                applicationContext = AndroidAppHelper.currentApplication().getApplicationContext();

                xSharedPrefs.reload();
                if (xSharedPrefs.getBoolean("pref_key_soccer_score_things_disable", false)) return;
                String log = LOG_TAG;
                log += "xpref olvasható? ";
                log += xSharedPrefs.getFile().length() != 0 ? "az" : "nem az";
                log += "\n\n";

                if (xSharedPrefs.getBoolean("pref_key_soccer_score_reset", false)){
                    param.args[1] = 1;
                    XposedHelpers.setIntField(param.args[0], "z", (Integer) param.args[1]);
                    if (param.args[0] == null)
                        XposedBridge.log(LOG_TAG + "a class null");
                    else XposedBridge.log(LOG_TAG + "a class " + param.args[0].toString());
                    XposedHelpers.setIntField(param.args[0], "A", (int) XposedHelpers.callMethod(param.args[0], "d"));
                    XposedHelpers.setBooleanField(param.args[0], "B", true);
                    log += "highScore resetelve";
                } else {
                    int newScore = xSharedPrefs.getInt("pref_key_soccer_score", 0);
                    if (newScore != 0) {
                        param.args[1] = newScore;
                        log += "eredmény átállítva: " + newScore;
                    } else log = "eredmény nincs átállítva, mert a kapott érték 0";
                }

                XposedBridge.log(log);
                ToastUtils.makeToast(applicationContext, log, 2000);
                super.beforeHookedMethod(param);
            }
        });

        /*XposedHelpers.findAndHookMethod(CLASS_SoccerView, loadPackageParam.classLoader, METHOD_highScoreIsNew, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(true); //lehet hogy ennek false-nek kellene inkább lennie
                XposedHelpers.setBooleanField(param.thisObject, "B", true);
                super.beforeHookedMethod(param);
            }
        });*/

        /*XposedHelpers.findAndHookMethod(CLASS_SoccerActivity, loadPackageParam.classLoader, METHOD_r$0_name, CLASS_SoccerActivity, new XC_MethodHook() {
            Context applicationContext;

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                applicationContext = AndroidAppHelper.currentApplication().getApplicationContext();

                String log = LOG_TAG;
                log += "SoccerActivity r$0 called";
                int savedHighScore = XposedHelpers.getIntField(param.args[0], FIELD_savedHighScore);
                log += "\n" + "savedHighScore: " + savedHighScore;

                ToastUtils.makeToast(applicationContext, log, 5000);
                super.beforeHookedMethod(param);
            }
        });*/

        /*XposedHelpers.findAndHookConstructor(MessengerConstants.CLASS_ThreadGameData, loadPackageParam.classLoader, String.class, int.class, new XC_MethodHook() {
            Context applicationContext;

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                applicationContext = AndroidAppHelper.currentApplication().getApplicationContext();

                String log = LOG_TAG;
                xSharedPrefs.reload();
                if (xSharedPrefs.getBoolean("pref_key_soccer_score_things_disable", false)) return;
                if (xSharedPrefs.getBoolean("pref_key_soccer_score_reset", false)){
                    param.args[1] = 1;
                    XposedHelpers.setIntField(param.args[0], "z", (Integer) param.args[1]);
                    if (param.args[0] == null)
                        XposedBridge.log(LOG_TAG + "a class null");
                    else XposedBridge.log(LOG_TAG + "a class " + param.args[0].toString());
                    XposedHelpers.setIntField(param.args[0], "A", (int) XposedHelpers.callMethod(param.args[0], "d"));
                    XposedHelpers.setBooleanField(param.args[0], "B", true);
                    log += "highScore resetelve ThreadGameData-ban";
                } else return;

                XposedBridge.log(log);
                ToastUtils.makeToast(applicationContext, log, 7000);
                super.beforeHookedMethod(param);
            }
        });*/

        /*XposedHelpers.findAndHookConstructor(CLASS_PostGameScoreParams, loadPackageParam.classLoader, CLASS_PostGameScoreParamsCache, new XC_MethodHook() {
            Context applicationContext;

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                xSharedPrefs.reload();
                if (xSharedPrefs.getBoolean("pref_key_soccer_score_things_disable", false)) return;
                if (xSharedPrefs.getBoolean("pref_key_soccer_score_reset", false)){
                    applicationContext = AndroidAppHelper.currentApplication().getApplicationContext();
                    StringBuilder log = new StringBuilder("PostGameScoreParams: by Cache\n");
                    for (int i = 0; i < param.args.length; i++) {
                        log.append(i).append(". ").append(param.args[0].getClass().getSimpleName()).append(": ").append(param.args[0].toString());
                    }

                    XposedHelpers.setIntField(param.thisObject, "e", 1);

                    XposedBridge.log(log.toString());
                    ToastUtils.makeToast(applicationContext, log.toString(), 7000);
                }
                super.afterHookedMethod(param);
            }
        });*/

        /*XposedHelpers.findAndHookConstructor(CLASS_PostGameScoreParams, loadPackageParam.classLoader, CLASS_Parcel, new XC_MethodHook() {
            Context applicationContext;

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                xSharedPrefs.reload();
                if (xSharedPrefs.getBoolean("pref_key_soccer_score_things_disable", false)) return;
                if (xSharedPrefs.getBoolean("pref_key_soccer_score_reset", false)){
                    applicationContext = AndroidAppHelper.currentApplication().getApplicationContext();
                    StringBuilder log = new StringBuilder("PostGameScoreParams: by Parcel\n");

                    XposedHelpers.setIntField(param.thisObject, "e", 1);

                    XposedBridge.log(log.toString());
                    ToastUtils.makeToast(applicationContext, log.toString(), 7000);
                }
                super.afterHookedMethod(param);
            }
        });*/

        /*XposedHelpers.findAndHookMethod(CLASS_SoccerView, loadPackageParam.classLoader, METHOD_getSaltedHighScore, new XC_MethodHook() {
            Context applicationContext;

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                applicationContext = AndroidAppHelper.currentApplication().getApplicationContext();

                String log;
                if (timeToReset && xSharedPrefs.getBoolean("pref_key_soccer_score_reset", false)){
                    param.args[1] = 0;
                    XposedHelpers.setIntField(param.thisObject, "z", 0);
                    XposedHelpers.setIntField(param.thisObject, "A", (int) XposedHelpers.callMethod(param.thisObject, "d"));
                    log = "highScore resetelve";
                }

                XposedBridge.log(LOG_TAG + SoccerViewHooks.class.getSimpleName() + log);
                ToastUtils.makeToast(applicationContext, SoccerViewHooks.class.getSimpleName() + ": " + log, 2000);

                super.beforeHookedMethod(param);
            }
        });*/
    }

    @Override
    public void unHookAll() {

    }
}
