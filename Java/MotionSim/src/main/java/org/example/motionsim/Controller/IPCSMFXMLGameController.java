package org.example.motionsim.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.motionsim.Model.*;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class IPCSMFXMLGameController implements Initializable {

    @FXML
    private MenuBar MenuBar;
    @FXML
    private Pane AnimationPane;
    @FXML
    private Rectangle GameReadingsRec;
    @FXML
    private Label GameReadingsLabel;
    @FXML
    private Line OrthogonalToLeftLine;
    @FXML
    private Rectangle AmplitudeRec;
    @FXML
    private Label AmplitudeLabel;
    @FXML
    private Rectangle AmplitudeFieldRec;
    @FXML
    private Label AmplitudeFieldLabel;
    @FXML
    private Slider AmplitudeSlider;
    @FXML
    private Rectangle AngleRec;
    @FXML
    private Label AngleLabel;
    @FXML
    private Rectangle AngleFieldRec;
    @FXML
    private Label AngleFieldLabel;
    @FXML
    private Slider AngleSlider;
    @FXML
    private Line OrthogonalToLeftLine3;
    @FXML
    private Rectangle ControlsRec;
    @FXML
    private Label ControlsLabel;
    @FXML
    private Line OrthogonalToLeftLine4;
    @FXML
    private Polyline StartShape;
    @FXML
    private Button StartBtn;
    @FXML
    private Button ResetBtn;
    @FXML
    private Polyline StartShape1;
    @FXML
    private Polyline StartShape11;
    @FXML
    private Rectangle VVelocityRec;
    @FXML
    private Label VVelocityLabel;
    @FXML
    private ComboBox<String> VVelocityComboBox;
    @FXML
    private Rectangle VVelocityFieldRec;
    @FXML
    private Label VVelocityFieldLabel;
    @FXML
    private Rectangle TimeRemainingRec;
    @FXML
    private Label TimeRemainingLabel;
    @FXML
    private Rectangle TimeRemainingFieldRec;
    @FXML
    private Label TimeRemainingFieldLabel;
    @FXML
    private Rectangle PointsRec;
    @FXML
    private Label PointsLabel;
    @FXML
    private Rectangle PointsFieldRec;
    @FXML
    private Label PointsFieldLabel;
    @FXML
    private Rectangle ParamAdjustRec;
    @FXML
    private Label ParamAdjustLabel;
    @FXML
    private Line OrthogonalToLeftLine2;
    @FXML
    private Line OrthogonalToLeftLine1;
    @FXML
    private Line OrthogonalToLeftLine5;
    @FXML
    private Rectangle DynamicLabelsRec;
    @FXML
    private Label DynamicLabelsLabel;
    @FXML
    private Line OrthogonalToLeftLine6;
    @FXML
    private Rectangle HVelocityRec;
    @FXML
    private Label HVelocityLabel;
    @FXML
    private ComboBox<String> HVelocityComboBox;
    @FXML
    private Rectangle HVelocityFieldRec;
    @FXML
    private Label HVelocityFieldLabel;
    @FXML
    private Line LeftLine;
    @FXML
    private Rectangle GravityRec;
    @FXML
    private Label GravityLabel;
    @FXML
    private Rectangle GravityFieldRec;
    @FXML
    private Label GravityFieldLabel;
    @FXML
    private Rectangle SpringConstantRec;
    @FXML
    private Label SpringConstantLabel;
    @FXML
    private Rectangle SpringConstantFieldRec;
    @FXML
    private Label SpringConstantFieldLabel;
    @FXML
    private Circle ball;
    @FXML
    private Line springPath;
    @FXML
    private Pane spring;
    @FXML
    private Circle springAdjuster;
    @FXML
    private AnchorPane SpringPane;
    @FXML
    private Group groupSpring;
    @FXML
    private Rectangle MassRec;
    @FXML
    private Rectangle MassFieldRec;
    @FXML
    private Label MassLabel;
    @FXML
    private Label MassFieldLabel;
    @FXML
    private Slider MassSlider;
    @FXML
    private Label HeightLabel;
    @FXML
    private Label HeightFieldLabel;
    @FXML
    private Rectangle HeightFieldRec;
    @FXML
    private Rectangle HeightRec;
    @FXML
    private ImageView backgroundImageView;
    @FXML
    private Line RightBorderLine;
    @FXML
    private Pane yourMainPane;
    @FXML
    private ImageView Target;

    private SpringPhysics physics;
    private Point2D originalSpringEndPosition;
    private List<Arc> springArcs;
    private List<Double> originalArcRadius = new ArrayList<>();
    private List<Double> originalArcPositions = new ArrayList<>();
    private Point2D initialBallPosition;
    private Point2D lineStart;
    private Point2D lineEnd;
    private DoubleProperty amplitude = new SimpleDoubleProperty(0.0);
    private double maxSpringDistance;
    private Circle launchBall;
    private boolean isLaunched = false;
    private Label resetMessage;
    private Timeline gameClock;
    private Timeline targetTimer;
    private Timeline collisionTimer;
    private int totalSecondsRemaining;
    private int perTargetSecondsRemaining;
    private boolean gameStarted = false;
    private boolean clockRunning = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        GameSettings.setDifficulty(Session.get().getSettings().getDifficulty());
        ThemeUtil.applyThemeToPane(AnimationPane);
        physics = SpringPhysics.getInstance();
        physics.setObject(ball);
        physics.addBoundaryLine(RightBorderLine);
        attachBallToLineEnd();

        HVelocityComboBox.setItems(FXCollections.observableArrayList("Kilometer(km/s)", "Meter(m/s)", "Centimeter(cm/s)", "Millimeter(mm/s)"));
        VVelocityComboBox.setItems(FXCollections.observableArrayList("Kilometer(km/s)", "Meter(m/s)", "Centimeter(cm/s)", "Millimeter(mm/s)"));

        physics.setVelocityUpdateCallback(velocities -> {
            Platform.runLater(() -> {
                VVelocityFieldLabel.setText(String.format("%.2f", -velocities.getY())); // Negative because Y is inverted
                HVelocityFieldLabel.setText(String.format("%.2f", velocities.getX()));
            });
        });

       physics.setOutOfBoundsCallback(isOutOfBounds -> {
            if (isOutOfBounds) {
                Platform.runLater(() -> {
                    showResetMessage();
                    resetBallOnly();
                });
            }
        });

        AmplitudeSlider.setMin(0);
        AmplitudeSlider.setMax(maxSpringDistance);
        amplitude.bindBidirectional(AmplitudeSlider.valueProperty());
        amplitude.addListener((obs, oldVal, newVal) -> {
            AmplitudeFieldLabel.setText(String.format("%.2f", newVal.doubleValue()));
            updateBallPosition(newVal.doubleValue());
            physics.setAmplitude(Double.valueOf(AmplitudeFieldLabel.getText()));
            updateRealTimeHeight();
        });

        springArcs = new ArrayList<>();
        for (Node node : spring.getChildren()) {
            if (node instanceof Arc) {
                Arc arc = (Arc) node;
                springArcs.add(arc);
                originalArcRadius.add(arc.getRadiusX());
                originalArcPositions.add(arc.getLayoutX());
            }
        }

        MassSlider.setMin(0);
        MassSlider.setMax(15);
        MassSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            MassFieldLabel.setText(String.format("%.2f", newValue.doubleValue()));
            physics.setMass(Double.valueOf(MassFieldLabel.getText()));
        });

        AngleSlider.setMin(0);
        AngleSlider.setMax(65);
        AngleFieldLabel.textProperty().bind(AngleSlider.valueProperty().asString("%.2f"));
        Rotate rotate = new Rotate();
        groupSpring.getTransforms().add(rotate);
        rotate.setPivotX(30);
        rotate.setPivotY(89);
        AngleSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            rotate.setAngle(newValue.doubleValue()*(-1));
            physics.setAngle(Double.valueOf(AngleFieldLabel.getText()));
            physics.setAngleRad(physics.calculateAngleRad(physics.getAngle()));
            updateRealTimeHeight();
        });
        physics.setGravity(Double.valueOf(GravityFieldLabel.getText()));
        physics.setSpringConstant(Double.valueOf(SpringConstantFieldLabel.getText()));

        Image targetImage = new Image(getClass().getResource("/motionsim/target.png").toExternalForm());
        Target.setImage(targetImage);

        MusicManager.get().play("MainMenuSong.mp3");
    }

    private void updateRealTimeHeight() {
        double angleDegrees = Double.parseDouble(AngleFieldLabel.getText());
        double angleRadians = Math.toRadians(angleDegrees);

        double currentAmplitude = Double.parseDouble(AmplitudeFieldLabel.getText());

        double rawHeight = (maxSpringDistance - currentAmplitude) * Math.sin(angleRadians);

        if (Math.abs(rawHeight) < 1e-9) {
            rawHeight = 0.0;
        }

        HeightFieldLabel.setText(String.format("%.2f", rawHeight));
        physics.setHeight(Double.valueOf(HeightFieldLabel.getText()));
    }

    /**
     * Places the ball based on the amplitude and also compresses the arcs.
     */
    private void updateBallPosition(double newAmplitude) {
        double ratio = newAmplitude/maxSpringDistance;

        Point2D newPos = lineEnd.add(lineStart.subtract(lineEnd).multiply(ratio));
        ball.setLayoutX(newPos.getX());
        ball.setLayoutY(newPos.getY());

        compressArcs(ratio);
    }

    /**
     * This forces the ball's center to match the end of springPath.
     */
    private void attachBallToLineEnd() {
        double lineEndXInPane = springPath.getLayoutX() + springPath.getEndX();
        double lineEndYInPane = springPath.getLayoutY() + springPath.getEndY();

        originalSpringEndPosition = new Point2D(lineEndXInPane, lineEndYInPane);

        ball.setLayoutX(lineEndXInPane);
        ball.setLayoutY(lineEndYInPane);

        physics.setInitialPosition(lineEndXInPane, lineEndYInPane);

        initialBallPosition = new Point2D(lineEndXInPane, lineEndYInPane);
        lineStart = new Point2D(springPath.getLayoutX() + springPath.getStartX(),
                springPath.getLayoutY() + springPath.getStartY());
        lineEnd = initialBallPosition;

        maxSpringDistance = lineStart.distance(lineEnd);

        System.out.println("Initial Setup:");
        System.out.println("Spring End Position: " + originalSpringEndPosition);
        System.out.println("Ball Position: (" + ball.getLayoutX() + ", " + ball.getLayoutY() + ")");
    }

    /**
     * This handles dragging the ball backward along the spring path.
     */
    @FXML
    private void handleBallDrag(MouseEvent event) {
        if (isLaunched) return;

        Point2D mousePosInLocal = ball.getParent().sceneToLocal(event.getSceneX(), event.getSceneY());
        Point2D projectedPoint = projectPointOntoLine(mousePosInLocal, lineStart, lineEnd);

        if (projectedPoint.distance(lineStart) > lineStart.distance(lineEnd)) {
            return;
        }

        ball.setLayoutX(projectedPoint.getX());
        ball.setLayoutY(projectedPoint.getY());

        double currentDistance = lineEnd.distance(projectedPoint);

        amplitude.set(currentDistance);

        updateRealTimeHeight();

        double maxDistance = lineStart.distance(lineEnd);
        double compressionRatio = currentDistance/maxDistance;

        compressArcs(compressionRatio);
    }

    /**
     * Compresses arcs based on a ratio.
     */
    private void compressArcs(double compressionRatio) {

        double minRadiusFactor = 0.5;
        double minSpacingFactor = 0.2;
        double anchorX = originalArcPositions.get(0);

        for (int i = 0; i < springArcs.size(); i++) {
            Arc arc = springArcs.get(i);

            double origRadius = originalArcRadius.get(i);
            double currentFactorRadius = 1.0 - compressionRatio * (1.0 - minRadiusFactor);
            double newRadiusX = origRadius * currentFactorRadius;
            arc.setRadiusX(newRadiusX);

            double origArcX = originalArcPositions.get(i);
            // How far is this arc from the first arc
            double offset = origArcX - anchorX;

            double currentFactorSpace = 1.0 - compressionRatio * (1.0 - minSpacingFactor);
            double newArcX = anchorX + offset * currentFactorSpace;
            arc.setLayoutX(newArcX);
        }
    }

    /**
     * This projects a given point onto the line defined by two points.
     * @param projectPoint The point to project
     * @param lineStart The start of the line
     * @param lineEnd The end of the line
     * @return The projected point on the line
     */
    private Point2D projectPointOntoLine(Point2D projectPoint, Point2D lineStart, Point2D lineEnd) {
        Point2D lineVector = lineEnd.subtract(lineStart);
        Point2D pointVector = projectPoint.subtract(lineStart);

        double projectionScale = pointVector.dotProduct(lineVector) / lineVector.dotProduct(lineVector);
        projectionScale = Math.max(0, Math.min(1, projectionScale));

        return lineStart.add(lineVector.multiply(projectionScale));
    }

    @FXML
    private void handleStartBtn(ActionEvent event) {
        if (isLaunched) return;

        VVelocityFieldLabel.setText(String.format("%.2f", physics.getVerticalVelocity()*(-1)));
        HVelocityFieldLabel.setText(String.format("%.2f", physics.getHorizontalVelocity()));

        launchBall = new Circle();
        launchBall.setRadius(ball.getRadius());
        launchBall.setFill(ball.getFill());
        launchBall.setStroke(ball.getStroke());
        launchBall.setStrokeType(ball.getStrokeType());

        double angle = physics.getAngle();
        double angleRad = Math.toRadians(angle);

        Point2D ballScenePos = ball.localToScene(0, 0);
        Point2D ballPanePos = AnimationPane.sceneToLocal(ballScenePos);

        launchBall.setLayoutX(ballPanePos.getX());
        launchBall.setLayoutY(ballPanePos.getY());

        AnimationPane.getChildren().add(launchBall);

        ball.setVisible(false);

        double launchVelocity = physics.calculateLaunchVelocity(
                physics.getSpringConstant(),
                physics.getMass(),
                physics.getAmplitude()
        );

        double horizontalVelocity = launchVelocity * Math.cos(angleRad);
        double verticalVelocity = -launchVelocity * Math.sin(angleRad);

        physics.setObject(launchBall);
        physics.setInitialPosition(ballPanePos.getX(), ballPanePos.getY());
        physics.setVelocity(launchVelocity);
        physics.setHorizontalVelocity(horizontalVelocity);
        physics.setVerticalVelocity(verticalVelocity);
        physics.addBoundaryLine(RightBorderLine);

        System.out.println("\nStarting Launch\n");

        isLaunched = true;
        physics.play();

        if (!gameStarted) {
            gameStarted = true;
            randomizeTargetPosition();
            startGameClock();
            startPerTargetTimer();
        }

        if (collisionTimer == null) {
            collisionTimer = new Timeline(new KeyFrame(Duration.millis(16), e -> {
                checkCollisions();
            }));
            collisionTimer.setCycleCount(Animation.INDEFINITE);
            collisionTimer.play();
        }
    }

    @FXML
    private void handleResetBtn(ActionEvent event) {
        resetBallOnly();

        VVelocityFieldLabel.setText("0.00");
        HVelocityFieldLabel.setText("0.00");

        if (launchBall != null) {
            AnimationPane.getChildren().remove(launchBall);
            launchBall = null;
        }

        ball.setVisible(true);

        physics.setObject(ball);

        amplitude.set(0);
        isLaunched = false;

        updateRealTimeHeight();

        if (targetTimer != null)  targetTimer.stop();
        if (collisionTimer != null) collisionTimer.stop();
        if (gameClock != null) gameClock.stop();

        gameStarted = false;
        clockRunning = false;
        TimeRemainingFieldLabel.setText("180");
        PointsFieldLabel.setText("0");
    }

    @FXML
    private void handleHVelocityComboBox(ActionEvent event) {
        String selectedUnit = HVelocityComboBox.getValue();
        double HorizontalVelocity = physics.getHorizontalVelocity();

        switch (selectedUnit) {
            case "Kilometer(km/s)":
                HorizontalVelocity /= 1000;
                break;
            case "Meter(m/s)":
                // No conversion needed; it's the base unit.
                break;
            case "Centimeter(cm/s)":
                HorizontalVelocity *= 100;
                break;
            case "Millimeter(mm/s)":
                HorizontalVelocity *= 1000;
                break;
            default:
                showAlert("Invalid Unit", "Please select a valid unit for Horizontal Velocity.");
                return;
        }
        HVelocityFieldLabel.setText(String.format("%.3f", HorizontalVelocity));
    }

    @FXML
    public void handleVVelocityComboBox(ActionEvent actionEvent) {
        String selectedUnit = VVelocityComboBox.getValue();
        double VerticalVelocity = physics.getVerticalVelocity();

        switch (selectedUnit) {
            case "Kilometer(km/s)":
                VerticalVelocity /= 1000;
                break;
            case "Meter(m/s)":
                // No conversion needed; it's the base unit.
                break;
            case "Centimeter(cm/s)":
                VerticalVelocity *= 100;
                break;
            case "Millimeter(mm/s)":
                VerticalVelocity *= 1000;
                break;
            default:
                showAlert("Invalid Unit", "Please select a valid unit for Vertical Velocity.");
                return;
        }
        VVelocityFieldLabel.setText(String.format("%.3f", VerticalVelocity));
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleMenuGoToSettings() {
        try {
            stopMusic(); // Stop the main menu song before switching
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/motionsim/SettingsScreen.fxml"));
            loader.setResources(ResourceBundle.getBundle("motionsim.messages",LanguageController.getCurrentLocale()));

            Parent root = loader.load();
            Stage stage = (Stage) MenuBar.getScene().getWindow();
            Scene scene = new Scene(root);
            SettingsApplier.applyToScene(scene);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to stop any currently playing song
    private void stopMusic() {
        MusicManager.get().stop();
    }

    @FXML
    private void handleMenuGoToManual() {
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

    @FXML
    private void handleMenuGoToSandbox() {
        try {
            stopMusic();
            SpringPhysics.resetInstance();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/motionsim/IPCSMFXMLSimulator.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) MenuBar.getScene().getWindow();
            Scene scene = new Scene(root);
            SettingsApplier.applyToScene(scene);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showResetMessage() {

        if (resetMessage == null) {
            resetMessage = new Label("BALL RESET OUT OF BOUNDS");
            resetMessage.setTextFill(Color.RED);
            resetMessage.setFont(new Font("System Bold", 24));
            resetMessage.setOpacity(0); // Start invisible
        }

        resetMessage.setLayoutX((AnimationPane.getWidth() - resetMessage.getWidth()) / 2);
        resetMessage.setLayoutY((AnimationPane.getHeight() - resetMessage.getHeight()) / 2);

        if (!AnimationPane.getChildren().contains(resetMessage)) {
            AnimationPane.getChildren().add(resetMessage);
        }

        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), resetMessage);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), resetMessage);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        PauseTransition pause = new PauseTransition(Duration.millis(600));

        SequentialTransition sequence = new SequentialTransition(fadeIn, pause, fadeOut);
        sequence.setOnFinished(e -> AnimationPane.getChildren().remove(resetMessage));

        sequence.play();
    }

    private void randomizeTargetPosition() {
        double paneW = AnimationPane.getWidth();
        double paneH = AnimationPane.getHeight();
        double targetW = Target.getBoundsInParent().getWidth();
        double targetH = Target.getBoundsInParent().getHeight();

        double minX = paneW / 2;
        double maxX = paneW - targetW;
        double minY = 100;
        double maxY = paneH - targetH - 50;

        Target.setLayoutX(minX + Math.random() * (maxX - minX));
        Target.setLayoutY(minY + Math.random() * (maxY - minY));
    }

    private void checkCollisions() {
        if (launchBall == null || !launchBall.isVisible()) return;
        Bounds ballB = launchBall.localToScene(launchBall.getBoundsInLocal());
        Bounds targetB = Target.localToScene(Target.getBoundsInLocal());

        if (ballB.intersects(targetB)) {
            int points = Integer.parseInt(PointsFieldLabel.getText());
            PointsFieldLabel.setText(String.valueOf(points + 1));
            resetPerTargetTimer();
            randomizeTargetPosition();
        }
    }

    private void startGameClock() {
        if (clockRunning) {
            return;
        }
        clockRunning = true;

        totalSecondsRemaining = 180;
        TimeRemainingFieldLabel.setText("180");

        gameClock = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            totalSecondsRemaining--;
            TimeRemainingFieldLabel.setText(String.valueOf(totalSecondsRemaining));
            if (totalSecondsRemaining <= 0) {
                endGame();
            }
        }));
        gameClock.setCycleCount(Timeline.INDEFINITE);
        gameClock.play();
    }

    private void startPerTargetTimer() {
        UserSettings.Difficulty diff = GameSettings.getDifficulty();
        switch (diff) {
            case EASY -> perTargetSecondsRemaining = Integer.MAX_VALUE;
            case NORMAL -> perTargetSecondsRemaining = 30;
            case HARD -> perTargetSecondsRemaining = 15;
        }

        if (diff == UserSettings.Difficulty.EASY) {
            return;
        }

        targetTimer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            perTargetSecondsRemaining--;
            if (perTargetSecondsRemaining <= 0) {
                randomizeTargetPosition();
                resetPerTargetTimer();
            }
        }));
        targetTimer.setCycleCount(Animation.INDEFINITE);
        targetTimer.play();
    }

    private void resetPerTargetTimer() {
        if (targetTimer != null) targetTimer.stop();
        startPerTargetTimer();
    }

    private void endGame(){
        physics.pause();

        Label gameOverLabel = new Label("Game Over!");
        gameOverLabel.setStyle("-fx-font-size: 36px; -fx-text-fill: red;");
        gameOverLabel.setLayoutX((AnimationPane.getWidth() - 200) / 2);
        gameOverLabel.setLayoutY((AnimationPane.getHeight() - 50) / 2);
        AnimationPane.getChildren().add(gameOverLabel);

        if(launchBall != null) {
            launchBall.setVisible(false);
        }
        ball.setVisible(false);
        StartBtn.setDisable(true);
        ResetBtn.setDisable(true);

        if (targetTimer != null) {
            targetTimer.stop();
        }

        if (gameClock != null) {
            gameClock.stop();
        }

        if (collisionTimer != null) {
            collisionTimer.stop();
        }
    }

    private void resetBallOnly() {
        physics.reset();

        if (launchBall != null) {
            AnimationPane.getChildren().remove(launchBall);
            launchBall = null;
        }

        ball.setVisible(true);
        ball.setLayoutX(originalSpringEndPosition.getX());
        ball.setLayoutY(originalSpringEndPosition.getY());

        physics.setObject(ball);
        amplitude.set(0);
        isLaunched = false;
        updateRealTimeHeight();

        VVelocityFieldLabel.setText("0.00");
        HVelocityFieldLabel.setText("0.00");
    }

    @FXML
    public void handleFileMenuClose(ActionEvent actionEvent) {
        Platform.exit();
    }
}
