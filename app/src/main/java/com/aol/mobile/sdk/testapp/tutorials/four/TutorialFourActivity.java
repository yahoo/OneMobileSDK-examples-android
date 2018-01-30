package com.aol.mobile.sdk.testapp.tutorials.four;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.aol.mobile.sdk.testapp.R;

public class TutorialFourActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_four);

        Button btnSDKInitializationErrors = findViewById(R.id.btn_sdk_initialization_errors);
        btnSDKInitializationErrors.setOnClickListener(this);
        Button btnPlayerInitializationErrors = findViewById(R.id.btn_player_initialization_errors);
        btnPlayerInitializationErrors.setOnClickListener(this);
        Button btnDeletedVideo = findViewById(R.id.btn_deleted_video);
        btnDeletedVideo.setOnClickListener(this);
        Button btnInvalidVideo = findViewById(R.id.btn_invalid_video);
        btnInvalidVideo.setOnClickListener(this);
        Button btnRestrictedVideo = findViewById(R.id.btn_restricted_video);
        btnRestrictedVideo.setOnClickListener(this);
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
            case R.id.btn_deleted_video:
                targetActivity = DeletedVideoActivity.class;
                break;
            case R.id.btn_invalid_video:
                targetActivity = InvalidVideoActivity.class;
                break;
            case R.id.btn_restricted_video:
                targetActivity = RestrictedVideoActivity.class;
                break;
            default:
                return;
        }

        Intent intent = new Intent(getApplicationContext(), targetActivity);
        startActivity(intent);
    }
}
