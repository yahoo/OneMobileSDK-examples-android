package com.aol.mobile.sdk.testapp.aol.middleware;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aol.mobile.sdk.controls.ContentControls;
import com.aol.mobile.sdk.player.Middleware;
import com.aol.mobile.sdk.player.model.properties.Properties;
import com.aol.mobile.sdk.player.view.PlayerViewport;
import com.aol.mobile.sdk.renderer.viewmodel.VideoVM;

public class VideoBufferingRestriction implements Middleware {
    private boolean isBufferingRestricted = true;
    private Boolean isActive;
    private Boolean wasRestricted;
    @NonNull
    private ExtraAction extraAction = ExtraAction.SAVE_POSITION;
    @Nullable
    private Long contentPos;
    @Nullable
    private Long adPos;
    @Nullable
    private ControlsBehavior controlsBehavior;

    @Override
    public void process(@NonNull Properties props, @NonNull ContentControls.ViewModel controlsVM,
                        @NonNull PlayerViewport.ViewModel viewportVM, @NonNull VideoVM videoVM,
                        @NonNull VideoVM adVM) {
        switch (extraAction) {
            case SAVE_POSITION:
                contentPos = videoVM.seekPosition != null ? videoVM.seekPosition : videoVM.currentPosition;
                adPos = adVM.currentPosition;
                extraAction = ExtraAction.IDLE;
                break;

            case RESTORE_POSITION:
                if (props.viewport.isAttached) {
                    if (contentPos != null) videoVM.seekPosition = contentPos;
                    if (adPos != null) adVM.seekPosition = adPos;
                    extraAction = ExtraAction.IDLE;
                }
                break;
        }

        if (isBufferingRestricted) {
            videoVM.videoUrl = null;
            adVM.videoUrl = null;
            viewportVM.isAdControlsVisible = false;
            viewportVM.isAdVisible = false;
            viewportVM.isContentControlsVisible = true;
            viewportVM.isContentVisible = true;
        }
    }

    public void setBufferingRestricted(boolean bufferingRestricted) {
        if (isBufferingRestricted != bufferingRestricted) {
            isBufferingRestricted = bufferingRestricted;

            extraAction = isBufferingRestricted ? ExtraAction.SAVE_POSITION : ExtraAction.RESTORE_POSITION;

            if (controlsBehavior != null) {
                controlsBehavior.setIsActive(!isBufferingRestricted);
            }
        }
    }

    public void setActive(boolean active) {
        if (isActive == null || isActive != active) {
            isActive = active;

            if (isActive) {
                if (wasRestricted != null)
                    setBufferingRestricted(wasRestricted);
            } else {
                wasRestricted = isBufferingRestricted;
                setBufferingRestricted(true);
            }
        }
    }

    public void setControlsBehaviour(@NonNull ControlsBehavior controlsBehavior) {
        this.controlsBehavior = controlsBehavior;
        controlsBehavior.setIsActive(!isBufferingRestricted);
    }

    private enum ExtraAction {IDLE, SAVE_POSITION, RESTORE_POSITION}
}
