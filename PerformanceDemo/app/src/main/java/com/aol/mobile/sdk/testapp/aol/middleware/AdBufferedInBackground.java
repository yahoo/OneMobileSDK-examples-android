package com.aol.mobile.sdk.testapp.aol.middleware;

import android.support.annotation.NonNull;

import com.aol.mobile.sdk.controls.ContentControls;
import com.aol.mobile.sdk.player.Middleware;
import com.aol.mobile.sdk.player.model.properties.Properties;
import com.aol.mobile.sdk.player.view.PlayerViewport;
import com.aol.mobile.sdk.renderer.viewmodel.VideoVM;

import static com.aol.mobile.sdk.player.model.properties.Properties.ViewState.Ad;

public class AdBufferedInBackground implements Middleware {
    @Override
    public void process(@NonNull Properties props, @NonNull ContentControls.ViewModel controlsVM,
                        @NonNull PlayerViewport.ViewModel viewportVM, @NonNull VideoVM videoVM,
                        @NonNull VideoVM adVM) {

        boolean isAdActualPlaying = props.ad.time != null;
        boolean preventContentBuffering = props.viewState == Ad && !isAdActualPlaying;

        if (preventContentBuffering) {
            viewportVM.isAdControlsVisible = false;
            viewportVM.isAdVisible = false;
            viewportVM.isContentControlsVisible = true;
            viewportVM.isContentVisible = true;
            controlsVM.isThumbnailImageVisible = true;
        }
    }
}
