package com.aol.mobile.sdk.testapp;


import com.aol.mobile.sdk.controls.utils.ImageUtils;

public final class ExampleApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ImageUtils.warmUpCache(this);
    }
}
