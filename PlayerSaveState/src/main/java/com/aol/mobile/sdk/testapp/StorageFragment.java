package com.aol.mobile.sdk.testapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.aol.mobile.sdk.player.Binder;

public class StorageFragment extends Fragment {
    private Binder binder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public Binder getBinder() {
        return binder;
    }

    public void setBinder(Binder binder) {
        this.binder = binder;
    }
}
