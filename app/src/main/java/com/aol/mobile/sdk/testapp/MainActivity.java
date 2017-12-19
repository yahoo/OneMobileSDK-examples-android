package com.aol.mobile.sdk.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.aol.mobile.sdk.testapp.tutorials.one.TutorialOneActivity;
import com.aol.mobile.sdk.testapp.tutorials.two.TutorialTwoActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnTutorialOne = findViewById(R.id.btn_tutorial_one);
        btnTutorialOne.setOnClickListener(this);
        Button btnTutorialTwo = findViewById(R.id.btn_tutorial_two);
        btnTutorialTwo.setOnClickListener(this);
        Button btnTutorialThree = findViewById(R.id.btn_tutorial_three);
        btnTutorialThree.setOnClickListener(this);
        Button btnTutorialFour = findViewById(R.id.btn_tutorial_four);
        btnTutorialFour.setOnClickListener(this);
        Button btnTutorialFive = findViewById(R.id.btn_tutorial_five);
        btnTutorialFive.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Class<? extends AppCompatActivity> targetActivity;

        switch (view.getId()) {
            case R.id.btn_tutorial_one:
                targetActivity = TutorialOneActivity.class;
                break;

            case R.id.btn_tutorial_two:
                targetActivity = TutorialTwoActivity.class;
                break;

            default:
                return;
        }

        Intent intent = new Intent(getApplicationContext(), targetActivity);
        startActivity(intent);
    }
}
