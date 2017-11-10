package com.challenge.jesus.passportchallenge;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Jesus on 10/31/2017.
 */

public class AddUserFragment extends Fragment {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference("user");

    EditText input_name, input_age, input_gender;
    CircleImageView input_image;
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_user, container, false);

        input_name = view.findViewById(R.id.input_first_name);
        input_age = view.findViewById(R.id.input_age);
        input_gender = view.findViewById(R.id.input_gender);
        input_image = view.findViewById(R.id.input_image);

        //Retrieve toolbar title
        String toolbar_title = getArguments().getString("toolbar_title");

        toolbar = view.findViewById(R.id.toolbar_add_user);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cancel);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Setting the toolbar title
        toolbar.setTitle(toolbar_title);

        //Call return to profile view
        backButtonPressed();

        if (toolbar_title.equals("Edit User")) {
            populateEditableUser();
        }


        return view;
    }

    //Show user details when user is editing profile
    public void populateEditableUser() {
        input_name.setText(getArguments().getString("name"));
        input_age.setText(getArguments().getString("age"));
        input_gender.setText(getArguments().getString("gender"));
        input_image.setImageBitmap(base64Decoder(getArguments().getString("image")));
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
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_confirm, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                getUserInput();
                Toast.makeText(getActivity(), "DONE", Toast.LENGTH_SHORT).show();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public void getUserInput() {
        ArrayList<String> hobbies = new ArrayList<>(3);
        hobbies.add("Running");
        hobbies.add("Lattes");
        hobbies.add("Yoga");
        String image = "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";

        try{
            //String age = input_age.getText().toString();

            writeToDb("green", String.valueOf(input_gender.getText()), String.valueOf(input_name.getText()), image, hobbies, 784823, Integer.parseInt(input_age.getText().toString()));
        }catch (NumberFormatException e){
            Log.v("number error", "error: " + e.toString());
        }

    }


    //Write user
    private void writeToDb(String background_color, String gender, String name, String image, List<String> hobbies, int _id, int age) {

        if (!gender.equals(null) && !name.equals(null) && age > 0) {
            //Uniquely generated id
            String id = reference.push().getKey();
            User user = new User(background_color, gender, name, image, hobbies, _id, age);
            reference.child(id).setValue(user);
        } else {
            Toast.makeText(getActivity(), "Something is not right", Toast.LENGTH_SHORT).show();
        }

    }
}
