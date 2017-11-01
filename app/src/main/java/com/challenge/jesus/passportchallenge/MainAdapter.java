package com.challenge.jesus.passportchallenge;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jesus on 9/14/17.
 *
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
                Bundle extras = new Bundle();
                extras.putString("name", user.getName());
                extras.putString("id", String.valueOf(user.get_id()));
                extras.putString("age", String.valueOf(user.getAge()));
                extras.putString("gender", user.getGender());
                extras.putString("image", user.getImage());

                Intent intent = new Intent(context, DisplayUserProfileActivity.class);
                intent.putExtras(extras);
                context.startActivity(intent);
                Toast.makeText(context, "POSITION: " + position, Toast.LENGTH_SHORT).show();
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
