package com.aol.mobile.sdk.example.controlscontentcontrolsno10sec;

import android.content.Context;
import android.support.annotation.NonNull;

import com.aol.mobile.sdk.controls.view.PlayerControlsView;
import com.aol.mobile.sdk.controls.viewmodel.PlayerControlsVM;

public class ContentControlsNo10SecView extends PlayerControlsView {
    public ContentControlsNo10SecView(@NonNull Context context) {
        super(context);
    }

    @Override
    public void render(@NonNull PlayerControlsVM viewModel) {
        viewModel.isSeekForwardButtonVisible = false;
        viewModel.isSeekBackButtonVisible = false;
        super.render(viewModel);
    }
}
