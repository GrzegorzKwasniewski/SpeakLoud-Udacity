package com.example.grzegorzkwasniewski.speakloududacity.settingsView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.grzegorzkwasniewski.speakloududacity.R;

/**
 * Created by grzegorz.kwasniewski on 2018-07-18.
 */

public class SettingsActivity extends AppCompatActivity {

    // region Class Constants
    public static String LOG_TAG = SettingsActivity.class.getSimpleName();
    // endregion

    // region View State
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new PreferencesFragmnet())
                .commit();
    }
    // endregion
}
