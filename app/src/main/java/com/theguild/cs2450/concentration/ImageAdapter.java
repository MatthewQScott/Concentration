package com.theguild.cs2450.concentration;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private GameActivity mGameActivity;
    private Game mGame;
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    LayoutInflater mInflater;

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


//        if (convertView == null) {
//            mCard = mGame.getCardAt(position);
//        }
//        else
//        {
//            imageView = (ImageView) convertView;
//        }
//
//        imageView.setImageResource(mGame.getCardAt());
//
//        imageView.setOnClickListener(new View.OnClickListener() {
//            boolean isBackShowing = true;
//
//            @Override
//            public void onClick(View v) {
//
//                Animator leftIn;
//                Animator leftOut;
//                Animator rightIn;
//                Animator rightOut;
//                leftIn = AnimatorInflater.loadAnimator(mGameActivity,
//                        R.animator.card_flip_left_in);
//                leftOut = AnimatorInflater.loadAnimator(mGameActivity,
//                        R.animator.card_flip_left_out);
//                rightIn = AnimatorInflater.loadAnimator(mGameActivity,
//                        R.animator.card_flip_right_in);
//                rightOut = AnimatorInflater.loadAnimator(mGameActivity,
//                        R.animator.card_flip_right_out);
//
//                isBackShowing = !isBackShowing;
//
//                AnimatorSet set = new AnimatorSet();
//
//                Animator.AnimatorListener listener = new Animator.AnimatorListener() {
//                    @Override
//                    public void onAnimationStart(Animator animation) { }
//
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        if (isBackShowing) {
//                            imageView.setImageResource(mCardBack);
//                        } else {
//                            imageView.setImageResource(mThumbIds[position]);
//                        }
//                    }
//
//                    @Override
//                    public void onAnimationCancel(Animator animation) {}
//
//                    @Override
//                    public void onAnimationRepeat(Animator animation) {}
//                };
//                leftOut.addListener(listener);
//                rightOut.addListener(listener);
//
//                if (isBackShowing) {
//                    set.playSequentially(leftOut, leftIn);
//                } else {
//                    set.playSequentially(rightOut, rightIn);
//                }
//
//                set.setTarget(v);
//                mGame.setTurnedCard(position);
//
//                if (mGame.getCardFlippingEnabled()) {
//                    set.start();
//
//                }
//            }
//        });
//
//        return imageView;
    }

    public void setGame(Game g) {
        mGame = g;
    }
}


