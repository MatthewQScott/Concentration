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
    private ArrayList<Integer> mCardFronts;

    public ImageAdapter(Context c, ArrayList<Integer> cardList) {
        mGameActivity = (GameActivity) c;
        mCardFronts = cardList;
    }

    public int getCount() {
        return mCardFronts.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mGameActivity);

            imageView.setImageResource(mCardFronts.get(position));
        } else {
            imageView = (ImageView) convertView;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
            convertView.setLayoutParams(new GridView.LayoutParams(params));
        }

        return imageView;
    }



}


