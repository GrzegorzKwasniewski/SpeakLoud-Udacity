package com.example.grzegorzkwasniewski.speakloududacity;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.grzegorzkwasniewski.speakloududacity.mainView.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickRecordButton_ShowRecordingScreen() {

        // find button with given id
        onView(withId(R.id.recordingViewButton)).perform(click());

        // find button with given id
        onView(withId(R.id.record_button)).check(matches(isDisplayed()));
    }

    @Test
    public void clickSettingsButton_ShowSettingsScreen() {

        // find button with given id
        onView(withId(R.id.settingsViewButton)).perform(click());

        // if layout with given id is displayed on screen, transition to new screen was successful
        onView(withId(R.id.activity_settings)).check(matches(isDisplayed()));
    }
}
