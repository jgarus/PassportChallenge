package com.challenge.jesus.passportchallenge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by jesus on 9/18/17.
 * Fragment will be used to create the new user
 */

public class OldCreateUserFragment extends Fragment {

    User user;
    TextView textView_profile_age;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout._old_fragment_create_user, container, false);
        textView_profile_age = (TextView) getActivity().findViewById(R.id.textview_profile_age);
        Log.v("v", "ACTIVITY: " + getActivity().getLocalClassName());
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        Log.v("v", "JOES: " + args);
        textView_profile_age.setText(user.getName());

        AppCompatActivity activity = (AppCompatActivity)getActivity();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_frag);
        activity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

    }
}
