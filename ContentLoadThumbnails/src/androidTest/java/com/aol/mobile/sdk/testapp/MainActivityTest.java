package com.aol.mobile.sdk.testapp;

import android.os.SystemClock;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.aol.mobile.sdk.player.model.properties.Properties;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import tools.fastlane.screengrab.Screengrab;
import tools.fastlane.screengrab.UiAutomatorScreenshotStrategy;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @BeforeClass
    public static void beforeAll() {
        Screengrab.setDefaultScreenshotStrategy(new UiAutomatorScreenshotStrategy());
    }

    @Test
    public void makeContentScreenshotPortrait() {
        boolean done = false;

        do {
            if (MainActivity.getPlayerFragment().getBinder().getPlayer() != null) {
                Properties properties = MainActivity.getPlayerFragment().getBinder().getPlayer().getProperties();
                if (properties.viewState == Properties.ViewState.Content && properties.playlistItem.video != null && properties.playlistItem.video.time != null) {
                    if (properties.playlistItem.video.canBePlayed && !properties.playlistItem.video.isPlaying) {
                        SystemClock.sleep(3000);
                        Espresso.onView(ViewMatchers.withId(R.id.thumbnail)).perform(ViewActions.swipeUp());
                        Screengrab.screenshot("contentLoadThumbnailsContentPortrait");
                        done = true;
                    }
                }
            }
        } while (!done);
    }
}
