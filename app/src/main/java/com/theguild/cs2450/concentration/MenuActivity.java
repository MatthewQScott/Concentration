package com.theguild.cs2450.concentration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    AudioPlayer audio;
    private Button mPlayButton;
    private Button mHighScoresButton;
    private Button mMusicButton;


    Animation mAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        if (audio == null) {
            audio = new AudioPlayer();
            audio.playMusic(MenuActivity.this);
        }

        mPlayButton = (Button) findViewById(R.id.play_button);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        mHighScoresButton = (Button) findViewById(R.id.highscores_button);
        mHighScoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, HighScoresActivity.class);
                startActivity(intent);
            }
        });

        mMusicButton = (Button) findViewById(R.id.music_button);

        mMusicButton.setOnClickListener(new View.OnClickListener() {
            public boolean musicEnabled = true;

            @Override
            public void onClick(View v) {
                if (musicEnabled) {
                    audio.stopMusic();
                    musicEnabled = false;
                    mMusicButton.setText("Enable Music");
                }
                else {
                    audio.playMusic(MenuActivity.this);
                    musicEnabled = true;
                    mMusicButton.setText("Disable Music");
                }
            }
        });

    }




}