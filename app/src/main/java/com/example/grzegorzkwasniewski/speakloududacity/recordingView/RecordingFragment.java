package com.example.grzegorzkwasniewski.speakloududacity.recordingView;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Chronometer;

import com.example.grzegorzkwasniewski.speakloududacity.R;
import com.example.grzegorzkwasniewski.speakloududacity.audioFilesView.PermissionDialogFragment;
import com.example.grzegorzkwasniewski.speakloududacity.popUp.ConfirmationDialog;
import com.example.grzegorzkwasniewski.speakloududacity.services.RecordService;
import com.example.grzegorzkwasniewski.speakloududacity.services.SPHelper;

import java.io.File;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by grzegorz.kwasniewski on 2018-07-17.
 */

public class RecordingFragment extends Fragment implements RecordService.ServiceCallbacks {

    //region Class Constants
    private static final String LOG_TAG = RecordingFragment.class.getSimpleName();
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    //endregion

    //region Class Fields
    private int recordAudioPermissonGranted;
    private int storagePermissonGranted;

    private RecordService mService;

    @BindView(R.id.chronometer)
    Chronometer mChronometer;
    @BindView(R.id.record_button)
    FloatingActionButton mRecordButton;
    @BindView(R.id.myCoordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;

    // Requesting permission to record audio and store files
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };

    private boolean mStartRecording = true;
    private boolean mPauseRecording = true;
    private boolean mBound = false;

    private String mFileName;
    //endregion

    //region Class Constructors
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Record_Fragment.
     */
    public static RecordingFragment newInstance(int position) {
        RecordingFragment recordingFragment = new RecordingFragment();
        Bundle bundle = new Bundle();
        recordingFragment.setArguments(bundle);

        return recordingFragment;
    }

    public RecordingFragment() {
    }
    //endregion


    //region View State
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recordAudioPermissonGranted = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.RECORD_AUDIO);
        storagePermissonGranted = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        ActivityCompat.requestPermissions(getActivity(), permissions, 0);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View recordingView = inflater.inflate(R.layout.fragment_recording, container, false);

        ButterKnife.bind(this, recordingView);

        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (recordAudioPermissonGranted == PackageManager.PERMISSION_GRANTED && storagePermissonGranted == PackageManager.PERMISSION_GRANTED) {
                    onRecord();
                } else {
                    try {
                        PermissionDialogFragment playbackFragment =
                                new PermissionDialogFragment();

                        FragmentTransaction transaction = ((FragmentActivity) getContext())
                                .getSupportFragmentManager()
                                .beginTransaction();

                        playbackFragment.show(transaction, "permission_dialog");
                    } catch (Exception e) {
                        Log.e(LOG_TAG, "Problems with fragment on file card view", e);
                    }
                }

            }
        });

        if (SPHelper.getRecordingState(getContext()) == false) {
            mRecordButton.setImageResource(R.drawable.ic_stop_white_48dp);

            long time = System.currentTimeMillis() - SPHelper.getStartTime(getContext());

            mChronometer.setBase(SystemClock.elapsedRealtime() - time);

            mChronometer.start();

        }

        return recordingView;
    }

    //endregion

    //region Private Methods
    //TODO: recording pause
    private void onRecord(){

        Intent intent = new Intent(getActivity(), RecordService.class);

        mStartRecording = SPHelper.getRecordingState(getContext());

        if (SPHelper.getRecordingState(getContext())) {

            mStartRecording = false;
            SPHelper.setRecordingState(getContext(), false);

            mRecordButton.setImageResource(R.drawable.ic_stop_white_48dp);

            Snackbar mySnackbar = Snackbar.make(mCoordinatorLayout, R.string.recording_starts, Snackbar.LENGTH_SHORT);
            mySnackbar.show();

            File folder = new File(Environment.getExternalStorageDirectory() + "/SpeakLoud");

            if (!folder.exists()) {
                folder.mkdir();
            }

            //start Chronometer
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.start();

            //mStartingTimeMilliseconds = System.currentTimeMillis();
            SPHelper.setStartTime(getContext(), System.currentTimeMillis());

            //start RecordingService
            getContext().startService(intent);
            //keep screen on while recording
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        } else {

            mStartRecording = true;
            SPHelper.setRecordingState(getContext(), true);

            //stop recording
            mRecordButton.setImageResource(R.drawable.ic_mic_white_48dp);

            mChronometer.stop();
            mChronometer.setBase(SystemClock.elapsedRealtime());

            SPHelper.setStartTime(getContext(), 0l);

            getContext().stopService(intent);

            showConfirmationDialog();

            //allow the screen to turn off again once recording is finished
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mChronometer.stop();
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {

            RecordService.LocalBinder binder = (RecordService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            mService.setCallbacks(RecordingFragment.this);
            mService.startRecording();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            mService.stopRecording();
        }
    };
    //endregion

    //region Public Methods
    @Override
    public void showConfirmationDialog() {
        try {
            ConfirmationDialog confirmationDialog =
                    new ConfirmationDialog();

            FragmentTransaction transaction = ((FragmentActivity) getContext())
                    .getSupportFragmentManager()
                    .beginTransaction();

            confirmationDialog.show(transaction, "permission_dialog");
        } catch (Exception e) {
            Log.e(LOG_TAG, "Problems with fragment on file card view", e);
        }
    }
    //endregion
}
