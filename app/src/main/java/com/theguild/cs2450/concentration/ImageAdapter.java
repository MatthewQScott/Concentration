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

    private GameActivity mGameActivity;
    private ImageView[] mCardImageViews;

    public ImageAdapter(Context c, ImageView[] cardImageViews) {
        mGameActivity = (GameActivity) c;
        mCardImageViews = cardImageViews;
    }

    public int getCount() {
        return mCardImageViews.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return mCardImageViews[position];
    }



}


