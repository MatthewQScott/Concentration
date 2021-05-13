//Author: Kaye Reeves Bronco #014865383

package com.theguild.cs2450.concentration;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class AudioService extends android.app.IntentService {
    private static int sSavedPosition = 0; //stores the position in the music track
    public static boolean sMusicPaused = false; //determines whether music is currently paused
    private static android.media.MediaPlayer sMediaPlayer;

    public AudioService() {
        super("AudioService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }
// wow@!
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();
    }

    @Override
    public void onCreate() {
        playMusic(this);
    }

    @Override
    public void onDestroy() {
        stopMusic();
    }

    public static void playMusic(android.content.Context c) {
        if (sMediaPlayer == null) {
            sMediaPlayer = new android.media.MediaPlayer();
        }

        if (!sMusicPaused) {
            sMediaPlayer = android.media.MediaPlayer.create(c, R.raw.music);
            sMediaPlayer.seekTo(sSavedPosition); //save position in track for resume
            sMediaPlayer.start();
            sMediaPlayer.setLooping(true); //set music to loop
        }
        else {
            //do nothing (stops disabled music from resuming when device is rotated)
        }
    }

    public static void stopMusic() {
        if (sMediaPlayer != null) {
            sMediaPlayer.pause();
            sSavedPosition = sMediaPlayer.getCurrentPosition(); //saves current place in music track
            sMediaPlayer.release();
            sMediaPlayer = null;
        }
    }
}
