package org.example.motionsim.Model;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import java.util.Objects;

public class ThemeUtil {

    public enum AppTheme {
        DEFAULT("/wallpapers/DEFAULT.png"),
        LIGHT("/wallpapers/LIGHT.jpg"),
        DARK("/wallpapers/dark.jpg");

        private final String path;

        AppTheme(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    private static AppTheme currentTheme = AppTheme.DEFAULT;

    public static void setTheme(AppTheme theme) {
        currentTheme = theme;
    }

    public static AppTheme getCurrentTheme() {
        return currentTheme;
    }

    public static String getWallpaperPath() {
        return currentTheme.getPath();
    }

    public static void applyThemeToPane(Pane pane) {
        try {
            Image image = new Image(Objects.requireNonNull(
                    ThemeUtil.class.getResourceAsStream(currentTheme.getPath())
            ));
            BackgroundImage backgroundImage = new BackgroundImage(
                    image,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(
                            BackgroundSize.AUTO, BackgroundSize.AUTO,
                            false, false, true, true
                    )
            );
            pane.setBackground(new Background(backgroundImage));
        } catch (Exception e) {
            System.err.println("Error applying theme to pane: " + currentTheme.getPath());
            e.printStackTrace();
        }
    }
}


