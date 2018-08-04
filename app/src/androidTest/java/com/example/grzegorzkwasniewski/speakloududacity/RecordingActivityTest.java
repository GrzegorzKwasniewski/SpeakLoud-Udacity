package com.example.grzegorzkwasniewski.speakloududacity;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.grzegorzkwasniewski.speakloududacity.recordingView.RecordingActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class RecordingActivityTest {

    @Rule public ActivityTestRule<RecordingActivity> recordingsActivityActivityTestRule = new ActivityTestRule<>(RecordingActivity.class);

    @Test
    public void clickRecordButton_StartRecording() {

        onView(withId(R.id.record_button)).perform(click());

        // check if drawable was changed to represent pause button
        onView(withId(R.id.record_button)).check(matches(CustomMatchers.withDrawable(R.drawable.ic_stop_white_48dp)));
    }
}
