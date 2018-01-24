package com.aol.mobile.sdk.testapp.tutorials.two;

import android.animation.ValueAnimator;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aol.mobile.sdk.controls.view.ContentControlsView;
import com.aol.mobile.sdk.controls.view.SidePanel;
import com.aol.mobile.sdk.player.OneSDK;
import com.aol.mobile.sdk.player.OneSDKBuilder;
import com.aol.mobile.sdk.player.Player;
import com.aol.mobile.sdk.player.view.PlayerFragment;
import com.aol.mobile.sdk.testapp.Data;
import com.aol.mobile.sdk.testapp.R;

public class SidebarFullscreenActivity extends AppCompatActivity {
    private boolean isFullscreenActive;
    private PlayerFragment playerFragment;
    private View fragmentParent;
    private RelativeLayout.LayoutParams ogLayoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_fragment_for_fullscreen);

        FragmentManager fm = getFragmentManager();
        playerFragment = (PlayerFragment) fm.findFragmentById(R.id.player_fragment);
        fragmentParent = (View) playerFragment.getPlayerView().getParent();
        ogLayoutParams = (RelativeLayout.LayoutParams) playerFragment.getView().getLayoutParams();

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

                        ContentControlsView playerControlsView = (ContentControlsView) playerFragment
                                .getPlayerView().getContentControls();

                        addSideBarButton(playerControlsView.getSidePanel());
                    }

                    @Override
                    public void error(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addSideBarButton(@NonNull SidePanel sidePanel) {
        final ImageButton fullscreenButton = makeFullscreenButton();
        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RelativeLayout.LayoutParams curLayoutParams = (RelativeLayout.LayoutParams) playerFragment.getPlayerView().getLayoutParams();
                if (isFullscreenActive) {
                    getSupportActionBar().show();
                    animateFromFullscreen(curLayoutParams);

                    fullscreenButton.setActivated(false);
                    isFullscreenActive = false;
                } else {
                    getSupportActionBar().hide();
                    animateToFullscreen(curLayoutParams);

                    fullscreenButton.setActivated(true);
                    isFullscreenActive = true;
                }
            }
        });

        sidePanel.addView(fullscreenButton);
        sidePanel.show();
    }

    private ImageButton makeFullscreenButton() {
        ImageButton button = new ImageButton(getApplicationContext());
        button.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.selector_btn_fullscreen));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(112, 112);
        layoutParams.setMargins(5, 5, 5, 5);
        button.setLayoutParams(layoutParams);
        button.setPadding(0, 0, 0, 0);
        button.setScaleType(ImageView.ScaleType.FIT_CENTER);

        return button;
    }

    private void animateToFullscreen(final RelativeLayout.LayoutParams curLayoutParams) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(curLayoutParams.height, fragmentParent.getHeight() + getSupportActionBar().getHeight());
        valueAnimator.setDuration(300);
        valueAnimator.setStartDelay(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                playerFragment.getPlayerView().setLayoutParams(new RelativeLayout.LayoutParams(curLayoutParams.width, (int) animation.getAnimatedValue()));
                playerFragment.getPlayerView().requestLayout();
            }
        });
        valueAnimator.start();
    }

    private void animateFromFullscreen(RelativeLayout.LayoutParams curLayoutParams) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(curLayoutParams.height, ogLayoutParams.height);
        valueAnimator.setDuration(600);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ogLayoutParams.height = (int) animation.getAnimatedValue();
                playerFragment.getPlayerView().setLayoutParams(ogLayoutParams);
                playerFragment.getPlayerView().requestLayout();
            }
        });
        valueAnimator.start();
    }
}
