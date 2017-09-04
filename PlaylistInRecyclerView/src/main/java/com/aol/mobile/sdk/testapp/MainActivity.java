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

import java.util.HashMap;

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
                        playerAdapter.setData(oneSDK, getVidsForPositions());
                    }

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error Creating SDK", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private HashMap<Integer, String> getVidsForPositions() {
        HashMap<Integer, String> vidsForPositions = new HashMap<>();
        vidsForPositions.put(0, "593967be9e45105fa1b5939a");
        vidsForPositions.put(1, "577cc23d50954952cc56bc47");
        vidsForPositions.put(2, "5939698f85eb427b86aa0a14");
        vidsForPositions.put(3, "593967be9e45105fa1b5939a");
        vidsForPositions.put(4, "577cc23d50954952cc56bc47");
        vidsForPositions.put(5, "5939698f85eb427b86aa0a14");
        vidsForPositions.put(6, "593967be9e45105fa1b5939a");
        vidsForPositions.put(7, "577cc23d50954952cc56bc47");
        vidsForPositions.put(8, "5939698f85eb427b86aa0a14");
        vidsForPositions.put(9, "593967be9e45105fa1b5939a");
        return vidsForPositions;
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
