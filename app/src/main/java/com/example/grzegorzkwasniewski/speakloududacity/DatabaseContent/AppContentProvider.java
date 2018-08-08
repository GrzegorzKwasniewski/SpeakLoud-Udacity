package com.example.grzegorzkwasniewski.speakloududacity.DatabaseContent;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class AppContentProvider extends ContentProvider {

    public static final int RECORDS = 10;
    public static final int RECORDS_ID = 100;

    DatabaseComponent component;
    DBHelper dbHelper;
    private UriMatcher uriMatcher;

    public static UriMatcher buildMatcher(DatabaseComponent component) {
        UriMatcher uriMatcher = component.getUriMatcher();

        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.PATH, RECORDS);
        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.PATH + "/#", RECORDS_ID);

        return uriMatcher;

    }

    @Override
    public boolean onCreate() {
        component = DaggerDatabaseComponent.builder()
                .contextModule(new ContextModule(getContext()))
                .build();

        dbHelper = component.getDBHelper();
        uriMatcher = buildMatcher(component);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();

        int uriResult = uriMatcher.match(uri);

        Cursor cursor;

        switch (uriResult) {
            case RECORDS:
                cursor = db.query(DBContract.Entry.TABLE_NAME,
                        strings,
                        s,
                        strings1,
                        null,
                        null,
                        s1);
                break;
            case RECORDS_ID:
                String id = uri.getPathSegments().get(1);
                String selection = "_id = ?";
                String[] selectionArgs = new String[]{id};

                cursor = db.query(DBContract.Entry.TABLE_NAME,
                        strings,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        s1);
                break;
            default:
                return null;
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        int uriResult = uriMatcher.match(uri);

        Uri returnedUri;

        switch (uriResult) {
            case RECORDS:
                long id = db.insert(DBContract.Entry.TABLE_NAME, null, contentValues);

                if (id > 0) {
                    returnedUri = ContentUris.withAppendedId(DBContract.Entry.CONTENT_URI, id);

                } else {
                    return null;
                }

                break;
            default:
                return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnedUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int val = db.delete(
                DBContract.Entry.TABLE_NAME,
                s,
                strings
        );

        getContext().getContentResolver().notifyChange(uri, null);

        return val;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
