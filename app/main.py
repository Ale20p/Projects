"""
Shell Simulation Script

This script simulates a basic shell environment that can handle built-in
commands (`exit`, `echo`, `type`, `pwd`, `cd`, `alias`, `unalias`, `set`, `unset`) and execute external programs.

Usage:
    Run the script and interact with the shell through the prompt.

Commands:
    - exit: Exit the shell.
    - echo: Print the given arguments.
    - type: Indicate whether the command is a shell builtin or an external program.
    - pwd: Print the current working directory.
    - cd: Change the current working directory.
    - alias: Create a command alias.
    - unalias: Remove a command alias.
    - set: Set an environment variable.
    - unset: Remove an environment variable.
    - [external commands]: Run external commands available in the system's PATH.
"""
import os
import subprocess
import readline
import json
import sys
import fnmatch

BUILTINS = [
    'exit', 'echo', 'type', 'pwd', 'cd', 'alias', 'unalias', 'set', 'unset', 'source', 'help', 
    'bookmark', 'bookmarks', 'exportenv', 'importenv', 'git-init', 'git-add', 'git-commit', 
    'git-push', 'git-pull', 'git-status', 'git-log', 'docker-run', 'docker-ps', 'docker-stop', 
    'docker-rm', 'docker-build', 'docker-pull', 'docker-images'
]
HISTORY_FILE = os.path.expanduser("~/.shell_history")
ALIASES = {}
ENV_VARS = {}
BOOKMARKS_FILE = os.path.expanduser("~/.shell_bookmarks")
NAV_HISTORY = []
MAX_NAV_HISTORY = 10
BOOKMARKS = {}

HELP_MESSAGES = {
    'exit': 'exit: Exit the shell.',
    'echo': 'echo: Print the given arguments. Usage: echo [arguments]',
    'type': 'type: Indicate whether the command is a shell builtin or an external program. Usage: type [command]',
    'pwd': 'pwd: Print the current working directory.',
    'cd': 'cd: Change the current working directory. Usage: cd [directory]',
    'alias': 'alias: Create a command alias. Usage: alias name=\'command\'',
    'unalias': 'unalias: Remove a command alias. Usage: unalias name',
    'set': 'set: Set an environment variable. Usage: set VAR=VALUE',
    'unset': 'unset: Remove an environment variable. Usage: unset VAR',
    'source': 'source: Source a script. Usage: source filename',
    'help': 'help: Display information about built-in commands. Usage: help [command]',
    'bookmark': 'bookmark: Bookmark the current directory or a specified directory. Usage: bookmark [name] [directory]',
    'bookmarks': 'bookmarks: List all bookmarked directories. Usage: bookmarks',
    'exportenv': 'exportenv: Export environment variables to a file. Usage: exportenv [filename]',
    'importenv': 'importenv: Import environment variables from a file. Usage: importenv [filename]',
    'git-init': 'git-init: Initialize a new Git repository. Usage: git-init',
    'git-add': 'git-add: Add file contents to the index. Usage: git-add [file]',
    'git-commit': 'git-commit: Record changes to the repository. Usage: git-commit -m "message"',
    'git-push': 'git-push: Update remote refs along with associated objects. Usage: git-push [remote] [branch]',
    'git-pull': 'git-pull: Fetch from and integrate with another repository or a local branch. Usage: git-pull [remote] [branch]',
    'git-status': 'git-status: Show the working tree status. Usage: git-status',
    'git-log': 'git-log: Show commit logs. Usage: git-log',
    'docker-run': 'docker-run: Run a command in a new container. Usage: docker-run [options] IMAGE [COMMAND] [ARG...]',
    'docker-ps': 'docker-ps: List containers. Usage: docker-ps [options]',
    'docker-stop': 'docker-stop: Stop one or more running containers. Usage: docker-stop [OPTIONS] CONTAINER [CONTAINER...]',
    'docker-rm': 'docker-rm: Remove one or more containers. Usage: docker-rm [OPTIONS] CONTAINER [CONTAINER...]',
    'docker-build': 'docker-build: Build an image from a Dockerfile. Usage: docker-build [OPTIONS] PATH | URL | -',
    'docker-pull': 'docker-pull: Pull an image or a repository from a registry. Usage: docker-pull [OPTIONS] NAME[:TAG|@DIGEST]',
    'docker-images': 'docker-images: List images. Usage: docker-images [OPTIONS] [REPOSITORY[:TAG]]'
}

