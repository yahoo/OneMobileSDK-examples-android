package com.aol.mobile.sdk.testapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aol.mobile.sdk.player.OneSDK;
import com.aol.mobile.sdk.player.OneSDKBuilder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PlayerAdapter playerAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_videos);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        playerAdapter = new PlayerAdapter();
        recyclerView.setAdapter(playerAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            private boolean isLoading = false;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy == 0) return;
                int childCount = linearLayoutManager.getChildCount();
                int itemCount = linearLayoutManager.getItemCount();
                int findFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                if (!isLoading && (childCount + findFirstVisibleItemPosition) >= itemCount) {
                    isLoading = true;
                    progressBar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isLoading = false;
                            progressBar.setVisibility(View.GONE);
                            playerAdapter.add(getVideos());
                        }
                    }, 500);
                }
            }
        });

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

    private List<String> getVideos() {
        List<String> videos = new ArrayList<>();
        videos.add("593967be9e45105fa1b5939a");
        videos.add("577cc23d50954952cc56bc47");
        videos.add("593967be9e45105fa1b5939a");
        videos.add("577cc23d50954952cc56bc47");
        videos.add("593967be9e45105fa1b5939a");
        videos.add("577cc23d50954952cc56bc47");
        videos.add("593967be9e45105fa1b5939a");
        videos.add("577cc23d50954952cc56bc47");
        videos.add("593967be9e45105fa1b5939a");
        videos.add("577cc23d50954952cc56bc47");
        videos.add("593967be9e45105fa1b5939a");
        videos.add("577cc23d50954952cc56bc47");
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
