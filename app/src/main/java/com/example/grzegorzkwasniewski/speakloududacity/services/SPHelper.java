package com.example.grzegorzkwasniewski.speakloududacity.services;

import android.app.Activity;
import android.content.Context;

public class SPHelper {

    private static final String SP_NAME = "com.udacity.speakloud";
    private static final String RECORDING_AUDIO = "recordingAudio";
    private static final String RECORDING_TIME = "recordingTime";
    private static final String RECORDING_START_TIME = "recordingStartTime";


    public static void setRecordingState(Context context, Boolean recordingState) {
        if (context != null) {
            context.getSharedPreferences(SP_NAME, Activity.MODE_PRIVATE).edit().putBoolean(RECORDING_AUDIO, recordingState).apply();
        }
    }

    public static Boolean getRecordingState(Context context) {
        if (context != null) {
            return context.getSharedPreferences(SP_NAME, Activity.MODE_PRIVATE).getBoolean(RECORDING_AUDIO, false);
        }

        return false;
    }

    public static void setTime(Context context, Long time) {
        if (context != null) {
            context.getSharedPreferences(SP_NAME, Activity.MODE_PRIVATE).edit().putLong(RECORDING_TIME, time).apply();
        }
    }

    public static Long getTime(Context context) {
        if (context != null) {
            return context.getSharedPreferences(SP_NAME, Activity.MODE_PRIVATE).getLong(RECORDING_TIME, 0);
        }

        return 0l;
    }

    public static void setStartTime(Context context, Long time) {
        if (context != null) {
            context.getSharedPreferences(SP_NAME, Activity.MODE_PRIVATE).edit().putLong(RECORDING_START_TIME, time).apply();
        }
    }

    public static Long getStartTime(Context context) {
        if (context != null) {
            return context.getSharedPreferences(SP_NAME, Activity.MODE_PRIVATE).getLong(RECORDING_START_TIME, 0);
        }

        return 0l;
    }

}
