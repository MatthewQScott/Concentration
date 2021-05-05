package com.theguild.cs2450.concentration;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ImageAdapter extends BaseAdapter {
    private GameActivity mGameActivity;
    private Game mGame;


    // Contructor
    public ImageAdapter(Context c) {
        mGameActivity = (GameActivity) c;
    }

    public int getCount() {
        return mGame.getNumberOfCards();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        return (View) mGame.getCardAt(position).getView();
    }

    public void setGame(Game g) {
        mGame = g;
    }
}


