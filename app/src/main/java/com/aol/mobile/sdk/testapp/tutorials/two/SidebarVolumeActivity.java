package com.aol.mobile.sdk.testapp.tutorials.two;

import android.app.FragmentManager;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aol.mobile.sdk.controls.view.PlayerControlsView;
import com.aol.mobile.sdk.controls.view.SidePanel;
import com.aol.mobile.sdk.player.OneSDK;
import com.aol.mobile.sdk.player.OneSDKBuilder;
import com.aol.mobile.sdk.player.Player;
import com.aol.mobile.sdk.player.view.PlayerFragment;
import com.aol.mobile.sdk.testapp.Data;
import com.aol.mobile.sdk.testapp.R;

public class SidebarVolumeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_fragment);

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
                .buildForVideo(Data.VIDEO_ID, new Player.Callback() {
                    @Override
                    public void success(@NonNull Player player) {
                        playerFragment.getBinder().setPlayer(player);

                        PlayerControlsView playerControlsView = playerFragment
                                .getPlayerView().getContentControls();

                        addSideBarButtons(playerControlsView.getSidePanel(), playerFragment.getBinder().getPlayer());

                    }

                    @Override
                    public void error(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addSideBarButtons(@NonNull SidePanel sidePanel, final Player player) {
        final AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        ImageButton plusButton = makeImageButton(R.drawable.plus);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
            }
        });

        ImageButton minusButton = makeImageButton(R.drawable.minus);
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
            }
        });

        ImageButton muteButton = makeImageButton(R.drawable.mute);
        muteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.setMute(true);
            }
        });

        ImageButton unmuteButton = makeImageButton(R.drawable.unmute);
        unmuteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.setMute(false);
            }
        });

        sidePanel.addView(plusButton);
        sidePanel.addView(minusButton);
        sidePanel.addView(muteButton);
        sidePanel.addView(unmuteButton);
        sidePanel.show();
    }

    private ImageButton makeImageButton(int imageResource) {
        ImageButton button = new ImageButton(getApplicationContext());
        button.setBackground(null);
        button.setImageResource(imageResource);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(112, 112);
        layoutParams.setMargins(5, 5, 5, 5);
        button.setLayoutParams(layoutParams);
        button.setPadding(0, 0, 0, 0);
        button.setScaleType(ImageView.ScaleType.FIT_CENTER);

        return button;
    }
}
