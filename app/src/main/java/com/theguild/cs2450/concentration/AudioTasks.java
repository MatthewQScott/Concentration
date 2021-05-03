package com.theguild.cs2450.concentration;

public class AudioTasks {
    private static int savedPosition = 0;
    public static boolean musicPaused = false;
    private static android.media.MediaPlayer mp;

    public static void playMusic(android.content.Context c) {
        if (mp == null) {
            mp = new android.media.MediaPlayer();
            mp.setLooping(true);
        }

        if (!musicPaused) {
            mp = android.media.MediaPlayer.create(c, R.raw.music);
            mp.seekTo(savedPosition);
            mp.start();
        }
        else {
            //do nothing (stops disabled music from resuming when device is rotated)
        }
    }

    public static void stopMusic() {
        if (mp != null) {
            mp.pause();
            savedPosition = mp.getCurrentPosition(); //saves current place in music track
            mp.release();
            mp = null;
        }
    }
}
