package com.aol.mobile.sdk.testapp.tutorials.five;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.aol.mobile.sdk.testapp.R;

public class TutorialFiveActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_five);

        Button btnChromecastSupport = findViewById(R.id.btn_chromecast_support);
        btnChromecastSupport.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Class<? extends AppCompatActivity> targetActivity;
        switch (view.getId()) {
            case R.id.btn_chromecast_support:
                targetActivity = ChromecastSupportActivity.class;
                break;
            default:
                return;
        }

        Intent intent = new Intent(getApplicationContext(), targetActivity);
        startActivity(intent);
    }
}
