package com.aol.mobile.sdk.testapp;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.aol.mobile.sdk.controls.view.AdControlsView;
import com.aol.mobile.sdk.controls.view.PlayerControlsView;
import com.aol.mobile.sdk.player.OneSDK;
import com.aol.mobile.sdk.player.OneSDKBuilder;
import com.aol.mobile.sdk.player.Player;
import com.aol.mobile.sdk.player.view.PlayerFragment;

public class MainActivity extends AppCompatActivity {
    public static final String VIDEO_ID = "577cc23d50954952cc56bc47";

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

    private void useSDK(@NonNull OneSDK oneSDK, @NonNull final PlayerFragment playerFragment) {
        oneSDK.createBuilder()
                .setAutoplay(true)
                .buildForVideo(VIDEO_ID, new Player.Callback() {
                    @Override
                    public void success(@NonNull Player player) {
                        playerFragment.getBinder().setPlayer(player);

                        PlayerControlsView contentControls = playerFragment.getPlayerView().getContentControls();
                        contentControls.setMainColor(getResources().getColor(R.color.colorPrimary));
                        contentControls.setAccentColor(getResources().getColor(R.color.colorAccent));

                        AdControlsView adControls = playerFragment.getPlayerView().getAdControls();
                        adControls.setMainColor(getResources().getColor(R.color.colorPrimary));
                        adControls.setAccentColor(getResources().getColor(R.color.colorAccent));
                    }

                    @Override
                    public void error(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
