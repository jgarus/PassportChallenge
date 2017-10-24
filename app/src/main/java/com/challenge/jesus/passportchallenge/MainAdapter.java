package com.challenge.jesus.passportchallenge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

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
        holder.text_id.setText(String.valueOf(user.get_id()));
        holder.text_name.setText(user.getName());

        holder.setRecyclerViewClickListener(new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle extras = new Bundle();
                extras.putString("name", user.getName());
                extras.putString("id", String.valueOf(user.get_id()));
                extras.putString("age", String.valueOf(user.getAge()));
                extras.putString("gender", user.getGender());

                Intent intent = new Intent(context, CreateUserActivity.class);
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
        //Our TextViews
        TextView text_id, text_name;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            text_id = itemView.findViewById(R.id.text_id);
            text_name = itemView.findViewById(R.id.text_name);

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
}
