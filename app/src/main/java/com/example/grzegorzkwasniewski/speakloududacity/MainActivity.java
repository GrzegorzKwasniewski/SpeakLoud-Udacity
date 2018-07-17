package com.example.grzegorzkwasniewski.speakloududacity;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

    private Context mContext;
    private Intent mIntent;

    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

    }
}
