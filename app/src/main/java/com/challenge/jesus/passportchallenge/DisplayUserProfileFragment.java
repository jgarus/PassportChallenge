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
    String name, id, age, gender, image;

    Toolbar toolbar;
    TextView textview_profile_id, textview_profile_age, textview_profile_gender;
    CircleImageView profile_image;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_user_profile, container, false);

        setStrings();

        textview_profile_id = view.findViewById(R.id.textview_profile_id);
        textview_profile_age = view.findViewById(R.id.textview_profile_age);
        textview_profile_gender = view.findViewById(R.id.textview_profile_gender);
        profile_image = view.findViewById(R.id.profile_image);

        toolbar = view.findViewById(R.id.toolbar_profile_view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setTitle(name);
        textview_profile_id.setText(id);
        textview_profile_age.setText(age);
        textview_profile_gender.setText(gender);
        profile_image.setImageBitmap(base64Decoder(image));

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
                editUserFragment();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }


    public void setStrings(){
        name = getArguments().getString("name");
        id = getArguments().getString("id");
        age = getArguments().getString("age");
        gender = getArguments().getString("gender");
        image = getArguments().getString("image");
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
        fragmentTransaction.replace(R.id.container, fragment, EDIT_FRAGMENT);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
