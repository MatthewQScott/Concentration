package com.theguild.cs2450.concentration;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

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
    private ArrayList<Integer> mCardFronts = new ArrayList<>();
    private boolean[] mIsCardLocked;
    private ImageView[] mCardImageViews;


    private GridView mGridView;
    private Button mX;
    private Button mY;
    private Button vSpace;
    private Button hSpace;
    private Button mStrech;
    private Button mColCount;
    private Button mColSize;

    private void testButtonWirings() {
        mX = findViewById(R.id.xxx);
        mY = findViewById(R.id.yyy);
        vSpace = findViewById(R.id.vspace);
        hSpace = findViewById(R.id.hspace);
        mStrech = findViewById(R.id.stretchmode);
        mColCount = findViewById(R.id.colcount);
        mColSize = findViewById(R.id.colsize);

        mX.setOnClickListener(new View.OnClickListener() {
            int horiSize = 0;
            double vertSize = 0;
            @Override
            public void onClick(View v) {
                for (int index = 0; index < mCardAmount; index++) {

                    int height = mCardImageViews[index].getLayoutParams().height;
                    vertSize = horiSize * 3 / 4;
                    mCardImageViews[index].setLayoutParams(new GridView.LayoutParams(horiSize,
                            (int)vertSize));

                }
                mGridView.setColumnWidth(horiSize);

                horiSize = (horiSize + 40) % 800;
            }
        });
        mY.setOnClickListener(new View.OnClickListener() {
            int vertSize = 0;
            @Override
            public void onClick(View v) {
                for (int index = 0; index < mCardAmount; index++) {
                    int width = mCardImageViews[index].getLayoutParams().width;
                    mCardImageViews[index].setLayoutParams(new GridView.LayoutParams(width, vertSize));
                }
                vertSize = (vertSize + 100) % 800;
            }
        });

        vSpace.setOnClickListener(new View.OnClickListener() {
            int spacev= 0;
            @Override
            public void onClick(View v) {
                mGridView.setVerticalSpacing(spacev);
                spacev = (spacev + 20) % 100;
            }
        });

        hSpace.setOnClickListener(new View.OnClickListener() {
            int spacex= 0;
            @Override
            public void onClick(View v) {
                mGridView.setHorizontalSpacing(spacex);
                spacex = (spacex + 20) % 100;
            }
        });

        mStrech.setOnClickListener(new View.OnClickListener() {
            int strech = 0;
            @Override
            public void onClick(View v) {
                mGridView.setStretchMode(strech);
                strech = (strech + 1) % 4;
            }
        });

        mColCount.setOnClickListener(new View.OnClickListener() {
            int colc = 1;
            @Override
            public void onClick(View v) {
                mGridView.setNumColumns((int) Math.ceil(Math.sqrt(mCardAmount)));
//                colc = ((colc)  % 7) + 1;
            }
        });

        mColSize.setOnClickListener(new View.OnClickListener() {
            int colsi = 50;
            @Override
            public void onClick(View v) {
                mGridView.setColumnWidth(colsi);
                colsi = (colsi) % 500 + 100;
            }
        });

    }


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
        testButtonWirings();
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
int xsize;
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

        // show the words in the image views at their respective positions
        mCardImageViews = new ImageView[mCardAmount];
        Configuration config = getResources().getConfiguration();
        int width = config.smallestScreenWidthDp;
//        if (mCardAmount > 4)
            xsize = (int)(width / Math.ceil(Math.sqrt(mCardAmount)-1));
//        else
//            xsize = (int)(width / Math.sqrt(mCardAmount)) - 300;
        int ysize = (xsize * 4) / 3;
        for (int index = 0; index < mCardAmount; index++) {
            mCardImageViews[index] = new ImageView(GameActivity.this);
           // mCardImageViews[index].setLayoutParams(new GridView.LayoutParams(250, 350));
            mCardImageViews[index].setLayoutParams(new GridView.LayoutParams(xsize,
                    ysize));

            mCardImageViews[index].setImageResource(R.drawable.a_back);
//            mCardImageViews[index].setImageResource(mCardFronts.get(index));
        }
    }

    private void displayCardGrid() {
        ImageAdapter imageAdapter = new ImageAdapter(this, mCardImageViews);
//        GridView gridView = (GridView) findViewById(R.id.gridView);
        mGridView = (GridView) findViewById(R.id.gridView);
//        if (mCardAmount >= 12)
//            gridView.setNumColumns(5);
//        else if (mCardAmount >= 6)
//            gridView.setNumColumns(4);

//        gridView.setAdapter(imageAdapter);


        mGridView.setGravity(11);
        mGridView.setStretchMode(GridView.STRETCH_SPACING_UNIFORM);
        mGridView.setAdapter(imageAdapter);
        mGridView.setColumnWidth(xsize);

        mGridView.setNumColumns((int) Math.sqrt(mCardAmount));

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