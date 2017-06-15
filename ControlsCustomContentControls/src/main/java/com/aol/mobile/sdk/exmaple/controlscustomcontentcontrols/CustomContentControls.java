package com.aol.mobile.sdk.exmaple.controlscustomcontentcontrols;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.aol.mobile.sdk.controls.ControlsButton;
import com.aol.mobile.sdk.controls.PlayerControls;
import com.aol.mobile.sdk.controls.viewmodel.PlayerControlsVM;

public class CustomContentControls extends LinearLayout implements PlayerControls {
    @NonNull
    private LinearLayout llControlsRootContainer;
    @NonNull
    private FrameLayout flButtonsContainer;
    @NonNull
    private LinearLayout llSubtitlesContainer;
    @NonNull
    private Button btnPlay;
    @NonNull
    private Button btnPause;
    @NonNull
    private Button btnReplay;
    @NonNull
    private ProgressBar pbLoading;
    @NonNull
    private Button btnNext;
    @NonNull
    private Button btnPrev;
    @NonNull
    private Button btnForward10Sec;
    @NonNull
    private Button btnBack10Sec;
    @NonNull
    private TextView tvTimeCurrent;
    @NonNull
    private SeekBar sbSeekBar;
    @NonNull
    private TextView tvTitle;
    @NonNull
    private TextView tvTimeLeft;
    @NonNull
    private TextView tvSubtitles;
    @NonNull
    private Button btnSubtitles;
    @Nullable
    private Listener listener;
    private boolean isControlsVisible = true;

    public CustomContentControls(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(), R.layout.custom_content_controls, this);

        llControlsRootContainer = (LinearLayout) findViewById(R.id.ll_controls_root_container);
        llControlsRootContainer.setOnClickListener(clickListener);
        flButtonsContainer = (FrameLayout) findViewById(R.id.fl_buttons_container);
        llSubtitlesContainer = (LinearLayout) findViewById(R.id.ll_subtitles_container);
        btnPlay = (Button) findViewById(R.id.btn_play);
        btnPlay.setOnClickListener(clickListener);
        btnPause = (Button) findViewById(R.id.btn_pause);
        btnPause.setOnClickListener(clickListener);
        btnReplay = (Button) findViewById(R.id.btn_replay);
        btnReplay.setOnClickListener(clickListener);
        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(clickListener);
        btnPrev = (Button) findViewById(R.id.btn_prev);
        btnPrev.setOnClickListener(clickListener);
        btnForward10Sec = (Button) findViewById(R.id.btn_forward_10_sec);
        btnForward10Sec.setOnClickListener(clickListener);
        btnBack10Sec = (Button) findViewById(R.id.btn_back_10_sec);
        btnBack10Sec.setOnClickListener(clickListener);
        tvTimeCurrent = (TextView) findViewById(R.id.tv_time_current);
        sbSeekBar = (SeekBar) findViewById(R.id.sb_seek_bar);
        sbSeekBar.setOnSeekBarChangeListener(seekBarListener);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTimeLeft = (TextView) findViewById(R.id.tv_time_left);
        tvSubtitles = (TextView) findViewById(R.id.tv_subtitles);
        btnSubtitles = (Button) findViewById(R.id.btn_subtitles);
        btnSubtitles.setOnClickListener(clickListener);
    }

    @NonNull
    private OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == llControlsRootContainer) {
                if (isControlsVisible) {
                    hideControls();
                    isControlsVisible = false;
                } else {
                    showControls();
                    isControlsVisible = true;
                }
            }

            if (listener == null) return;

            if (v == btnPlay) listener.onButtonClick(ControlsButton.PLAY);
            if (v == btnPause) listener.onButtonClick(ControlsButton.PAUSE);
            if (v == btnReplay) listener.onButtonClick(ControlsButton.REPLAY);
            if (v == btnNext) listener.onButtonClick(ControlsButton.NEXT);
            if (v == btnPrev) listener.onButtonClick(ControlsButton.PREVIOUS);
            if (v == btnForward10Sec) listener.onButtonClick(ControlsButton.SEEK_FORWARD);
            if (v == btnBack10Sec) listener.onButtonClick(ControlsButton.SEEK_BACKWARD);
            if (v == btnSubtitles) {
                btnSubtitles.setSelected(!btnSubtitles.isSelected());
                listener.onSubtitleToggled(btnSubtitles.isSelected());
            }
        }
    };

    @NonNull
    private final SeekBar.OnSeekBarChangeListener seekBarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
            if (listener == null) return;

            if (fromUser) listener.onSeekTo(progress / (float) seekBar.getMax());
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            if (listener == null) return;

            listener.onSeekStarted();
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (listener == null) return;

            listener.onSeekStopped();
        }
    };

    private void showControls() {
        flButtonsContainer.setVisibility(VISIBLE);
        tvTimeCurrent.setVisibility(VISIBLE);
        sbSeekBar.setVisibility(VISIBLE);
        tvTitle.setVisibility(VISIBLE);
        tvTimeLeft.setVisibility(VISIBLE);
        llSubtitlesContainer.setVisibility(VISIBLE);
    }

    private void hideControls() {
        flButtonsContainer.setVisibility(INVISIBLE);
        tvTimeCurrent.setVisibility(INVISIBLE);
        sbSeekBar.setVisibility(INVISIBLE);
        tvTitle.setVisibility(INVISIBLE);
        tvTimeLeft.setVisibility(INVISIBLE);
        llSubtitlesContainer.setVisibility(INVISIBLE);
    }

    @Override
    public void setListener(@Nullable Listener listener) {
        this.listener = listener;
    }

    @Override
    public void render(@NonNull PlayerControlsVM viewModel) {
        btnPlay.setVisibility(viewModel.isPlayButtonVisible ? VISIBLE : INVISIBLE);
        btnPause.setVisibility(viewModel.isPauseButtonVisible ? VISIBLE : INVISIBLE);
        btnReplay.setVisibility(viewModel.isReplayButtonVisible ? VISIBLE : INVISIBLE);
        pbLoading.setVisibility(viewModel.isLoading ? VISIBLE : INVISIBLE);
        btnNext.setVisibility(viewModel.isNextButtonVisible ? VISIBLE : INVISIBLE);
        btnNext.setEnabled(viewModel.isNextButtonEnabled);
        btnPrev.setVisibility(viewModel.isPrevButtonVisible ? VISIBLE : INVISIBLE);
        btnPrev.setEnabled(viewModel.isPrevButtonEnabled);
        tvTimeCurrent.setText(viewModel.seekerCurrentTimeText);
        sbSeekBar.setMax(viewModel.seekerMaxValue);
        sbSeekBar.setProgress((int) Math.round(viewModel.seekerMaxValue * viewModel.seekerProgress));
        tvTitle.setText(viewModel.titleText);
        tvTimeLeft.setText(viewModel.seekerTimeLeftText);
        tvSubtitles.setText(viewModel.subtitlesText);
        tvSubtitles.setVisibility(viewModel.isSubtitlesTextVisible ? VISIBLE : INVISIBLE);
        btnSubtitles.setEnabled(viewModel.isSubtitlesButtonEnabled);
        btnSubtitles.setSelected(viewModel.isSubtitlesButtonSelected);
        btnSubtitles.setVisibility(viewModel.isSubtitlesButtonVisible ? VISIBLE : INVISIBLE);
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }
}
