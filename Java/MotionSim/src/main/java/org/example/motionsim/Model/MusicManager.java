package org.example.motionsim.Model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicManager {
    private static final MusicManager INSTANCE = new MusicManager();
    public static MusicManager get() {
        return INSTANCE;
    }

    private MediaPlayer player;
    private double volume = 0.5;
    private boolean muted = false;

    public void play(String resourcePath) {
        stop();
        try {
            String url = getClass().getResource("/motionsim/songs/" + resourcePath).toString();
            player = new MediaPlayer(new Media(url));
            applyVolMute();
            player.play();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void stop() {
        if (player != null) player.stop();
        player = null;
    }

    public void setVolume(double v) {
        volume = Math.max(0, Math.min(1, v));
        applyVolMute();
    }

    public void setMute(boolean m) {
        muted = m;
        applyVolMute();
    }

    private void applyVolMute() {
        if (player != null) {
            player.setVolume(muted ? 0 : volume);
            player.setMute(muted);
        }
    }

    private MusicManager(){}
}
