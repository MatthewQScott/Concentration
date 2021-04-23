package com.theguild.cs2450.concentration;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;

import static android.util.Log.d;

public class Game {

    private AudioPlayer audio;
    private GameActivity mGameActivity;
    private ImageAdapter mImageAdapter;
    private Card mTurnedCard1;
    private Card mTurnedCard2;
    private int mNumberOfCards;
    private ArrayList<Card> mCardList;
    private boolean mCardFlippingEnabled = true;
    private int mScore;

    public Game (Context c, int numberOfCards) {
        //audio.playMusic(c);
        mGameActivity = (GameActivity) c;
        mNumberOfCards = numberOfCards;
        mScore = 0;

        fillCardPairList();

    }

    private void fillCardPairList() {
        mNumberOfCards = 8;
        mCardList = new ArrayList<>();

        for (int count = 0; count < (mNumberOfCards / 2); count++) {
            int randCardIndex = (int) (Math.random() * Card.getCardListSize());
            System.out.println(randCardIndex + "RRRRRRRRR");
            mCardList.add(new Card(Game.this, randCardIndex));
            mCardList.add(new Card(Game.this, randCardIndex));
        }
        Collections.shuffle(mCardList);
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
            if (mScore > 0) {
                mScore -= 1;
            }
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
        return mCardList.get(index);
    }

    public GameActivity getGameActivity() {
        return mGameActivity;
    }

}
