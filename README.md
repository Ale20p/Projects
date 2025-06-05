# Shell Simulation Script

This script simulates a basic shell environment that can handle built-in commands and execute external programs.

## Table of Contents
- [Usage](#usage)
- [Commands](#commands)
- [Features](#features)
- [Command Execution](#command-execution)
- [Example Usage](#example-usage)
- [Installation](#installation)
- [Git Integration](#git-integration)
- [Docker Integration](#docker-integration)
- [Contributing](#contributing)
- [License](#license)

## Usage
Run the script and interact with the shell through the prompt.

## Commands
- `exit`: Exit the shell.
- `echo`: Print the given arguments. Usage: `echo [arguments]`
- `type`: Indicate whether the command is a shell builtin or an external program. Usage: `type [command]`
- `pwd`: Print the current working directory.
- `cd`: Change the current working directory. Usage: `cd [directory]`
- `alias`: Create a command alias. Usage: `alias name='command'`
- `unalias`: Remove a command alias. Usage: `unalias name`
- `set`: Set an environment variable. Usage: `set VAR=VALUE`
- `unset`: Remove an environment variable. Usage: `unset VAR`
- `source`: Source a script. Usage: `source filename`
- `help`: Display information about built-in commands. Usage: `help [command]`
- `[external commands]`: Run external commands available in the system's PATH.

## Features
- **Command History**: Saves and loads command history from `~/.shell_history`.
- **Command Aliases**: Create and remove command aliases.
- **Environment Variables**: Set and unset environment variables.
- **Command Completion**: Supports tab completion for built-in commands.
- **External Commands**: Executes external commands available in the system's PATH.
- **Script Sourcing**: Source scripts and execute them line by line.
- **Loop Handling**: Supports `while` and `for` loops in sourced scripts.

## Command Execution
- Reads user input and splits it into components (command and arguments).
- Handles built-in commands and attempts to execute external commands using `subprocess.run()`.
- Captures standard output and error, treating the output as a string.
- Raises exceptions for non-zero exit statuses and handles command not found errors and execution failures.

## Example Usage
To use the script, run it and interact with the shell through the prompt. The prompt will display `$` for new commands and `>` for partial commands.


