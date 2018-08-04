package com.example.grzegorzkwasniewski.speakloududacity;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.grzegorzkwasniewski.speakloududacity.audioFilesView.AudioFilesActivity;
import com.example.grzegorzkwasniewski.speakloududacity.database.RecordDBHelper;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;


@RunWith(AndroidJUnit4.class)
public class AudioFilesActivityTest {

    private static Uri INTENT_DATA_SAVED_RECORD;

    private RecordDBHelper mDatabase;

    /**
     * A JUnit {@link Rule @Rule} to init and release Espresso Intents before and after each
     * test run.
     * <p>
     * Rules are interceptors which are executed for each test method and will run before
     * any of your setup code in the {@link Before @Before} method.
     * <p>
     * This rule is based on {@link ActivityTestRule} and will create and launch of the activity
     * for you and also expose the activity under test.
     */
    @Rule
    public IntentsTestRule<AudioFilesActivity> mActivityRule = new IntentsTestRule<>(
            AudioFilesActivity.class);

    @Before
    public void stubAllExternalIntents() {
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Before
    public void getDatabaseReference() {
        mDatabase = new RecordDBHelper(mActivityRule.getActivity().getApplicationContext());
        //INTENT_DATA_SAVED_RECORD = Uri.fromFile(new File(mDatabase.getItemAt(0).getFilePath()));
    }

    @Test
    public void clickSavedRecordings_ShowPlaybackPopUp() {

        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // if playback pop up is shown than transition was correct
        onView(withId(R.id.fragment_play_audio)).check(matches(isDisplayed()));
    }

    @Test
    public void clickSavedRecordings_ShowPlaybackPopUp_PlayAudio() {

        clickSavedRecordings_ShowPlaybackPopUp();

        onView(withId(R.id.play_button)).perform(click());

        // check if drawable was changed to represent pause button
        onView(withId(R.id.play_button)).check(matches(CustomMatchers.withDrawable(R.drawable.ic_pause_white_48dp)));
    }

    @Test
    public void clickBackButton_HidePlaybackPopUp() {

        clickSavedRecordings_ShowPlaybackPopUp();

        pressBack();

        onView(withId(R.id.fragment_play_audio)).check(doesNotExist());
    }

    @Test
    public void longPressSavedRecording_ShowOptionsDialog() {

        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()));

        // alert dialog have title set to "Options"
        // and this title will be used to confirm that alert is displayed on the screen
        // search is case sensitive
        onView(allOf(withText("Options"))).check(matches(isDisplayed()));
    }

    @Test
    public void pressCancelButton_DismissOptionsDialog() {

        longPressSavedRecording_ShowOptionsDialog();

        onView(withText("Cancel")).perform(click());

        // check if options dialog was dismissed
        onView(allOf(withText("Options"))).check(doesNotExist());
    }

    @Test
    public void selectShareFile_ShowShareDialog() {

        longPressSavedRecording_ShowOptionsDialog();

        // search for view with given text and click on it
        onView(allOf(withText("Share file"))).perform(click());

        // Verify that an intent to the dialer was sent with the correct action, phone
        // number and package. Think of Intents intended API as the equivalent to Mockito's verify.
        intended(allOf(
                hasAction(Intent.ACTION_CHOOSER)));

    }

    @Test
    public void selectRenameFile_ShowRenameDialog() {

        longPressSavedRecording_ShowOptionsDialog();

        // search for view with given text and click on it
        onView(allOf(withText("Rename file"))).perform(click());

        // check if rename file dialog was displayed
        onView(withId(R.id.rename_file_dialog)).check(matches(isDisplayed()));

        // check if options dialog was dismissed
        onView(allOf(withText("Options"))).check(doesNotExist());

    }

    @Test
    public void renameFile() {

        selectRenameFile_ShowRenameDialog();

        // enter new name for the file
        onView(withId(R.id.new_name)).perform(typeText("New file"));

        // search for confirmation button and click on it
        onView(withText("OK")).perform(click());

        // check if rename file dialog was hidden
        onView(withId(R.id.rename_file_dialog)).check(doesNotExist());

        // check if options dialog was hidden
        onView(allOf(withText("Options"))).check(doesNotExist());

    }

    @Test
    public void cancelRenameFile() {

        selectRenameFile_ShowRenameDialog();

        // search for confirmation button and click on it
        onView(withText("Cancel")).perform(click());

        // check if rename file dialog was hidden
        onView(withId(R.id.rename_file_dialog)).check(doesNotExist());

        // check if options dialog was hidden
        onView(allOf(withText("Options"))).check(doesNotExist());

    }

    @Test
    public void selectDeleteFile_ShowDeleteDialog() {

        longPressSavedRecording_ShowOptionsDialog();

        // search for view with given text and click on it
        onView(allOf(withText("Delete file"))).perform(click());

        // check if delete file dialog was displayed
        onView(withText("Delete saved recording")).check(matches(isDisplayed()));

    }

    @Test
    public void deleteFile() {

        selectDeleteFile_ShowDeleteDialog();

        // check if delete file dialog was displayed and click it
        onView(withText("OK to delete")).perform(click());

        // check if delete file dialog was hidden
        onView(withText("Delete file")).check(doesNotExist());

        // check if options dialog was hidden
        onView(allOf(withText("Options"))).check(doesNotExist());

        //TODO check if collection count is one less
    }

    @Test
    public void cancel_DeleteFile() {

        selectDeleteFile_ShowDeleteDialog();

        // check if delete file dialog was displayed and click it
        onView(withText("Don\'t delete")).perform(click());

        // check if delete file dialog was hidden
        onView(withText("Delete file")).check(doesNotExist());

        // check if options dialog was hidden
        onView(allOf(withText("Options"))).check(doesNotExist());
    }
}
