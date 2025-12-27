# Startup Bust

## Overview
**Startup Bust** is a command-line strategy game based on the 'Sink a Dot Com' game from *Head First Java*. The player's goal is to find and sink three hidden "Startup" companies on a 7x7 grid using the fewest number of guesses.

## How to Play
1. The game places three Startups (`poniez`, `hacqi`, `cabista`) at random locations on a 7x7 grid.
2. The grid uses:
   - **Columns**: `a` through `g`
   - **Rows**: `0` through `6`
3. Enter your guess at the prompt (e.g., `a3`, `d4`).
4. The game will respond with:
   - **"miss"**: No startup at that location.
   - **"hit"**: You hit part of a startup.
   - **"kill"**: You completely sank a startup.
5. Win by sinking all three startups. Your score is determined by the number of guesses taken.

## Technical Concepts
This program serves as a practical example of Object-Oriented Programming in Java, demonstrating:

- **Encapsulation**: Using classes (`Startup`, `StartupBust`, `GameHelper`) to hide implementation details and manage state.
- **Java Collections**: Utilizing `ArrayList<String>` and `ArrayList<Startup>` for dynamic lists that grow and shrink.
- **Input/Output**: Using `java.util.Scanner` to read user input from the command line.
- **Randomization**: Using helper logic to randomly place items on a grid while avoiding collisions.
- **Game Logic**: State management for tracking hits, misses, and game completion.

## Project Structure
- `src/main/java/StartupBust.java`: The main game class containing the `main` method and game loop.
- `src/main/java/Startup.java`: Represents a single Startup entity with its location cells.
- `src/main/java/GameHelper.java`: Utility class for grid placement and user input handling.