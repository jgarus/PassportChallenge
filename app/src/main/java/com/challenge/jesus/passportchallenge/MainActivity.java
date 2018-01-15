package com.challenge.jesus.passportchallenge;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by jesus on 9/14/17.
 */

public class MainActivity extends AppCompatActivity {

    List<Profile> profileList = new ArrayList<>();

    //Firebase instantiation
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference("profile");

    RecyclerView recyclerView;
    MainAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Setting the toolbar
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        //Initializing our RecyclerView (list)
        recyclerView = findViewById(R.id.recycler_view_main);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        addUserFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Load the data at the start of the application
        readFromDb();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_list_options:
                return true;
            case R.id.action_sort_options:
                return true;
            case R.id.action_filter_by:
                return true;
            case R.id.action_sort_by_id:
                sortByID();
                return true;
            case R.id.action_sort_by_name_asc:
                sortByNameAsc();
                return true;
            case R.id.action_sort_by_name_desc:
                sortByNameDesc();
                return true;
            case R.id.action_sort_by_age_asc:
                sortByAgeAscending();
                return true;
            case R.id.action_sort_by_age_desc:
                sortByAgeDescending();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }


    //Used to read data from firebase database
    void readFromDb() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clear if contains user
                profileList.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Profile profile = userSnapshot.getValue(Profile.class);
                    profileList.add(profile);
                }
                //Setting list to our adapter and attaching it to RecyclerView
                adapter = new MainAdapter(profileList, MainActivity.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.v("v", "Failed to load data.", error.toException());
                Toast.makeText(MainActivity.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addUserFragment(){
        FloatingActionButton floatingActionButton = findViewById(R.id.add_user);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddUserFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }


    public void sortByID() {
        Collections.sort(profileList, new Comparator<Profile>() {
            @Override
            public int compare(Profile profile1, Profile profile2) {
                return String.valueOf(profile1.get_id()).compareTo(String.valueOf(profile2.get_id()));
            }
        });
        adapter.notifyDataSetChanged();
    }

    public void sortByNameAsc() {
        Collections.sort(profileList, new Comparator<Profile>() {
            @Override
            public int compare(Profile profile1, Profile profile2) {
                return profile1.getName().compareTo(profile2.getName());
            }
        });
        adapter.notifyDataSetChanged();
    }

    public void sortByNameDesc() {
        Collections.sort(profileList, new Comparator<Profile>() {
            @Override
            public int compare(Profile profile1, Profile profile2) {
                return profile2.getName().compareTo(profile1.getName());
            }
        });
        adapter.notifyDataSetChanged();
    }

    public void sortByAgeAscending() {
        Collections.sort(profileList, new Comparator<Profile>() {
            @Override
            public int compare(Profile profile1, Profile profile2) {
                return String.valueOf(profile1.getAge()).compareTo(String.valueOf(profile2.getAge()));
            }
        });
        adapter.notifyDataSetChanged();
    }

    public void sortByAgeDescending() {
        Collections.sort(profileList, new Comparator<Profile>() {
            @Override
            public int compare(Profile profile1, Profile profile2) {
                return String.valueOf(profile2.getAge()).compareTo(String.valueOf(profile1.getAge()));
            }
        });
        adapter.notifyDataSetChanged();
    }
}

