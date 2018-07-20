package com.mpeter.xrandomtweaks.utils;

import org.threeten.bp.Duration;

public class TimeUtils {
    public static String getDurationString(String componentName, Duration duration){
        String nano = String.valueOf(duration.getNano());
        nano = nano.substring(0, nano.length() < 2 ? nano.length() : 2);

        String pre = componentName + "took ";
        String hours = "";
        String minutes = "";
        String seconds = "";


        if (duration.toHours() == 0){
            if (duration.toMinutes() == 0){
                seconds = duration.getSeconds() + "." + nano + "seconds";
            } else minutes = duration.toMinutes() + " minutes and ";
        } else hours = duration.toHours() + ", ";

        return pre + hours + minutes + seconds;
    }
}