# Helper function to print error messages
def print_error_message(error, suggestion=""):
    print(f"Error: {error}")
    if suggestion:
        print(f"Suggestion: {suggestion}")

def completer(text, state):
    options = [cmd for cmd in BUILTINS + list(ALIASES.keys()) if cmd.startswith(text)]
    if not options:
        current_dir = os.listdir('.')
        options = [item for item in current_dir if item.startswith(text)]
    if state < len(options):
        return options[state]
    else:
        return None

def save_history():
    readline.write_history_file(HISTORY_FILE)

def load_history():
    if os.path.exists(HISTORY_FILE):
        readline.read_history_file(HISTORY_FILE)

def handle_alias(args):
    if len(args) == 0:
        for alias, command in ALIASES.items():
            print(f"alias {alias}='{command}'")
    elif len(args) == 1 and '=' in args[0]:
        alias, command = args[0].split('=', 1)
        ALIASES[alias] = command
    else:
        print("Invalid alias format. Use: alias name='command'")

def handle_unalias(args):
    if len(args) == 1 and args[0] in ALIASES:
        del ALIASES[args[0]]
    else:
        print("unalias: usage: unalias name")

def handle_set(args):
    if len(args) == 1 and '=' in args[0]:
        var, value = args[0].split('=', 1)
        ENV_VARS[var] = value
        os.environ[var] = value
    else:
        print("set: usage: set VAR=VALUE")

def handle_unset(args):
    if len(args) == 1 and args[0] in ENV_VARS:
        var = args[0]
        del ENV_VARS[var]
        del os.environ[var]
    else:
        print("unset: usage: unset VAR")
        
def builtin_edit(args):
    if not args:
        print("Usage: edit <filename>")
        return
    
    filename = args[0]
    
    if os.path.exists(filename):
        with open(filename, 'r') as file:
            content = file.read()
    else:
        content = ""
    
    print("Editing file:", filename)
    print("Enter '.save' to save and exit, '.exit' to exit without saving.")
    
    lines = content.split('\n')
    while True:
        try:
            line = input()
            if line == '.save':
                with open(filename, 'w') as file:
                    file.write('\n'.join(lines))
                print("File saved.")
                break
            elif line == '.exit':
                print("Exiting without saving.")
                break
            else:
                lines.append(line)
        except KeyboardInterrupt:
            print("\nExiting without saving.")
            break

def execute_command(command, args, input=None, output=None, append=False):
    if command in ALIASES:
        command = ALIASES[command]
        args = command.split() + args[1:]

    if command in BUILTINS:
        if command == 'exit':
            save_history()
            sys.exit(0)
        elif command == 'echo':
            print(' '.join(args[1:]))
        elif command == 'type':
            if args[1] in BUILTINS:
                print(f"{args[1]} is a shell builtin")
            elif args[1] in ALIASES:
                print(f"{args[1]} is an alias for {ALIASES[args[1]]}")
            else:
                print(f"{args[1]} is {subprocess.getoutput('which ' + args[1])}")
        elif command == 'pwd':
            print(os.getcwd())
        elif command == 'cd':
            if len(args) > 1:
                os.chdir(args[1])
            else:
                os.chdir(os.path.expanduser('~'))
        elif command == 'alias':
            handle_alias(args[1:])
        elif command == 'unalias':
            handle_unalias(args[1:])
        elif command == 'set':
            handle_set(args[1:])
        elif command == 'unset':
            handle_unset(args[1:])
        elif command == 'source':
            if len(args) > 1:
                execute_script(args[1])
            else:
                print("source: usage: source filename")
        elif command == 'help':
            handle_help(args[1:])
    else:
        try:
            if input:
                with open(input, 'r') as infile:
                    result = subprocess.run([command] + args[1:], stdin=infile, capture_output=True, text=True)
            else:
                result = subprocess.run([command] + args[1:], capture_output=True, text=True)

            if output:
                mode = 'a' if append else 'w'
                with open(output, mode) as outfile:
                    outfile.write(result.stdout)
            else:
                print(result.stdout, end='')

            if result.stderr:
                print(result.stderr, end='', file=sys.stderr)
        except FileNotFoundError:
            print(f"{command}: command not found")

