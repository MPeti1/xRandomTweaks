package com.mpeter.xrandomtweaks.utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Matrix;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;

import com.mpeter.xrandomtweaks.xposed.XposedModule;

import org.threeten.bp.Duration;
import org.threeten.bp.Instant;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("StringConcatenationInLoop")
public class CodeUtils {

    private static String LOGTAG = XposedModule.getLogtag(CodeUtils.class);

    public static void printStackTrace() {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();

        for (StackTraceElement currentElement : stack) {
            Log.i(LOGTAG, currentElement.toString());
        }

    }

    public static void printArguments(String indent, Object[] args) {
        //Log.e("###", "Arg count: "+args.length);

        StringBuilder builder = new StringBuilder();
        builder.append("-> ");

        if (args == null) {
            Log.i(LOGTAG, "NULL");
            return;
        }


        for (Object arg : args) {
            if (arg == null)
                builder.append(", \"NULL\"");
            else
                builder.append("\"" + arg.toString() + "\", ");
        }

        Log.i(LOGTAG, indent + builder.toString());

    }

    public static void printViewInfo(View view, int depth) {
        String depthIndent = "";
        for (int i = 0; i < depth; i++) {
            depthIndent = depthIndent + "|  ";
        }


        String className = view.getClass().getName();

        int viewID = view.getId();

        String viewIdName = null;
        try {
            viewIdName = view.getResources().getResourceName(viewID);
        } catch (Exception ex) {

        }

        if (viewIdName == null && viewID != View.NO_ID) {
            viewIdName = "#" + viewID;
        }


        Log.i(LOGTAG, depthIndent + className + ", ID: " + viewIdName);


    }

    public static View getTopParent(View view) {
        View finalParent = view;
        Object currentParent = (Object) view;

        while (currentParent != null) {
            finalParent = (View) currentParent;

            currentParent = (Object) ((View) currentParent).getParent();

            if (!(currentParent instanceof View))
                break;


        }

        return finalParent;


    }

    public static String getParamValues(Object[] params) {
        StringBuilder builder = new StringBuilder();

        for (Object currentParam : params) {
            if (currentParam != null) {
                try {
                    builder.append(currentParam.toString() + ", ");
                } catch (Exception ex) {
                    builder.append("EXCEPTION" + ", ");
                }


            } else {
                builder.append("NULL" + ", ");
            }


        }

        return builder.toString();


    }

    public static void traverseLayout(View rootView, int depth) {
        if (rootView != null)
            printViewInfo(rootView, depth);

        if (rootView != null && rootView instanceof ViewGroup) {
            int childCount = ((ViewGroup) rootView).getChildCount();

            for (int i = 0; i < childCount; i++) {
                traverseLayout(((ViewGroup) rootView).getChildAt(i), depth + 1);
            }
        }
    }

    public static Enum findEnumByName(Enum[] enums, String searchString) {
        for (Enum currentEnum : enums) {
            if (currentEnum.name().equals(searchString))
                return currentEnum;
        }

        //Default
        return enums[0];
    }

    public static int findViewPosition(ViewGroup root, View target) {
        int childCount = root.getChildCount();

        for (int i = 0; i < childCount; i++) {
            if (root.getChildAt(i) == target)
                return i;
        }

        return -1;
    }

    private List<View> getAllChildren(View view) {
        ArrayList<View> views = new ArrayList<>();

        if (!ViewGroup.class.isAssignableFrom(view.getClass()))
            return views;

        ViewGroup viewGroup = (ViewGroup) view;

        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            views.addAll(getAllChildren((viewGroup.getChildAt(i))));
        }

