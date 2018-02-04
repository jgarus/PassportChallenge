package com.challenge.jesus.passportchallenge;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DisplayUserProfileFragment extends Fragment {

    MenuItem editProfile;
    MenuItem editDone;
    final String EDIT_FRAGMENT = "Edit Profile";

    Bundle bundle;
    Profile profile;
    Toolbar toolbar;
    TextView textview_profile_id, textview_profile_age, textview_profile_gender;
    CircleImageView profile_image;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_user_profile, container, false);

        textview_profile_id = view.findViewById(R.id.textview_profile_id);
        textview_profile_age = view.findViewById(R.id.textview_profile_age);
        textview_profile_gender = view.findViewById(R.id.textview_profile_gender);
        profile_image = view.findViewById(R.id.profile_image);

        toolbar = view.findViewById(R.id.toolbar_profile_view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        setValues();

        //call return action
        returnToList();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_edit_user, menu);
        editProfile = menu.findItem(R.id.action_edit);
        editDone = menu.findItem(R.id.action_done).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                editProfile.setVisible(false);
                editDone.setVisible(true);
                animationEdit(textview_profile_age);

                textview_profile_age.requestFocus();
                textview_profile_age.setFocusable(true);
                textview_profile_age.setFocusableInTouchMode(true);
                textview_profile_age.setClickable(true);
                textview_profile_age.setEnabled(true);
                textview_profile_age.setCursorVisible(true);
                textview_profile_age.setInputType(InputType.TYPE_CLASS_NUMBER);
                return true;
            case R.id.action_done:
                updateProfile(Integer.valueOf(textview_profile_age.getText().toString()));
                editProfile.setVisible(true);
                editDone.setVisible(false);
                textview_profile_age.setEnabled(false);
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public void setValues() {
        bundle = this.getArguments();
        profile = bundle.getParcelable("profile");
        toolbar.setTitle(profile.getName());
        textview_profile_id.setText(String.valueOf(profile.get_id()));
        textview_profile_age.setText(String.valueOf(profile.getAge()));
        textview_profile_gender.setText(profile.getGender());

        //Decode and load image with Glide
        Glide.with(DisplayUserProfileFragment.this)
                .asBitmap()
                .load(Base64.decode(profile.getImage(), Base64.DEFAULT))
                .into(profile_image);
    }

    //go back to list of users from toolbar back button
    public void returnToList() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    public void animationEdit(View view) {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(650);
        anim.setStartOffset(15);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        view.setAnimation(anim);
        view.startAnimation(anim);
    }

    void updateProfile(final int age) {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //Get profile keys
                    String key = postSnapshot.getKey();

                    //If our current profile key matches a retrieved key, then update
                    if (profile.getKey().equals(key)) {
                       //TODO: Update profile here
                        Log.v("v", "PROFILE KEY: " + key);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.v("v", "Failed to load data.", error.toException());
            }
        });
    }
}
