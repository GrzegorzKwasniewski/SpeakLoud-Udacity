package com.example.grzegorzkwasniewski.speakloududacity.DatabaseContent;

import android.net.Uri;
import android.provider.BaseColumns;


public class DBContract {

    public static final String AUTHORITY = "com.example.grzegorzkwasniewski.speakloududacity.DatabaseContent.AppContentProvider";

    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH = "records";

    public static final class Entry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(PATH).build();

        public static final String _ID = "_id";
        public static final String TABLE_NAME = "records";
        public static final String COLUMN_PATH = "file_path";
        public static final String COLUMN_TITLE = "recording_name";
        public static final String COLUMN_LENGHT = "length";
        public static final String COLUMN_TIME_ADDED = "time_added";
    }
}
