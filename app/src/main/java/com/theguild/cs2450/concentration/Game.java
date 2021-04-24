package com.theguild.cs2450.concentration;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;

import static android.util.Log.d;

public class Game {

    private GameActivity mGameActivity;
    private ImageAdapter mImageAdapter;
    private ArrayList<Card> mCardList;
    private Card mTurnedCard1;
    private Card mTurnedCard2;
    private int mNumberOfCards;
    private int mScore;
    private boolean mCardFlippingEnabled = true;
    private String mUsername;


    public Game (Context c, int numberOfCards) {
        mGameActivity = (GameActivity) c;
        mNumberOfCards = numberOfCards;
        mScore = 0;



        fillRandCardList();
        resizeCards();
    }

    private void resizeCards() {
        ViewGroup.LayoutParams params;
        if (mNumberOfCards >= 16)
            params = new ViewGroup.LayoutParams(150, 250);
        else
            params = new ViewGroup.LayoutParams(250, 350);



        for (int i = 0; i < mCardList.size(); i++) {
           View view = mCardList.get(i).getView();
            view.setLayoutParams(params);
        }
    }

    private void fillRandCardList() {
        mCardList = new ArrayList<>();
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int index = 0; index < 10; index += 1)
            indexes.add(index);


        for (int count = 0; count < (mNumberOfCards / 2); count++) {
            int randCardIndex = (int) (Math.random() * indexes.size());
            randCardIndex = indexes.remove(randCardIndex);
            mCardList.add(new Card(Game.this, randCardIndex));
            mCardList.add(new Card(Game.this, randCardIndex));
        }
        Collections.shuffle(mCardList);
    }


    public void selectCard(Card card) {
        if (mTurnedCard1 == null) {
            mTurnedCard1 = card;
        } else if (mTurnedCard2 == null) {
            mTurnedCard2 = card;
            mCardFlippingEnabled = false;
            compareSelectedCards();
            if (isGameOver()) {
                endGame();
            }
        }
    }

    private void endGame() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getGameActivity());

        LayoutInflater inflater = getGameActivity().getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_name_prompt, null);
        TextView mNameEditText = dialogLayout.findViewById(R.id.username_edit_view);
        builder.setView(dialogLayout)

            .setPositiveButton(R.string.enter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mUsername = mNameEditText.getText().toString();
                    saveHighScore(mUsername, mScore);
                }
            });

        builder.setMessage(R.string.name_prompt_message)
                .setTitle(R.string.name_prompt_title);
        

        AlertDialog dialog = builder.create();
        dialog.findViewById(R.id.username_edit_view);
        dialog.show();

    }

    private void saveHighScore(String username, int score) {


        getGameActivity().finish();
    }

    public void compareSelectedCards() {
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

    private boolean isGameOver() {
        boolean isOver = true;
        for (Card card: mCardList) {
            if (card.isCardLocked() == false) {
                isOver = false;
            }
        }
        return isOver;
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

    public boolean getCardFlippingEnabled() {
        return mCardFlippingEnabled;
    }

    public void setImageAdapter(ImageAdapter i) {
        mImageAdapter = i;
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
