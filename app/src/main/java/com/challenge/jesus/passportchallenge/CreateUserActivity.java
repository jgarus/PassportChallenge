package com.challenge.jesus.passportchallenge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class CreateUserActivity extends AppCompatActivity {

    TextView textview_profile_id, textview_profile_age, textview_profile_gender, textview_profile_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_frag);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        textview_profile_name = (TextView) findViewById(R.id.textview_profile_name);
        textview_profile_id = (TextView) findViewById(R.id.textview_profile_id);
        textview_profile_age = (TextView) findViewById(R.id.textview_profile_age);
        textview_profile_gender = (TextView) findViewById(R.id.textview_profile_gender);

        textview_profile_name.setText(getIntent().getStringExtra("name"));
        textview_profile_id.setText(getIntent().getStringExtra("id"));
        textview_profile_age.setText(getIntent().getStringExtra("age"));
        textview_profile_gender.setText(getIntent().getStringExtra("gender"));

    }
}
