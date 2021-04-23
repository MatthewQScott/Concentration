package com.theguild.cs2450.concentration;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;


public class GameActivity extends FragmentActivity {
    private AudioPlayer audio;
    private Button mBackButton;
    private Button mTryAgainButton;
    private ImageAdapter mImageAdapter;
    private Game mGame;
    private int mNumberOfCards = 8;
    private TextView mScoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

         mGame = new Game(this, mNumberOfCards);
         mImageAdapter = new ImageAdapter(this);
         mGame.setImageAdapter(mImageAdapter);
         mImageAdapter.setGame(mGame);

         GridView gridView = (GridView) findViewById(R.id.gridView);
         gridView.setAdapter(mImageAdapter);

         mScoreTextView = (TextView) findViewById(R.id.score_text_view);

         mBackButton = (Button) findViewById(R.id.back_button);
         mBackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   finish();
                }
         });

        mTryAgainButton = (Button) findViewById(R.id.try_again_button);
        mTryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGame.tryAgain();
            }
        });
    }

    public void setScore(int score) {
        mScoreTextView.setText("Your Score: " + score);
    }

    private void writeScoreToFile() {
        // I don't know if it makes more sense to write the score-saving code here
        // or in the Game.java class. Do what you think's best
    }
}