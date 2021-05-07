package com.theguild.cs2450.concentration;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.Collections;

public class GameActivity extends FragmentActivity {


    private int mCardAmount;
    private TextView mScoreTextView;
    private Button mBackButton;
    private Button mTryAgainButton;
    private boolean mIsFlashing = true;
    private int mScore = 0;
    ArrayList<Integer> mCardFronts = new ArrayList<>();
    private boolean[] mIsCardLocked;


    private static Integer[] mThumbIds = {
            R.drawable.a_1, R.drawable.a_2,
            R.drawable.a_3, R.drawable.a_4,
            R.drawable.a_5, R.drawable.a_6,
            R.drawable.a_7, R.drawable.a_8,
            R.drawable.a_9, R.drawable.a_10,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        wireWidgets();
        promptCardCountDialog();
        Log.i("mCardAmount", mCardAmount + "");

    }

    private void wireWidgets() {
        mScoreTextView = (TextView) findViewById(R.id.score_text_view);

        mBackButton = (Button) findViewById(R.id.back_button);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsFlashing = false;
                finish();
            }
        });

        mTryAgainButton = (Button) findViewById(R.id.try_again_button);
        mTryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsFlashing = false;
                tryAgain();
            }
        });
    }

    private void promptCardCountDialog() {
        CharSequence[] possCardAmounts = { "4", "6", "8", "10", "12", "14", "16", "18", "20" };
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setTitle(R.string.choose_card_number)
                .setCancelable(false)
                .setItems(possCardAmounts, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCardAmount = Integer.parseInt(possCardAmounts[which].toString());
                        createGame();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createGame() {
        setupCards();
        displayCardGrid();
    }

    private void startButtonFlash() {
        android.view.animation.Animation anim = new android.view.animation.AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(450);
        anim.setStartOffset(20);
        anim.setRepeatMode(android.view.animation.Animation.REVERSE);
        anim.setRepeatCount(android.view.animation.Animation.INFINITE);
        mTryAgainButton.startAnimation(anim);
    }

    private void setupCards() {
        // setup pool of possible words
        ArrayList<Integer> possWordIndexes = new ArrayList<>();
        for (int index = 0; index < 10; index += 1)
            possWordIndexes.add(index);

        // choose random words to be included in this game
        for (int count = 0; count < (mCardAmount / 2); count++) {
            int wordIndex = (int) (Math.random() * possWordIndexes.size());
            wordIndex = possWordIndexes.remove(wordIndex);

            mCardFronts.add(mThumbIds[wordIndex]);
            mCardFronts.add(mThumbIds[wordIndex]);
        }

        // shuffle the words to be in different positions on the grid
        Collections.shuffle(mCardFronts);
    }

    private void displayCardGrid() {
        ImageAdapter imageAdapter = new ImageAdapter(this, mCardFronts);
        GridView gridView = (GridView) findViewById(R.id.gridView);
        if (mCardAmount >= 12)
            gridView.setNumColumns(5);
        else if (mCardAmount >= 6)
            gridView.setNumColumns(4);

        gridView.setAdapter(imageAdapter);
        gridView.setStretchMode(GridView.NO_STRETCH);


        Log.i("params", "" );
    }


    private void endButtonFlash() {
        mTryAgainButton.clearAnimation();
    }

    public void tryAgain() {
        mScoreTextView.setText("try again TBI");
        endButtonFlash();
    }











    /*
    private static final String TAG = "GameActivity";
    private static final String KEY_FLASH_CONDITION = "flashCondition";
    private static final String KEY_IMAGE_INDEXES = "imageIndexes";
    private static final String KEY_IS_LOCKED = "isLocked";
    private static final String KEY_CURRENT_FACINGS = "currentFacings";
    private static final String KEY_CARD_AMOUNT = "cardAmount";
    private static final String KEY_SCORE = "score";
    private boolean mFlashCondition = false;
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


        wireWidgets();

        Log.i(TAG, "onRecreate");
        if (savedInstanceState != null) {

            mFlashCondition = savedInstanceState.getBoolean(KEY_FLASH_CONDITION, false);
            mNumberOfCards = savedInstanceState.getInt(KEY_CARD_AMOUNT);
            createGame();
            buttonFlash();

            mGame.setScore(savedInstanceState.getInt(KEY_SCORE));


            Card tempCard;
            int[] imageIndexes = savedInstanceState.getIntArray(KEY_IMAGE_INDEXES);
            boolean[] isLocked = savedInstanceState.getBooleanArray(KEY_IS_LOCKED);
            int[] currentFacings = savedInstanceState.getIntArray(KEY_CURRENT_FACINGS);

            for (int i = 0; i < mGame.getNumberOfCards(); i++) {
                tempCard = mGame.getCardAt(i);
                tempCard.setCurrentFacing(currentFacings[i]);
                tempCard.setImageIndex(imageIndexes[i]);
                tempCard.setLocked(isLocked[i]);
            }

        } else {
            showCardNumDialog();
        }
    }

    public void setFlashCondition(boolean flashCondition) {
        mFlashCondition = flashCondition;
    }

    private void wireWidgets() {
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
                mFlashCondition = false;
                mGame.tryAgain();
            }
        });
    }

    private void showCardNumDialog() {
        CharSequence[] possCardAmounts = { "4", "6", "8", "10", "12", "14", "16", "18", "20" };
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setTitle(R.string.choose_card_number)
                .setCancelable(false)
                .setItems(possCardAmounts, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mNumberOfCards = Integer.parseInt(possCardAmounts[which].toString());
                        createGame();
                        buttonFlash();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void buttonFlash() {
        android.view.animation.Animation anim = new android.view.animation.AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(450);
        anim.setStartOffset(20);
        anim.setRepeatMode(android.view.animation.Animation.REVERSE);
        anim.setRepeatCount(android.view.animation.Animation.INFINITE);

        flashThread = new Thread() {
            public void run() {
                while (true) {
                while (mFlashCondition) {
                        mTryAgainButton.startAnimation(anim);
                        try {
                            flashThread.sleep(750);
                        } catch (InterruptedException e) {

                        }
                        anim.cancel();
                    }
                }
            }
        };
        flashThread.start();
    }

    private void createGame() {

        mGame = new Game(this, mNumberOfCards);
        mImageAdapter = new ImageAdapter(this);
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


    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putBoolean(KEY_FLASH_CONDITION, mFlashCondition);
        savedInstanceState.putInt(KEY_CARD_AMOUNT, mNumberOfCards);



        Card tempCard;
        int[] imageIndexes = new int[mGame.getNumberOfCards()];
        boolean[] isLocked = new boolean[mGame.getNumberOfCards()];
        int[] currentFacings = new int[mGame.getNumberOfCards()];


        for (int i = 0; i < mGame.getNumberOfCards(); i++) {
            tempCard = mGame.getCardAt(i);
            imageIndexes[i] = tempCard.getIndex();
            isLocked[i] = tempCard.isCardLocked();
            currentFacings[i] = tempCard.getCurrentFacing();
        }

        savedInstanceState.putIntArray(KEY_IMAGE_INDEXES, imageIndexes);
        savedInstanceState.putBooleanArray(KEY_IS_LOCKED, isLocked);
        savedInstanceState.putIntArray(KEY_CURRENT_FACINGS, currentFacings);
        savedInstanceState.putInt(KEY_SCORE, mGame.getScore());


    }*/
}