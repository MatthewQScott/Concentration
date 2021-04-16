package com.theguild.cs2450.concentration;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


public class GameActivity extends FragmentActivity {

    private Button mBackButton;
    private int mTurnedCard1;
    private int mTurnedCard2;
    private boolean mCardFlippingEnabled = true;
    private TextView mScoreTextView;
    private int score = 0;
    private ImageAdapter myImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

         GridView gridView = (GridView) findViewById(R.id.gridView);
         myImageAdapter = new ImageAdapter(this);
         /*Integer[] intArray = {
                 R.drawable.dice_1, R.drawable.dice_2,
         };
         myImageAdapter.mThumbIds = intArray;*/
         gridView.setAdapter(myImageAdapter);

        mScoreTextView = (TextView) findViewById(R.id.score_text_view);

         mBackButton = (Button) findViewById(R.id.back_button);
         mBackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   finish();
                }
         });
    }

    public void setTurnedCard(int card) {
        if (mTurnedCard1 == 0) {
            mTurnedCard1 = card;
        } else if (mTurnedCard1 != 0 && mTurnedCard2 == 0) {
            mTurnedCard2 = card;
        } else {
           // mCardFlippingEnabled = false;
        }
        compareFlippedCards();
        System.out.println((mTurnedCard1 == 0) + " " + (mTurnedCard2 == 0));
        System.out.println("compareFlipped called");
    }

    public boolean getCardFlippingEnabled() {
        return mCardFlippingEnabled;
    }

    public void compareFlippedCards() {
        if (mTurnedCard1 == 0 || mTurnedCard2 == 0){}
        else if (myImageAdapter.mThumbIds[mTurnedCard1] == myImageAdapter.mThumbIds[mTurnedCard2]) {
            System.out.println("weeeeeeeeeeeeeeee");
            score++;
            mScoreTextView.setText("" + score);

        } else {

        }
    }
}