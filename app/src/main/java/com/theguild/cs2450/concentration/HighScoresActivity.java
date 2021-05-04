package com.theguild.cs2450.concentration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class HighScoresActivity extends AppCompatActivity {

    private Button hsBackButton;
    static int prevScore;
    static int highScore1;
    static int highScore2;
    static int highScore3;
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

        SharedPreferences prefs = getSharedPreferences("PREFS", 0);
        prevScore = prefs.getInt("Score", 0);
        prevName = prefs.getString("Name", "");
        highScore1 = prefs.getInt("HighScore1", 0);
        highScore2 = prefs.getInt("HighScore2", 0);
        highScore3 = prefs.getInt("HighScore3", 0);
        name1 = prefs.getString("Name1", "ABC");
        name2 = prefs.getString("Name2", "DEF");
        name3 = prefs.getString("Name3", "GHI");

        textViewScore.setText("High Scores" + "\nPrevious Score: " + prevScore + "\n1. " + name1 + " " + highScore1 + "\n2. " + name2 + " " + highScore2 + "\n3. " + name3 + " " + highScore3);
    }

    // updates high scores
    public static void updateScores(String name, int score, SharedPreferences prefs) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Name", name);
        editor.putInt("Score", score);

        if (score > highScore3) {
            highScore3 = score;
            name3 = name;
            editor.putString("Name3", name3);
            editor.putInt("HighScore3", highScore3);
            editor.apply();
        }

        if (score > highScore2) {
            int temp = highScore2;
            String tempName = name2;
            highScore2 = score;
            name2 = name;
            highScore3 = temp;
            name3 = tempName;
            editor.putString("Name3", name3);
            editor.putString("Name2", name2);
            editor.putInt("HighScore3", highScore3);
            editor.putInt("HighScore2", highScore2);
            editor.apply();
        }

        if (score > highScore1) {
            int temp = highScore1;
            String tempName = name1;
            highScore1 = score;
            name1 = name;
            highScore2 = temp;
            name2 = tempName;
            editor.putString("Name2", name2);
            editor.putString("Name1", name1);
            editor.putInt("HighScore2", highScore2);
            editor.putInt("HighScore1", highScore1);
            editor.apply();
        }
    }


}
