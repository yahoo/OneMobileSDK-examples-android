package com.aol.mobile.sdk.testapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aol.mobile.sdk.player.Binder;
import com.aol.mobile.sdk.player.OneSDK;
import com.aol.mobile.sdk.player.Player;
import com.aol.mobile.sdk.player.view.PlayerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerHolder> {
    private List<Binder> binders = new ArrayList<>();
    private List<String> videos;
    private OneSDK oneSDK;

    @Override
    public PlayerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player, parent, false);

        return new PlayerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PlayerHolder holder, int position) {
        holder.textViewPosition.setText(String.valueOf(position));
        if (oneSDK == null) {
            return;
        }

        if (binders.get(position) == null) {
            final Binder binder = new Binder();
            oneSDK.createBuilder()
                    .setAutoplay(false)
                    .buildForVideo(videos.get(position), new Player.Callback() {
                        @Override
                        public void success(@NonNull Player player) {
                            binder.setPlayer(player);
                            binder.setPlayerView(holder.playerView);
                        }

                        @Override
                        public void error(@NonNull Exception e) {
                        }
                    });
            binders.set(position, binder);
        }
    }

    @Override
    public void onViewDetachedFromWindow(PlayerHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder.getAdapterPosition() == RecyclerView.NO_POSITION) return;
        Binder binder = binders.get(holder.getAdapterPosition());
        if (binder != null) {
            if (binder.getPlayer() != null) {
                binder.getPlayer().pause();
            }
            binder.setPlayerView(null);
        }
    }

    @Override
    public void onViewAttachedToWindow(PlayerHolder holder) {
        super.onViewAttachedToWindow(holder);
        Binder binder = binders.get(holder.getAdapterPosition());
        if (binder != null && binder.getPlayer() != null) {
            binder.setPlayerView(holder.playerView);
        }
    }

    @Override
    public int getItemCount() {
        return videos != null ? videos.size() : 0;
    }

    public void setData(@NonNull final OneSDK oneSDK, @NonNull List<String> videos) {
        this.oneSDK = oneSDK;
        this.videos = videos;
        binders.addAll(Collections.<Binder>nCopies(videos.size(), null));
        notifyDataSetChanged();
    }

    public void add(@NonNull List<String> videos) {
        this.videos.addAll(videos);
        binders.addAll(Collections.<Binder>nCopies(videos.size(), null));
        notifyItemRangeChanged(this.videos.size() - videos.size(), this.videos.size());
    }

    class PlayerHolder extends RecyclerView.ViewHolder {
        TextView textViewPosition;
        PlayerView playerView;

        PlayerHolder(final View itemView) {
            super(itemView);
            textViewPosition = (TextView) itemView.findViewById(R.id.item_tv_position);
            playerView = (PlayerView) itemView.findViewById(R.id.item_playerview);
        }
    }

    @Override
    public void onViewRecycled(PlayerHolder holder) {
        super.onViewRecycled(holder);
    }

    public void bindersOnResume() {
        for (Binder binder : binders) {
            if (binder != null) {
                binder.onResume();
            }
        }
    }

    public void bindersOnPause() {
        for (Binder binder : binders) {
            if (binder != null) {
                binder.onPause();
            }
        }
    }

    public void bindersOnDestroy() {
        for (Binder binder : binders) {
            if (binder != null) {
                binder.onDestroy();
            }
        }
    }
}
