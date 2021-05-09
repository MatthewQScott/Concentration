package com.theguild.cs2450.concentration;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class HighScoresActivity extends AppCompatActivity {

    private Button hsBackButton;
    static int highScore1;
    static int highScore2;
    static int highScore3;
    static int numCards;
    static String prevName;
    static String name1;
    static String name2;
    static String name3;
    TextView textViewScore;

    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(getApplicationContext(), Game.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        CharSequence[] possCardAmounts = { "4", "6", "8", "10", "12", "14", "16", "18", "20" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.choose_card_number)
                .setCancelable(false)
                .setItems(possCardAmounts, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        numCards = Integer.parseInt(possCardAmounts[which].toString());
                        SharedPreferences prefs = getSharedPreferences("PREFS", 0);
                        highScore1 = prefs.getInt("HighScore1-" + Integer.toString(numCards), 0);
                        highScore2 = prefs.getInt("HighScore2-" + Integer.toString(numCards), 0);
                        highScore3 = prefs.getInt("HighScore3-" + Integer.toString(numCards), 0);
                        name1 = prefs.getString("Name1-" + Integer.toString(numCards), "YOURNAMEHERE");
                        name2 = prefs.getString("Name2-" + Integer.toString(numCards), "YOURNAMEHERE");
                        name3 = prefs.getString("Name3-" + Integer.toString(numCards), "YOURNAMEHERE");

                        textViewScore.setText("High Scores for " + Integer.toString(numCards) + "-card games:" + "\n1. " + name1 + " " + highScore1 + "\n2. " + name2 + " " + highScore2 + "\n3. " + name3 + " " + highScore3);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        System.out.println(numCards);

        setContentView(R.layout.activity_high_scores);
        textViewScore = (TextView) findViewById(R.id.textViewScore);
        textViewScore.setTextSize(36);

        hsBackButton = (Button) findViewById(R.id.hs_back_button);
        hsBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //comment out next two lines to disable automatic reset of scores, I just did this for testing
                //final SharedPreferences pref = getSharedPreferences("PREFS", android.content.Context.MODE_PRIVATE);
                //pref.edit().clear().commit();
                finish();
            }
        });
    }

    // updates high scores
    public static void updateScores(String name, int score, SharedPreferences prefs, int noOfCards) {
        SharedPreferences.Editor editor = prefs.edit();

        if (score > highScore3) {
            editor.putString("Name3-" + Integer.toString(noOfCards), name3);
            editor.putInt("HighScore3-" + Integer.toString(noOfCards), highScore3);
            editor.apply();
        }

        if (score > highScore2) {
            int temp = prefs.getInt("HighScore2-" + Integer.toString(noOfCards), 0);
            String tempName = prefs.getString("Name2-" + Integer.toString(noOfCards), "YOURNAMEHERE");
            editor.putString("Name3-" + Integer.toString(noOfCards), tempName);
            editor.putString("Name2-" + Integer.toString(noOfCards), name);
            editor.putInt("HighScore3-" + Integer.toString(noOfCards), temp);
            editor.putInt("HighScore2-" + Integer.toString(noOfCards), score);
            editor.apply();
        }

        if (score > highScore1) {
            int temp = prefs.getInt("HighScore1-" + Integer.toString(noOfCards), 0);
            String tempName = prefs.getString("Name1-" + Integer.toString(noOfCards), "YOURNAMEHERE");
            editor.putString("Name2-" + Integer.toString(noOfCards), tempName);
            editor.putString("Name1-" + Integer.toString(noOfCards), name);
            editor.putInt("HighScore2-" + Integer.toString(noOfCards), temp);
            editor.putInt("HighScore1-" + Integer.toString(noOfCards), score);
            editor.apply();
        }
    }


}
