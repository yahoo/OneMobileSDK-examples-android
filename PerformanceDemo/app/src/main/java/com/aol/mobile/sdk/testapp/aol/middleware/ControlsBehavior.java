package com.aol.mobile.sdk.testapp.aol.middleware;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import com.aol.mobile.sdk.controls.ContentControls;
import com.aol.mobile.sdk.player.Binder;
import com.aol.mobile.sdk.player.Middleware;
import com.aol.mobile.sdk.player.Player;
import com.aol.mobile.sdk.player.model.properties.Properties;
import com.aol.mobile.sdk.player.model.properties.VideoProperties;
import com.aol.mobile.sdk.player.view.PlayerViewport;
import com.aol.mobile.sdk.renderer.viewmodel.VideoVM;
import com.aol.mobile.sdk.testapp.aol.view.AolControlsView;
import com.aol.mobile.sdk.testapp.aol.view.AolControlsView.ExtraViewModel;

import java.util.HashMap;

public class ControlsBehavior implements Middleware {
    @NonNull
    private final Binder binder;
    @NonNull
    private final ExtraViewModel extraVM = new ExtraViewModel();
    @NonNull
    private final Handler handler;
    @NonNull
    private final Runnable hideAction = () -> {
        extraVM.areControlsVisible = false;
        renderViewModelToControls();
    };
    private boolean isPlaybackStarted;
    private boolean shouldPlay;
    private boolean isFinished;
    @Nullable
    private String videoUrl;
    private Boolean wereControlsVisible;

    public ControlsBehavior(@NonNull Binder binder, @NonNull Handler handler) {
        this.binder = binder;
        this.handler = handler;
        extraVM.areControlsVisible = true;

        extraVM.muteAction = isMuted -> {
            Player player = binder.getPlayer();
            if (player != null) {
                player.setMute(isMuted);
            }
        };


        extraVM.throughControlsClickAction = __ -> {
            extraVM.areControlsVisible = !extraVM.areControlsVisible;
            if (extraVM.areControlsVisible) {
                scheduleControlsHide();
            }
            renderViewModelToControls();

            Player player = binder.getPlayer();
            if (player != null && (binder.getPlayer().getProperties().camera.longitude == 1 || !binder.getPlayer().getProperties().isPlaybackStarted)) {
                VideoProperties video = player.getProperties().playlistItem.video;
                if (video != null && video.isFinished) {
                    player.replay();
                } else {
                    player.play();
                }
            }
        };

        //extraVM.onControlsClickAction = __ -> scheduleControlsHide();
    }

    private void scheduleControlsHide() {
        handler.removeCallbacks(hideAction);
        handler.postDelayed(hideAction, 3000L);
    }

    @Nullable
    private AolControlsView getAolControlsView() {
        PlayerViewport playerView = binder.getPlayerViewport();
        if (playerView != null) {
            ContentControls controls = playerView.getContentControls();

            if (controls instanceof AolControlsView) return (AolControlsView) controls;
        }

        return null;
    }

    @Override
    public void process(@NonNull Properties props, @NonNull ContentControls.ViewModel controlsVM, @NonNull PlayerViewport.ViewModel viewportVM, @NonNull VideoVM videoVM, @NonNull VideoVM adVM) {
        VideoProperties video = props.playlistItem.video;

        if (isPlaybackStarted != props.isPlaybackStarted) {
            isPlaybackStarted = props.isPlaybackStarted;
            if (isPlaybackStarted) extraVM.areControlsVisible = false;
        }

        if (shouldPlay != props.shouldPlay) {
            shouldPlay = props.shouldPlay;

            if (!props.shouldPlay) {
                wereControlsVisible = wereControlsVisible != null ? wereControlsVisible : extraVM.areControlsVisible;
                handler.removeCallbacks(hideAction);
            } else {
                extraVM.areControlsVisible = wereControlsVisible != null ? wereControlsVisible : extraVM.areControlsVisible;
                wereControlsVisible = null;
                if (extraVM.areControlsVisible) scheduleControlsHide();
            }
        }

        if ((videoUrl == null && videoVM.videoUrl != null) || (videoUrl != null && videoVM.videoUrl == null)) {
            videoUrl = videoVM.videoUrl;

            if (videoUrl == null) {
                wereControlsVisible = wereControlsVisible != null ? wereControlsVisible : extraVM.areControlsVisible;
                extraVM.areControlsVisible = true;
            } else {
                extraVM.areControlsVisible = wereControlsVisible != null ? wereControlsVisible : extraVM.areControlsVisible;
                wereControlsVisible = null;
                if (extraVM.areControlsVisible) scheduleControlsHide();
            }
        }

        if (isFinished != (video != null && video.isFinished)) {
            isFinished = (video != null && video.isFinished);
            if (isFinished) {
                extraVM.areControlsVisible = true;
                handler.removeCallbacks(hideAction);
            }
        }

        if (video != null) {
            controlsVM.isPlayButtonVisible = controlsVM.isPlayButtonVisible || (!props.isPlaybackStarted && !props.isAutoplay && !props.shouldPlay);
            controlsVM.isLoading = controlsVM.isLoading && !controlsVM.isPlayButtonVisible;
            controlsVM.isThumbnailImageVisible = !props.isPlaybackStarted || !video.isFrameVisible || adVM.videoUrl != null;
            controlsVM.thumbnailImageUrl = getBestFitThumbnail(video.model.thumbnails, props.viewport.width, props.viewport.height);
        }

        controlsVM.isSeekerVisible = controlsVM.isSeekerVisible && controlsVM.compassLongitude != 1;

        if (!controlsVM.isSeekerVisible) {
            controlsVM.seekerCurrentTimeText = null;
            controlsVM.seekerDurationText = null;
            controlsVM.seekerTimeLeftText = null;
        }

        extraVM.isMuted = props.isMuted;
        extraVM.isFullscreenButtonVisible = controlsVM.isSeekerVisible;
        extraVM.isMuteButtonVisible = controlsVM.isSeekerVisible;

        if (binder != null && binder.getPlayer() != null) {
            if (props.shouldPlay && binder.getPlayer().getProperties().camera.longitude == 1) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (binder == null || binder.getPlayer() == null) return;
                        binder.getPlayer().setCameraDirection(0, 0);
                    }
                });
            }
        }

        renderViewModelToControls();
    }

    private void renderViewModelToControls() {
        AolControlsView controls = getAolControlsView();
        if (controls != null) {
            controls.render(extraVM);
        }
    }

    private String getBestFitThumbnail(HashMap<Pair<Integer, Integer>, String> thumbnails, int width, int height) {
        String result = null;

        int area = 0;

        for (Pair<Integer, Integer> size : thumbnails.keySet()) {
            if (size.first == null || size.second == null) throw new IllegalArgumentException();
            int thumbArea = size.first * size.second;

            if (area < thumbArea && (thumbArea <= width * height || area == 0)) {
                result = thumbnails.get(size);
                area = thumbArea;
            }
        }

        return result;
    }
}
