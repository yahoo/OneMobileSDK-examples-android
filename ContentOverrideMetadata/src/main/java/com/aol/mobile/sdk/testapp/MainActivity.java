package com.aol.mobile.sdk.testapp;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.aol.mobile.sdk.player.EmptyPlaylistException;
import com.aol.mobile.sdk.player.InvalidRendererException;
import com.aol.mobile.sdk.player.OneSDK;
import com.aol.mobile.sdk.player.OneSDKBuilder;
import com.aol.mobile.sdk.player.Player;
import com.aol.mobile.sdk.player.VideoProvider;
import com.aol.mobile.sdk.player.VideoProviderResponse;
import com.aol.mobile.sdk.player.view.PlayerFragment;

public class MainActivity extends AppCompatActivity {
    public static final String[] VIDEO_LIST = new String[]{"593967be9e45105fa1b5939a", "577cc23d50954952cc56bc47", "5939698f85eb427b86aa0a14"};

    /**
     * VIDEO_LIST constant above has 3 values. It means we request only 3 videos.
     * That is why we need 3 corresponding titles in titleArray to override them.
     * You should know the number of videos in your playlist to avoid exceptions.
     */

    private final String[] titleArray = new String[]{"Overridden title 1", "Overridden title 2", "Overridden title 3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getFragmentManager();
        final PlayerFragment playerFragment = (PlayerFragment) fm.findFragmentById(R.id.player_fragment);

        new OneSDKBuilder(getApplicationContext())
                .create(new OneSDKBuilder.Callback() {
                    @Override
                    public void onSuccess(@NonNull OneSDK oneSDK) {
                        useSDK(oneSDK, playerFragment);
                    }

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error Creating SDK", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void useSDK(@NonNull final OneSDK oneSDK, @NonNull final PlayerFragment playerFragment) {
        oneSDK.createBuilder()
                .setAutoplay(true)
                .requestForVideoList(VIDEO_LIST, new VideoProvider.Callback() {
                    @Override
                    public void success(@NonNull VideoProviderResponse videoProviderResponse) {
                        try {
                            VideoProviderResponse.PlaylistItem[] modifiedPlaylistItems = new VideoProviderResponse.PlaylistItem[videoProviderResponse.playlistItems.length];
                            for (int i = 0; i < modifiedPlaylistItems.length; i++) {
                                if (videoProviderResponse.playlistItems[i].video != null) {
                                    modifiedPlaylistItems[i] = new VideoProviderResponse.PlaylistItem(
                                            videoProviderResponse.playlistItems[i].video.withTitle(titleArray[i]));
                                } else {
                                    modifiedPlaylistItems[i] = videoProviderResponse.playlistItems[i];
                                }
                            }
                            videoProviderResponse = videoProviderResponse.withPlaylistItems(modifiedPlaylistItems);

                            Player player = oneSDK.createBuilder()
                                    .buildFrom(videoProviderResponse);
                            playerFragment.getBinder().setPlayer(player);
                        } catch (EmptyPlaylistException e) {
                            error(e);
                        } catch (InvalidRendererException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void error(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
