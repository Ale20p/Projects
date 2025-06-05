package org.example.motionsim.Model;

public class UserSettings {
    public enum Difficulty { EASY, NORMAL, HARD}

    private String language = "English";
    private ThemeUtil.AppTheme theme = ThemeUtil.AppTheme.DEFAULT;
    private Difficulty difficulty = Difficulty.NORMAL;
    private double musicVolume = 0.5;
    private boolean musicMuted = false;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public ThemeUtil.AppTheme getTheme() {
        return theme;
    }

    public void setTheme(ThemeUtil.AppTheme theme) {
        this.theme = theme;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public double getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(double musicVolume) {
        this.musicVolume = musicVolume;
    }

    public boolean isMusicMuted() {
        return musicMuted;
    }

    public void setMusicMuted(boolean musicMuted) {
        this.musicMuted = musicMuted;
    }
}
