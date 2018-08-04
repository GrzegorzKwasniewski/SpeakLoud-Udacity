package com.example.grzegorzkwasniewski.speakloududacity.services;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.grzegorzkwasniewski.speakloududacity.R;
import com.example.grzegorzkwasniewski.speakloududacity.database.RecordDBHelper;
import com.example.grzegorzkwasniewski.speakloududacity.widget.Widget;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by grzegorz.kwasniewski on 2018-07-23.
 */

public class RecordService extends Service {

    //region Class Constants
    private static final String LOG_TAG = "RecordService";
    //endregion

    //region Class Fields

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    // Registered callbacks
    private ServiceCallbacks serviceCallbacks;

    private String mFileName = null;
    private String mFilePath = null;
    private MediaRecorder mRecorder = null;
    private RecordDBHelper mDatabase;

    private long mStartingTimeMilliseconds = 0;
    private long mElapsedMillis = 0;

    //endregion

    //region Public Methods

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public RecordService getService() {
            // Return this instance of RecordService so clients can call public methods
            return RecordService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDatabase = new RecordDBHelper(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startRecording();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        if (mRecorder != null) {
            stopRecording();
        }

        super.onDestroy();
    }

    public void startRecording() {

        createFileNameAndPath();

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setOutputFile(mFilePath);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mRecorder.setAudioChannels(1);

        try {
            mRecorder.prepare();
            mStartingTimeMilliseconds = System.currentTimeMillis();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Can't set media recorder");
        }

        mRecorder.start();
    }

    public void stopRecording() {
        mRecorder.stop();
        mElapsedMillis = (System.currentTimeMillis() - mStartingTimeMilliseconds);
        mRecorder.release();

        if (serviceCallbacks != null) {
            serviceCallbacks.showConfirmationDialog();
        }

        mRecorder = null;

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        int[] widgetIDs = appWidgetManager.getAppWidgetIds(new ComponentName(getApplicationContext(), Widget.class));

        Widget.recordingName = mFileName;

        Widget.updateWidget(getApplicationContext(), appWidgetManager, widgetIDs);

        try {
            mDatabase.addRecording(mFileName, mFilePath, mElapsedMillis);

        } catch (Exception e){
            Log.e(LOG_TAG, "Something went wrong", e);
        }
    }

    public void createFileNameAndPath(){
        int count = 0;
        File file;

        do{
            count++;

            mFileName = getString(R.string.file_name)
                    + "_" + (mDatabase.countRecords() + count) + ".mp4";
            mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            mFilePath += "/SpeakLoud/" + mFileName;

            file = new File(mFilePath);
        }while (file.exists() && !file.isDirectory());
    }

    // set callbacks methods for service
    public void setCallbacks(ServiceCallbacks callbacks) {
        serviceCallbacks = callbacks;
    }

    public interface ServiceCallbacks {
        void showConfirmationDialog();
    }

    //endregion
}