package com.example.grzegorzkwasniewski.speakloududacity.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.grzegorzkwasniewski.speakloududacity.model.RecordItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by grzegorz.kwasniewski on 2018-07-19.
 */

public class RecordDBHelper extends SQLiteOpenHelper {

    //region Class Constants
    private static final String LOG_TAG = "RecordsDBHelper";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "records.db";
    //endregion

    //region Class Constructors
    public RecordDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //endregion

    //region Public Methods

    public static abstract class RecordsDB implements BaseColumns {
        public static final String TABLE_NAME = "records";
        public static final String COLUMN_NAME_RECORD_NAME = "recording_name";
        public static final String COLUMN_NAME_RECORD_FILE_PATH = "file_path";
        public static final String COLUMN_NAME_RECORD_LENGTH = "length";
        public static final String COLUMN_NAME_RECORD_TIME_ADDED = "time_added";
    }

    // strings literals that will support working with database
    private static final String COLUMN_TYPE = " TEXT";
    private static final String SEPARATION = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + RecordsDB.TABLE_NAME + " (" +
                    RecordsDB._ID + " INTEGER PRIMARY KEY" + SEPARATION +
                    RecordsDB.COLUMN_NAME_RECORD_NAME + COLUMN_TYPE + SEPARATION +
                    RecordsDB.COLUMN_NAME_RECORD_FILE_PATH + COLUMN_TYPE + SEPARATION +
                    RecordsDB.COLUMN_NAME_RECORD_LENGTH + " INTEGER " + SEPARATION +
                    RecordsDB.COLUMN_NAME_RECORD_TIME_ADDED + " INTEGER " + ")";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + RecordsDB.TABLE_NAME;

    // functions responsible for working with database
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public long addRecording(String recordingName, String filePath, long length) {

        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(RecordsDB.COLUMN_NAME_RECORD_NAME, recordingName);
        contentValues.put(RecordsDB.COLUMN_NAME_RECORD_FILE_PATH, filePath);
        contentValues.put(RecordsDB.COLUMN_NAME_RECORD_LENGTH, length);
        contentValues.put(RecordsDB.COLUMN_NAME_RECORD_TIME_ADDED, System.currentTimeMillis());
        long rowId = database.insert(RecordsDB.TABLE_NAME, null, contentValues);

        return rowId;
    }

    public RecordItem getItemAt(int position) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                RecordsDB._ID,
                RecordsDB.COLUMN_NAME_RECORD_NAME,
                RecordsDB.COLUMN_NAME_RECORD_FILE_PATH,
                RecordsDB.COLUMN_NAME_RECORD_LENGTH,
                RecordsDB.COLUMN_NAME_RECORD_TIME_ADDED
        };
        Cursor cursor = db.query(RecordsDB.TABLE_NAME, projection, null, null, null, null, null);
        if (cursor.moveToPosition(position)) {
            RecordItem item = new RecordItem();
            item.setDatabaseID(cursor.getInt(cursor.getColumnIndex(RecordsDB._ID)));
            item.setFileName(cursor.getString(cursor.getColumnIndex(RecordsDB.COLUMN_NAME_RECORD_NAME)));
            item.setFilePath(cursor.getString(cursor.getColumnIndex(RecordsDB.COLUMN_NAME_RECORD_FILE_PATH)));
            item.setRecordLength(cursor.getInt(cursor.getColumnIndex(RecordsDB.COLUMN_NAME_RECORD_LENGTH)));
            item.setRecordTime(cursor.getLong(cursor.getColumnIndex(RecordsDB.COLUMN_NAME_RECORD_TIME_ADDED)));
            cursor.close();
            return item;
        }
        return null;
    }

    public List<RecordItem> getAllRecords() {

        List recordsItems = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                RecordsDB._ID,
                RecordsDB.COLUMN_NAME_RECORD_NAME,
                RecordsDB.COLUMN_NAME_RECORD_FILE_PATH,
                RecordsDB.COLUMN_NAME_RECORD_LENGTH,
                RecordsDB.COLUMN_NAME_RECORD_TIME_ADDED
        };
        Cursor cursor = db.query(RecordsDB.TABLE_NAME, projection, null, null, null, null, null);
        while (cursor.moveToNext()) {
            RecordItem item = new RecordItem();
            item.setDatabaseID(cursor.getInt(cursor.getColumnIndex(RecordsDB._ID)));
            item.setFileName(cursor.getString(cursor.getColumnIndex(RecordsDB.COLUMN_NAME_RECORD_NAME)));
            item.setFilePath(cursor.getString(cursor.getColumnIndex(RecordsDB.COLUMN_NAME_RECORD_FILE_PATH)));
            item.setRecordLength(cursor.getInt(cursor.getColumnIndex(RecordsDB.COLUMN_NAME_RECORD_LENGTH)));
            item.setRecordTime(cursor.getLong(cursor.getColumnIndex(RecordsDB.COLUMN_NAME_RECORD_TIME_ADDED)));

            recordsItems.add(item);
        }

        cursor.close();

        return recordsItems;
    }


    public void removeItemWithId(int id) {
        SQLiteDatabase db = getWritableDatabase();

        // selection clause
        String selection = "_ID=?";

        // selection arguments
        String[] selectionArgs  = { String.valueOf(id) };

        // issue SQL statement
        db.delete(RecordsDB.TABLE_NAME, selection, selectionArgs );
    }

    public void renameRecordItem(RecordItem item, String recordingName, String filePath) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(RecordsDB.COLUMN_NAME_RECORD_NAME, recordingName);
        contentValues.put(RecordsDB.COLUMN_NAME_RECORD_FILE_PATH, filePath);
        db.update(RecordsDB.TABLE_NAME, contentValues,
                RecordsDB._ID + "=" + item.getmDatabaseID(), null);
    }

    public int countRecords() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = { RecordsDB._ID };
        Cursor cursor = db.query(RecordsDB.TABLE_NAME, projection, null, null, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    //endregion
}
