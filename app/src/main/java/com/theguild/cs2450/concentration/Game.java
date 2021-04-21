package com.theguild.cs2450.concentration;

import android.content.Context;

public class Game {

    private GameActivity mGameActivity;
    private ImageAdapter mImageAdapter;
    private Card mTurnedCard1;
    private Card mTurnedCard2;
    private int mNumberOfCards;
    private Card mCardArray[];
    private boolean mCardFlippingEnabled = true;
    private int mScore;

    public Game (Context c, int numberOfCards) {
        mScore = 0;

        mGameActivity = (GameActivity) c;
        mNumberOfCards = numberOfCards;

        mNumberOfCards = 8;
        mCardArray = new Card[mNumberOfCards];

        for (int index = 0; index < mNumberOfCards; index++) {
            mCardArray[index] = new Card(this, index);
        }

    }

    public void setTurnedCard(Card card) {
        if (mTurnedCard1 == null) {
            mTurnedCard1 = card;
        } else if (mTurnedCard1 != null && mTurnedCard2 == null) {
            mTurnedCard2 = card;
        } else {
            // mCardFlippingEnabled = false;
        }
        compareFlippedCards();
        System.out.println((mTurnedCard1 == null) + " " + (mTurnedCard2 == null));
        System.out.println("compareFlipped called");
    }

    public boolean getCardFlippingEnabled() {
        return mCardFlippingEnabled;
    }

    public void compareFlippedCards() {
        if (mTurnedCard1 == null || mTurnedCard2 == null){}
        else if (mTurnedCard1.getCardFront() == mTurnedCard2.getCardFront()) {
            mScore++;
            mGameActivity.setScore(mScore);

        } else {

        }
    }

    public void setImageAdapter(ImageAdapter i) {
        mImageAdapter = i;
    }

    public int getNumberOfCards() {
        return mNumberOfCards;
    }

    public Card getCardAt(int index) {
        return mCardArray[index];
    }

    public GameActivity getGameActivity() {
        return mGameActivity;
    }

}
