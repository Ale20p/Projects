package org.example.motionsim.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.scene.Node;
import org.example.motionsim.Model.*;

public class SettingsScreenController implements Initializable {
    @FXML
    private Rectangle TopRec, LanguageRec, GameplayRec, WallpaperRec, AudioRec, MusicVolumeRec, BottomRec;
    @FXML
    private Line TopLine, BottomLine;
    @FXML
    private ImageView backgroundImageView;
    @FXML
    private Label SettingsScreenLabel, LanguageLabel, GameplayLabel, WallpaperLabel, AudioLabel, MusicVolumeLabel;
    @FXML
    private ComboBox<String> LanguageComboBox, WallpaperComboBox, ThemeComboBox;
    @FXML
    private RadioButton EasyOption, NormalOption, HardOption;
    @FXML
    private Slider MusicVolumeSlider;
    @FXML
    private CheckBox MusicVolumeMuteBox;
    @FXML
    private Button DefaultBtn, SaveBtn, ExitBtn, HelpBtn;
    private Map<Label, TooltipData> tooltipDataMap = new HashMap<>();
    private ToggleGroup difficultyGroup;
    @FXML
    private Button PlayMusicBtn;
    @FXML
    private Button StopMusicBtn;
    @FXML
    private Pane MainPane;
    @FXML
    private ComboBox<String> themeComboBox;
    private final Map<String, ThemeUtil.AppTheme> wallpaperMap = new HashMap<>();
    private UserSettings userSettings;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userSettings = Session.get().getSettings();

        setupVolumeControl();
        setupTooltips();

        LanguageComboBox.getItems().addAll("English", "Français");
        // Difficulty setup
        difficultyGroup = new ToggleGroup();
        EasyOption.setToggleGroup(difficultyGroup);
        NormalOption.setToggleGroup(difficultyGroup);
        HardOption.setToggleGroup(difficultyGroup);

        // Volume Slider setup
        MusicVolumeSlider.setMin(0);
        MusicVolumeSlider.setMax(100);
        MusicVolumeSlider.setValue(50);
        MusicVolumeSlider.setShowTickLabels(true);
        MusicVolumeSlider.setShowTickMarks(true);

        LanguageComboBox.setValue(userSettings.getLanguage());
        LanguageController.setLanguage(userSettings.getLanguage());
        updateLanguage();

        ThemeUtil.setTheme(userSettings.getTheme());
        ThemeUtil.applyThemeToPane(MainPane);
        populateWallpaperCombo();

        switch (userSettings.getDifficulty()) {
            case EASY -> EasyOption.setSelected(true);
            case NORMAL -> NormalOption.setSelected(true);
            case HARD -> HardOption.setSelected(true);
        }
        GameSettings.setDifficulty(userSettings.getDifficulty());

        MusicVolumeSlider.setValue(userSettings.getMusicVolume() * 100);
        MusicVolumeMuteBox.setSelected(userSettings.isMusicMuted());
        MusicManager.get().setVolume(userSettings.getMusicVolume());
        MusicManager.get().setMute(userSettings.isMusicMuted());

