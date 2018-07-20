package com.mpeter.xrandomtweaks.utils;

import android.util.Log;

import com.mpeter.xrandomtweaks.xposed.XposedModule;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

public class SteppedHookLog {
    //Hooks all declared methods of a class, printing a stepped
    //view of how they are called

    private static String LOG_TAG = XposedModule.getLogtag(SteppedHookLog.class);

    private Class[] mClass;

    private static String logMethod(Method method, Object[] args, String indent) {
//        Log.i(LOGTAG, indent + method.toString());
//        CodeUtils.printArguments(indent, args);

        String log = "";
        log += indent + method.toString();

        if (args != null && args.length != 0){
            log += "   (";

            for (Object param : args) {
                log += param.getClass() + ", " + param.toString();
                log += ", ";
            }
            log = log.substring(0, log.length() - 2);

            log += ")";
        }

        return log;
    }

    public SteppedHookLog(Class... clazz) {
        mClass = clazz;
    }


    private int mDepth = 0;

    private static String mIndent = "|    ";

    private static String getIndent(int depth) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < depth; i++) {
            builder.append(mIndent);
        }

        return builder.toString();

    }

    public void hookOne(Class clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        String log = "";

        for (Method method : methods) {
            try {
                XposedBridge.hookMethod(method, new XC_MethodHook() {

                    private boolean wasFirst = false;

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        if (mDepth == 0) {
                            wasFirst = true;
                            Log.i(LOG_TAG, "############# ENTERED #############");
                        }

                        Log.i(LOG_TAG, logMethod((Method) param.method, param.args, getIndent(mDepth)));

                        mDepth++;
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                        mDepth--;

                        if (mDepth == 0 || wasFirst) {
                            wasFirst = false;
                            mDepth = 0;
                            Log.i(LOG_TAG, "############# EXITED #############");
                        }
                    }
                });
            } catch (Exception ex) {

            }


        }
    }

    public void hookAll() {
        for (Class clazz : mClass) {
            hookOne(clazz);
        }

    }


}
