package com.aol.mobile.sdk.testapp.aol.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.aol.mobile.sdk.controls.ContentControls;
import com.aol.mobile.sdk.testapp.R;
import com.squareup.picasso.Picasso;

public class AolControlsView extends FrameLayout implements ContentControls {
    @NonNull
    private LinearLayout llControlsRootContainer;
    @NonNull
    private android.widget.Button btnPlay;
    @NonNull
    private android.widget.Button btnPause;
    @NonNull
    private android.widget.Button btnReplay;
    @NonNull
    private FrameLayout pbLoading;
    @NonNull
    private TextView tvTimeCurrent;
    @NonNull
    private SeekBar sbSeekBar;
    @NonNull
    private TextView tvTitle;
    @NonNull
    private TextView tvTimeLeft;
    @NonNull
    private TextView tvFullScreen;
    @NonNull
    private TextView tvVolume;
    @NonNull
    private ImageView thumbnail;
    @Nullable
    private String thumbnailUrl;
    @Nullable
    private Listener listener;
    @Nullable
    private FullscreenListener fullscreenListener;
    @NonNull
    private final ExtraViewModel extraVM = new ExtraViewModel();
    private boolean isFullscreen;

    public AolControlsView(Context context) {
        this(context, null);
    }

    public AolControlsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AolControlsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(), R.layout.aol_custom_controls, this);

        llControlsRootContainer = findViewById(R.id.ll_controls_root_container);

        OnClickListener clickListener = v -> {
            switch (v.getId()) {
                case R.id.ll_controls_root_container:
                    extraVM.throughControlsClickAction.onChange(null);
                    break;

                case R.id.btn_play:
                    extraVM.onControlsClickAction.onChange(null);
                    if (listener != null) listener.onButtonClick(Button.PLAY);
                    break;

                case R.id.btn_pause:
                    extraVM.onControlsClickAction.onChange(null);
                    if (listener != null) listener.onButtonClick(Button.PAUSE);
                    break;

                case R.id.btn_replay:
                    extraVM.onControlsClickAction.onChange(null);
                    if (listener != null) listener.onButtonClick(Button.REPLAY);
                    break;

                case R.id.volume:
                    extraVM.onControlsClickAction.onChange(null);
                    extraVM.muteAction.onChange(!extraVM.isMuted);
                    break;

                case R.id.full_screen:
                    extraVM.onControlsClickAction.onChange(null);
                    if (fullscreenListener != null)
                        fullscreenListener.onFullscreenChanged(!isFullscreen);
                    break;
            }
        };

        llControlsRootContainer.setOnClickListener(clickListener);

        btnPlay = findViewById(R.id.btn_play);
        btnPlay.setBackground(getContext().getResources().getDrawable(R.drawable.aol_icon_video));
        btnPlay.setOnClickListener(clickListener);
        btnPause = findViewById(R.id.btn_pause);
        btnPause.setOnClickListener(clickListener);
        btnPause.setBackground(getContext().getResources().getDrawable(R.drawable.aol_stop));
        btnReplay = findViewById(R.id.btn_replay);
        btnReplay.setBackground(getContext().getResources().getDrawable(R.drawable.aol_icon_video));
        btnReplay.setOnClickListener(clickListener);
        pbLoading = findViewById(R.id.pb_loading);
        tvTimeCurrent = findViewById(R.id.tv_time_current);
        sbSeekBar = findViewById(R.id.sb_seek_bar);
        final SeekBar.OnSeekBarChangeListener seekBarListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
                if (listener == null) return;

                if (fromUser) {
                    extraVM.onControlsClickAction.onChange(null);
                    listener.onSeekTo(progress / (float) seekBar.getMax());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                extraVM.onControlsClickAction.onChange(null);

                if (listener == null) return;

                listener.onSeekStarted();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                extraVM.onControlsClickAction.onChange(null);

                if (listener == null) return;

                listener.onSeekStopped();
            }
        };
        sbSeekBar.setOnSeekBarChangeListener(seekBarListener);
        tvTitle = findViewById(R.id.tv_title);
        tvTimeLeft = findViewById(R.id.tv_time_left);
        tvFullScreen = findViewById(R.id.full_screen);
        tvFullScreen.setOnClickListener(clickListener);
        tvVolume = findViewById(R.id.volume);
        tvVolume.setOnClickListener(clickListener);
        thumbnail = findViewById(R.id.thumbnail_view);
        thumbnail.setBackgroundColor(Color.BLACK);
        isFullscreen = false;

        setOnClickListener(v -> extraVM.throughControlsClickAction.onChange(null));
    }

    @Override
    public void render(@NonNull ContentControls.ViewModel vm) {
        btnPlay.setVisibility(vm.isPlayButtonVisible ? VISIBLE : INVISIBLE);
        btnPause.setVisibility(vm.isPauseButtonVisible ? VISIBLE : INVISIBLE);
        btnReplay.setVisibility(vm.isReplayButtonVisible ? VISIBLE : INVISIBLE);
        pbLoading.setVisibility(vm.isLoading ? VISIBLE : INVISIBLE);
        thumbnail.setVisibility(vm.isThumbnailImageVisible ? VISIBLE : INVISIBLE);
        sbSeekBar.setVisibility(vm.isSeekerVisible ? VISIBLE : INVISIBLE);

        tvTitle.setText(vm.titleText);
        tvTimeCurrent.setText(vm.seekerCurrentTimeText);
        tvTimeLeft.setText(vm.seekerTimeLeftText);

        sbSeekBar.setMax(vm.seekerMaxValue);
        sbSeekBar.setProgress((int) Math.round(vm.seekerMaxValue * vm.seekerProgress));

        renderThumbnailUrl(vm);
    }

    @Override
    public void setListener(@Nullable Listener listener) {
        this.listener = listener;
    }

    public void setFullscreenListener(@Nullable FullscreenListener fullscreenListener) {
        this.fullscreenListener = fullscreenListener;
    }

    public void setFullscreenState(boolean fullscreenFlag) {
        if (isFullscreen != fullscreenFlag) {
            isFullscreen = fullscreenFlag;
            Drawable drawable = getResources().getDrawable(isFullscreen ? R.drawable.aol_resize : R.drawable.aol_fullscreen);
            tvFullScreen.setBackground(drawable);
        }
    }

    private void renderThumbnailUrl(@NonNull ViewModel vm) {
        if ((thumbnailUrl == null && vm.thumbnailImageUrl != null) ||
                (thumbnailUrl != null && !thumbnailUrl.equals(vm.thumbnailImageUrl))) {
            thumbnailUrl = vm.thumbnailImageUrl;

            Picasso.with(getContext()).cancelRequest(thumbnail);
            if (thumbnailUrl != null) {
                Picasso.with(getContext())
                        .load(thumbnailUrl)
                        .placeholder(android.R.color.black)
                        .into(thumbnail);
            }
        }
    }

    public void render(@NonNull ExtraViewModel vm) {
        if (extraVM.onControlsClickAction != vm.onControlsClickAction) {
            extraVM.onControlsClickAction = vm.onControlsClickAction;
        }

        if (extraVM.throughControlsClickAction != vm.throughControlsClickAction) {
            extraVM.throughControlsClickAction = vm.throughControlsClickAction;
        }

        if (extraVM.muteAction != vm.muteAction) {
            extraVM.muteAction = vm.muteAction;
        }

        if (extraVM.isMuteButtonVisible != vm.isMuteButtonVisible) {
            extraVM.isMuteButtonVisible = vm.isMuteButtonVisible;
            tvVolume.setVisibility(extraVM.isMuteButtonVisible ? VISIBLE : INVISIBLE);
        }

        if (extraVM.isFullscreenButtonVisible != vm.isFullscreenButtonVisible) {
            extraVM.isFullscreenButtonVisible = vm.isFullscreenButtonVisible;
            tvFullScreen.setVisibility(extraVM.isMuteButtonVisible ? VISIBLE : INVISIBLE);
        }

        if (extraVM.isMuted != vm.isMuted) {
            extraVM.isMuted = vm.isMuted;
            Drawable drawable = getResources().getDrawable(extraVM.isMuted ? R.drawable.aol_mute : R.drawable.aol_volume);
            tvVolume.setBackground(drawable);
        }

        if (extraVM.areControlsVisible != vm.areControlsVisible) {
            extraVM.areControlsVisible = vm.areControlsVisible;
            llControlsRootContainer.setVisibility(extraVM.areControlsVisible ? VISIBLE : INVISIBLE);
        }
    }

    public interface FullscreenListener {
        void onFullscreenChanged(boolean isFullscreen);
    }

    public static class ExtraViewModel {
        public boolean isMuted;
        public boolean isMuteButtonVisible;
        public boolean isFullscreenButtonVisible;
        public boolean areControlsVisible;

        @NonNull
        public CallbackAction<Void> onControlsClickAction = value -> {
        };
        @NonNull
        public CallbackAction<Void> throughControlsClickAction = value -> {
        };
        @NonNull
        public CallbackAction<Boolean> muteAction = value -> {
        };

        public interface CallbackAction<T> {
            void onChange(T value);
        }
    }
}
