package com.aol.mobile.sdk.testapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aol.mobile.sdk.controls.ImageLoader;
import com.aol.mobile.sdk.controls.view.SidePanel;
import com.aol.mobile.sdk.player.Binder;
import com.aol.mobile.sdk.player.OneSDK;
import com.aol.mobile.sdk.player.Player;
import com.aol.mobile.sdk.player.view.PlayerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerHolder> {
    private List<Binder> binders = new ArrayList<>();
    private List<String> videoIds;
    private OneSDK oneSDK;
    private Context context;
    private boolean isFullscreenActive;

    public PlayerAdapter(Context context) {
        this.context = context;
    }

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
                    .buildForVideo(videoIds.get(position), new Player.Callback() {
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
        return videoIds != null ? videoIds.size() : 0;
    }

    public void setData(@NonNull final OneSDK oneSDK, @NonNull List<String> videoIds) {
        this.oneSDK = oneSDK;
        this.videoIds = videoIds;
        binders.addAll(Collections.<Binder>nCopies(videoIds.size(), null));
        notifyDataSetChanged();
    }

    public void add(@NonNull List<String> videoIds) {
        this.videoIds.addAll(videoIds);
        binders.addAll(Collections.<Binder>nCopies(videoIds.size(), null));
        notifyItemRangeChanged(this.videoIds.size() - videoIds.size(), this.videoIds.size());
    }

    class PlayerHolder extends RecyclerView.ViewHolder {
        TextView textViewPosition;
        PlayerView playerView;

        PlayerHolder(final View itemView) {
            super(itemView);
            textViewPosition = itemView.findViewById(R.id.item_tv_position);
            playerView = itemView.findViewById(R.id.item_playerview);
            playerView.getContentControls().setImageLoader(new ImageLoader() {
                @Override
                public void load(@Nullable String url, @NonNull ImageView imageView) {
                    Picasso.with(context).load(url).into(imageView);
                }

                @Override
                public void cancelLoad(@NonNull ImageView imageView) {
                    Picasso.with(context).cancelRequest(imageView);
                }
            });
            addSideBarButton(playerView.getContentControls().getSidePanel(), playerView);
        }
    }

    @Override
    public void onViewRecycled(@NonNull PlayerHolder holder) {
        super.onViewRecycled(holder);
    }


    private void addSideBarButton(@NonNull SidePanel sidePanel, final PlayerView playerView) {
        final ImageButton fullscreenButton = makeFullscreenButton();
        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout.LayoutParams curLayoutParams = (LinearLayout.LayoutParams) playerView.getLayoutParams();
                if (isFullscreenActive) {
                    animateFromFullscreen(curLayoutParams);

                    fullscreenButton.setActivated(false);
                    isFullscreenActive = false;
                } else {
                    animateToFullscreen(curLayoutParams);

                    fullscreenButton.setActivated(true);
                    isFullscreenActive = true;
                }
            }
        });

        sidePanel.addView(fullscreenButton);
        sidePanel.show();
    }

    private ImageButton makeFullscreenButton() {
        ImageButton button = new ImageButton(context);
        button.setBackground(ContextCompat.getDrawable(context, R.drawable.selector_btn_fullscreen));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(112, 112);
        layoutParams.setMargins(5, 5, 5, 5);
        button.setLayoutParams(layoutParams);
        button.setPadding(0, 0, 0, 0);
        button.setScaleType(ImageView.ScaleType.FIT_CENTER);

        return button;
    }

    private void animateToFullscreen(final LinearLayout.LayoutParams curLayoutParams) {

    }

    private void animateFromFullscreen(LinearLayout.LayoutParams curLayoutParams) {

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
