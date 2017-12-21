package com.aol.mobile.sdk.testapp.tutorials.two;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.aol.mobile.sdk.controls.view.SidePanel;
import com.aol.mobile.sdk.controls.view.TintableImageButton;
import com.aol.mobile.sdk.player.OneSDK;
import com.aol.mobile.sdk.player.OneSDKBuilder;
import com.aol.mobile.sdk.player.Player;
import com.aol.mobile.sdk.player.view.PlayerFragment;
import com.aol.mobile.sdk.testapp.Data;
import com.aol.mobile.sdk.testapp.R;

public class SidebarButtonsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
                        addSideBarButtons(playerFragment.getPlayerView()
                                .getContentControls()
                                .getSidePanel());

                        playerFragment.getBinder().setPlayer(player);
                    }

                    @Override
                    public void error(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addSideBarButtons(@NonNull SidePanel sidePanel) {
        sidePanel.addView(getButton(R.drawable.ic_add, getString(R.string.add)));
        sidePanel.addView(getButton(R.drawable.ic_fav, getString(R.string.fav)));
        sidePanel.addView(getButton(R.drawable.ic_later, getString(R.string.later)));
        sidePanel.addView(getButton(R.drawable.ic_share, getString(R.string.share)));
        sidePanel.show();
    }

    private TintableImageButton getButton(int imageId, final String message) {
        TintableImageButton button = new TintableImageButton(getApplicationContext());
        button.setImageResource(imageId);
        button.setOnClickListener(new View.OnClickListener() {
            boolean fav = false;

            @Override
            public void onClick(View v) {
                if (message.equals(getString(R.string.fav))) {
                    if (fav) {
                        Toast.makeText(getApplicationContext(), getString(R.string.un_fav), Toast.LENGTH_SHORT).show();
                        fav = false;
                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        fav = true;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }

        });

        return button;
    }
}
