package com.csabacsete.imgursmostviral;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by ccsete on 5/3/16.
 */
public class ImgurApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);
    }
}
