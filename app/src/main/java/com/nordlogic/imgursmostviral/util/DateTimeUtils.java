package com.nordlogic.imgursmostviral.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ccsete on 4/24/16.
 */
public class DateTimeUtils {

    private DateTimeUtils() {
    }

    public static String getReadableTimeElapsed(long dateTime) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(dateTime));
        // TODO: 4/24/16 get proper readable date
        return c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
    }

    public static String getReadableTimeElapsedSHort(long dateTime) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(dateTime));
        // TODO: 4/24/16 get proper readable date
        return c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
    }
}
