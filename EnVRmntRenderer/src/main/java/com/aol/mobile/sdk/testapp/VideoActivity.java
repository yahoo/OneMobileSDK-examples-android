package com.aol.mobile.sdk.testapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aol.mobile.sdk.controls.view.PlayerControlsView;
import com.aol.mobile.sdk.player.Binder;
import com.aol.mobile.sdk.player.OneSDK;
import com.aol.mobile.sdk.player.OneSDKBuilder;
import com.aol.mobile.sdk.player.Player;
import com.aol.mobile.sdk.player.listener.ErrorListener;
import com.aol.mobile.sdk.player.model.ErrorState;
import com.aol.mobile.sdk.player.view.PlayerView;

import static com.aol.mobile.sdk.envrmnt.renderer.EnVrMntRenderer.ENVRMNT_RENDERER_VERSION;
import static com.aol.mobile.sdk.envrmnt.renderer.EnVrMntRenderer.EnVrMntRendererProducer;

public class VideoActivity extends Activity {
    private Binder binder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = new Binder();
        binder.setPlayerView(new PlayerView(this));

        setContentView(binder.getPlayerView());

        new OneSDKBuilder(getApplicationContext())
                .registerRenderer(ENVRMNT_RENDERER_VERSION, new EnVrMntRendererProducer())
                .create(new OneSDKBuilder.Callback() {
                    @Override
                    public void onSuccess(@NonNull OneSDK oneSDK) {
                        constructPlayer(oneSDK, binder);
                    }

                    @Override
                    public void onFailure(@NonNull Exception error) {
                        Toast.makeText(getApplicationContext(),
                                "JSON parsing error", Toast.LENGTH_SHORT).show();
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

    private void constructPlayer(@NonNull OneSDK oneSDK, final Binder binder) {
        oneSDK.createBuilder()
                .buildForPlaylist("591debb4f3bdc94d6a779732", new Player.Callback() {
                    @Override
                    public void success(@NonNull final Player player) {
                        player.addErrorListener(new ErrorListener() {
                            @Override
                            public void onError(@NonNull ErrorState errorState) {
                                Toast.makeText(getApplicationContext(),
                                        errorState.name(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        binder.setPlayer(player);

                        PlayerView playerView = binder.getPlayerView();
                        if (playerView == null ||
                                !(playerView.getContentControls() instanceof PlayerControlsView))
                            return;

                        PlayerControlsView controlsView =
                                (PlayerControlsView) playerView.getContentControls();
                        ImageButton muteBtn = new ImageButton(VideoActivity.this);
                        muteBtn.setImageResource(R.drawable.ic_halo);
                        muteBtn.setLayoutParams(new LinearLayout.LayoutParams(121, 121));

                        controlsView.getSidePanel().addView(muteBtn);

                        muteBtn.setOnClickListener(new View.OnClickListener() {
                            boolean isMuted = true;

                            @Override
                            public void onClick(View v) {
                                player.setMute(isMuted);
                                isMuted = !isMuted;
                            }
                        });
                    }

                    @Override
                    public void error(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

}
