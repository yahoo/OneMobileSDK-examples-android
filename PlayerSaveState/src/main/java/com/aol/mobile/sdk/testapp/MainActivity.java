package com.aol.mobile.sdk.testapp;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.aol.mobile.sdk.player.Binder;
import com.aol.mobile.sdk.player.OneSDK;
import com.aol.mobile.sdk.player.OneSDKBuilder;
import com.aol.mobile.sdk.player.Player;
import com.aol.mobile.sdk.player.model.properties.Properties;
import com.aol.mobile.sdk.player.view.PlayerView;

public class MainActivity extends AppCompatActivity {
    public static final String VIDEO_ID = "577cc23d50954952cc56bc47";

    private Binder binder;
    private PlayerView playerView;
    private StorageFragment storageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playerView = (PlayerView) findViewById(R.id.player_view);

        FragmentManager fm = getFragmentManager();
        storageFragment = (StorageFragment) fm.findFragmentByTag("storageFragment");
        if (storageFragment == null) {
            storageFragment = new StorageFragment();
            fm.beginTransaction().add(storageFragment, "storageFragment").commit();
        }

        if (storageFragment.getBinder() != null && storageFragment.getBinder().getPlayer() != null) {
            binder = storageFragment.getBinder();
            binder.setPlayerView(playerView);
            Properties playerProperties = binder.getPlayer().getProperties();
            if (playerProperties.viewState == Properties.ViewState.Content) {
                binder.getPlayer().seekTo((playerProperties.playlistItem.video.time.progress));
            }
        } else {
            binder = new Binder();
            new OneSDKBuilder(getApplicationContext())
                    .create(new OneSDKBuilder.Callback() {
                        @Override
                        public void onSuccess(@NonNull OneSDK oneSDK) {
                            useSDK(oneSDK);
                        }

                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Error Creating SDK", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void useSDK(OneSDK oneSDK) {
        oneSDK.createBuilder()
                .setAutoplay(true)
                .buildForVideo(VIDEO_ID, new Player.Callback() {
                    @Override
                    public void success(@NonNull Player player) {
                        binder.setPlayerView(playerView);
                        binder.setPlayer(player);
                    }

                    @Override
                    public void error(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        binder.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        binder.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            binder.onDestroy();
        } else {
            storageFragment.setBinder(binder);
        }
        playerView.dispose();
    }
}
