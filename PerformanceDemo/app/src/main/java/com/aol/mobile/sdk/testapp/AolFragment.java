package com.aol.mobile.sdk.testapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.AsyncLayoutInflater;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.aol.mobile.sdk.player.OneSDK;
import com.aol.mobile.sdk.testapp.aol.SimpleItemDecorator;
import com.aol.mobile.sdk.testapp.aol.adapter.AolRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR;

@SuppressLint("ValidFragment")
public class AolFragment extends Fragment {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<String> videoIds = new ArrayList<>();
    private AolRecyclerViewAdapter aolPlayerAdapter;
    private Integer screenOrientation;
    private boolean isResumed;

    public AolFragment() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (aolPlayerAdapter == null) return;

        aolPlayerAdapter.setActive(isVisibleToUser);

        if (isVisibleToUser && recyclerView != null) {
            recyclerView.postDelayed(() -> {
                if (getUserVisibleHint()) {
                    int position = aolPlayerAdapter.getLastPlayedItemPosition();
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    layoutManager.scrollToPositionWithOffset(position, 20);
                }
            }, 300);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Activity activity = getActivity();
        if (activity == null) throw
                new IllegalStateException("Fragment is not attached to activity");
        if (!(activity instanceof AolActivity))
            throw new IllegalStateException("Fragment should be used in conjunction with AolActivity");

        AolActivity aolActivity = (AolActivity) activity;
        OneSDK oneSDK = aolActivity.getOneSDK();

        FrameLayout root = new FrameLayout(activity);
        root.setBackgroundResource(android.R.color.darker_gray);

        AsyncLayoutInflater layoutInflater = new AsyncLayoutInflater(activity);

        layoutInflater.inflate(R.layout.fragment_aol, container, (view, resId, parent) -> {
            LinearLayoutManager layoutManager = new LinearLayoutManager(activity);

            aolPlayerAdapter = new AolRecyclerViewAdapter(oneSDK, (isInFullscreen) -> {
                if (isInFullscreen) {
                    screenOrientation = activity.getRequestedOrientation();

                    recyclerView.postDelayed(() ->
                            activity.setRequestedOrientation(SCREEN_ORIENTATION_FULL_SENSOR), 300);
                } else {
                    recyclerView.postDelayed(() -> {
                        if (screenOrientation != null) {
                            activity.setRequestedOrientation(screenOrientation);
                        }
                    }, 300);
                }
            }, layoutInflater);
            aolPlayerAdapter.setActive(getUserVisibleHint());
            aolPlayerAdapter.setResumed(isResumed);
            aolPlayerAdapter.setData(getVideoIds());

            recyclerView = view.findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(null);
            recyclerView.setLayoutAnimation(null);
            recyclerView.setAdapter(aolPlayerAdapter);
            recyclerView.addOnChildAttachStateChangeListener(aolPlayerAdapter);

            Resources r = getResources();
            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, r.getDisplayMetrics());

            recyclerView.addItemDecoration(new SimpleItemDecorator((int) px));

            swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
            swipeRefreshLayout.setOnRefreshListener(() -> {
                aolPlayerAdapter.setData(getVideoIds());
                swipeRefreshLayout.setRefreshing(false);
            });

            root.addView(view);
        });

        return root;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (aolPlayerAdapter != null && recyclerView != null) {
            int position = aolPlayerAdapter.getLastPlayedItemPosition();
            recyclerView.postDelayed(() -> {
                if (getUserVisibleHint()) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    layoutManager.scrollToPositionWithOffset(position, 20);
                }
            }, 300);
        }
    }

    public void setResumed(boolean isResumed) {
        this.isResumed = isResumed;

        if (aolPlayerAdapter != null) {
            aolPlayerAdapter.setResumed(isResumed);
        }
    }

    private List<String> getVideoIds() {
        videoIds.clear();

        videoIds.add("5b573909b7043450e7633c43");
        videoIds.add("5b5737ff943c3c494b795019");
        videoIds.add("5b5737ace880db574c68201d");
        videoIds.add("5b57280d4deb1a50f2ef3171");
        videoIds.add("5b57243b8256b65744909958");
        videoIds.add("5b5724218256b657449098e2");
        videoIds.add("5b5723f9523dc356e9d54a59");
        videoIds.add("5b57228d90143776affb0d0c");

        videoIds.add("5b5882ae8256b671d7ab5dd7");
        videoIds.add("5b5882ae98f45d469983e502");
        videoIds.add("5b58816734f69d73536e5e17");
        videoIds.add("5b5872d234f69d73536e53d9");
        videoIds.add("5b586a5d158f8572a798e88d");
        videoIds.add("5b5859618fcaa943cb84aec6");
        videoIds.add("5b5858c598f45d469983c816");
        videoIds.add("5b5858c5e880db574c69fe03");
        videoIds.add("5b584afc821576445ea5c050");
        videoIds.add("5b584134600c9a099935e785");
        videoIds.add("5b583fd8e880db574c69ef07");
        videoIds.add("5b58372db7043450e764fbb5");
        videoIds.add("5b582e8e9b74b63d4057fee6");

        return videoIds;
    }
}
