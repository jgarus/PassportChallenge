package com.challenge.jesus.passportchallenge;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;


import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Jesus on 10/31/2017.
 */

public class AddUserFragment extends Fragment {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    String imageURL;

    private static int RESULT_LOAD_IMAGE = 1;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    EditText input_name, input_age;
    CircleImageView input_image;
    Toolbar toolbar;
    Spinner genderSelectionSpinner;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_user, container, false);

        genderSelectionSpinner = view.findViewById(R.id.spinner);

        input_name = view.findViewById(R.id.input_name);
        input_age = view.findViewById(R.id.input_age);
        input_image = view.findViewById(R.id.input_image);

        toolbar = view.findViewById(R.id.toolbar_add_user);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cancel);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Call return to profile view
        onCancel();
        selectGender();
        setImage();

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
        getActivity().getMenuInflater().inflate(R.menu.menu_confirm, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                getUserInput();
                getActivity().getFragmentManager().popBackStack();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    //Setting the spinner here
    public void selectGender() {
        List<String> genderValues = new ArrayList<>(Arrays.asList("Male", "Female"));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, genderValues);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSelectionSpinner.setAdapter(arrayAdapter);
    }

    //Toolbar back button back action
    public void onCancel() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    public void getUserInput() {

        List<String> hobbies = new ArrayList<>(3);
        hobbies.add("Running");
        hobbies.add("Lattes");
        hobbies.add("Yoga");

        if (TextUtils.isEmpty(input_name.getText()) && TextUtils.isEmpty(input_age.getText())
                && TextUtils.isEmpty(genderSelectionSpinner.getSelectedItem().toString())) {

            Toast.makeText(getActivity(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            try {
                writeToDb("green", String.valueOf(genderSelectionSpinner.getSelectedItem().toString()),
                        String.valueOf(input_name.getText()), imageURL, hobbies, generateID(), Integer.parseInt(input_age.getText().toString()));
            } catch (NumberFormatException e) {
                Log.v("number error", "error: " + e.toString());
            }

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

        InputStream imageStream;
        Bitmap selectedImage;

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE) {
                //get the URL from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    //if our image uri isn't null, set the image
                    Log.v("uri", "IMAGE URI: " + selectedImageUri);
                    try {
                        imageStream = getActivity().getContentResolver().openInputStream(selectedImageUri);
                        selectedImage = BitmapFactory.decodeStream(imageStream);

                        //Load image with Glide
                        Glide.with(AddUserFragment.this)
                                .asBitmap()
                                .load(selectedImage)
                                .into(input_image);

                        //Encode the set image which is being sent to the database
                        //imageURL = encodeImage(selectedImage);
                        encodeImage(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //Encode image selected
    public void encodeImage(Bitmap bitmap) {
        String path = "prfile_images/" + UUID.randomUUID() + ".png";
        StorageReference storageReference = storage.getReference(path);


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] b = byteArrayOutputStream.toByteArray();

        //upload task, will be used to show upload action
        UploadTask uploadTask = storageReference.putBytes(b);
        uploadTask.addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //getting download url
                Uri url = taskSnapshot.getDownloadUrl();
                //Log.v("v", "URL: " + url);
                assert url != null;
                imageURL = url.toString();
                Log.v("v", "URL: " + imageURL);
            }
        });
        //return Base64.encodeToString(b, Base64.DEFAULT);
    }

    //Write user
    private void writeToDb(String background_color, String gender, String name, String imageURL, List<String> hobbies, long _id, int age) {

        //If the user doesn't select an image, set a default
        if (imageURL == null) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.profile);
            //input_image.setImageBitmap(base64Decoder(encodeImage(bitmap)));
            input_image.setImageBitmap(bitmap);
            encodeImage(bitmap);
        }

        if (!gender.equals("") && !name.equals("") && age > 0) {
            //Uniquely generated id
            String key = reference.push().getKey();
            Profile profile = new Profile(background_color, gender, name, imageURL, hobbies, _id, age);
            reference.child("profile").child(key).setValue(profile);
        } else {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    //Will generate a simple ID for the profile being created
    public Long generateID() {
        Long currentTimeMillis = System.currentTimeMillis() % 10000000;
        AtomicLong id = new AtomicLong(currentTimeMillis);
        return id.getAndIncrement();
    }
}
