package com.tomoima.rxtestproject;

import android.app.Application;

import timber.log.Timber;


public class RxTestApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());
    }
}
