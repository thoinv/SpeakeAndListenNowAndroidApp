package vn.eway.englishpronunciationpractice;

import android.app.Application;

import com.google.android.gms.analytics.Tracker;

/**
 * Created by nguye_000 on 1/25/2016.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AnalyticsTrackers.initialize(this);
        Tracker tracker = AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);
        tracker.enableAutoActivityTracking(true);
        tracker.enableAdvertisingIdCollection(true);
    }
}
