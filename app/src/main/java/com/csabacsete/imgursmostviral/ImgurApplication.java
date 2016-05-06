package com.csabacsete.imgursmostviral;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by ccsete on 5/3/16.
 */
public class ImgurApplication extends Application {

    private Tracker tracker;

    private static Context context;

    public static Context getImgurAppContext() {
        return context;
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (tracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            tracker = analytics.newTracker(R.xml.global_tracker);
        }
        return tracker;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        Stetho.initializeWithDefaults(this);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader.getInstance().init(config);
    }
}
