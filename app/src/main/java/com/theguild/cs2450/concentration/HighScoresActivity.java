package com.theguild.cs2450.concentration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.TextView;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class HighScoresActivity extends AppCompatActivity {

    int lastScore;
    int highScore1;
    int highScore2;
    int highScore3;
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
        SharedPreferences prefs = getSharedPreferences("PREFS", 0);
        lastScore = prefs.getInt("Score", 0);
        highScore1 = prefs.getInt("HighScore1", 0);
        highScore2 = prefs.getInt("HighScore2", 0);
        highScore3 = prefs.getInt("HighScore3", 0);

        // updates high scores
        if(lastScore > highScore3)
        {
            highScore3 = lastScore;
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("HighScore3", highScore3);
            editor.apply();
        }

        if(lastScore > highScore2)
        {
            int temp = highScore2;
            highScore2 = lastScore;
            highScore3 = temp;
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("HighScore3", highScore3);
            editor.putInt("HighScore2", highScore2);
            editor.apply();
        }

        if(lastScore > highScore1)
        {
            int temp = highScore1;
            highScore1 = lastScore;
            highScore2 = temp;
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("HighScore2", highScore2);
            editor.putInt("HighScore1", highScore1);
            editor.apply();
        }

        textViewScore.setText("Score: " + lastScore + "\nHighScore1: " + highScore1 + "\nHighScore2: " + highScore2 + "\nHighScore3: " + highScore3);

    }


}
