package com.csabacsete.imgursmostviral.util;

import com.google.android.gms.analytics.HitBuilders;

import java.util.Map;

/**
 * Created by ccsete on 5/3/16.
 */
public class AnalyticsUtil {

    public static Map<String, String> getEvent(String category, String action) {
        return new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .build();
    }
}
