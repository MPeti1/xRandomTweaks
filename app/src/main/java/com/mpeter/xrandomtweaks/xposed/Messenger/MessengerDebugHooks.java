package com.mpeter.xrandomtweaks.xposed.Messenger;

import com.mpeter.xrandomtweaks.xposed.HookedApp;
import com.mpeter.xrandomtweaks.xposed.XposedModule;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MessengerDebugHooks extends MessengerConstants implements HookedApp {
    public static final String LOG_TAG = XposedModule.getLogtag(MessengerDebugHooks.class);

    @Override
    public void initHooks(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        /*XposedHelpers.findAndHookMethod(CLASS_StartScreenActivity, loadPackageParam.classLoader, METHOD_onActivityCreate, Bundle.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Bundle bundle = (Bundle) param.args[0];
                String log = LOG_TAG + "StartScreenActivity: onActivityCreate\n";

                if (bundle != null) {
                    Set<String> keySet = bundle.keySet();
                    Iterator<String> iterator = keySet.iterator();
                    log += "Logging Bundle data\n";
                    while (iterator.hasNext()){
                        log += new CodeUtils().printSingleFieldValues(0, bundle.get(iterator.next()), 6);
                        log += "\n";
                    }
                }

                XposedBridge.log(log);

                final Context applicationContext = AndroidAppHelper.currentApplication().getApplicationContext();

                new Handler(applicationContext.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.setComponent(new ComponentName("com.facebook.orca", "com.facebook.messaging.internalprefs.MessengerInternalSandboxSettingsActivity"));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        applicationContext.startActivity(intent);
                    }
                }, 4000);

                super.beforeHookedMethod(param);
            }
        });*/
    }

    @Override
    public void unHookAll() {

    }
}
