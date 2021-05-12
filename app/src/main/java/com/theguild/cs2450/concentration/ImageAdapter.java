//Author: Matthew Scott Bronco #014966432

package com.theguild.cs2450.concentration;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    // The Card Views to be displayed in a grid
    private ArrayList<ImageView> mCardImageViews;

    // The constructor that takes in the card views to be displayed
    public ImageAdapter(ArrayList<ImageView> cardImageViews) {
        mCardImageViews = cardImageViews;
    }

    // Tells the grid view how many cards from the card list should be displayed on the screen
    // In this case: all of them
    public int getCount() {
        return mCardImageViews.size();
    }

    //  Not implemented in this adapter
    public Object getItem(int position) {
        return null;
    }

    // Not implemented in this adapter
    public long getItemId(int position) {
        return 0;
    }

    // returns to the grid view the image view for the card at "position"
    public View getView(int position, View convertView, ViewGroup parent) {
        return mCardImageViews.get(position);
    }
}


