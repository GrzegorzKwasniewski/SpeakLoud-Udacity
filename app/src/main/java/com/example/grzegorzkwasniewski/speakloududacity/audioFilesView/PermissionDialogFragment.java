package com.example.grzegorzkwasniewski.speakloududacity.audioFilesView;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.grzegorzkwasniewski.speakloududacity.R;

/**
 * Created by grzegorz.kwasniewski on 2018-07-23.
 */

public class PermissionDialogFragment extends DialogFragment {

    //  region Constructors
    public PermissionDialogFragment() {
        // Required empty public constructor
    }
    // endregion

    // region View State
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_permission_dialog, container, false);
    }
    // endregion
}