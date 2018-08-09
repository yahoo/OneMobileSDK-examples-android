package com.aol.mobile.sdk.testapp;


import com.squareup.picasso.Picasso;
import com.squareup.picasso.UrlConnectionDownloader;

public final class ExampleApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Picasso picasso = new Picasso.Builder(this)
                .indicatorsEnabled(false)
                .downloader(new UrlConnectionDownloader(this))
                .build();

        Picasso.setSingletonInstance(picasso);
    }
}
