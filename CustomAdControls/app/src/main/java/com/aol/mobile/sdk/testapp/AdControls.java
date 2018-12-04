package com.aol.mobile.sdk.testapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.aol.mobile.sdk.controls.view.AdControlsView;

public class AdControls extends AdControlsView {
    private final ProgressBar adProgress;

    public AdControls(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        adProgress = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        adProgress.setIndeterminate(false);
        addView(adProgress);
    }

    @Override
    public void render(@NonNull ViewModel vm) {
        super.render(vm);

        adProgress.setMax(vm.seekerMaxValue);
        adProgress.setProgress((int) Math.round(vm.seekerMaxValue * (1 - vm.seekerProgress)));
    }
}
