package com.aol.mobile.sdk.testapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.aol.mobile.sdk.player.Binder;
import com.aol.mobile.sdk.player.EmptyPlaylistException;
import com.aol.mobile.sdk.player.InvalidRendererException;
import com.aol.mobile.sdk.player.OneSDK;
import com.aol.mobile.sdk.player.OneSDKBuilder;
import com.aol.mobile.sdk.player.Player;
import com.aol.mobile.sdk.player.VideoProvider;
import com.aol.mobile.sdk.player.VideoProviderResponse;
import com.aol.mobile.sdk.player.view.PlayerView;
import com.aol.mobile.sdk.renderer.ExternalSubtitle;

import java.util.Collections;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    private static final String[] PLAYLIST = {
            "5c123fc4655b68404c142acb",
            "59397934955a316f1c4f65b4",
            "59397934955a316f1c4f65b4",
            "5a79c8ee85eb424e94d7e1bf",
            "593967be9e45105fa1b5939a",
            "5a29790883b51f467d9d6b6a"
    };
    private Binder binder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View root = findViewById(R.id.root);
        PlayerView playerView = findViewById(R.id.player_view);

        binder = new Binder();
        binder.setPlayerViewport(playerView);

        new OneSDKBuilder(this).create(new OneSDKBuilder.Callback() {
            @Override
            public void onSuccess(@NonNull OneSDK oneSDK) {
                oneSDK.getVideoProvider().requestPlaylistModel(PLAYLIST, new VideoProvider.Callback() {
                    @Override
                    public void success(@NonNull VideoProviderResponse videoProviderResponse) {
                        VideoProviderResponse.PlaylistItem[] playlistItems = videoProviderResponse.playlistItems;

                        changeSubtitlesForVideo(playlistItems, 1, new ExternalSubtitle(
                                "en",
                                "https://raw.githubusercontent.com/andreyvit/subtitle-tools/master/sample.srt",
                                "srt"
                        ));

                        Player player;
                        try {
                            player = oneSDK.createBuilder()
                                    .setAutoplay(true)
                                    .buildFrom(videoProviderResponse.withPlaylistItems(playlistItems));
                        } catch (EmptyPlaylistException | InvalidRendererException e) {
                            Snackbar.make(root, "Error while creating playlist: " + e.getMessage(), Snackbar.LENGTH_LONG)
                                    .setAction("Close", null)
                                    .show();
                            return;
                        }


                        binder.setPlayer(player);

                        player.addPlayerStateObserver(new SubtitleSwitcher(player, Locale.ENGLISH));
                        player.addPlayerStateObserver(new PlaylistLooper(player));
                    }

                    @Override
                    public void error(@NonNull Exception e) {
                        Snackbar.make(root, "Error while creating playlist: " + e.getMessage(), Snackbar.LENGTH_LONG)
                                .setAction("Close", null)
                                .show();
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Exception error) {
                Snackbar.make(root, "Error while creating SDK: " + error.getMessage(), Snackbar.LENGTH_LONG)
                        .setAction("Close", null)
                        .show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        binder.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binder.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binder.onDestroy();
    }

    private void changeSubtitlesForVideo(VideoProviderResponse.PlaylistItem[] playlistItems, int index, ExternalSubtitle externalSubtitle) {
        VideoProviderResponse.Video video = playlistItems[index].video;
        if (video != null) {
            VideoProviderResponse.Video modifiedVideo = new VideoProviderResponse.Video(
                    video.thumbnails,
                    video.id,
                    video.url,
                    video.title,
                    // Substitution of subtitle
                    // You can add to existing ones video.externalSubtitles
                    Collections.singletonList(externalSubtitle),
                    video.renderer,
                    video.preroll,
                    video.midrolls,
                    video.isScreenCastingEnabled,
                    video.brandedContent
            );
            playlistItems[index] = new VideoProviderResponse.PlaylistItem(modifiedVideo);
        }
    }
}
