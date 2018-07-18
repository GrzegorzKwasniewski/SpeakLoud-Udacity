package com.example.grzegorzkwasniewski.speakloududacity.settingsView;

/**
 * Created by grzegorz.kwasniewski on 2018-07-18.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import com.example.grzegorzkwasniewski.speakloududacity.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutDialogFragment extends DialogFragment {

    // region Constructors
    public AboutDialogFragment() {
        // Required empty public constructor
    }

    // static method for creating instance
    static AboutDialogFragment newInstance(int styleNumber) {

        AboutDialogFragment aboutFragment = new AboutDialogFragment();

        // styleNumer as an argument
        Bundle arguments = new Bundle();
        arguments.putInt("styleNumber", styleNumber);

        aboutFragment.setArguments(arguments);

        return aboutFragment;
    }
    // endregion

    // region View State
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater dialogInflater = getActivity().getLayoutInflater();
        View openSourceLicensesView = dialogInflater.inflate(R.layout.fragment_about_dialog, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(openSourceLicensesView)
                .setTitle((getString(R.string.about_alert_title)))
                .setMessage(R.string.about_alert_message)
                .setNeutralButton(android.R.string.ok, null);

        return dialogBuilder.create();
    }
    // endregion
}

