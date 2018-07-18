package com.example.grzegorzkwasniewski.speakloududacity.settingsView;

/**
 * Created by grzegorz.kwasniewski on 2018-07-18.
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;

import com.example.grzegorzkwasniewski.speakloududacity.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreferencesFragmenet extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    // region Class Fields

    /**
     * List for selecting app theme
     */
    private ListPreference mAppTheme;

    //endregion

    //region Constructors
    public PreferencesFragmenet() {
    }
    //endregion

    //region View State
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        Preference aboutPref = findPreference(getString(R.string.pref_about_key));
        aboutPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AboutDialogFragment licensesFragment = AboutDialogFragment.newInstance(1);
                licensesFragment.show(((SettingsActivity)getActivity()).getSupportFragmentManager().beginTransaction(), "dialog");
                return true;
            }
        });
    }
    //endregion

    //region Public Methods
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
    }
    //endregion
}
