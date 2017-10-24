package com.challenge.jesus.passportchallenge;

import android.view.View;

/**
 * Created by jesus on 9/18/17.
 *
 * This interface will allow us to set the ViewHolder as a click listener,
 * we can pass in an instance of this interface then set the view as a click listener
 * then call the interface with the right position.
 *
 * Can be used on other views that use the RecyclerView
 */

public interface RecyclerViewClickListener {
    void onClick(View view, int position);
}
