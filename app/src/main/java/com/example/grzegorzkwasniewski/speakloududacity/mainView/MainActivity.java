package com.example.grzegorzkwasniewski.speakloududacity.mainView;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.grzegorzkwasniewski.speakloududacity.R;
import com.example.grzegorzkwasniewski.speakloududacity.audioFilesView.AudioFilesActivity;
import com.example.grzegorzkwasniewski.speakloududacity.recordingView.RecordingActivity;
import com.example.grzegorzkwasniewski.speakloududacity.settingsView.SettingsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    //region Class Constants
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    // endregion

    // region Class Fields

    @BindView(R.id.constraintLayout)
    ConstraintLayout mConstraintLayout;
    @BindView(R.id.recordingViewButton)
    FloatingActionButton mRecordingViewButton;
    @BindView(R.id.recordsViewButton)
    FloatingActionButton mRecordsViewButton;
    @BindView(R.id.settingsViewButton)
    FloatingActionButton settingsViewButton;

    private Intent mIntent;

    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mRecordingViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(mRecordingViewButton.getContext(), RecordingActivity.class);
                startActivity(mIntent);
            }
        });

        mRecordsViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(settingsViewButton.getContext(), AudioFilesActivity.class);
                startActivity(mIntent);
            }
        });

        settingsViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(settingsViewButton.getContext(), SettingsActivity.class);
                startActivity(mIntent);
            }
        });
    }
}
