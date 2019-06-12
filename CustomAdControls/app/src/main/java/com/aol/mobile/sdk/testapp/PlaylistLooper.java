package com.aol.mobile.sdk.testapp;

import android.support.annotation.NonNull;

import com.aol.mobile.sdk.player.Player;
import com.aol.mobile.sdk.player.PlayerStateObserver;
import com.aol.mobile.sdk.player.model.properties.Properties;
import com.aol.mobile.sdk.player.model.properties.VideoProperties;

class PlaylistLooper implements PlayerStateObserver {
    @NonNull
    private final Player player;
    private boolean wasLooped;

    PlaylistLooper(@NonNull Player player) {
        this.player = player;
    }

    @Override
    public void onPlayerStateChanged(@NonNull Properties props) {
        VideoProperties video = props.playlistItem.video;
        if (video == null) return;

        if ((!video.isFinished || !video.isVideoStreamPlaying) && wasLooped) {
            wasLooped = false;
        }

        if (!wasLooped && video.isFinished && props.playlist.isLastVideo) {
            player.replay();
            player.playAtIndex(0);
            wasLooped = true;
        }
    }
}
