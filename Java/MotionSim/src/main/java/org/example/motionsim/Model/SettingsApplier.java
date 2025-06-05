package org.example.motionsim.Model;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import org.example.motionsim.Controller.LanguageController;

public final class SettingsApplier {
    public static void applyToScene(Scene scene) {
        UserSettings userSettings = Session.get().getSettings();

        LanguageController.setLanguage(userSettings.getLanguage());

        Pane rootPane = (Pane) scene.getRoot();
        ThemeUtil.setTheme(userSettings.getTheme());
        ThemeUtil.applyThemeToPane(rootPane);

        MusicManager.get().setVolume(userSettings.getMusicVolume());
        MusicManager.get().setMute(userSettings.isMusicMuted());
    }

    private SettingsApplier(){}
}
