package com.challenge.jesus.passportchallenge;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jesus on 9/14/17.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.RecyclerViewHolder> {

    private Context context;
    private List<User> userList;

    MainAdapter(List<User> userList, Context context) {
        this.context = context;
        this.userList = userList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_row, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        final User user = userList.get(position);

        //This is our Base64 string; it will be passed when calling the base64Decoder method
        //The method will return our image.
        final String base64String = user.getImage();

        holder.text_id.setText(String.valueOf(user.get_id()));
        holder.text_name.setText(user.getName());
        holder.user_image.setImageBitmap(base64Decoder(base64String));

        //Send our extras to the profile view activity
        holder.setRecyclerViewClickListener(new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

                Fragment fragment = new DisplayUserProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putString("name", user.getName());
                bundle.putString("id", String.valueOf(user.get_id()));
                bundle.putString("age", String.valueOf(user.getAge()));
                bundle.putString("gender", user.getGender());
                bundle.putString("image", user.getImage());
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = ((Activity)context).getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.container, fragment, "Display User");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    //ViewHolder, for efficiency
    static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Creating instance of our click handler interface
        private RecyclerViewClickListener recyclerViewClickListener;
        //Our views
        TextView text_id, text_name;
        CircleImageView user_image;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            text_id = itemView.findViewById(R.id.text_id);
            text_name = itemView.findViewById(R.id.text_name);
            user_image = itemView.findViewById(R.id.user_image);

            itemView.setOnClickListener(this);
        }

        private void setRecyclerViewClickListener(RecyclerViewClickListener recyclerViewClickListener) {
            this.recyclerViewClickListener = recyclerViewClickListener;
        }


        //Using only onClick, onLongClick can be added if needed
        @Override
        public void onClick(View view) {
            recyclerViewClickListener.onClick(view, getAdapterPosition());
        }
    }

    //We will use this to decode image
    private Bitmap base64Decoder(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