def handle_help(args):
    if len(args) == 0:
        print("Built-in commands:")
        for cmd in BUILTINS:
            print(f"  {cmd}")
        print("Use 'help [command]' for more information on a specific command.")
    else:
        cmd = args[0]
        if cmd in HELP_MESSAGES:
            print(HELP_MESSAGES[cmd])
        else:
            print(f"No help available for {cmd}")

def process_command(command_line):
    if '|' in command_line:
        handle_pipeline(command_line)
    else:
        handle_single_command(command_line)

def handle_pipeline(command_line):
    parts = command_line.split('|')
    num_parts = len(parts)
    processes = []
    input_file = None

    for i, part in enumerate(parts):
        args = part.strip().split()
        command = args[0]

        if i == 0:
            p = subprocess.Popen(command.split(), stdout=subprocess.PIPE, stderr=subprocess.PIPE)
        elif i == num_parts - 1:
            p = subprocess.Popen(command.split(), stdin=processes[-1].stdout, stderr=subprocess.PIPE)
        else:
            p = subprocess.Popen(command.split(), stdin=processes[-1].stdout, stdout=subprocess.PIPE, stderr=subprocess.PIPE)

        processes.append(p)

    for p in processes:
        stdout, stderr = p.communicate()
        if stderr:
            print(stderr.decode().strip())

def handle_single_command(command_line):
    args = command_line.split()
    command = args[0]

    input_file = None
    output_file = None
    append = False

    if '>' in args or '>>' in args:
        if '>' in args:
            index = args.index('>')
            append = False
        else:
            index = args.index('>>')
            append = True
        output_file = args[index + 1]
        args = args[:index]

    if '<' in args:
        index = args.index('<')
        input_file = args[index + 1]
        args = args[:index]

    execute_command(command, args, input=input_file, output=output_file, append=append)

def execute_script(filename):
    with open(filename, 'r') as script_file:
        script_lines = script_file.readlines()
    i = 0
    while i < len(script_lines):
        line = script_lines[i].strip()
        if line.startswith('#') or not line:
            i += 1
            continue

        if line.startswith('if'):
            condition = line[2:].strip()
            if eval_condition(condition):
                i += 1
                while not script_lines[i].strip() == 'fi':
                    if script_lines[i].strip() == 'else':
                        i += 1
                        while not script_lines[i].strip() == 'fi':
                            i += 1
                        break
                    process_command(script_lines[i].strip())
                    i += 1
            else:
                while not script_lines[i].strip() == 'else':
                    if script_lines[i].strip() == 'fi':
                        break
                    i += 1
                i += 1
                while not script_lines[i].strip() == 'fi':
                    process_command(script_lines[i].strip())
                    i += 1
        elif line.startswith('while'):
            condition = line[5:].strip()
            loop_start = i + 1
            while eval_condition(condition):
                i = loop_start
                while not script_lines[i].strip() == 'done':
                    process_command(script_lines[i].strip())
                    i += 1
            while not script_lines[i].strip() == 'done':
                i += 1
        elif line.startswith('for'):
            parts = line[3:].strip().split()
            var = parts[0]
            items = parts[2:]
            for item in items:
                ENV_VARS[var] = item
                os.environ[var] = item
                i += 1
                while not script_lines[i].strip() == 'done':
                    process_command(script_lines[i].strip())
                    i += 1
            while not script_lines[i].strip() == 'done':
                i += 1
        else:
            process_command(line)
        i += 1

def eval_condition(condition):
    try:
        result = eval(condition, {}, ENV_VARS)
        return result
    except:
        return False
    
def save_bookmarks():
    with open(BOOKMARKS_FILE, 'w') as file:
        json.dump(BOOKMARKS, file)

def load_bookmarks():
    global BOOKMARKS
    if os.path.exists(BOOKMARKS_FILE):
        with open(BOOKMARKS_FILE, 'r') as file:
            BOOKMARKS = json.load(file)

