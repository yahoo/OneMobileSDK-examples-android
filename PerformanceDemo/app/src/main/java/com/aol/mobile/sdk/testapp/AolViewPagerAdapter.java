package com.aol.mobile.sdk.testapp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AolViewPagerAdapter extends FragmentStatePagerAdapter {
    private final List<String> mFragmentsTitles = new ArrayList<>();
    @Nullable
    private AolFragment primaryElement;
    private boolean isResumed;

    public AolViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public void addFragment(@NonNull String fragmentTitle) {
        mFragmentsTitles.add(fragmentTitle);
    }

    public void setFragments(@NonNull String... fragmentTitles) {
        mFragmentsTitles.clear();
        mFragmentsTitles.addAll(Arrays.asList(fragmentTitles));
    }

    @Override
    public Fragment getItem(int position) {
        return new AolFragment();
    }

    @Override
    public int getCount() {
        return mFragmentsTitles.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);

        AolFragment aolFragment = (AolFragment) object;
        if (aolFragment != primaryElement) {
            primaryElement = aolFragment;
            if (primaryElement != null) {
                primaryElement.setResumed(this.isResumed);
            }
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentsTitles.get(position);
    }

    public void setResumed(boolean isResumed) {
        this.isResumed = isResumed;
        if (primaryElement != null) {
            primaryElement.setResumed(this.isResumed);
        }
    }
}
