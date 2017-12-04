package com.aol.mobile.sdk.testapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.aol.mobile.sdk.controls.ControlsButton;
import com.aol.mobile.sdk.controls.PlayerControls;
import com.aol.mobile.sdk.controls.viewmodel.PlayerControlsVM;
import com.aol.mobile.sdk.controls.viewmodel.TrackOptionVM;

import java.util.LinkedList;

public class CustomContentControls extends LinearLayout implements PlayerControls {
    @NonNull
    private LinearLayout llControlsRootContainer;
    @NonNull
    private LinearLayout llLiveIndicator;
    @NonNull
    private FrameLayout flButtonsContainer;
    @NonNull
    private LinearLayout llTrackContainer;
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
    private Button btnChooseTrack;
    @NonNull
    private final LinkedList<TrackOptionVM> ccTracks = new LinkedList<>();
    @NonNull
    private final LinkedList<TrackOptionVM> audioTracks = new LinkedList<>();
    @NonNull
    private final TrackChooserAdapter adapter = new TrackChooserAdapter();
    @Nullable
    private Dialog dialog;
    @Nullable
    private Listener listener;
    private boolean isControlsVisible = true;

    public CustomContentControls(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(), R.layout.custom_content_controls, this);

        llControlsRootContainer = findViewById(R.id.ll_controls_root_container);
        llControlsRootContainer.setOnClickListener(clickListener);
        llLiveIndicator = findViewById(R.id.ll_live_indicator);
        flButtonsContainer = findViewById(R.id.fl_buttons_container);
        btnPlay = findViewById(R.id.btn_play);
        btnPlay.setOnClickListener(clickListener);
        btnPause = findViewById(R.id.btn_pause);
        btnPause.setOnClickListener(clickListener);
        btnReplay = findViewById(R.id.btn_replay);
        btnReplay.setOnClickListener(clickListener);
        pbLoading = findViewById(R.id.pb_loading);
        btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(clickListener);
        btnPrev = findViewById(R.id.btn_prev);
        btnPrev.setOnClickListener(clickListener);
        btnForward10Sec = findViewById(R.id.btn_forward_10_sec);
        btnForward10Sec.setOnClickListener(clickListener);
        btnBack10Sec = findViewById(R.id.btn_back_10_sec);
        btnBack10Sec.setOnClickListener(clickListener);
        tvTimeCurrent = findViewById(R.id.tv_time_current);
        sbSeekBar = findViewById(R.id.sb_seek_bar);
        sbSeekBar.setOnSeekBarChangeListener(seekBarListener);
        tvTitle = findViewById(R.id.tv_title);
        tvTimeLeft = findViewById(R.id.tv_time_left);
        tvSubtitles = findViewById(R.id.tv_subtitles);
        llTrackContainer = findViewById(R.id.ll_track_container);
        btnChooseTrack = findViewById(R.id.btn_choose_track);
        btnChooseTrack.setOnClickListener(clickListener);
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
            if (v == btnChooseTrack) {
                adapter.updateData(getContext(), audioTracks, ccTracks);
                dialog = new Dialog(getContext());
                dialog.setContentView(new Button(getContext()));
                dialog.show();
                dialog.setCanceledOnTouchOutside(true);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        CustomContentControls.this.dialog = null;
                    }
                });
                ListView listView = new ListView(getContext());
                listView.setDivider(null);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        adapter.select(position);

                        TrackChooserAdapter.Item item = adapter.getItem(position);

                        switch (item.type) {
                            case CC:
                                listener.onCcTrackSelected(item.index);
                                break;

                            case AUDIO:
                                listener.onAudioTrackSelected(item.index);
                                break;

                            case CLOSE:
                                dialog.dismiss();
                                break;
                        }
                    }
                });
                dialog.setContentView(listView);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                Window window = dialog.getWindow();
                window.getAttributes().windowAnimations = R.style.TracksDialogAnimation;
                window.getAttributes().gravity = Gravity.BOTTOM | Gravity.FILL_HORIZONTAL;
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

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
        llTrackContainer.setVisibility(VISIBLE);
    }

    private void hideControls() {
        flButtonsContainer.setVisibility(INVISIBLE);
        tvTimeCurrent.setVisibility(INVISIBLE);
        sbSeekBar.setVisibility(INVISIBLE);
        tvTitle.setVisibility(INVISIBLE);
        tvTimeLeft.setVisibility(INVISIBLE);
        llTrackContainer.setVisibility(INVISIBLE);
    }

    @Override
    public void setListener(@Nullable Listener listener) {
        this.listener = listener;
    }

    @Override
    public void render(@NonNull PlayerControlsVM viewModel) {
        llLiveIndicator.setVisibility(viewModel.isLiveIndicatorVisible ? VISIBLE : INVISIBLE);
        Drawable liveDot = llLiveIndicator.getChildAt(0).getBackground();
        if (viewModel.isOnLiveEdge) {
            liveDot.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        } else {
            liveDot.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        }

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
        btnChooseTrack.setVisibility(viewModel.isTrackChooserButtonVisible ? VISIBLE : INVISIBLE);
        btnChooseTrack.setEnabled(viewModel.isTrackChooserButtonEnabled);

        this.audioTracks.clear();
        this.audioTracks.addAll(viewModel.audioTracks);
        this.ccTracks.clear();
        this.ccTracks.addAll(viewModel.ccTracks);
        if (audioTracks.isEmpty() && ccTracks.isEmpty()) {
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
        }
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }
}
