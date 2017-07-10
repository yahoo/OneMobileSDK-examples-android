package com.aol.mobile.sdk.testapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aol.mobile.sdk.player.Binder;
import com.aol.mobile.sdk.player.OneSDK;
import com.aol.mobile.sdk.player.Player;
import com.aol.mobile.sdk.player.listener.ErrorListener;
import com.aol.mobile.sdk.player.model.ErrorState;
import com.aol.mobile.sdk.player.view.PlayerView;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerHolder> {
    private final static int PLAYER_COUNT = 10;

    private Context context;
    private int currentlyPlayingItemPosition = 0;
    private Binder binder = new Binder();
    private String videoId;
    private OneSDK sdk;

    public PlayerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public PlayerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player, parent, false);

        return new PlayerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PlayerHolder holder, int pos) {
        final int position = pos;
        holder.textViewPosition.setText(String.valueOf(position));
        if (position == currentlyPlayingItemPosition) {
            if (sdk == null) return;
            sdk.createBuilder()
                    .setAutoplay(true)
                    .buildForVideo(videoId, new Player.Callback() {
                        @Override
                        public void success(@NonNull Player player) {
                            if (position == currentlyPlayingItemPosition) {
                                binder.setPlayer(player);
                                binder.setPlayerView(holder.playerView);
                                if (player != null) {
                                    player.addErrorListener(new ErrorListener() {
                                        @Override
                                        public void onError(ErrorState errorState) {
                                            Toast.makeText(context, errorState.name(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    player.play();
                                }
                            }
                        }

                        @Override
                        public void error(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            holder.playerView.dispose();
        }
    }

    public Binder getBinder() {
        return binder;
    }

    @Override
    public int getItemCount() {
        return PLAYER_COUNT;
    }

    public void setData(@NonNull final OneSDK sdk, String videoId) {
        this.videoId = videoId;
        this.sdk = sdk;
        notifyDataSetChanged();
    }

    public int getCurrentlyPlayingItemPosition() {
        return currentlyPlayingItemPosition;
    }

    public void playAtPosition(int position) {
        this.currentlyPlayingItemPosition = position;
        if (binder.getPlayerView() != null && binder.getPlayer() != null) {
            binder.onDestroy();
        }
        notifyDataSetChanged();
    }

    class PlayerHolder extends RecyclerView.ViewHolder {
        TextView textViewPosition;
        PlayerView playerView;

        PlayerHolder(final View itemView) {
            super(itemView);
            textViewPosition = (TextView) itemView.findViewById(R.id.item_tv_position);
            playerView = (PlayerView) itemView.findViewById(R.id.item_playerview);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playAtPosition(getAdapterPosition());
                }
            });
        }
    }
}
