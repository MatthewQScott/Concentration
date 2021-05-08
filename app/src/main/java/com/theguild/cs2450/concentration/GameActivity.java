package com.theguild.cs2450.concentration;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
    private Button mBackButton;
    private Button mTryAgainButton;
    private boolean mIsFlashing = true;
    private int mScore = 0;
    private int mCardSizeX;
    private ArrayList<Integer> mCardFronts = new ArrayList<>();
    private ArrayList<ImageView>  mCardImageViews = new ArrayList<>();
    private boolean[] mIsCardFlipped;
    private boolean[] mIsCardLocked;
    private int mSelectedIndex1 = -1; //-1 means none is selected
    private int mSelectedIndex2 = -1;
    private boolean mIsAllFlippingDisabled = false;
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
            mIsFlashing = savedInstanceState.getBoolean(KEY_ISFLASHING);
            mScore = savedInstanceState.getInt(KEY_SCORE);
            mCardFronts = savedInstanceState.getIntegerArrayList(KEY_CARDFRONTS);
            mIsCardFlipped = savedInstanceState.getBooleanArray(KEY_ISCARDFLIPPED);
            mIsCardLocked = savedInstanceState.getBooleanArray(KEY_ISCARDLOCKED);
            mSelectedIndex1 = savedInstanceState.getInt(KEY_SELECTEDINDEX1);
            mSelectedIndex2 = savedInstanceState.getInt(KEY_SELECTEDINDEX2);
            mIsAllFlippingDisabled = savedInstanceState.getBoolean(KEY_ISALLFLIPPINGDISABLED);
            mWhichDialogShowing = savedInstanceState.getInt(KEY_WHICHDIALOGSHOWING, mWhichDialogShowing);

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

    private void startButtonFlash() {
        android.view.animation.Animation anim = new android.view.animation.AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(450);
        anim.setStartOffset(20);
        anim.setRepeatMode(android.view.animation.Animation.REVERSE);
        anim.setRepeatCount(android.view.animation.Animation.INFINITE);
        mTryAgainButton.startAnimation(anim);
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
        int sizeY = (int)(size / Math.ceil(Math.sqrt(mCardAmount)));
        mCardSizeX = (sizeY * 3) / 4;

        // show resized card image views with back showing
        for (int index = 0; index < mCardAmount; index++) {
            ImageView view = new ImageView(GameActivity.this);
            view.setLayoutParams(new GridView.LayoutParams(mCardSizeX, sizeY));
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
        HighScoresActivity.updateScores(username, score, prefs);
        GameActivity.this.finish();
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
            if (mScore > 0) {
                mScore -= 1;
            }
        }
        mScoreTextView.setText("Your Score: " + mScore);
    }

    private void selectCard(int index) {
        if (mSelectedIndex1 == -1) {
            mSelectedIndex1 = index;
        } else if (mSelectedIndex2 == -1) {
            mSelectedIndex2 = index;
            mIsAllFlippingDisabled = true;
        }
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

    private void displayCardGrid() {
        ImageAdapter imageAdapter = new ImageAdapter(this, mCardImageViews);
        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setColumnWidth(mCardSizeX);
        gridView.setAdapter(imageAdapter);
    }

    private void endButtonFlash() {
        mTryAgainButton.clearAnimation();
    }

    public void tryAgain() {
        endButtonFlash();
        animateFlippingCard(mSelectedIndex1);
        animateFlippingCard(mSelectedIndex2);
        mIsCardFlipped[mSelectedIndex1] = false;
        mIsCardFlipped[mSelectedIndex2] = false;
        mSelectedIndex1 = -1;
        mSelectedIndex2 = -1;
        mIsAllFlippingDisabled = false;
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");





        savedInstanceState.putInt(KEY_CARDAMOUNT, mCardAmount);
        savedInstanceState.putBoolean(KEY_ISFLASHING, mIsFlashing);
        savedInstanceState.putInt(KEY_SCORE, mScore);
        savedInstanceState.putIntegerArrayList(KEY_CARDFRONTS, mCardFronts);
        savedInstanceState.putBooleanArray(KEY_ISCARDFLIPPED, mIsCardFlipped);
        savedInstanceState.putBooleanArray(KEY_ISCARDLOCKED, mIsCardLocked);
        savedInstanceState.putInt(KEY_SELECTEDINDEX1, mSelectedIndex1);
        savedInstanceState.putInt(KEY_SELECTEDINDEX2, mSelectedIndex2);
        savedInstanceState.putBoolean(KEY_ISALLFLIPPINGDISABLED, mIsAllFlippingDisabled);
        savedInstanceState.putInt(KEY_WHICHDIALOGSHOWING, mWhichDialogShowing);



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