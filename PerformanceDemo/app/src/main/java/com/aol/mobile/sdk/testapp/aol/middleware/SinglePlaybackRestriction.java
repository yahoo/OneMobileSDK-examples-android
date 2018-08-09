package com.aol.mobile.sdk.testapp.aol.middleware;

import android.support.annotation.NonNull;
import android.util.Pair;

import com.aol.mobile.sdk.controls.ContentControls;
import com.aol.mobile.sdk.player.Binder;
import com.aol.mobile.sdk.player.Middleware;
import com.aol.mobile.sdk.player.Player;
import com.aol.mobile.sdk.player.model.properties.Properties;
import com.aol.mobile.sdk.player.view.PlayerViewport;
import com.aol.mobile.sdk.renderer.viewmodel.VideoVM;

import java.util.ArrayList;

public class SinglePlaybackRestriction implements Middleware {
    @NonNull
    private final Pair<Binder, VideoBufferingRestriction> binderWithRestriction;
    @NonNull
    private final ArrayList<Pair<Binder, VideoBufferingRestriction>> binders;
    @NonNull
    private final Listener listener;
    private Boolean shouldPlay;

    public SinglePlaybackRestriction(@NonNull ArrayList<Pair<Binder, VideoBufferingRestriction>> binders, @NonNull Pair<Binder, VideoBufferingRestriction> binderWithRestriction, @NonNull Listener listener) {
        this.binders = binders;
        this.binderWithRestriction = binderWithRestriction;
        this.listener = listener;
    }

    @Override
    public void process(@NonNull Properties props, @NonNull ContentControls.ViewModel controlsVM,
                        @NonNull PlayerViewport.ViewModel viewportVM, @NonNull VideoVM videoVM,
                        @NonNull VideoVM adVM) {
        if (shouldPlay == null || props.shouldPlay != shouldPlay) {
            shouldPlay = props.shouldPlay;

            if (shouldPlay) {
                for (int i = 0; i < binders.size(); i++) {
                    Pair<Binder, VideoBufferingRestriction> binderWithRestriction = binders.get(i);
                    Binder binder = binderWithRestriction.first;
                    VideoBufferingRestriction restriction = binderWithRestriction.second;

                    if (this.binderWithRestriction == binderWithRestriction) {
                        restriction.setBufferingRestricted(false);
                        listener.onPlayItemChanged(i);
                    } else {
                        restriction.setBufferingRestricted(true);
                        Player player = binder.getPlayer();
                        if (player != null) player.pause();
                    }
                }
            }
        }
    }

    public interface Listener {
        void onPlayItemChanged(int position);
    }
}
