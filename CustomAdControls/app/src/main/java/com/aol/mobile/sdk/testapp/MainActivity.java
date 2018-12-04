package com.aol.mobile.sdk.testapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.aol.mobile.sdk.player.Binder;
import com.aol.mobile.sdk.player.OneSDK;
import com.aol.mobile.sdk.player.OneSDKBuilder;
import com.aol.mobile.sdk.player.Player;
import com.aol.mobile.sdk.player.view.PlayerView;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {
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
                Snackbar.make(root, "Error while creating SDK", Snackbar.LENGTH_LONG)
                        .setAction("Close", null)
                        .show();

                oneSDK.createBuilder()
                        .setAutoplay(true)
                        .buildForVideoList(new String[]{
                                "59b0122a8c08e07695c98519",
                                "59b0122a8c08e07695c98519",
                                "59b0122a8c08e07695c98519",
                                "59b0122a8c08e07695c98519"
                        }, new Player.Callback() {
                            @Override
                            public void success(@NonNull Player player) {
                                binder.setPlayer(player);
                                player.addPlayerStateObserver(new SubtitleSwitcher(player, Locale.ENGLISH));
                            }

                            @Override
                            public void error(@NonNull Exception e) {

                            }
                        });
            }

            @Override
            public void onFailure(@NonNull Exception error) {

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
}
