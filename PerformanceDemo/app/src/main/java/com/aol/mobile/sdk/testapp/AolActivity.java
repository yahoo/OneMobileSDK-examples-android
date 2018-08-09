package com.aol.mobile.sdk.testapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.aol.mobile.sdk.player.OneSDK;
import com.aol.mobile.sdk.player.OneSDKBuilder;

/**
 * Created by yumengtao on 2018/3/27.
 */
public class AolActivity extends AppCompatActivity {
    private ViewPager pager;
    private OneSDK oneSDK;
    private AolViewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_aol);
        super.onCreate(savedInstanceState);
        pager = findViewById(R.id.viewpager);
        pagerAdapter = new AolViewPagerAdapter(getSupportFragmentManager());

        new OneSDKBuilder(getApplicationContext())
                .create(new OneSDKBuilder.Callback() {
                    @Override
                    public void onSuccess(@NonNull OneSDK oneSDK) {
                        AolActivity.this.oneSDK = oneSDK;
                        initViews();
                    }

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AolActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }

    public OneSDK getOneSDK() {
        return oneSDK;
    }

    @Override
    protected void onPause() {
        super.onPause();
        pagerAdapter.setResumed(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pagerAdapter.setResumed(true);
    }

    private void initViews() {
        pagerAdapter.setFragments("Tab 1", "Tab 2", "Tab 3", "Tab 4", "Tab 5", "Tab 6");
        pager.setOffscreenPageLimit(1);
        pager.setAdapter(pagerAdapter);

        TabLayout mTabLayout = findViewById(R.id.tabLayout);
        for (int i = 0, count = pagerAdapter.getCount(); i < count; i++) {
            CharSequence title = pagerAdapter.getPageTitle(i);
            TabLayout.Tab top = mTabLayout.newTab().setText(title);
            mTabLayout.addTab(top);
        }

        mTabLayout.setupWithViewPager(pager);
    }
}
