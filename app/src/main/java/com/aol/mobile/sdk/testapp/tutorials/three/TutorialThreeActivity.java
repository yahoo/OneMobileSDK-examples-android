package com.aol.mobile.sdk.testapp.tutorials.three;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.aol.mobile.sdk.testapp.R;

public class TutorialThreeActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_three);

        Button btnCurrentState = findViewById(R.id.btn_current_state);
        btnCurrentState.setOnClickListener(this);
        Button btnPausePlaySeek = findViewById(R.id.btn_pause_seek_play);
        btnPausePlaySeek.setOnClickListener(this);
        Button btnLoopPlayback = findViewById(R.id.btn_loop_playback);
        btnLoopPlayback.setOnClickListener(this);
        Button btnInspectVideoType = findViewById(R.id.btn_inspect_video_type);
        btnInspectVideoType.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Class<? extends AppCompatActivity> targetActivity;
        switch (view.getId()) {
            case R.id.btn_current_state:
                targetActivity = CurrentStateActivity.class;
                break;
            case R.id.btn_pause_seek_play:
                targetActivity = PauseSeekPlayActivity.class;
                break;
            case R.id.btn_loop_playback:
                targetActivity = LoopPlaybackActivity.class;
                break;
            case R.id.btn_inspect_video_type:
                targetActivity = InspectVideoTypeActivity.class;
                break;
            default:
                return;
        }

        Intent intent = new Intent(getApplicationContext(), targetActivity);
        startActivity(intent);
    }
}
