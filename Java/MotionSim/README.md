# MotionSim

MotionSim is a JavaFX application that demonstrates spring physics and projectile motion.  Users can sign up, log in and configure application settings before running an interactive simulation.  The project includes a Maven build with unit tests for the physics model.

## Features
- **Spring simulation** – drag the spring to launch a projectile and observe position and energy graphs in real time.
- **User accounts** – sign up and log in; accounts are stored in `Data/users.json`.
- **Settings screen** – choose language (English or French), theme/wallpaper, difficulty level and music volume.
- **Music playback** – background music with volume and mute controls.
- **User manual** – accessible from the menu describing how the simulation works.

## Requirements
- Java 22+
- Maven 3 (the project includes the Maven wrapper script `mvnw`)

## Building
```bash
cd MotionSim
./mvnw clean package
```

## Running
```bash
./mvnw javafx:run
```
The application launches at the login screen.  Create a new account or use one of the accounts in `users.json`.

## Testing
```bash
./mvnw test
```

## Project Layout
- `src/main/java` – Java source code for controllers and models
- `src/main/resources` – FXML layouts, images, music and sample data
- `src/test/java` – JUnit tests for the physics engine

## Credits
Developed by Luca Furino, Tioluwani Adesina, Michel Al Khouri and Alessandro Pomponi.