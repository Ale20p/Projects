package org.example.motionsim.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class StartingMenuController implements Initializable {
    @FXML
    private Button startButton;
    @FXML
    private Button settingsButton;
    @FXML
    private Button exitButton;
    @FXML
    private Pane SpringPane;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private void handleGoToSimScene(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/motionsim/IPCSMFXMLSimulator.fxml"));
        Parent nextRoot = fxmlLoader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(nextRoot);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleGoToSettings(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/motionsim/SettingsScreen.fxml"));
        fxmlLoader.setResources(ResourceBundle.getBundle("motionsim.messages",LanguageController.getCurrentLocale()));

        Parent nextRoot = fxmlLoader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(nextRoot);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleExit(ActionEvent actionEvent) throws IOException {
        Platform.exit();
    }
}
