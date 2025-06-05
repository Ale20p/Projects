/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.example.motionsim.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.example.motionsim.Model.MusicManager;
import org.example.motionsim.Model.SettingsApplier;
import org.example.motionsim.Model.ThemeUtil;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
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
import org.example.motionsim.Model.SpringPhysics;

public class IPCSMFXMLSimulatorController implements Initializable {

    @FXML
    private MenuBar MenuBar;
    @FXML
    private Pane AnimationPane;
    @FXML
    private Rectangle ParamAdjustRec;
    @FXML
    private Label ParamAdjustLabel;
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
    private Rectangle GravityRec;
    @FXML
    private Label GravityLabel;
    @FXML
    private Slider GravitySlider;
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
    private Rectangle SpringConstantRec;
    @FXML
    private Label SpringConstantLabel;
    @FXML
    private Rectangle SpringConstantFieldRec;
    @FXML
    private Label SpringConstantFieldLabel;
    @FXML
    private Slider SpringConstantSlider;
    @FXML
    private Rectangle MassRec;
    @FXML
    private Label MassLabel;
    @FXML
    private Rectangle MassFieldRec;
    @FXML
    private Label MassFieldLabel;
    @FXML
    private Slider MassSlider;
    @FXML
    private Line OrthogonalToLeftLine1;
    @FXML
    private Rectangle BarGraphRec;
    @FXML
    private Label BarGraphLabel;
    @FXML
    private BarChart<String, Number> BarGraph;
    @FXML
    private NumberAxis BarChartYAxis;
    @FXML
    private CategoryAxis BarChartXAxis;
    @FXML
    private Line OrthogonalToLeftLine2;
    @FXML
    private Line OrthogonalToLeftLine3;
    @FXML
    private Rectangle ControlsRec;
    @FXML
    private Label ControlsLabel;
    @FXML
    private Line OrthogonalToLeftLine4;
    @FXML
    private Line LeftLine;
    @FXML
    private Rectangle EnterTimeRec;
    @FXML
    private Label EnterTimeLabel;
    @FXML
    private TextField EnterTimeField;
    @FXML
    private Button EnterTimeBtn;
    @FXML
    private Polyline StartShape;
    @FXML
    private Button StartBtn;
    @FXML
    private Rectangle StopShape;
    @FXML
    private Button StopBtn;
    @FXML
    private Button ResetBtn;
    @FXML
    private Polyline StartShape1;
    @FXML
    private Polyline StartShape11;
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
    private Rectangle SimulationTimeRec;
    @FXML
    private Label SimulationTimeLabel;
    @FXML
    private ComboBox<String> SimulationTimeComboBox;
    @FXML
    private Rectangle SimulationTimeFieldRec;
    @FXML
    private Label SimulationTimeFieldLabel;
    @FXML
    private Rectangle GravityFieldRec;
    @FXML
    private Pane spring;
    @FXML
    private Circle springAdjuster;
    @FXML
    private AnchorPane SpringPane;
    @FXML
    private Group groupSpring;
    @FXML
    private Label GravityFieldField;
    @FXML
    private Circle ball;
    @FXML
    private Label HeightLabel;
    @FXML
    private Rectangle HeightFieldRec;
    @FXML
    private Label HeightFieldLabel;
    @FXML
    private Rectangle HeightRec;
    @FXML
    private ImageView backgroundImageView;
    @FXML
    private Pane mainPane;
    @FXML
    private Line springPath;

    private SpringPhysics physics;
    private Point2D originalSpringEndPosition;
    private Circle launchBall;
    private boolean isLaunched = false;
    private Point2D lineStart;
    private Point2D lineEnd;
    private Point2D initialBallPosition;
    private double maxSpringDistance;
    private DoubleProperty amplitude = new SimpleDoubleProperty(0.0);
    private List<Double> originalArcPositions = new ArrayList<>();
    private List<Arc> springArcs;
    private List<Double> originalArcRadius = new ArrayList<>();
    private Label resetMessage;
    private XYChart.Series<String, Number> energySeries;
    private double storedMechanicalEnergy;
    private double storedSpringPotentialEnergy;
    private boolean springReturnInProgress = false;
    private long springReturnStartTime;
    private final double springReturnDuration = 300;
    private boolean isPausedByUser = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ThemeUtil.applyThemeToPane(AnimationPane);
        physics = SpringPhysics.getInstance();
        physics.setObject(ball);
        physics.addBoundaryLine(OrthogonalToLeftLine4);
        attachBallToLineEnd();

