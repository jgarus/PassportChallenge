package com.challenge.jesus.passportchallenge;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class DisplayUserProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView textview_profile_id, textview_profile_age, textview_profile_gender, textview_profile_name;
    CircleImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_user_profile);

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

        getSupportActionBar().setTitle(getIntent().getStringExtra("name"));
        //textview_profile_name.setText(getIntent().getStringExtra("name"));
        textview_profile_id.setText(getIntent().getStringExtra("id"));
        textview_profile_age.setText(getIntent().getStringExtra("age"));
        textview_profile_gender.setText(getIntent().getStringExtra("gender"));
        profile_image.setImageBitmap(base64Decoder(getIntent().getStringExtra("image")));
    }

    //We will decode our image here
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                editUser();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public void editUser() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, new AddUserFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
