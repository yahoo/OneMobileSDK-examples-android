package com.aol.mobile.sdk.testapp.tutorials.two;

import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.aol.mobile.sdk.player.OneSDK;
import com.aol.mobile.sdk.player.OneSDKBuilder;
import com.aol.mobile.sdk.player.Player;
import com.aol.mobile.sdk.player.PlayerStateObserver;
import com.aol.mobile.sdk.player.model.properties.Properties;
import com.aol.mobile.sdk.player.view.PlayerFragment;
import com.aol.mobile.sdk.testapp.Data;
import com.aol.mobile.sdk.testapp.R;

import static com.aol.mobile.sdk.player.model.properties.Properties.ViewState.Content;

public class AdsOnlyPlayerActivity extends AppCompatActivity {
    private PlayerStateObserver playerStateObserver;
    private boolean isAdFinished;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_fragment);

        FragmentManager fm = getFragmentManager();
        final PlayerFragment playerFragment = (PlayerFragment) fm.findFragmentById(R.id.player_fragment);

        playerStateObserver = new PlayerStateObserver() {
            @Override
            public void onPlayerStateChanged(@NonNull Properties properties) {
                if (!isAdFinished && properties.viewState == Content && properties.playlistItem.video.isPlaying) {
                    isAdFinished = true;


                    // Pause player on advertisement playback end.
                    // You can replace this action with your own logic.
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            playerFragment.getBinder().getPlayer().pause();
                        }
                    });
                }
            }
        };

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

    private void useSDK(@NonNull OneSDK oneSDK, @NonNull final PlayerFragment playerFragment) {
        oneSDK.createBuilder()
                .buildForVideo(Data.BLANK_VIDEO, new Player.Callback() {
                    @Override
                    public void success(@NonNull Player player) {
                        player.addPlayerStateObserver(playerStateObserver);
                        if (playerFragment.getPlayerView() != null) {
                            playerFragment.getPlayerView()
                                    .setContentControls(
                                            new AdsOnlyPlayerEmptyContentControls(AdsOnlyPlayerActivity.this));
                        }
                        playerFragment.getBinder().setPlayer(player);
                    }

                    @Override
                    public void error(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
