# Simple Startup

## Project Overview
Simple Startup is a minimalist Java console game inspired by the classic "Battleship" exercises from *Head First Java*. The player attempts to "sink" a startup by guessing the integer positions of a three-cell target on a one-dimensional grid. Each turn the player enters a number, receives feedback (`hit`, `miss`, or `kill`), and the game tracks how many guesses were required to win. The codebase also contains a `SimpleStartupTestDrive` class that demonstrates how the core game logic responds to a scripted guess sequence, which can be used as a starting point for automated tests or experimentation with the game logic.

### Key Components
- `SimpleStartup` manages the target's location, evaluates guesses, and terminates the game when all target cells are hit.
- `GameHelper` wraps console input, prompting the user and returning numeric guesses via Java's `Scanner` API.
- `SimpleStartupTestDrive` offers a lightweight test harness that calls into the game logic without user interaction.

## Programming and Java Concepts Demonstrated
- **Object-Oriented Design** – The game state and behavior are encapsulated within dedicated classes (`SimpleStartup`, `GameHelper`), highlighting separation of concerns and encapsulation.
- **Arrays and Iteration** – Target locations are stored in an `int[]`, and guesses are checked by iterating through the array with an enhanced `for` loop.
- **Control Flow** – The main game loop employs conditionals (`if`/`else`) and a `while` loop to continue prompting the player until the startup is "killed".
- **Random Number Generation** – `Math.random()` is used to place the startup at a random location each time the game is launched, demonstrating basic pseudo-random number usage in Java.
- **User Input Handling** – `Scanner` reads user guesses from standard input, illustrating console-based interaction and parsing numeric input.
- **State Management** – The `numOfHits` counter tracks cumulative hits, showing how object fields maintain state across method calls.
- **Testing Mindset** – `SimpleStartupTestDrive` mimics a unit test by running the game logic with known inputs and verifying the outcome, encouraging test-driven thinking even in small programs.