        MusicManager.get().play("SettingsMenuSong.wav");
    }

    @FXML
    private void handleSaveBtn(ActionEvent e) {
        userSettings.setLanguage(LanguageComboBox.getValue());
        userSettings.setTheme(wallpaperMap.get(WallpaperComboBox.getValue()));
        userSettings.setDifficulty(EasyOption.isSelected()  ? UserSettings.Difficulty.EASY
                : NormalOption.isSelected()? UserSettings.Difficulty.NORMAL
                : UserSettings.Difficulty.HARD);
        GameSettings.setDifficulty(userSettings.getDifficulty());
        userSettings.setMusicVolume(MusicVolumeSlider.getValue() / 100.0);
        userSettings.setMusicMuted(MusicVolumeMuteBox.isSelected());

        List<User> all = UserStore.load();
        // this replaces the matching user object (by email) with Session.get()
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getEmail().equals(Session.get().getEmail())) {
                all.set(i, Session.get());
                break;
            }
        }
        UserStore.save(all);

        SettingsApplier.applyToScene(SaveBtn.getScene());
        System.out.println("Settings saved!");
    }

    private void populateWallpaperCombo() {
        wallpaperMap.clear();
        wallpaperMap.put(LanguageController.getString("settings.default"), ThemeUtil.AppTheme.DEFAULT);
        wallpaperMap.put(LanguageController.getString("settings.dark"), ThemeUtil.AppTheme.DARK);
        wallpaperMap.put(LanguageController.getString("settings.light"), ThemeUtil.AppTheme.LIGHT);

        WallpaperComboBox.getItems().setAll(wallpaperMap.keySet());

        ThemeUtil.AppTheme current = ThemeUtil.getCurrentTheme();
        wallpaperMap.forEach((label, theme) -> {
            if (theme == current) {
                WallpaperComboBox.setValue(label);
            }
        });
        if (WallpaperComboBox.getValue() == null) {
            WallpaperComboBox.setValue(LanguageController.getString("settings.default"));
        }
    }

    private void setupTooltips() {
        // Map the labels to their respective language keys and create tooltips
        tooltipDataMap.put(LanguageLabel, createTooltip(LanguageLabel, "settings.lltooltip"));
        tooltipDataMap.put(GameplayLabel, createTooltip(GameplayLabel, "settings.gltooltip"));
        tooltipDataMap.put(WallpaperLabel, createTooltip(WallpaperLabel, "settings.wltooltip"));
        tooltipDataMap.put(AudioLabel, createTooltip(AudioLabel, "settings.altooltip"));
    }

    private TooltipData createTooltip(Label label, String languageKey) {
        VBox tooltipBox = new VBox();
        tooltipBox.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-padding: 5px;");
        Popup popup = new Popup();
        Text tooltipText = new Text(LanguageController.getString(languageKey));
        tooltipText.setFill(Color.BLACK);
        tooltipBox.getChildren().add(tooltipText);
        popup.getContent().add(tooltipBox);

        label.setOnMouseEntered(e -> popup.show(label, e.getScreenX() + 10, e.getScreenY() + 10));
        label.setOnMouseExited(e -> popup.hide());
        label.setOnMouseDragged(e -> popup.hide());
        label.setOnMouseMoved(e -> {
            popup.setX(e.getScreenX() + 10);
            popup.setY(e.getScreenY() + 10);
        });

        return new TooltipData(languageKey, popup, tooltipText);
    }

    private void updateTooltips() {
        tooltipDataMap.forEach((label, tooltipData) -> {
            String updatedMessage = LanguageController.getString(tooltipData.languageKey);

            // Update the tooltip text dynamically
            tooltipData.tooltipText.setText(updatedMessage);
        });
    }

    private static class TooltipData {
        String languageKey;
        Popup popup;
        Text tooltipText;

        TooltipData(String languageKey, Popup popup, Text tooltipText) {
            this.languageKey = languageKey;
            this.popup = popup;
            this.tooltipText = tooltipText;
        }
    }

    @FXML
    private void handleLanguageComboBox(ActionEvent event) {
        String language = LanguageComboBox.getValue();
        LanguageController.setLanguage(language);
        updateLanguage();
    }

    @FXML
    private void handleWallpaperComboBox() {
        String label = WallpaperComboBox.getValue();
        if (label == null) return;

        ThemeUtil.AppTheme theme = wallpaperMap.get(label);
        if (theme != null) {
            ThemeUtil.setTheme(theme);
            ThemeUtil.applyThemeToPane(MainPane);
        }
    }

    @FXML
    private void handleMuteToggle(ActionEvent event) {
        MusicManager.get().setMute(MusicVolumeMuteBox.isSelected());
    }

    @FXML
    private void handleHelpBtn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/motionsim/UserManual.fxml"));
            Parent root = loader.load();
            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root));
            popupStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class AppTheme {
        private static String wallpaperPath = "/wallpapers/DEFAULT.png";
        public static String getWallpaperPath() {
            return wallpaperPath;
        }
    }

    @FXML
    private void handleExitBtn(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/motionsim/IPCSMFXMLGame.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            SettingsApplier.applyToScene(scene);
            stage.setScene(scene);
            stage.show();

            MusicManager.get().play("MainMenuSong.mp3");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleDefaultBtn(ActionEvent event) {
        LanguageComboBox.setValue("English");
        NormalOption.setSelected(true);
        WallpaperComboBox.setValue("Default");
        MusicVolumeSlider.setValue(50);
        System.out.println("Settings reset to default.");
    }

    private void setupVolumeControl() {
        MusicVolumeSlider.valueProperty().addListener((obs,o,n)->
                MusicManager.get().setVolume(n.doubleValue()/100.0));
    }

    //from 186 to end
    private void updateLanguage() {
        SettingsScreenLabel.setText(LanguageController.getString("settings.title"));
        LanguageLabel.setText(LanguageController.getString("settings.language"));
        GameplayLabel.setText(LanguageController.getString("settings.gameplay"));
        WallpaperLabel.setText(LanguageController.getString("settings.wallpaper"));
        AudioLabel.setText(LanguageController.getString("settings.audio"));
        MusicVolumeLabel.setText(LanguageController.getString("settings.music"));
        MusicVolumeMuteBox.setText(LanguageController.getString("settings.mute"));
        EasyOption.setText(LanguageController.getString("settings.easy"));
        NormalOption.setText(LanguageController.getString("settings.normal"));
        HardOption.setText(LanguageController.getString("settings.hard"));
        SaveBtn.setText(LanguageController.getString("settings.save"));
        ExitBtn.setText(LanguageController.getString("settings.exit"));
        HelpBtn.setText(LanguageController.getString("settings.help"));
        DefaultBtn.setText(LanguageController.getString("settings.default"));
        populateWallpaperCombo();
        updateTooltips();
    }
}