package com.theguild.cs2450.concentration;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.Collections;

public class GameActivity extends FragmentActivity {

    private static final String TAG = "GameActivity";
    private static final String KEY_CARDAMOUNT = "cardAmount";
    private static final String KEY_ISFLASHING = "isFlashing";
    private static final String KEY_SCORE = "score";
    private static final String KEY_CARDFRONTS = "cardFronts";
    private static final String KEY_ISCARDFLIPPED = "isCardFlipped";
    private static final String KEY_ISCARDLOCKED = "isCardLocked";
    private static final String KEY_SELECTEDINDEX1 = "selectedIndex1";
    private static final String KEY_SELECTEDINDEX2 = "selectedIndex2";
    private static final String KEY_ISALLFLIPPINGDISABLED = "isAllFlippingDisabled";
    private static final String KEY_WHICHDIALOGSHOWING = "mWhichDialogShowing";



    private int mCardAmount;
    private TextView mScoreTextView;
    private Button mEndGameButton;
    private Button mNewGameButton;
    private Button mTryAgainButton;
    private int mScore = 0;
    private int mCardSizeX;
    private ArrayList<Integer> mCardFronts = new ArrayList<>();
    private ArrayList<ImageView>  mCardImageViews = new ArrayList<>();
    private boolean[] mIsCardFlipped;
    private boolean[] mIsCardLocked;
    private int mSelectedIndex1 = -1; //-1 means none is selected
    private int mSelectedIndex2 = -1;
    private boolean mIsAllFlippingDisabled = false;
    private boolean mIsFlashing;
    private String mUsername;
    private int mWhichDialogShowing = 0; // 0=no dialog showing, 1=cardnumDialog showing, 2= usernamedialog

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

