package com.example.grzegorzkwasniewski.speakloududacity.DatabaseContent;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

public class AudioFilesAsyncLoader extends AsyncTaskLoader<Cursor> {

    private String selection;
    private String[] selectionArgs;
    private String[] projection;
    private Uri uri;
    private Cursor cursor;

    public AudioFilesAsyncLoader(Context context, @Nullable Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs) {
        super(context);
        this.selection = selection;
        this.selectionArgs = selectionArgs;
        this.projection = projection;
        this.uri = uri;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        if (cursor != null) {
            deliverResult(cursor);
        } else {
            forceLoad();
        }
    }

    @Override
    public Cursor loadInBackground() {
        try {
            return getContext().getContentResolver().query(uri,
                    projection,
                    selection,
                    selectionArgs,
                    DBContract.Entry._ID);
        } catch (Exception e) {
            return null;
        }
    }

    public void deliverResult(Cursor cursor) {
        this.cursor = cursor;
        super.deliverResult(cursor);
    }
}
