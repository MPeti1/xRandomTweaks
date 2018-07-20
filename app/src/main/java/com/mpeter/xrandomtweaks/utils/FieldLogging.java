package com.mpeter.xrandomtweaks.utils;

import android.app.AndroidAppHelper;
import android.content.Context;

import java.lang.reflect.Field;
import java.util.ArrayList;

import de.robv.android.xposed.XC_MethodHook;

public class FieldLogging {
    public static String logInstanceFields(Class _class, XC_MethodHook.MethodHookParam param, boolean onlyDeclared){
        Field[] instanceFields;
        if (onlyDeclared)
            instanceFields = _class.getDeclaredFields();
        else instanceFields = _class.getFields();

        String log = _class.getSimpleName() + "'s fields:\n";
        int maxFieldNameLength = 0;
        int maxFieldValueLength = 0;

        ArrayList<String> fieldNames = new ArrayList<>();
        ArrayList<String> fieldValues = new ArrayList<>();

        String fieldName = "";
        String fieldValue = "";

        for (int i = 0; i < instanceFields.length; i++) {
            fieldName = instanceFields[i].getClass().getSimpleName();
            fieldName += ", ";
            fieldName += instanceFields[i].getName();

            fieldNames.add(i, fieldName);

            instanceFields[i].setAccessible(true);
            try {
                fieldValue = (String) instanceFields[i].get(param.thisObject);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                ToastUtils.makeToast(AndroidAppHelper.currentApplication().getApplicationContext(), e.getMessage(), 7000);
            }

            fieldValues.add(i, fieldValue);
        }

        String output = "";

        for (int i = 0; i < fieldNames.size(); i++) {
            output += fieldNames.get(i);
            output += ": ";
            output += fieldValues.get(i);
            output += "\n";
        }

        return null;
    }

    @Deprecated
    public static String logArgs(XC_MethodHook.MethodHookParam param, String componentName){
        Context applicationContext = AndroidAppHelper.currentApplication().getApplicationContext();
        StringBuilder log = new StringBuilder("com.mpeter.xrandomtweaks: " + componentName + " called: ");

        if (param.args != null){
            log.append(param.args[0].getClass().getSimpleName());

            for (int i = 1; i < param.args.length; i++) {
                log.append(", ");
                if (param.args != null)
                    log.append(param.args[i].getClass().getSimpleName());
                else log.append("null");
            }

            log.append("\n\n");

            for (int i = 0; i < param.args.length; i++) {
                log.append(i).append(". ");
                if (param.args != null){
                    log.append(param.args[i].getClass().getSimpleName()).append(": ");
                    log.append(param.args[i].toString()).append("\n");
                } else {
                    log.append("null");
                }
            }
        }
        else log.append("null");

        return log.toString();
    }
}
