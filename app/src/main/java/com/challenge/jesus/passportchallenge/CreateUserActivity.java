package com.challenge.jesus.passportchallenge;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateUserActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView textview_profile_id, textview_profile_age, textview_profile_gender, textview_profile_name;
    CircleImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        textview_profile_name = findViewById(R.id.textview_profile_name);
        textview_profile_id = findViewById(R.id.textview_profile_id);
        textview_profile_age = findViewById(R.id.textview_profile_age);
        textview_profile_gender = findViewById(R.id.textview_profile_gender);
        profile_image = findViewById(R.id.profile_image);

        toolbar = findViewById(R.id.toolbar_frag);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //toolbar back button action
        backButtonPressed();

        textview_profile_name.setText(getIntent().getStringExtra("name"));
        textview_profile_id.setText(getIntent().getStringExtra("id"));
        textview_profile_age.setText(getIntent().getStringExtra("age"));
        textview_profile_gender.setText(getIntent().getStringExtra("gender"));
        profile_image.setImageBitmap(base64Decoder(getIntent().getStringExtra("image")));
    }

    //We will decode our image here. Could probably put this somewhere more convenient
    private Bitmap base64Decoder(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    //Toolbar back button back action
    public void backButtonPressed() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
