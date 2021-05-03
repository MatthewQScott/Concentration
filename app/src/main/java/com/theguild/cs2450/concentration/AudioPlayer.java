package com.theguild.cs2450.concentration;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class AudioPlayer extends android.app.IntentService {

    public AudioPlayer() {
        super("AudioPlayer");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

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
        AudioTasks.playMusic(this);
    }

    @Override
    public void onDestroy() {
        AudioTasks.stopMusic();
    }
}
