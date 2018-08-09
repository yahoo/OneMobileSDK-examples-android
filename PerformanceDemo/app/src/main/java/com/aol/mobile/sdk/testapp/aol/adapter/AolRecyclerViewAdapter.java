package com.aol.mobile.sdk.testapp.aol.adapter;


import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.AsyncLayoutInflater;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.aol.mobile.sdk.controls.ContentControls;
import com.aol.mobile.sdk.player.Binder;
import com.aol.mobile.sdk.player.EmptyPlaylistException;
import com.aol.mobile.sdk.player.InvalidRendererException;
import com.aol.mobile.sdk.player.OneSDK;
import com.aol.mobile.sdk.player.Player;
import com.aol.mobile.sdk.player.Player.Callback;
import com.aol.mobile.sdk.player.VideoProvider;
import com.aol.mobile.sdk.player.VideoProviderResponse;
import com.aol.mobile.sdk.player.view.PlayerView;
import com.aol.mobile.sdk.player.view.PlayerViewport;
import com.aol.mobile.sdk.renderer.viewmodel.VideoVM;
import com.aol.mobile.sdk.testapp.R;
import com.aol.mobile.sdk.testapp.aol.FullscreenController;
import com.aol.mobile.sdk.testapp.aol.middleware.AdBufferedInBackground;
import com.aol.mobile.sdk.testapp.aol.middleware.ControlsBehavior;
import com.aol.mobile.sdk.testapp.aol.middleware.SinglePlaybackRestriction;
import com.aol.mobile.sdk.testapp.aol.middleware.VideoBufferingRestriction;
import com.aol.mobile.sdk.testapp.aol.view.AolControlsView;
import com.aol.mobile.sdk.testapp.aol.view.AspectFrameLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public final class AolRecyclerViewAdapter extends RecyclerView.Adapter<AolRecyclerViewAdapter.ViewHolder> implements RecyclerView.OnChildAttachStateChangeListener {
    @NonNull
    private final ArrayList<Pair<Binder, VideoBufferingRestriction>> binders = new ArrayList<>();
    @NonNull
    private final ArrayList<Callback> callbacks = new ArrayList<>();
    @NonNull
    private final ArrayList<String> videoIds = new ArrayList<>();
    @NonNull
    private final HashSet<Binder> visibleItemBinders = new HashSet<>();
    @NonNull
    private final OneSDK oneSDK;
    @NonNull
    private final AsyncLayoutInflater layoutInflater;
    @NonNull
    private final FullscreenController.Listener fullscreenStateListener;
    private int lastPlayedItemPos;
    private boolean isActive;
    private boolean isResumed;

    public AolRecyclerViewAdapter(@NonNull OneSDK oneSDK, @NonNull FullscreenController.Listener fullscreenStateListener, @NonNull AsyncLayoutInflater layoutInflater) {
        this.oneSDK = oneSDK;
        this.fullscreenStateListener = fullscreenStateListener;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(parent.getContext());

        layoutInflater.inflate(R.layout.item_recycler, parent,
                (view, __, ___) -> viewHolder.setPlayerView((PlayerView) view));

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.binder = binders.get(position).first;
        Player player = holder.binder.getPlayer();

        if (player == null) {
            makeRequestForPlayerIfNeeded(position);
        } else {
            holder.binder.setPlayerViewport(holder.playerView);
        }
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        PlayerView playerView = holder.playerView;
        if (playerView != null) {
            PlayerViewport.ViewModel vm = new PlayerViewport.ViewModel();
            vm.isContentControlsVisible = true;
            playerView.render(vm);
            playerView.getAdVideoRenderer().render(new VideoVM());
            playerView.getContentVideoRenderer().render(new VideoVM());
        }

        if (holder.controlsView != null) {
            ContentControls.ViewModel vm = new ContentControls.ViewModel();
            vm.isLoading = true;
            holder.controlsView.render(vm);
        }

        if (holder.binder != null) {
            holder.binder.setPlayerViewport(null);
            holder.binder = null;
        }
    }

    @Override
    public int getItemCount() {
        return videoIds.size();
    }

    @Override
    public void onChildViewAttachedToWindow(View view) {
        for (int i = 0, bindersSize = binders.size(); i < bindersSize; i++) {
            final Pair<Binder, VideoBufferingRestriction> binderWithRestriction = binders.get(i);

            Binder binder = binderWithRestriction.first;
            PlayerViewport playerView = binder.getPlayerViewport();
            View childAt = ((FrameLayout) view).getChildAt(0);

            if (childAt == playerView) {
                visibleItemBinders.add(binder);
                if (isResumed) {
                    binder.onResume();
                }
            }
        }
    }

    @Override
    public void onChildViewDetachedFromWindow(View view) {
        for (int i = 0, bindersSize = binders.size(); i < bindersSize; i++) {
            final Pair<Binder, VideoBufferingRestriction> binderWithRestriction = binders.get(i);
            Binder binder = binderWithRestriction.first;
            PlayerViewport playerView = binder.getPlayerViewport();
            View childAt = ((FrameLayout) view).getChildAt(0);

            if (childAt == playerView) {
                binder.onPause();
                visibleItemBinders.remove(binder);
            }
        }
    }

    public void setData(@NonNull List<String> videos) {
        for (Pair<Binder, VideoBufferingRestriction> binderWithRestriction : binders) {
            Binder binder = binderWithRestriction.first;
            binder.onDestroy();
        }

        binders.clear();
        callbacks.clear();
        videoIds.clear();

        videoIds.addAll(videos);
        binders.ensureCapacity(videoIds.size());
        callbacks.ensureCapacity(videoIds.size());

        for (int i = 0; i < videos.size(); i++) {
            binders.add(Pair.create(new Binder(), new VideoBufferingRestriction()));
            callbacks.add(null);
        }

        notifyDataSetChanged();
    }

    @SuppressWarnings("unused")
    public void addData(@NonNull List<String> videos) {
        int oldSize = videoIds.size();

        binders.ensureCapacity(binders.size() + videos.size());
        callbacks.ensureCapacity(binders.size() + videos.size());
        for (int i = 0; i < videos.size(); i++) {
            binders.add(Pair.create(new Binder(), new VideoBufferingRestriction()));
            callbacks.add(null);
        }
        videoIds.addAll(videos);

        notifyItemRangeChanged(oldSize, videoIds.size());
    }

    private void makeRequestForPlayerIfNeeded(int position) {
        if (callbacks.get(position) == null) {
            Callback callback = new Callback() {
                @Override
                public void success(@NonNull Player player) {
                    Pair<Binder, VideoBufferingRestriction> binderAndRestriction = binders.get(position);
                    Binder binder = binderAndRestriction.first;
                    VideoBufferingRestriction bufferingRestriction = binderAndRestriction.second;
                    bufferingRestriction.setBufferingRestricted(true);

                    binder.addMiddleware(new SinglePlaybackRestriction(binders, binderAndRestriction, pos -> lastPlayedItemPos = pos));
                    binder.addMiddleware(bufferingRestriction);
                    binder.addMiddleware(new ControlsBehavior(binder, new Handler()));
                    binder.addMiddleware(new AdBufferedInBackground());

                    binder.setPlayer(player);

                    callbacks.set(position, null);
                    notifyItemChanged(position);
                }

                @Override
                public void error(@NonNull Exception e) {
                    callbacks.set(position, null);
                    notifyItemChanged(position);
                }
            };
            callbacks.set(position, callback);
            makeRequestFor(videoIds.get(position), callback);
        }
    }

    private void makeRequestFor(@NonNull String videoId, @NonNull final Callback callback) {
        oneSDK.getVideoProvider().requestVideoModel(videoId, false, null, new VideoProvider.Callback() {
            @Override
            public void success(@NonNull VideoProviderResponse videoProviderResponse) {
                try {
                    callback.success(oneSDK.createBuilder().buildFrom(videoProviderResponse));
                } catch (EmptyPlaylistException | InvalidRendererException e) {
                    callback.error(e);
                }
            }

            @Override
            public void error(@NonNull Exception e) {
                callback.error(e);
            }
        });
    }

    public int getLastPlayedItemPosition() {
        return lastPlayedItemPos;
    }

    public void setActive(boolean active) {
        isActive = active;
        for (Pair<Binder, VideoBufferingRestriction> binderWithRestriction : binders) {
            VideoBufferingRestriction restriction = binderWithRestriction.second;
            restriction.setActive(isActive);
        }
        setResumed(isResumed);
    }

    public void setResumed(boolean resumed) {
        isResumed = resumed;

        for (Binder binder : visibleItemBinders) {
            if (isResumed && isActive)
                binder.onResume();
            else
                binder.onPause();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        Binder binder;
        @Nullable
        PlayerView playerView;
        @Nullable
        AolControlsView controlsView;
        @NonNull
        private final AspectFrameLayout root;

        ViewHolder(@NonNull Context context) {
            super(new AspectFrameLayout(context));
            root = (AspectFrameLayout) itemView;
            root.setXRatio(16);
            root.setYRatio(9);
        }

        void setPlayerView(@NonNull PlayerView view) {
            playerView = view;
            controlsView = (AolControlsView) playerView.getContentControls();
            ContentControls.ViewModel viewModel = new ContentControls.ViewModel();
            viewModel.isLoading = true;
            controlsView.render(viewModel);
            root.addView(view, new LayoutParams(MATCH_PARENT, MATCH_PARENT));

            if (binder != null) {
                binder.setPlayerViewport(playerView);
            }

            FullscreenController controller = new FullscreenController(playerView, fullscreenFlag -> {
                controlsView.setFullscreenState(fullscreenFlag);
                fullscreenStateListener.onFullscreenStateChanged(fullscreenFlag);
            });

            controlsView.setFullscreenListener(controller::changeFullscreen);
        }
    }
}
