package org.example.motionsim.Model;

public final class GameSettings {
    private static UserSettings.Difficulty difficulty = UserSettings.Difficulty.NORMAL;

    private GameSettings() {}

    public static UserSettings.Difficulty getDifficulty() {
        return difficulty;
    }

    public static void setDifficulty(UserSettings.Difficulty d) {
        difficulty = d;
    }
}