        HVelocityComboBox.setItems(FXCollections.observableArrayList("Kilometer(km/s)", "Meter(m/s)", "Centimeter(cm/s)", "Millimeter(mm/s)"));
        VVelocityComboBox.setItems(FXCollections.observableArrayList("Kilometer(km/s)", "Meter(m/s)", "Centimeter(cm/s)", "Millimeter(mm/s)"));
        SimulationTimeComboBox.setItems(FXCollections.observableArrayList("Hour(h)", "Minute(min)", "Second(s)", "Millisecond(ms)"));

        physics.setTimeUpdateCallback(time -> {
            Platform.runLater(() -> {
                SimulationTimeFieldLabel.setText(String.format("%.2f", time));
            });
        });

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
                    handleResetBtn(new ActionEvent());
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

        SpringConstantSlider.setMin(0);
        SpringConstantSlider.setMax(30);
        SpringConstantSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            SpringConstantFieldLabel.setText(String.format("%.2f", newValue.doubleValue()));
            physics.setSpringConstant(Double.valueOf(SpringConstantFieldLabel.getText()));
        });

        GravitySlider.setMin(0);
        GravitySlider.setMax(30);
        GravitySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            GravityFieldField.setText(String.format("%.2f", newValue.doubleValue()));
            physics.setGravity(Double.valueOf(GravityFieldField.getText()));
        });

        AngleSlider.setMin(0);
        AngleSlider.setMax(65);
        AngleFieldLabel.textProperty().bind(AngleSlider.valueProperty().asString("%.2f"));
        Rotate rotate = new Rotate();
        groupSpring.getTransforms().add(rotate);
        rotate.setPivotX(10);
        rotate.setPivotY(39);
        AngleSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            rotate.setAngle(newValue.doubleValue()*(-1));
            physics.setAngle(Double.valueOf(AngleFieldLabel.getText()));
            physics.setAngleRad(physics.calculateAngleRad(physics.getAngle()));
            updateRealTimeHeight();
        });

        energySeries = new XYChart.Series<>();
        energySeries.getData().add(new XYChart.Data<>("Mechanical", 0));
        energySeries.getData().add(new XYChart.Data<>("Kinetic", 0));
        energySeries.getData().add(new XYChart.Data<>("Spring Potential", 0));
        BarGraph.getData().add(energySeries);

        BarGraph.setLegendVisible(false);
        BarChartYAxis.setAutoRanging(false);
        BarChartYAxis.setLowerBound(0);
        BarChartYAxis.setUpperBound(250);
        BarChartYAxis.setTickUnit(50);
        BarGraph.setAnimated(false);

        Timeline energyTimeline = new Timeline(new KeyFrame(Duration.millis(1), e -> {
            updateEnergyBarChart();
        }));
        energyTimeline.setCycleCount(Timeline.INDEFINITE);
        energyTimeline.play();
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

    @FXML
    private void handleEnterTimeBtn(ActionEvent event) {
        try {
            double input = Double.valueOf(EnterTimeField.getText());

            System.out.println("[2025-03-28 19:35:24] Enter Time Button Pressed - User: LGF-1800");
            System.out.println("Requested time: " + input);

            if (!isLaunched) {
                showAlert("Invalid Input", "Please start the simulation before using Enter Time.");
                return;
            }

            if (input < 0) {
                showAlert("Invalid Input", "Please enter a non-negative time value.");
                return;
            }

            double futureX = physics.calculateX(input);
            double futureY = physics.calculateY(input);
            double radius = ((Circle) physics.getObject()).getRadius();

            double parentWidth = AnimationPane.getWidth();
            double parentHeight = AnimationPane.getHeight();

            boolean wouldBeOutOfBounds = (futureX - radius < 0) ||
                    (futureX + radius > parentWidth) ||
                    (futureY - radius < 0) ||
                    (futureY + radius > parentHeight);

            boolean wouldIntersectLines = physics.intersectsAnyLine(futureX, futureY, radius);

            if (wouldBeOutOfBounds || wouldIntersectLines) {
                showAlert("Invalid Input",
                        "The ball would be out of bounds at the specified time.\n" +
                                "Please enter a valid time value.");
                return;
            }

            boolean wasPaused = physics.isPaused();
            if (!wasPaused) {
                physics.pause();
            }

            physics.jumpTo(input);

            if (!wasPaused) {
                physics.play();
            }

            System.out.println("Successfully jumped to time: " + input);

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid number for Enter Time.");
            System.out.println("Error: Invalid number format");
        }
    }

    @FXML
    private void handleStartBtn(ActionEvent event) {
        if (!isLaunched) {
            // Initial launch
            VVelocityFieldLabel.setText(String.format("%.2f", physics.getVerticalVelocity()*(-1)));
            HVelocityFieldLabel.setText(String.format("%.2f", physics.getHorizontalVelocity()));

            physics.setOutOfBoundsCallback(isOutOfBounds -> {
                if (isOutOfBounds) {
                    Platform.runLater(() -> {
                        showResetMessage();
                        handleResetBtn(new ActionEvent());
                    });
                }
            });

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
            physics.addBoundaryLine(OrthogonalToLeftLine4);

            // Energy values
            double springPotential = SpringPhysics.calculateSpringPotentialEnergy(physics.getSpringConstant(), physics.getAmplitude()) / 1000;
            storedMechanicalEnergy = springPotential;

            isLaunched = true;
        }

        if (!isPausedByUser) {
            // Stores the energy at the moment of launch (so when kinetic energy is 0)
            storedSpringPotentialEnergy = storedMechanicalEnergy;
            springReturnInProgress = true;
            springReturnStartTime = System.currentTimeMillis();

            // animates the spring returning back to its original state when launched
            KeyValue amplitudeToZero = new KeyValue(amplitude, 0, Interpolator.EASE_OUT);
            KeyFrame springResetFrame = new KeyFrame(Duration.millis(300), amplitudeToZero);
            Timeline springReturn = new Timeline(springResetFrame);
            springReturn.play();
        }

        physics.play();

        isPausedByUser = false;
    }

    @FXML
    private void handleStopBtn(ActionEvent event) {
        if (isLaunched) {
            physics.pause();
            isPausedByUser = true;
        }
    }

    @FXML
    private void handleResetBtn(ActionEvent event) {
        physics.reset();
        isLaunched = false;

        VVelocityFieldLabel.setText("0.00");
        HVelocityFieldLabel.setText("0.00");

        if (launchBall != null) {
            AnimationPane.getChildren().remove(launchBall);
            launchBall = null;
        }

        ball.setVisible(true);
        ball.setLayoutX(originalSpringEndPosition.getX());
        ball.setLayoutY(originalSpringEndPosition.getY());

        physics.setObject(ball);
        amplitude.set(0);
        updateRealTimeHeight();
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
    private void handleVVelocityComboBox(ActionEvent event) {
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

    @FXML
    private void handleSimulationTimeComboBox(ActionEvent event) {
        String selectedUnit = SimulationTimeComboBox.getValue();
        double simulationTime = physics.getElapsedTime();

        switch (selectedUnit) {
            case "Hour(h)":
                simulationTime /= 3600;
                break;
            case "Minute(min)":
                simulationTime /= 60;
                break;
            case "Second(s)":
                // No conversion needed; it's the base unit.
                break;
            case "Millisecond(ms)":
                simulationTime *= 1000;
                break;
            default:
                showAlert("Invalid Unit", "Please select a valid unit for Simulation Time.");
                return;
        }

        SimulationTimeFieldLabel.setText(String.format("%.3f", simulationTime));
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

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void updateEnergyBarChart() {
        if (!isLaunched) {
            double amplitudeValue = Double.parseDouble(AmplitudeFieldLabel.getText());
            double potential = SpringPhysics.calculateSpringPotentialEnergy(physics.getSpringConstant(), amplitudeValue) / 1000.0;
            // Before the launch, mechanical energy equals the potential energy and kinetic energy is 0.
            energySeries.getData().get(0).setYValue(potential);  // Mechanical Energy
            energySeries.getData().get(1).setYValue(0);          // Kinetic Energy
            energySeries.getData().get(2).setYValue(potential);  // Spring Potential
        } else if (springReturnInProgress) {
            // During the spring return animation
            long elapsed = System.currentTimeMillis() - springReturnStartTime;
            double fraction = Math.min(1.0, (double) elapsed / springReturnDuration);
            double currentSpringPotential = storedSpringPotentialEnergy * (1 - fraction);
            double currentKinetic = storedSpringPotentialEnergy * fraction;
            energySeries.getData().get(0).setYValue(storedMechanicalEnergy); // Mechanical remains locked
            energySeries.getData().get(1).setYValue(currentKinetic);         // Kinetic increases
            energySeries.getData().get(2).setYValue(currentSpringPotential);  // Potential decreases
            if (fraction >= 1.0) {
                springReturnInProgress = false;
                // Final state after animation.
                energySeries.getData().get(0).setYValue(storedMechanicalEnergy);
                energySeries.getData().get(1).setYValue(storedSpringPotentialEnergy);
                energySeries.getData().get(2).setYValue(0);
            }
        } else {
            // After launch and after the spring return animation
            energySeries.getData().get(0).setYValue(storedMechanicalEnergy);
            energySeries.getData().get(1).setYValue(storedSpringPotentialEnergy);
            energySeries.getData().get(2).setYValue(0);
        }
    }

    @FXML
    private void handleMenuGoToGame() {
        try {
            stopMusic();
            SpringPhysics.resetInstance();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/motionsim/IPCSMFXMLGame.fxml"));
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

    @FXML
    public void handleFileMenuClose(ActionEvent actionEvent) {
        Platform.exit();
    }
}
