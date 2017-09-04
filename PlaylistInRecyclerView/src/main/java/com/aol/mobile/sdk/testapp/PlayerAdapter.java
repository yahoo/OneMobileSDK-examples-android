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

import java.util.HashMap;
import java.util.Map;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerHolder> {
    private HashMap<Integer, String> vidByPosition;
    private HashMap<Integer, Binder> binderByPosition = new HashMap<>();
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

        if (binderByPosition.get(position) == null) {
            final Binder binder = new Binder();
            oneSDK.createBuilder()
                    .setAutoplay(false)
                    .buildForVideo(vidByPosition.get(position), new Player.Callback() {
                        @Override
                        public void success(@NonNull Player player) {
                            binder.setPlayer(player);
                            binder.setPlayerView(holder.playerView);
                        }

                        @Override
                        public void error(@NonNull Exception e) {
                        }
                    });
            binderByPosition.put(position, binder);
        }
    }

    @Override
    public void onViewDetachedFromWindow(PlayerHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (binderByPosition.get(holder.getAdapterPosition()) != null && binderByPosition.get(holder.getAdapterPosition()).getPlayer() != null) {
            binderByPosition.get(holder.getAdapterPosition()).getPlayer().pause();
            binderByPosition.get(holder.getAdapterPosition()).setPlayerView(null);
        }
    }

    @Override
    public void onViewAttachedToWindow(PlayerHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (binderByPosition.get(holder.getAdapterPosition()) != null && binderByPosition.get(holder.getAdapterPosition()).getPlayer() != null) {
            binderByPosition.get(holder.getAdapterPosition()).setPlayerView(holder.playerView);
        }
    }

    @Override
    public int getItemCount() {
        return vidByPosition != null ? vidByPosition.size() : 0;
    }

    public void setData(@NonNull final OneSDK oneSDK, @NonNull HashMap<Integer, String> vidByPosition) {
        this.oneSDK = oneSDK;
        this.vidByPosition = vidByPosition;
        notifyDataSetChanged();
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
        for (Map.Entry<Integer, Binder> entry : binderByPosition.entrySet()) {
            if (entry.getValue() != null) {
                entry.getValue().onPause();
            }
        }
    }

    public void bindersOnPause() {
        for (Map.Entry<Integer, Binder> entry : binderByPosition.entrySet()) {
            if (entry.getValue() != null) {
                entry.getValue().onPause();
            }
        }
    }

    public void bindersOnDestroy() {
        for (Map.Entry<Integer, Binder> entry : binderByPosition.entrySet()) {
            if (entry.getValue() != null) {
                entry.getValue().onPause();
            }
        }
    }
}
