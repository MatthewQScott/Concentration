package com.theguild.cs2450.concentration;

public class AudioPlayer {
    private android.media.MediaPlayer mp = new android.media.MediaPlayer();

    public void playMusic(android.content.Context c) {
        if (mp == null) {
            mp = new android.media.MediaPlayer();
        }

        mp = android.media.MediaPlayer.create(c, R.raw.music);
        mp.start();
    }

    public void stopMusic() {
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }
}
