# PendulumSim

PendulumSim is a JavaFX application that demonstrates the motion of a simple pendulum. Users can adjust parameters such as length, amplitude, gravity and mass, while observing values like period, angular frequency and displacement in real time. The interface also includes informational and settings screens.

## Features
- **Animated pendulum** driven by physics equations
- **Adjustable parameters** for length, amplitude, gravity and mass
- **Toggleable displays** for period, angular frequency, displacement, velocity and acceleration
- **Energy visualization** showing kinetic, potential and mechanical energy
- **Settings** screen with music playback and volume control
- **Info** screen summarizing the physics background
- User preferences persisted to `Data.JSON`

## Requirements
- Java Development Kit 22 (or compatible)
- Maven 3

## Building and Running
1. Ensure Java and Maven are available on your system. The project includes the Maven wrapper so you can run using `./mvnw`.
2. Execute:

```bash
./mvnw clean javafx:run
```

The first run downloads dependencies from Maven Central. Afterwards the JavaFX window should launch showing the starting screen.

## Project Structure
```
src/main/java/pendulumsim     # Java source code
src/main/resources/pendulumsim # FXML layouts and media
```

The main entry point is `pendulumsim.Main`. FXML files define the user interface for the starting screen, pendulum physics screen, settings and info pages.

## Credits
Developed by Tristan Giannopoulos and Alessandro Pomponi.
