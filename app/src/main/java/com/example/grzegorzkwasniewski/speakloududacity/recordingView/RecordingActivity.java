package com.example.grzegorzkwasniewski.speakloududacity.recordingView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.grzegorzkwasniewski.speakloududacity.R;

/**
 * Created by grzegorz.kwasniewski on 2018-07-17.
 */

public class RecordingActivity extends AppCompatActivity {

    // region Class Constants
    private static final String LOG_TAG = RecordingActivity.class.getSimpleName();
    // endregion

    // region View State
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new RecordingFragment())
                .commit();

    }
    // endregion
}
