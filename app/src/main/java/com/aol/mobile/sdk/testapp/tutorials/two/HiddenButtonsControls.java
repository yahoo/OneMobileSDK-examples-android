package com.aol.mobile.sdk.testapp.tutorials.two;

import android.content.Context;
import android.support.annotation.NonNull;

import com.aol.mobile.sdk.controls.view.PlayerControlsView;
import com.aol.mobile.sdk.controls.viewmodel.PlayerControlsVM;

public class HiddenButtonsControls extends PlayerControlsView {
    public HiddenButtonsControls(@NonNull Context context) {
        super(context);
    }

    @Override
    public void render(@NonNull PlayerControlsVM viewModel) {
        /**
         * Here you can modify any property of the default content controls you like.
         * @see PlayerControlsVM class that holds information whether an item should
         * be visible, enabled, selected or even what text should be displayed on it.
         * Keep in mind that all changes here are not per video. For example, if you
         * change video title here, the same title will be displayed for every video.
         **/

        // Hide seek forward/back 10 seconds buttons.
        viewModel.isSeekForwardButtonVisible = false;
        viewModel.isSeekBackButtonVisible = false;

        // Hide video title.
        viewModel.isTitleVisible = false;

        // Hide CC/SAP button.
        viewModel.isTrackChooserButtonVisible = false;

        super.render(viewModel);
    }
}
