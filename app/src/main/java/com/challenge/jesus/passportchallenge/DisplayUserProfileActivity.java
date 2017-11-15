package com.challenge.jesus.passportchallenge;

import android.app.Fragment;
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

    final String EDIT_FRAGMENT = "Edit User";
    String name, id, age, gender, image;

    Toolbar toolbar;
    TextView textview_profile_id, textview_profile_age, textview_profile_gender;
    CircleImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_user_profile);

        setStrings();

        textview_profile_id = findViewById(R.id.textview_profile_id);
        textview_profile_age = findViewById(R.id.textview_profile_age);
        textview_profile_gender = findViewById(R.id.textview_profile_gender);
        profile_image = findViewById(R.id.profile_image);

        toolbar = findViewById(R.id.toolbar_frag);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle(name);
        textview_profile_id.setText(id);
        textview_profile_age.setText(age);
        textview_profile_gender.setText(gender);
        profile_image.setImageBitmap(base64Decoder(image));

        //call return action
        returnToList();
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
                editUserFragment();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }


    public void setStrings(){
        name = getIntent().getStringExtra("name");
        id = getIntent().getStringExtra("id");
        age = getIntent().getStringExtra("age");
        gender = getIntent().getStringExtra("gender");
        image = getIntent().getStringExtra("image");
    }

    //We will decode our image here
    private Bitmap base64Decoder(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    //go back to list of users from toolbar back button
    public void returnToList() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void editUserFragment() {
        Fragment fragment = new AddUserFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("age", age);
        bundle.putString("gender", gender);
        bundle.putString("image", image);
        bundle.putString("toolbar_title", EDIT_FRAGMENT);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, fragment, EDIT_FRAGMENT);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