def save_env_vars(filename):
    with open(filename, 'w') as file:
        json.dump(ENV_VARS, file)

def load_env_vars(filename):
    global ENV_VARS
    if os.path.exists(filename):
        with open(filename, 'r') as file:
            ENV_VARS = json.load(file)
            for key, value in ENV_VARS.items():
                os.environ[key] = value

def process_command(command):
    try:
        parts = command.split()
        cmd = parts[0]
        
        if cmd in BUILTINS:
            # Call the corresponding built-in function
            globals()[f"builtin_{cmd.replace('-', '_')}"](parts[1:])
        else:
            # Run the external command
            run_external_command(parts)
    except IndexError as e:
        print_error_message("Missing arguments for command.", "Check the command usage.")
    except FileNotFoundError as e:
        print_error_message(f"Command not found: {cmd}", "Ensure the command is installed and in your PATH.")
    except PermissionError as e:
        print_error_message("Permission denied.", "Check the permissions of the command or file.")
    except Exception as e:
        print_error_message(f"An unexpected error occurred: {str(e)}", "Check the command syntax and try again.")

def builtin_cd(args):
    global NAV_HISTORY
    if len(args) == 0:
        path = os.path.expanduser("~")
    elif args[0] in BOOKMARKS:
        path = BOOKMARKS[args[0]]
    else:
        path = args[0]

    try:
        os.chdir(path)
        NAV_HISTORY.append(os.getcwd())
        if len(NAV_HISTORY) > MAX_NAV_HISTORY:
            NAV_HISTORY.pop(0)
    except FileNotFoundError:
        print(f"cd: {path}: No such file or directory")

def builtin_bookmark(args):
    if len(args) == 0:
        print("Usage: bookmark [name] [directory]")
        return

    name = args[0]
    directory = args[1] if len(args) > 1 else os.getcwd()
    BOOKMARKS[name] = directory
    save_bookmarks()

def builtin_bookmarks(args):
    for name, directory in BOOKMARKS.items():
        print(f"{name}: {directory}")

def builtin_set(args):
    if len(args) != 1:
        print("Usage: set VAR=VALUE")
        return

    var, value = args[0].split('=', 1)
    ENV_VARS[var] = value
    os.environ[var] = value

def builtin_unset(args):
    if len(args) != 1:
        print("Usage: unset VAR")
        return

    var = args[0]
    if var in ENV_VARS:
        del ENV_VARS[var]
        del os.environ[var]

def builtin_exportenv(args):
    if len(args) != 1:
        print("Usage: exportenv [filename]")
        return

    filename = args[0]
    save_env_vars(filename)

def builtin_importenv(args):
    if len(args) != 1:
        print("Usage: importenv [filename]")
        return

    filename = args[0]
    load_env_vars(filename)
    
def builtin_git_init(args):
    run_external_command(['git', 'init'] + args)

def builtin_git_add(args):
    run_external_command(['git', 'add'] + args)

def builtin_git_commit(args):
    run_external_command(['git', 'commit'] + args)

def builtin_git_push(args):
    run_external_command(['git', 'push'] + args)

def builtin_git_pull(args):
    run_external_command(['git', 'pull'] + args)

def builtin_git_status(args):
    run_external_command(['git', 'status'] + args)

def builtin_git_log(args):
    run_external_command(['git', 'log'] + args)

def builtin_docker_run(args):
    run_external_command(['docker', 'run'] + args)

def builtin_docker_ps(args):
    run_external_command(['docker', 'ps'] + args)

def builtin_docker_stop(args):
    run_external_command(['docker', 'stop'] + args)

def builtin_docker_rm(args):
    run_external_command(['docker', 'rm'] + args)

def builtin_docker_build(args):
    run_external_command(['docker', 'build'] + args)

def builtin_docker_pull(args):
    run_external_command(['docker', 'pull'] + args)

def builtin_docker_images(args):
    run_external_command(['docker', 'images'] + args)

def builtin_help(args):
    if len(args) == 0:
        for cmd in BUILTINS:
            print(HELP_MESSAGES[cmd])
    else:
        cmd = args[0]
        if cmd in HELP_MESSAGES:
            print(HELP_MESSAGES[cmd])
        else:
            print(f"No help available for {cmd}")

