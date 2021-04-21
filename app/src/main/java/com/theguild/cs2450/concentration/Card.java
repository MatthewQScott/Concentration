package com.theguild.cs2450.concentration;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class Card {

    private static Integer mCardBack = R.drawable.a_back;
    private GameActivity mGameActivity;
    private Game mGame;
    private Integer mCardFront;
    private Integer mCurrentFacing;
    private ImageView mImageView;

    // not all are used depending on the number of cards
    public Integer[] mThumbIds = {
            R.drawable.a_1, R.drawable.a_2,
            R.drawable.a_3, R.drawable.a_4,
            R.drawable.a_5, R.drawable.a_6,
            R.drawable.a_7, R.drawable.a_8,
            R.drawable.a_9, R.drawable.a_10,
            R.drawable.a_1, R.drawable.a_2,
            R.drawable.a_3, R.drawable.a_4,
            R.drawable.a_5, R.drawable.a_6,
            R.drawable.a_7, R.drawable.a_8,
            R.drawable.a_9, R.drawable.a_10
    };

    public Card(Game g, int index) {
        mCardFront = mThumbIds[index];
        mCurrentFacing = mCardBack;
        mGame = g;
        mGameActivity = mGame.getGameActivity();
        mImageView = new ImageView(mGameActivity);

        // I need to put this layout information in an xml file
        mImageView.setImageResource(mCurrentFacing);
        mImageView.setLayoutParams(new ViewGroup.LayoutParams(250, 350));
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mImageView.setPadding(20,20,20,20);
        setFlipAction();
    }

    public View getView() {
        return mImageView;
    }

    private void setFlipAction() {
        mImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Animator leftIn;
                Animator leftOut;
                Animator rightIn;
                Animator rightOut;
                leftIn = AnimatorInflater.loadAnimator(mGameActivity,
                        R.animator.card_flip_left_in);
                leftOut = AnimatorInflater.loadAnimator(mGameActivity,
                        R.animator.card_flip_left_out);
                rightIn = AnimatorInflater.loadAnimator(mGameActivity,
                        R.animator.card_flip_right_in);
                rightOut = AnimatorInflater.loadAnimator(mGameActivity,
                        R.animator.card_flip_right_out);

                AnimatorSet set = new AnimatorSet();

                Animator.AnimatorListener listener = new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) { }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (mCurrentFacing == mCardBack) {
                            mCurrentFacing = mCardFront;
                            mImageView.setImageResource(mCardFront);
                        } else {
                            mCurrentFacing = mCardBack;
                            mImageView.setImageResource(mCardBack);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {}

                    @Override
                    public void onAnimationRepeat(Animator animation) {}
                };
                leftOut.addListener(listener);
                rightOut.addListener(listener);

                if (mCurrentFacing == mCardFront) {
                    set.playSequentially(leftOut, leftIn);
                } else {
                    set.playSequentially(rightOut, rightIn);
                }

                set.setTarget(v);
                mGame.setTurnedCard(Card.this);

                if (mGame.getCardFlippingEnabled()) {
                    set.start();

                }
            }
        });

    }

    public Integer getCardFront() {
        return mCardFront;
    }
}
