package com.theguild.cs2450.concentration;

public class AudioTasks {
    private static int sSavedPosition = 0;
    public static boolean sMusicPaused = false;
    private static android.media.MediaPlayer sMediaPlayer;

    public static void playMusic(android.content.Context c) {
        if (sMediaPlayer == null) {
            sMediaPlayer = new android.media.MediaPlayer();
            sMediaPlayer.setLooping(true);
        }

        if (!sMusicPaused) {
            sMediaPlayer = android.media.MediaPlayer.create(c, R.raw.music);
            sMediaPlayer.seekTo(sSavedPosition);
            sMediaPlayer.start();
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