def run_external_command(parts):
    try:
        result = subprocess.run(parts, check=True)
    except subprocess.CalledProcessError as e:
        print_error_message(f"Command '{parts[0]}' failed with exit code {e.returncode}.", "Check the command output for details.")
    except FileNotFoundError as e:
        print_error_message(f"Command not found: {parts[0]}", "Ensure the command is installed and in your PATH.")
    except PermissionError as e:
        print_error_message("Permission denied.", "Check the permissions of the command or file.")
    except Exception as e:
        print_error_message(f"An unexpected error occurred: {str(e)}", "Check the command syntax and try again.")

def search(args):
    """
    Search for files or text within files.
    Usage:
        search filename [pattern] - Search for files with a specific pattern in the filename.
        search text "search_string" - Search for text within files.
    """
    if len(args) < 2:
        print("Usage: search filename [pattern] | search text \"search_string\"")
        return
    
    search_type = args[0]
    search_query = args[1]

    if search_type == 'filename':
        pattern = args[2] if len(args) > 2 else '*'
        for root, dirs, files in os.walk('.'):
            for filename in fnmatch.filter(files, pattern):
                print(os.path.join(root, filename))
    elif search_type == 'text':
        search_string = search_query
        for root, dirs, files in os.walk('.'):
            for filename in files:
                try:
                    with open(os.path.join(root, filename), 'r') as f:
                        if search_string in f.read():
                            print(f"Found in {os.path.join(root, filename)}")
                except (IOError, UnicodeDecodeError):
                    continue
    else:
        print("Invalid search type. Use 'filename' or 'text'.")

def search_history(args):
    """
    Search through the command history.
    Usage:
        search_history "search_string" - Search for commands in history containing the search string.
    """
    if len(args) != 1:
        print("Usage: search_history \"search_string\"")
        return

    search_string = args[0]
    try:
        with open(HISTORY_FILE, 'r') as history_file:
            history_lines = history_file.readlines()
            for i, line in enumerate(history_lines, start=1):
                if search_string in line:
                    print(f"{i}: {line.strip()}")
    except IOError:
        print("No history found.")

def execute_history_command(args):
    """
    Execute a command from the history.
    Usage:
        !n - Execute the nth command from history.
    """
    if len(args) != 1 or not args[0].startswith('!'):
        print("Usage: !n (where n is the command number from history)")
        return

    try:
        command_number = int(args[0][1:])
        with open(HISTORY_FILE, 'r') as history_file:
            history_lines = history_file.readlines()
            if command_number > 0 and command_number <= len(history_lines):
                command = history_lines[command_number - 1].strip()
                print(f"Executing: {command}")
                handle_command(command)  # Recursively handle the command
            else:
                print(f"No command found at position {command_number}")
    except (ValueError, IOError):
        print("Invalid command number or no history found.")

def handle_command(command):
    parts = command.split()
    if not parts:
        return

    cmd = parts[0]
    args = parts[1:]

    if cmd in BUILTINS:
        if cmd == 'search':
            search(args)
        elif cmd == 'search_history':
            search_history(args)
        elif cmd.startswith('!'):
            execute_history_command([cmd])
        else:
            print(f"Handling built-in command: {cmd}")
            # Implement other built-in commands as needed
    else:
        try:
            subprocess.run(command, shell=True)
        except Exception as e:
            print(f"Error executing command: {e}")


def main():
    load_bookmarks()
    readline.set_completer(completer)
    readline.parse_and_bind('tab: complete')
    readline.parse_and_bind('set enable-keypad on')
    readline.parse_and_bind('"\\e[A": history-search-backward')
    readline.parse_and_bind('"\\e[B": history-search-forward')

    load_history()

    partial_command = ""
    while True:
        PATH = os.environ.get("PATH")

        prompt = "> " if partial_command else "$ "
        sys.stdout.write(prompt)
        sys.stdout.flush()

        line = sys.stdin.readline().strip()
        if partial_command:
            line = partial_command + " " + line
            partial_command = ""

        if not line:
            continue

        process_command(line)

if __name__ == "__main__":
    main()
