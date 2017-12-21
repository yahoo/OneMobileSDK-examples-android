package com.aol.mobile.sdk.testapp.tutorials.two;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.aol.mobile.sdk.testapp.R;

public class TutorialTwoActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_two);

        Button btnHiddenButtonsControls = findViewById(R.id.btn_hidden_buttons_controls);
        btnHiddenButtonsControls.setOnClickListener(this);
        Button btnCcSapSettings = findViewById(R.id.btn_cc_sap_settings);
        btnCcSapSettings.setOnClickListener(this);
        Button btnSidebarButtons = findViewById(R.id.btn_sidebar_buttons);
        btnSidebarButtons.setOnClickListener(this);
        Button btnSidebarVoulme = findViewById(R.id.btn_sidebar_volume);
        btnSidebarVoulme.setOnClickListener(this);
        Button btnSidebarFullscreen = findViewById(R.id.btn_sidebar_fullscreen);
        btnSidebarFullscreen.setOnClickListener(this);
        Button btnLiveIndicatorTint = findViewById(R.id.btn_live_indicator_tint);
        btnLiveIndicatorTint.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Class<? extends AppCompatActivity> targetActivity;
        switch (view.getId()) {
            case R.id.btn_hidden_buttons_controls:
                targetActivity = HiddenButtonsControlsActivity.class;
                break;
            case R.id.btn_cc_sap_settings:
                targetActivity = ModifiedCcSapActivity.class;
                break;
            case R.id.btn_sidebar_buttons:
                targetActivity = SidebarButtonsActivity.class;
                break;
            case R.id.btn_sidebar_volume:
                targetActivity = SidebarVolumeActivity.class;
                break;
            case R.id.btn_sidebar_fullscreen:
                targetActivity = SidebarFullscreenActivity.class;
                break;
            case R.id.btn_live_indicator_tint:
                targetActivity = LiveIndicatorTintActivity.class;
                break;
            default:
                return;
        }

        Intent intent = new Intent(getApplicationContext(), targetActivity);
        startActivity(intent);
    }
}
