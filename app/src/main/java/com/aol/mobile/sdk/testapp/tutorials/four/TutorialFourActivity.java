package com.aol.mobile.sdk.testapp.tutorials.four;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.aol.mobile.sdk.testapp.R;
import com.aol.mobile.sdk.testapp.tutorials.three.CurrentStateActivity;
import com.aol.mobile.sdk.testapp.tutorials.three.LoopPlaybackActivity;
import com.aol.mobile.sdk.testapp.tutorials.three.PauseSeekPlayActivity;

public class TutorialFourActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_four);

        Button btnSDKInitializationErrors = findViewById(R.id.btn_sdk_initialization_errors);
        btnSDKInitializationErrors.setOnClickListener(this);
        Button btnPlayerInitializationErrors = findViewById(R.id.btn_player_initialization_errors);
        btnPlayerInitializationErrors.setOnClickListener(this);
        Button btnRestrictedVideos = findViewById(R.id.btn_restricted_videos);
        btnRestrictedVideos.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Class<? extends AppCompatActivity> targetActivity;
        switch (view.getId()) {
            case R.id.btn_sdk_initialization_errors:
                targetActivity = SDKErrorsActivity.class;
                break;
            case R.id.btn_player_initialization_errors:
                targetActivity = PlayerErrorsActivity.class;
                break;
            case R.id.btn_restricted_videos:
                targetActivity = RestrictedVideoActivity.class;
                break;
            default:
                return;
        }

        Intent intent = new Intent(getApplicationContext(), targetActivity);
        startActivity(intent);
    }
}
