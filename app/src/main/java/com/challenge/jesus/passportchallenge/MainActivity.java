package com.challenge.jesus.passportchallenge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

    final String KEY = "LIST";
    List<User> userList = new ArrayList<>();

    //Firebase instantiation
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference("user");

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

        adapter = new MainAdapter(userList, MainActivity.this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        //Load the data at the start of the application
        readFromDb();
    }

    //Used to read data from firebase database
    void readFromDb() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clear if contains user
                userList.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    userList.add(user);
                }
                //Setting list to our adapter and attaching it to RecyclerView
                adapter = new MainAdapter(userList, MainActivity.this);
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

    //Write user. Will be used later
    private void writeToDb(String background_color, String gender, String name, String image, List<String> hobbies, int _id, int age) {
        String id = reference.push().getKey();
        User user = new User(background_color, gender, name, image, hobbies, _id, age);
        reference.child(id).setValue(user);
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
                return true;
            case R.id.action_sort_by_age_desc:
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }


    public void sortByID() {
        Collections.sort(userList, new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                return String.valueOf(user1.get_id()).compareTo(String.valueOf(user2.get_id()));
            }
        });
        adapter.notifyDataSetChanged();
    }

    public void sortByNameAsc() {
        Collections.sort(userList, new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                return user1.getName().compareTo(user2.getName());
            }
        });
        adapter.notifyDataSetChanged();
    }

    public void sortByNameDesc() {
        Collections.sort(userList, new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                return user2.getName().compareTo(user1.getName());
            }
        });
        adapter.notifyDataSetChanged();
    }

}

