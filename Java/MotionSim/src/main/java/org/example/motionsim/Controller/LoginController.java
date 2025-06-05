package org.example.motionsim.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.motionsim.Model.*;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private ToggleButton hidePasswordBtn;
    @FXML private Button loginBtn;
    @FXML private Button signUpBtn;
    @FXML private AnchorPane LoginScreen;
    @FXML private Text loginNotSuccessfulMsg;
    @FXML private Pane SpringPane;
    private TextField visiblePasswordField = new TextField();
    private List<User> userList;
    private final File userFile = new File("Data/users.json");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userList = UserStore.load();
        passwordToggle();
    }

    @FXML
    private void handleLoginBtn(ActionEvent e) throws IOException {

        String email = emailField.getText().trim();
        String pw = getPasswordText().trim();

        User found = userList.stream().filter(u -> u.getEmail().equals(email) && u.getPassword().equals(pw)).findFirst().orElse(null);

        if (found != null) {
            Session.set(found);                 // remember who is logged in
            GameSettings.setDifficulty(Session.get().getSettings().getDifficulty());
            loginNotSuccessfulMsg.setOpacity(0);
            Stage stage = (Stage) loginBtn.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/motionsim/StartingMenu.fxml"));
            Scene scene = new Scene(root);
            SettingsApplier.applyToScene(scene);
            stage.setScene(scene);
        } else {
            loginNotSuccessfulMsg.setOpacity(1);
        }
    }

    @FXML
    private void handleSignUpBtn(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/motionsim/SignUpScreen.fxml"));
            Stage stage = (Stage) signUpBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void passwordToggle() {
        visiblePasswordField.setLayoutX(passwordField.getLayoutX());
        visiblePasswordField.setLayoutY(passwordField.getLayoutY());
        visiblePasswordField.setPrefWidth(passwordField.getPrefWidth());
        visiblePasswordField.setPrefHeight(passwordField.getPrefHeight());
        visiblePasswordField.setVisible(false);

        LoginScreen.getChildren().add(visiblePasswordField);

        visiblePasswordField.managedProperty().bind(visiblePasswordField.visibleProperty());
        passwordField.managedProperty().bind(passwordField.visibleProperty());

        hidePasswordBtn.setOnAction(e -> {
            if (hidePasswordBtn.isSelected()) {
                visiblePasswordField.setText(passwordField.getText());
                visiblePasswordField.setVisible(true);
                passwordField.setVisible(false);
                hidePasswordBtn.setText("Show Password");
            } else {
                passwordField.setText(visiblePasswordField.getText());
                passwordField.setVisible(true);
                visiblePasswordField.setVisible(false);
                hidePasswordBtn.setText("Hide Password");
            }
        });

        visiblePasswordField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!passwordField.isVisible()) passwordField.setText(newVal);
        });

        passwordField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!visiblePasswordField.isVisible()) visiblePasswordField.setText(newVal);
        });
    }

    private String getPasswordText() {
        return passwordField.isVisible() ? passwordField.getText() : visiblePasswordField.getText();
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public TextField getVisiblePasswordField() {
        return visiblePasswordField;
    }
}
