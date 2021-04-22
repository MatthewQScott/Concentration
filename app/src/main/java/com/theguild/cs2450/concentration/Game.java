package com.theguild.cs2450.concentration;

import android.content.Context;

import java.util.ArrayList;

import static android.util.Log.d;

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
        ArrayList<Card> bag = new ArrayList<>();

        for (int index = 0; index < (mNumberOfCards / 2); index++) {
            bag.add(new Card(Game.this, index));
            bag.add(new Card(Game.this, index));
        }

        mCardArray = new Card[mNumberOfCards];
        int index = 0;
        int randBagIndex;

        do {
            randBagIndex = (int) Math.random() % bag.size();

            mCardArray[index] = bag.remove(randBagIndex);

            index++;
        } while (bag.isEmpty() == false);

    }

    public void setCardAsSelected(Card card) {
        if (mTurnedCard1 == null) {
            mTurnedCard1 = card;
            System.out.println("mTurnedCard1AA" + mTurnedCard1);
        } else if (mTurnedCard2 == null) {
            mTurnedCard2 = card;
            mCardFlippingEnabled = false;
            compareFlippedCards();
            System.out.println("mTurnedCard1" + mTurnedCard1);
            System.out.println("mTurnedCard2" + mTurnedCard2);
        }
    }

    public boolean getCardFlippingEnabled() {
        return mCardFlippingEnabled;
    }

    public void compareFlippedCards() {
        if (mTurnedCard1.getCardFront().equals(mTurnedCard2.getCardFront())) {
            mScore += 2;
            mTurnedCard1.lockCard();
            mTurnedCard2.lockCard();
            mTurnedCard1 = null;
            mTurnedCard2 = null;
            mCardFlippingEnabled = true;
        } else {
            mScore -= 1;
        }
        mGameActivity.setScore(mScore);

    }

    public void setImageAdapter(ImageAdapter i) {
        mImageAdapter = i;
    }

    public void tryAgain() {
        if (mTurnedCard1 != null)
            mTurnedCard1.flipCard(mTurnedCard1.getView());
        if (mTurnedCard2 != null)
            mTurnedCard2.flipCard(mTurnedCard2.getView());
        mTurnedCard1 = null;
        mTurnedCard2 = null;
        mCardFlippingEnabled = true;
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
