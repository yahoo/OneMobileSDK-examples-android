package com.aol.mobile.sdk.testapp.tutorials.two;

import android.content.Context;
import android.support.annotation.NonNull;

import com.aol.mobile.sdk.controls.ContentControls;
import com.aol.mobile.sdk.controls.view.ContentControlsView;

public class ModifiedCcSapControls extends ContentControlsView {
    public ModifiedCcSapControls(@NonNull Context context) {
        super(context);
    }

    @Override
    public void render(@NonNull ContentControls.ViewModel viewModel) {
        if (!viewModel.ccTracks.isEmpty()) {
            /**
             * If CC or SAP options are available for video, CC/SAP settings button becomes visible
             * @see viewModel.audioTracks collection represents SAP options
             * @see viewModel.ccTracks collection represents CC options
             * First item in these collections represents off state and by default has "None" title
             **/

            // Change "None" option to "Modified".
            viewModel.ccTracks.get(0).title = "Modified";
        }
        super.render(viewModel);
    }
}
