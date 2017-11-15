package com.challenge.jesus.passportchallenge;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Jesus on 10/31/2017.
 */

public class AddUserFragment extends Fragment {

    String image;

    String _rand = UUID.randomUUID().toString();


    private static int RESULT_LOAD_IMAGE = 1;

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
        onCancel();

        if (toolbar_title.equals("Edit User")) {
            populateEditableUser();
        }

        setImage();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v("random", "ID: " +_rand);
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
    public void onCancel() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Back", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        });
    }

    public void getUserInput() {
        List<String> hobbies = new ArrayList<>(3);
        hobbies.add("Running");
        hobbies.add("Lattes");
        hobbies.add("Yoga");

        try {
            writeToDb("green", String.valueOf(input_gender.getText()), String.valueOf(input_name.getText()), image, hobbies, 784823, Integer.parseInt(input_age.getText().toString()));
        } catch (NumberFormatException e) {
            Log.v("number error", "error: " + e.toString());
        }
    }

    //Set image from gallery
    public void setImage() {
        input_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery, RESULT_LOAD_IMAGE);
            }
        });
    }

    // pick and receive image
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        final InputStream imageStream;
        final Bitmap selectedImage;

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE) {
                //get the URL from data
                Uri imageUri = data.getData();
                if (null != imageUri) {
                    //if our image uri isn't null, set the image
                    Log.v("uri", "IMAGE URI: " + imageUri);
                    try {
                        imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                        selectedImage = BitmapFactory.decodeStream(imageStream);
                        input_image.setImageBitmap(base64Decoder(encodedImage(selectedImage)));
                        image = encodedImage(selectedImage);
                        Log.v("base64", "BASE 64 STRING: " + encodedImage(selectedImage));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //Encoding the image selected by the user
    public String encodedImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] b = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
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
