package com.aol.mobile.sdk.testapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.aol.mobile.sdk.player.OneSDK;
import com.aol.mobile.sdk.player.OneSDKBuilder;

public class MainActivity extends AppCompatActivity {
    public static final String VIDEO_ID = "577cc23d50954952cc56bc47";

    private PlayerAdapter playerAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_videos);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        playerAdapter = new PlayerAdapter(getApplicationContext());
        recyclerView.setAdapter(playerAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, int newState) {
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    return;
                }
                int currentlyPlayingItemPosition = ((PlayerAdapter) recyclerView.getAdapter()).getCurrentlyPlayingItemPosition();
                int firstCompletelyVisibleItemPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                if (currentlyPlayingItemPosition != firstCompletelyVisibleItemPosition) {
                    currentlyPlayingItemPosition = firstCompletelyVisibleItemPosition;
                    playerAdapter.playAtPosition(currentlyPlayingItemPosition);
                }
            }
        });

        new OneSDKBuilder(getApplicationContext())
                .create(new OneSDKBuilder.Callback() {
                    @Override
                    public void onSuccess(@NonNull OneSDK oneSDK) {
                        playerAdapter.setData(oneSDK, VIDEO_ID);
                    }

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error Creating SDK", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((PlayerAdapter) recyclerView.getAdapter()).getBinder().onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((PlayerAdapter) recyclerView.getAdapter()).getBinder().onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((PlayerAdapter) recyclerView.getAdapter()).getBinder().onDestroy();
    }
}
