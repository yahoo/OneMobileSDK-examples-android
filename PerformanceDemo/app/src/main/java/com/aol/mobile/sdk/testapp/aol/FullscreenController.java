package com.aol.mobile.sdk.testapp.aol;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public final class FullscreenController {
    @NonNull
    private final View playerView;
    @NonNull
    private final Listener listener;
    @Nullable
    private Dialog fullScreenDialog;

    public FullscreenController(@NonNull View playerView, @NonNull Listener listener) {
        this.playerView = playerView;
        this.listener = listener;
    }

    public void changeFullscreen(boolean shouldGoFullscreen) {
        if (shouldGoFullscreen) {
            if (fullScreenDialog == null) {
                Context context = playerView.getContext();
                fullScreenDialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                ViewGroup parent = (ViewGroup) playerView.getParent();

                playerView.setBackgroundColor(Color.BLACK);
                FrameLayout root = new FrameLayout(context);
                root.setBackgroundColor(Color.BLACK);
                fullScreenDialog.setContentView(root);

                fullScreenDialog.setOnShowListener(dialogInterface -> {
                    if (parent != null) parent.removeView(playerView);
                    root.addView(playerView, MATCH_PARENT, MATCH_PARENT);
                    listener.onFullscreenStateChanged(true);
                });

                fullScreenDialog.setOnDismissListener(dialogInterface -> {
                    fullScreenDialog = null;
                    listener.onFullscreenStateChanged(false);
                    playerView.setBackgroundResource(android.R.color.darker_gray);

                    root.removeAllViews();
                    if (parent != null) parent.addView(playerView);
                });

                fullScreenDialog.setCancelable(true);
                fullScreenDialog.show();
            }
        } else {
            if (fullScreenDialog != null) {
                fullScreenDialog.dismiss();
            }
        }
    }

    public interface Listener {
        void onFullscreenStateChanged(boolean isInFullscreen);
    }
}
