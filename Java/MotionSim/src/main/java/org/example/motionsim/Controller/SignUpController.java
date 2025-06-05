package org.example.motionsim.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.motionsim.Model.ThemeUtil;
import org.example.motionsim.Model.User;
import org.example.motionsim.Model.UserStore;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SignUpController implements Initializable {
    @FXML
    Pane SpringPane;
    @FXML
    private Pane mainPane;
    @FXML
    private AnchorPane signUpScreen;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ToggleButton hidePasswordBtn;
    @FXML
    private Button createAccBtn;
    @FXML
    private Text problemMsg;

    private TextField visiblePasswordField = new TextField();
    private List<User> userList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userList = UserStore.load();
        passwordToggle();
    }

    @FXML
    private void handleCreateAccount(ActionEvent e) {
        String email = emailField.getText().trim();
        String password = getPasswordText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showProblem("Please fill in both fields.");
            return;
        }
        if (userList.stream().anyMatch(u -> u.getEmail().equals(email))) {
            showProblem("This email already exists.");
            return;
        }

        User newUser = new User(email, password);

        userList.add(newUser);
        UserStore.save(userList);

        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/motionsim/LoginScreen.fxml"));
            Stage stage = (Stage) createAccBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException ex) {
            showProblem("Could not load Login screen.");
            ex.printStackTrace();
        }
    }

    private void passwordToggle() {
        visiblePasswordField.setLayoutX(passwordField.getLayoutX());
        visiblePasswordField.setLayoutY(passwordField.getLayoutY());
        visiblePasswordField.setPrefWidth(passwordField.getPrefWidth());
        visiblePasswordField.setPrefHeight(passwordField.getPrefHeight());
        visiblePasswordField.setVisible(false);

        signUpScreen.getChildren().add(visiblePasswordField);

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

    private void showProblem(String message) {
        problemMsg.setText(message);
        problemMsg.setOpacity(1.0);
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public TextField getVisiblePasswordField() {
        return visiblePasswordField;
    }
}
