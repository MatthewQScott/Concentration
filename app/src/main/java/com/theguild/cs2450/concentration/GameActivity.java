package com.theguild.cs2450.concentration;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;


public class GameActivity extends FragmentActivity {
    private boolean mFlashCondition = true;
    private Button mBackButton;
    private Button mTryAgainButton;
    private ImageAdapter mImageAdapter;
    private Game mGame;
    private TextView mScoreTextView;
    private Thread flashThread;
    private int mNumberOfCards = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        CharSequence[] possCardAmounts = { "4", "6", "8", "10", "12", "14", "16", "18", "20" };
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setTitle(R.string.choose_card_number)
                .setItems(possCardAmounts, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mNumberOfCards = Integer.parseInt(possCardAmounts[which].toString());
                       // System.out.println(mNumberOfCards + " cards were chosen");
                        createGame();
                        buttonFlash();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();



         mScoreTextView = (TextView) findViewById(R.id.score_text_view);

         mBackButton = (Button) findViewById(R.id.back_button);
         mBackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFlashCondition = false;
                    finish();
                }
         });

        mTryAgainButton = (Button) findViewById(R.id.try_again_button);
        mTryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGame.mTryButtonFlash = false;
                mGame.tryAgain();
            }
        });
    }

    private void buttonFlash() {
        android.view.animation.Animation anim = new android.view.animation.AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(450);
        anim.setStartOffset(20);
        anim.setRepeatMode(android.view.animation.Animation.REVERSE);
        anim.setRepeatCount(android.view.animation.Animation.INFINITE);

        flashThread = new Thread() {
            public void run() {
                while (mFlashCondition) {
                    while (mGame.mTryButtonFlash) {
                        mTryAgainButton.startAnimation(anim);
                        try {
                            flashThread.sleep(750);
                        }
                        catch (InterruptedException e) {

                        }
                    }
                    anim.cancel();
                }
            }
        };
        flashThread.start();
    }

    private void createGame() {

        mGame = new Game(this, mNumberOfCards);
        mImageAdapter = new ImageAdapter(this);
        mGame.setImageAdapter(mImageAdapter);
        mImageAdapter.setGame(mGame);

        GridView gridView = (GridView) findViewById(R.id.gridView);
        if (mNumberOfCards >= 12)
            gridView.setNumColumns(5);
        else if (mNumberOfCards >= 6)
            gridView.setNumColumns(4);
        gridView.setAdapter(mImageAdapter);

    }

    public void setScore(int score) {
        mScoreTextView.setText("Your Score: " + score);
    }

}