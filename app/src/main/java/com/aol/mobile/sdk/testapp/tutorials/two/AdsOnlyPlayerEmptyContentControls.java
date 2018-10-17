package com.aol.mobile.sdk.testapp.tutorials.two;

import android.content.Context;
import android.support.annotation.NonNull;

import com.aol.mobile.sdk.controls.view.ContentControlsView;

public class AdsOnlyPlayerEmptyContentControls extends ContentControlsView {
    public AdsOnlyPlayerEmptyContentControls(@NonNull Context context) {
        super(context);
    }

    @Override
    public void render(@NonNull ViewModel viewModel) {
        viewModel.isLoading = false;
        viewModel.isPlayButtonVisible = false;
        viewModel.isPauseButtonVisible = false;
        viewModel.isReplayButtonVisible = false;
        viewModel.isNextButtonVisible = false;
        viewModel.isPrevButtonVisible = false;
        viewModel.isSeekerVisible = false;
        viewModel.isSeekForwardButtonVisible = false;
        viewModel.isSeekBackButtonVisible = false;
        viewModel.isTitleVisible = false;
        viewModel.isCompassViewVisible = false;
        viewModel.isThumbnailImageVisible = false;
        viewModel.isTrackChooserButtonVisible = false;
        viewModel.isLiveIndicatorVisible = false;
        viewModel.isCastButtonVisible = false;

        super.render(viewModel);
    }
}
