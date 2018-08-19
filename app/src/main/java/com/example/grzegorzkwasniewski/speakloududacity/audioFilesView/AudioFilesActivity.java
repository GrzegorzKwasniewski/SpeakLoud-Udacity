package com.example.grzegorzkwasniewski.speakloududacity.audioFilesView;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.database.Cursor;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.grzegorzkwasniewski.speakloududacity.DatabaseContent.DBContract;
import com.example.grzegorzkwasniewski.speakloududacity.DatabaseContent.AudioFilesAsyncLoader;
import com.example.grzegorzkwasniewski.speakloududacity.R;
import com.example.grzegorzkwasniewski.speakloududacity.adapter.RecordsAdapter;
import com.example.grzegorzkwasniewski.speakloududacity.database.RecordDBHelper;
import com.example.grzegorzkwasniewski.speakloududacity.model.RecordItem;
import com.example.grzegorzkwasniewski.speakloududacity.widget.Widget;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AudioFilesActivity extends AppCompatActivity {

    // region Class Constants
    private static final String LOG_TAG = "FileViewerFragment";
    private static final Comparator<RecordItem> ALPHABETICAL_COMPARATOR = new Comparator<RecordItem>() {
        @Override
        public int compare(RecordItem a, RecordItem b) {
            return a.getFileName().compareTo(b.getFileName());
        }
    };
    // endregion

    // region Class Fields

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.rootLayout)
    ConstraintLayout mRootLayout;

    private RecordsAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private List<RecordItem> mModels;
    // endregion

    // region View State
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_files);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List recordsItems = new ArrayList<>();

        String[] projection = {
                DBContract.Entry._ID,
                DBContract.Entry.COLUMN_TITLE,
                DBContract.Entry.COLUMN_PATH,
                DBContract.Entry.COLUMN_LENGHT,
                DBContract.Entry.COLUMN_TIME_ADDED
        };

        AudioFilesAsyncLoader loader = new AudioFilesAsyncLoader(
                this,
                DBContract.Entry.CONTENT_URI,
                projection,
                null,
                null);

        Cursor cursor = loader.loadInBackground();

        while (cursor.moveToNext()) {
            RecordItem item = new RecordItem();
            item.setDatabaseID(cursor.getInt(cursor.getColumnIndex(DBContract.Entry._ID)));
            item.setFileName(cursor.getString(cursor.getColumnIndex(DBContract.Entry.COLUMN_TITLE)));
            item.setFilePath(cursor.getString(cursor.getColumnIndex(DBContract.Entry.COLUMN_PATH)));
            item.setRecordLength(cursor.getInt(cursor.getColumnIndex(DBContract.Entry.COLUMN_LENGHT)));
            item.setRecordTime(cursor.getLong(cursor.getColumnIndex(DBContract.Entry.COLUMN_TIME_ADDED)));

            recordsItems.add(item);
        }

        mModels = recordsItems;

        cursor.close();

        List<String> recordingsNames = new ArrayList<>();

        for (RecordItem record: mModels) {
            //Log.d("fsdfdsfsdf", "onCreate:" + record.getFileName());
            recordingsNames.add(record.getFileName());
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        int[] widgetIDs = appWidgetManager.getAppWidgetIds(new ComponentName(getApplicationContext(), Widget.class));

        Widget.recordingsName = recordingsNames;

        Widget.updateWidget(getApplicationContext(), appWidgetManager, widgetIDs);


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // specify an adapter
        mAdapter = new RecordsAdapter(this, mModels, mLayoutManager, mRootLayout ,ALPHABETICAL_COMPARATOR);
        mRecyclerView.setAdapter(mAdapter);
    }
    // endregion
}
