package com.bogarsoft.covidcertificate;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;
import net.danlew.android.joda.JodaTimeInitializer;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }
}
