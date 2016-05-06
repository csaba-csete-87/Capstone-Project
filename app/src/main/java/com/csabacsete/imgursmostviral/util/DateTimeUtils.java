package com.csabacsete.imgursmostviral.util;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateUtils;

import com.csabacsete.imgursmostviral.R;

/**
 * Created by ccsete on 4/24/16.
 */
public class DateTimeUtils {

    private DateTimeUtils() {
    }

    public static String getReadableTimeElapsed(final long dateTime) {
        return DateUtils.getRelativeTimeSpanString(dateTime * 1000).toString();
    }

    public static String getReadableTimeElapsedShort(final Context context, final long datetime) {
        String readableShort = getReadableTimeElapsed(datetime);
        readableShort = readableShort.replace(context.getString(R.string.days_ago), context.getString(R.string.days_ago_short));
        readableShort = readableShort.replace(context.getString(R.string.day_ago), context.getString(R.string.days_ago_short));
        readableShort = readableShort.replace(context.getString(R.string.hours_ago), context.getString(R.string.hours_ago_short));
        readableShort = readableShort.replace(context.getString(R.string.hour_ago), context.getString(R.string.hours_ago_short));
        readableShort = readableShort.replace(context.getString(R.string.minutes_ago), context.getString(R.string.minutes_ago_short));
        readableShort = readableShort.replace(context.getString(R.string.minute_ago), context.getString(R.string.minutes_ago_short));
        if (TextUtils.equals(readableShort, context.getString(R.string.zero_minutes_ago_short))) {
            readableShort = context.getString(R.string.just_now);
        }
        return readableShort;
    }
}
