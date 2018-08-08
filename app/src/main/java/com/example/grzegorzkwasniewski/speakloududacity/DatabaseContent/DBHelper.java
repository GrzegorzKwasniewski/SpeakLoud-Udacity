package com.example.grzegorzkwasniewski.speakloududacity.DatabaseContent;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "local.db";

    private static final int VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createFavoriteTable = "CREATE TABLE " + DBContract.Entry.TABLE_NAME + " (" +
                DBContract.Entry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DBContract.Entry.COLUMN_PATH + " TEXT," +
                DBContract.Entry.COLUMN_TITLE + " TEXT," +
                DBContract.Entry.COLUMN_LENGHT + " TEXT," +
                DBContract.Entry.COLUMN_TIME_ADDED + " TEXT" +
                ");";

        sqLiteDatabase.execSQL(createFavoriteTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
