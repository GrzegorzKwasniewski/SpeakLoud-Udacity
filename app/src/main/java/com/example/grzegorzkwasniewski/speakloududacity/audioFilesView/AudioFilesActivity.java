package com.example.grzegorzkwasniewski.speakloududacity.audioFilesView;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.grzegorzkwasniewski.speakloududacity.R;
import com.example.grzegorzkwasniewski.speakloududacity.adapter.RecordsAdapter;
import com.example.grzegorzkwasniewski.speakloududacity.database.RecordDBHelper;
import com.example.grzegorzkwasniewski.speakloududacity.model.RecordItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by grzegorz.kwasniewski on 2018-07-19.
 */

public class AudioFilesActivity extends AppCompatActivity {

    // dokończ ten widok i dodaj PlayAudioFragment

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
    private RecyclerView mRecyclerView;
    private RecordsAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ConstraintLayout mRootLayout;

    private RecordDBHelper mDatabase;
    private List<RecordItem> mModels;
    // endregion

    // region View State
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_files);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase = new RecordDBHelper(this);
        mModels = mDatabase.getAllRecords();

        mRootLayout = (ConstraintLayout) findViewById(R.id.rootLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // specify an adapter
        mAdapter = new RecordsAdapter(this, mLayoutManager, mRootLayout ,ALPHABETICAL_COMPARATOR);
        mRecyclerView.setAdapter(mAdapter);
    }
    // endregion
}
