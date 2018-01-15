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
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class DisplayUserProfileFragment extends Fragment {

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
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                //will make editable
                return true;
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
        profile_image.setImageBitmap(base64Decoder(profile.getImage()));
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
                getActivity().onBackPressed();
            }
        });
    }

    //Testing
//    private void updateUser(String background_color, String gender, String name, String image, List<String> hobbies, int _id, int age) {
//
//        Profile profile = new Profile(background_color, gender, name, image, hobbies, _id, age);
//        Map<String, Object> userValues = profile.toMap();
//
//        Map<String, Object> childUpdates = new HashMap<>();
//        childUpdates.put(id, userValues);
//        reference.updateChildren(childUpdates);
//    }
}
