package com.aol.mobile.sdk.testapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.aol.mobile.sdk.player.OneSDK;
import com.aol.mobile.sdk.player.OneSDKBuilder;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PlayerAdapter playerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_videos);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        playerAdapter = new PlayerAdapter();
        recyclerView.setAdapter(playerAdapter);

        new OneSDKBuilder(getApplicationContext())
                .create(new OneSDKBuilder.Callback() {
                    @Override
                    public void onSuccess(@NonNull OneSDK oneSDK) {
                        playerAdapter.setData(oneSDK, getVideos());
                    }

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error Creating SDK", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String[] getVideos() {
        String videos[] = {
                "593967be9e45105fa1b5939a",
                "577cc23d50954952cc56bc47",
                "5939698f85eb427b86aa0a14",
                "593967be9e45105fa1b5939a",
                "577cc23d50954952cc56bc47",
                "5939698f85eb427b86aa0a14",
                "593967be9e45105fa1b5939a",
                "577cc23d50954952cc56bc47",
                "5939698f85eb427b86aa0a14",
                "593967be9e45105fa1b5939a"};
        return videos;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((PlayerAdapter) recyclerView.getAdapter()).bindersOnResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((PlayerAdapter) recyclerView.getAdapter()).bindersOnPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((PlayerAdapter) recyclerView.getAdapter()).bindersOnDestroy();
    }
}
