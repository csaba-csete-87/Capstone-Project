package com.csabacsete.imgursmostviral.util;

import android.text.format.DateUtils;

/**
 * Created by ccsete on 4/24/16.
 */
public class DateTimeUtils {

    private DateTimeUtils() {
    }

    public static String getReadableTimeElapsed(long dateTime) {
        return DateUtils.getRelativeTimeSpanString(dateTime * 1000).toString();
    }

    public static String getReadableTimeElapsedShort(long datetime) {
        String readableShort = getReadableTimeElapsed(datetime);
        readableShort = readableShort.replace("days ago", "d");
        readableShort = readableShort.replace("day ago", "d");
        readableShort = readableShort.replace("hours ago", "h");
        readableShort = readableShort.replace("hour ago", "h");
        readableShort = readableShort.replace("minutes ago", "m");
        readableShort = readableShort.replace("minute ago", "m");
        readableShort = readableShort.replace("0 m", "just now");
        return readableShort;
    }
}