        return views;
    }

    public static boolean isPrimitive(Class clazz) {
        if (clazz == int.class)
            return true;
        if (clazz == float.class)
            return true;
        if (clazz == String.class)
            return true;
        if (clazz == double.class)
            return true;
        if (clazz == long.class)
            return true;
        if (clazz == char.class)
            return true;
        if (clazz == boolean.class)
            return true;
        if (clazz == void.class)
            return true;
        if (clazz == short.class)
            return true;
        if (clazz == byte.class)
            return true;
        if (clazz == Integer.class)
            return true;
        if (clazz == Float.class)
            return true;
        if (clazz == Double.class)
            return true;
        if (clazz == Long.class)
            return true;
        if (clazz == Boolean.class)
            return true;
        if (clazz == Void.class)
            return true;
        if (clazz == Short.class)
            return true;
        if (clazz == Byte.class)
            return true;
        if (clazz == Object.class)
            return true;

        if (clazz == MotionEvent.class)
            return true;
        if (clazz.isEnum())
            return true;
        if (clazz == Activity.class)
            return true;
        if (clazz == Fragment.class)
            return true;
        if (clazz == Context.class)
            return true;
        if (clazz == Matrix.class)
            return true;

        if (clazz == AccessibilityManager.AccessibilityStateChangeListener.class)
            return true;

       /* {
            if (clazz.getName().startsWith("android."))
                return true;
        }*/

        try {
            if (clazz == Class.forName("sun.misc.Unsafe")) return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;

    }

    static int fieldCounter = 0;
    Instant sTime;
    Instant eTime;

    public String printSingleFieldValues(int depth, Object object, int maxDepth) {
        return printSingleFieldValues(depth, object, maxDepth, true);
    }

    public String printSingleFieldValues(int depth, Object object, int maxDepth, boolean logMaxDepth) {
        String indent = "";
        String indent2 = "  ";
        for (int i = 0; i < depth; i++) {
            indent = indent + "|    ";
        }
        indent = "\n" + indent;
        indent2 += indent.replace("|", " ");

        String javaMap = "";

        if (depth > maxDepth) {
            if (logMaxDepth) return indent + "####MAX DEPTH###";
            return null;
        }


        if (object != null) {

            Class objectClass = object.getClass();
            Field[] fields = objectClass.getDeclaredFields();

            if (depth == 0) {
                sTime = Instant.now();
                javaMap = "Printing fields for Object of type " + objectClass.toString() + javaMap;
            }

            try {
//                int fieldCounter = 0;

                for (Field currentField : fields) {
                    currentField.setAccessible(true);
                    //getting field data
                    Object fieldObject = currentField.get(object);
                    Class fieldClass = null;

                    fieldCounter++;

                    //if (fieldCounter > 10)
                    //	break;

                    //if the field is not null, map it
                    if (fieldObject != null) {
                        fieldClass = fieldObject.getClass(); //getting field class
                        //getting field name
                        javaMap += indent + currentField.getName() + ", ";
                        //if primitive, print its value and go for the next field
                        if (isPrimitive(fieldClass)) {
                            if (fieldClass.getName().length() == 1)
                                javaMap += "(" + fieldClass.getName() + ")";

                            javaMap += fieldObject.toString();
                            continue;
                        }
                        /*//if array, iterate it
                        if (fieldClass.isArray()){
                            javaMap += printArrayValues(fieldObject);
                            continue;
                        }*/
                        //if not primitive, than append the full class name
                        javaMap += fieldClass.getCanonicalName() + ": ";
                        //if its a map, iterate through it
                        if (Map.class.isInstance(fieldObject)) {
                            Map map = (Map) fieldObject;
                            Set set = map.entrySet();
                            for (Map.Entry entry : (Iterable<Map.Entry>) set) {
                                javaMap += indent2 + Map.Entry.class.getSimpleName() + ": " + entry.getKey().toString();
                                javaMap += " = ";
                                if (!isPrimitive(entry.getValue().getClass())) {
                                    javaMap += entry.getValue().getClass().getCanonicalName() + ": " + printSingleFieldValues(depth + 1, entry.getValue(), maxDepth, logMaxDepth);
                                } else javaMap += entry.getValue().toString();
                            }
                        } else if (!isPrimitive(fieldClass)) { //if not primitive and not map, go deeper
//                            javaMap += currentField.get(object).toString();
                            javaMap += printSingleFieldValues(depth + 1, fieldObject, maxDepth, logMaxDepth);
                        }
                        //if null, print its null
                    } else {
                        javaMap += indent + currentField.getName() + ": " + "NULL@";
                    }
                }


            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

        if (depth == 0) {
            eTime = Instant.now();

            Duration duration = Duration.between(sTime, eTime);
            javaMap += "\n\n";
            javaMap += TimeUtils.getDurationString("printSingleFieldValues", duration);
        }

        return javaMap;
    }

    private <T> String printArrayValues(T arr) {
        String ret = "";
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            ret += printSingleFieldValues(0, Array.get(arr, i), 2, false);
        }
        return ret;
    }

    public static void printFieldValues(Class clazz, Object instance) {
        Log.i(LOGTAG, "Printing fields for: " + clazz.getName());

        try {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Log.i(LOGTAG, "Field: " + field.getName() + ", Value: " + field.get(instance));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
