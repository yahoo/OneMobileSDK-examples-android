package com.aol.mobile.sdk.testapp.tutorials.one;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.aol.mobile.sdk.testapp.R;

public class TutorialOneActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_one);
        Button btnPlayVideo = findViewById(R.id.btn_play_video);
        btnPlayVideo.setOnClickListener(this);
        Button btnPlayArray = findViewById(R.id.btn_play_array);
        btnPlayArray.setOnClickListener(this);
        Button btnPlayPlaylist = findViewById(R.id.btn_play_playlist);
        btnPlayPlaylist.setOnClickListener(this);
        Button btnSetTintColor = findViewById(R.id.btn_set_tint_color);
        btnSetTintColor.setOnClickListener(this);
        Button btnAutoplayOff = findViewById(R.id.btn_autoplay_off);
        btnAutoplayOff.setOnClickListener(this);
        Button btnPlayMuted = findViewById(R.id.btn_play_muted);
        btnPlayMuted.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Class<? extends AppCompatActivity> targetActivity;
        switch (view.getId()) {
            case R.id.btn_play_video:
                targetActivity = PlayVideoActivity.class;
                break;
            case R.id.btn_play_array:
                targetActivity = PlayArrayActivity.class;
                break;
            case R.id.btn_play_playlist:
                targetActivity = PlayPlaylistActivity.class;
                break;
            case R.id.btn_set_tint_color:
                targetActivity = SetTintColorActivity.class;
                break;
            case R.id.btn_autoplay_off:
                targetActivity = AutoplayOffActivity.class;
                break;
            case R.id.btn_play_muted:
                targetActivity = PlayMutedActivity.class;
                break;
            default:
                return;
        }

        Intent intent = new Intent(getApplicationContext(), targetActivity);
        startActivity(intent);
    }
}