        if (savedInstanceState != null) {
            mCardAmount = savedInstanceState.getInt(KEY_CARDAMOUNT);
            mScore = savedInstanceState.getInt(KEY_SCORE);
            mCardFronts = savedInstanceState.getIntegerArrayList(KEY_CARDFRONTS);
            mIsCardFlipped = savedInstanceState.getBooleanArray(KEY_ISCARDFLIPPED);
            mIsCardLocked = savedInstanceState.getBooleanArray(KEY_ISCARDLOCKED);
            mSelectedIndex1 = savedInstanceState.getInt(KEY_SELECTEDINDEX1);
            mSelectedIndex2 = savedInstanceState.getInt(KEY_SELECTEDINDEX2);
            mIsAllFlippingDisabled = savedInstanceState.getBoolean(KEY_ISALLFLIPPINGDISABLED);
            mWhichDialogShowing = savedInstanceState.getInt(KEY_WHICHDIALOGSHOWING);
            mIsFlashing = savedInstanceState.getBoolean(KEY_ISFLASHING);

            if (mIsFlashing) {
                startButtonFlash();
            }

            if (mWhichDialogShowing == 1) {
                showCardCountDialog();
            } else if (mWhichDialogShowing == 2) {
                showUsernameDialog();
            } else {
                createCardViews();
                addFlipListeners();
                displayCardGrid();
            }
            mScoreTextView.setText("Your Score: " + mScore);

        } else {
            showCardCountDialog();
        }
    }

    private void wireWidgets() {
        mScoreTextView = (TextView) findViewById(R.id.score_text_view);

        mEndGameButton = (Button) findViewById(R.id.end_game_button);
        mEndGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endButtonFlash();
                mIsFlashing = false;
                mSelectedIndex1 = -1;
                mSelectedIndex2 = -1;
                mScore = 0;
                mScoreTextView.setText("Your Score: " + mScore);
                for (int cardIndex = 0; cardIndex < mCardAmount; cardIndex++) {
                    mIsAllFlippingDisabled = true;
                    if (!mIsCardFlipped[cardIndex]) {
                        mIsCardFlipped[cardIndex] = true;
                        mIsCardLocked[cardIndex] = true;
                        animateFlippingCard(cardIndex);
                    }
                }
            }
        });

        mNewGameButton = (Button) findViewById(R.id.new_game_button);
        mNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        mTryAgainButton = (Button) findViewById(R.id.try_again_button);
        mTryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryAgain();
            }
        });
    }

    private void showCardCountDialog () {
        mWhichDialogShowing = 1;
        CharSequence[] possCardAmounts = { "4", "6", "8", "10", "12", "14", "16", "18", "20" };
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setTitle(R.string.choose_card_number)
                .setCancelable(false)
                .setItems(possCardAmounts, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCardAmount = Integer.parseInt(possCardAmounts[which].toString());
                        mWhichDialogShowing = 0;
                        createGame();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createGame() {
        randomizeCards();
        createCardViews();
        addFlipListeners();
        displayCardGrid();
    }

    private void randomizeCards() {
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

        // iniialize other card attributes
        mIsCardFlipped = new boolean[mCardAmount];
        mIsCardLocked = new boolean[mCardAmount];
    }

    private void createCardViews() {
        // setup the size parameters for the cards customized for the device size
        DisplayMetrics dm = GameActivity.this.getResources().getDisplayMetrics();
        int size;
        if (dm.heightPixels > dm.widthPixels)
            size = dm.widthPixels;
        else
            size = dm.heightPixels;
        int sizeY = (int)(size / (Math.ceil(Math.sqrt(mCardAmount))+1));
        mCardSizeX = (sizeY * 3) / 4;

        // show resized card image views with back showing
        for (int index = 0; index < mCardAmount; index++) {
            ImageView view = new ImageView(GameActivity.this);
            view.setLayoutParams(new GridView.LayoutParams(mCardSizeX, sizeY * 3 / 2));
            if (mIsCardFlipped[index]) {
                view.setImageResource(mCardFronts.get(index));
            } else {
                view.setImageResource(R.drawable.a_back);
            }
            mCardImageViews.add(view);

        }
    }

    private void addFlipListeners() {
        for (ImageView view: mCardImageViews) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = mCardImageViews.indexOf(v);
                    if (!mIsCardFlipped[index] && !mIsAllFlippingDisabled) {
                        mIsCardFlipped[index] = true;
                        animateFlippingCard(index);
                        selectCard(index);
                        if (mSelectedIndex1 != -1 && mSelectedIndex2 != -1)
                            compareSelectedCards();
                        if (isGameWon())
                            showUsernameDialog();
                    }
                }
            });
        }
    }

    private void displayCardGrid() {
        ImageAdapter imageAdapter = new ImageAdapter(mCardImageViews);
        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setColumnWidth(mCardSizeX);
        gridView.setAdapter(imageAdapter);
    }

    private void animateFlippingCard(int cardIndex) {
        Animator leftIn;
        Animator leftOut;
        Animator rightIn;
        Animator rightOut;
        leftIn = AnimatorInflater.loadAnimator(GameActivity.this,
                R.animator.card_flip_left_in);
        leftOut = AnimatorInflater.loadAnimator(GameActivity.this,
                R.animator.card_flip_left_out);
        rightIn = AnimatorInflater.loadAnimator(GameActivity.this,
                R.animator.card_flip_right_in);
        rightOut = AnimatorInflater.loadAnimator(GameActivity.this,
                R.animator.card_flip_right_out);

        AnimatorSet set = new AnimatorSet();

        Animator.AnimatorListener listener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // Change the views' image half way to simulate another side
                if (mIsCardFlipped[cardIndex]) {
                    mCardImageViews.get(cardIndex).setImageResource(mCardFronts.get(cardIndex));
                } else {
                    mCardImageViews.get(cardIndex).setImageResource(R.drawable.a_back);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        };
        leftOut.addListener(listener);
        rightOut.addListener(listener);

        if (mIsCardFlipped[cardIndex]) {
            set.playSequentially(leftOut, leftIn);
        } else {
            set.playSequentially(rightOut, rightIn);
        }

        set.setTarget(mCardImageViews.get(cardIndex));
        set.start();
    }

    private void selectCard(int index) {
        if (mSelectedIndex1 == -1) {
            mSelectedIndex1 = index;
        } else if (mSelectedIndex2 == -1) {
            mSelectedIndex2 = index;
            mIsAllFlippingDisabled = true;
        }
    }

    private void compareSelectedCards() {
        if (mCardFronts.get(mSelectedIndex1) == mCardFronts.get(mSelectedIndex2)) {
            mScore += 2;
            mIsCardLocked[mSelectedIndex1] = true;
            mIsCardLocked[mSelectedIndex2] = true;
            mSelectedIndex1 = -1;
            mSelectedIndex2 = -1;
            mIsAllFlippingDisabled = false;
        } else {
            startButtonFlash();
            mIsFlashing = true;
            if (mScore > 0) {
                mScore -= 1;
            }
        }
        mScoreTextView.setText("Your Score: " + mScore);
    }

    public void tryAgain() {
        if (mSelectedIndex1 != -1 && mSelectedIndex2 != -1) {
            animateFlippingCard(mSelectedIndex1);
            animateFlippingCard(mSelectedIndex2);
            mIsCardFlipped[mSelectedIndex1] = false;
            mIsCardFlipped[mSelectedIndex2] = false;
            mSelectedIndex1 = -1;
            mSelectedIndex2 = -1;
            endButtonFlash();
            mIsFlashing = false;
            mIsAllFlippingDisabled = false;
        }
    }

    private void startButtonFlash() {
        android.view.animation.Animation anim = new android.view.animation.AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(450);
        anim.setStartOffset(20);
        anim.setRepeatMode(android.view.animation.Animation.REVERSE);
        anim.setRepeatCount(android.view.animation.Animation.INFINITE);
        mTryAgainButton.startAnimation(anim);
    }

    private void endButtonFlash() {
        mTryAgainButton.clearAnimation();
    }

    private boolean isGameWon() {
        boolean isGameWon = true;
        for(boolean isLocked: mIsCardLocked) {
            if (isLocked == false) {
                isGameWon = false;
            }
        }
        return isGameWon;
    }

    private void showUsernameDialog() {
        mWhichDialogShowing = 2;
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);

        LayoutInflater inflater = GameActivity.this.getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_name_prompt, null);
        TextView mNameEditText = dialogLayout.findViewById(R.id.username_edit_view);
        builder.setView(dialogLayout)


                .setPositiveButton(R.string.enter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mWhichDialogShowing = 0;
                        mUsername = mNameEditText.getText().toString();
                        saveHighScore(mUsername, mScore);
                    }
                });

        builder.setMessage(R.string.name_prompt_message)
                .setTitle(R.string.name_prompt_title)
                .setCancelable(false);


        AlertDialog dialog = builder.create();
        dialog.findViewById(R.id.username_edit_view);
        dialog.show();
    }

    private void saveHighScore(String username, int score) {
        SharedPreferences prefs = GameActivity.this.getSharedPreferences("PREFS", 0);
        HighScoresActivity.updateScores(username, score, prefs, mCardAmount);
        GameActivity.this.finish();
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");

        savedInstanceState.putInt(KEY_CARDAMOUNT, mCardAmount);
        savedInstanceState.putInt(KEY_SCORE, mScore);
        savedInstanceState.putIntegerArrayList(KEY_CARDFRONTS, mCardFronts);
        savedInstanceState.putBooleanArray(KEY_ISCARDFLIPPED, mIsCardFlipped);
        savedInstanceState.putBooleanArray(KEY_ISCARDLOCKED, mIsCardLocked);
        savedInstanceState.putInt(KEY_SELECTEDINDEX1, mSelectedIndex1);
        savedInstanceState.putInt(KEY_SELECTEDINDEX2, mSelectedIndex2);
        savedInstanceState.putBoolean(KEY_ISALLFLIPPINGDISABLED, mIsAllFlippingDisabled);
        savedInstanceState.putInt(KEY_WHICHDIALOGSHOWING, mWhichDialogShowing);
        savedInstanceState.putBoolean(KEY_ISFLASHING, mIsFlashing);



    }

}