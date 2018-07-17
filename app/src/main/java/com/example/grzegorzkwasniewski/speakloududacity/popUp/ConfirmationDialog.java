package com.example.grzegorzkwasniewski.speakloududacity.popUp;

/**
 * Created by grzegorz.kwasniewski on 2018-07-17.
 */

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.grzegorzkwasniewski.speakloududacity.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmationDialog extends DialogFragment {

    private static final String LOG_TAG = ConfirmationDialog.class.getSimpleName();

    public ConfirmationDialog() {
        // Required empty public constructor
    }

    public static ConfirmationDialog newInstance(int position) {
        ConfirmationDialog confirmationDialog = new ConfirmationDialog();
        Bundle bundle = new Bundle();
        confirmationDialog.setArguments(bundle);

        return confirmationDialog;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View confirmationDialog = inflater.inflate(R.layout.fragment_confirmation, container, false);

        return confirmationDialog;
    }

}