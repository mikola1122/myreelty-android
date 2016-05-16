package com.helio.myreelty;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.helio.myreelty.models.HouseSingleton;

/**
 * Created by Taras on 28.03.2016.
 */
public class MyApplication extends Application {

    public static HouseSingleton houseSingleton;
    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        houseSingleton = HouseSingleton.getInstance();
        getDefaultTracker();
    }

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }




}