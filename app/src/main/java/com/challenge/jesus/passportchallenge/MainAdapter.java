package com.challenge.jesus.passportchallenge;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jesus on 9/14/17.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.RecyclerViewHolder> {

    private Context context;
    private List<Profile> profileList;

    MainAdapter(List<Profile> profileList, Context context) {
        this.context = context;
        this.profileList = profileList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_row, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        final Profile profile = profileList.get(position);

        //This is our Base64 string; it will be passed when calling the base64Decoder method
        //The method will return our image.

        holder.text_id.setText(String.valueOf(profile.get_id()));
        holder.text_name.setText(profile.getName());

        //Decode and load image with Glide
        Glide.with(context)
                .asBitmap()
                .load(profile.getImage())
                .into(holder.user_image);

        Log.v("v", "image url from profile: " + profile.getImage());

        //Send our extras to the profile view activity
        holder.setRecyclerViewClickListener(new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

                Fragment fragment = new DisplayProfileFragment();

                Bundle bundle = new Bundle();
                bundle.putParcelable("profile", profile);
                fragment.setArguments(bundle);

                FragmentManager fragmentManager = ((Activity)context).getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.container, fragment, "Display Profile");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }


    @Override
    public int getItemCount() {
        return profileList.size();
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

        @Override
        public void onClick(View view) {
            recyclerViewClickListener.onClick(view, getAdapterPosition());
        }
    }
}
