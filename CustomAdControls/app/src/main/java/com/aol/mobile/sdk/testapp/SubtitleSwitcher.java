package com.aol.mobile.sdk.testapp;

import android.support.annotation.NonNull;

import com.aol.mobile.sdk.player.Player;
import com.aol.mobile.sdk.player.PlayerStateObserver;
import com.aol.mobile.sdk.player.model.properties.Properties;
import com.aol.mobile.sdk.renderer.TextTrack;

import java.util.LinkedList;
import java.util.Locale;

class SubtitleSwitcher implements PlayerStateObserver {
    @NonNull
    private final Player player;
    @NonNull
    private final LinkedList<TextTrack> tracks = new LinkedList<>();
    private final Locale language;
    private int currentIndex = -1;

    SubtitleSwitcher(@NonNull Player player, @NonNull Locale language) {
        this.player = player;
        this.language = language;
    }

    @Override
    public void onPlayerStateChanged(@NonNull Properties props) {
        if (props.playlist.currentIndex != currentIndex) {
            currentIndex = props.playlist.currentIndex;

            tracks.clear();
        }

        if (tracks.isEmpty() && props.playlistItem.video != null && !props.playlistItem.video.textTrackList.isEmpty()) {
            tracks.addAll(props.playlistItem.video.textTrackList);

            for (TextTrack track : tracks) {
                if (track.isEmpty) continue;

                if (track.language.toLowerCase().equals(language.getLanguage().toLowerCase())) {
                    player.selectTextTrack(track);
                    break;
                }
            }
        }
    }
}
