package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static CommandLineInterpreter CommandLineInterpreter = new CommandLineInterpreter();

    public static void main(String[] args) throws IOException {

        System.out.println("Welcome to the Simple CLI! Type 'help' for a list of commands.");
        while (true) {
            System.out.print("cli> ");
            String command = scanner.nextLine().trim();
            if (command.equalsIgnoreCase("exit")) {
                System.out.println("Exiting the CLI. Goodbye!");
                break;
            }
            processCommand(command);
        }
    }

    private static void processCommand(String command) throws IOException {
        if (command.contains(" > ")) {
            handleRedirectionCommand(command);
        } else if (command.contains(" | ")) {
            handlePipingCommand(command);
        } else if (command.contains(" >> ")) {
            handleAppendCommand(command);
        } else {
            executeSingleCommand(command);
        }
    }

    private static void handleRedirectionCommand(String command) {
        String[] parts = command.split(" > ", 2);
        if (parts.length != 2) {
            System.out.println("Invalid command syntax for redirection.");
            return;
        }
        String inputCommand = parts[0].trim();
        String outputFileName = parts[1].trim();

        try {
            CommandLineInterpreter.redirectInput(inputCommand, outputFileName);
        } catch (IOException e) {
            System.err.println("An error occurred during redirection: " + e.getMessage());
        }
    }

    private static void handleAppendCommand(String command) throws IOException {
        String[] parts = command.split(" >> ", 2);
        if (parts.length != 2) {
            System.out.println("Invalid command syntax for appending.");
            return;
        }
        String inputCommand = parts[0].trim();
        String outputFileName = parts[1].trim();

        String appendResult = CommandLineInterpreter.append(inputCommand, outputFileName);
//        System.out.println(appendResult);
    }

    private static void handlePipingCommand(String command) {
        String[] parts = command.split(" \\| ", 2);
        if (parts.length != 2) {
            System.out.println("Invalid command syntax for piping.");
            return;
        }
        String command1 = parts[0].trim();
        String command2 = parts[1].trim();

        try {
            String pipeResult = CommandLineInterpreter.piping(command1, command2);
            System.out.println(pipeResult);
        } catch (FileNotFoundException e) {
            System.err.println("An error occurred during piping: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void executeSingleCommand(String command) throws FileNotFoundException {
        String[] tokens = command.split("\\s+");
        String mainCommand = tokens[0].toLowerCase();

        switch (mainCommand) {
            case "help":
                CommandLineInterpreter.help();
                break;

            case "exit":
                CommandLineInterpreter.exit();
                break;

            case "ls":
                String lsResult = CommandLineInterpreter.ls(tokens.length > 1 ? tokens[1] : ".");
                System.out.println(lsResult);
                break;

            case "lsa":
                String lsaResult = CommandLineInterpreter.lsa(tokens.length > 1 ? tokens[1] : ".");
                System.out.println(lsaResult);
                break;

            case "lsr":
                File directory = new File(tokens.length > 1 ? tokens[1] : ".");
                String lsrResult = CommandLineInterpreter.lsr(directory);
                System.out.println(lsrResult);
                break;

            case "rm":
                String[] namesToRemove = Arrays.copyOfRange(tokens, 1, tokens.length);
                CommandLineInterpreter.rm(namesToRemove);
                break;

            case "mkdir":
                String[] directoryNames = Arrays.copyOfRange(tokens, 1, tokens.length);
                CommandLineInterpreter.makeDirectories(directoryNames);
                break;

            case "rmdir":
                String[] directoriesToDelete = Arrays.copyOfRange(tokens, 1, tokens.length);
                CommandLineInterpreter.deleteDirectory(directoriesToDelete);
                break;

            case "cat":
                String[] filesToCat = Arrays.copyOfRange(tokens, 1, tokens.length);
                try {
                    String catResult = CommandLineInterpreter.cat(filesToCat);
                    System.out.println(catResult.isEmpty() ? "No content to display from the provided files." : catResult);
                } catch (FileNotFoundException e) {
                    System.out.println("Error: " + e.getMessage());
                }
                break;

            case "cd":
                if (tokens.length < 2) {
                    System.out.println("Please specify the directory to change to after the command.");
                } else {
                    try {
                        String newPath = CommandLineInterpreter.cdCommand(tokens[1]);
                        System.out.println("Current Directory: " + newPath);
                    } catch (IOException e) {
                        System.err.println("An error occurred: " + e.getMessage());
                    }
                }
                break;

            case "touch":
                String[] fileNamesToCreate = Arrays.copyOfRange(tokens, 1, tokens.length);
                try {
                    CommandLineInterpreter.touchCommand(fileNamesToCreate);
                } catch (IOException e) {
                    System.err.println("An error occurred: " + e.getMessage());
                }
                break;

            case "mv":
                String[] fileNames = Arrays.copyOfRange(tokens, 1, tokens.length);
                try {
                        CommandLineInterpreter.mv(fileNames);
                } catch (IOException e) {
                        System.err.println("An error occurred: " + e.getMessage());
                }
                break;
/*                if (tokens.length < 3) {
                    System.out.println("Please specify the source and destination for the mv command.");
                } else {
                    try {
                        CommandLineInterpreter.mvCommand(tokens[1], tokens[2]);
                    } catch (IOException e) {
                        System.err.println("An error occurred: " + e.getMessage());
                    }
                }
                break;*/

            case "pwd":
                try {
                    String currentDir = CommandLineInterpreter.printWorkingDirectory();
                    System.out.println(currentDir);
                } catch (IOException e) {
                    System.err.println("An error occurred: " + e.getMessage());
                }
                break;

            default:
                System.err.println("Command not found: " + mainCommand);
        }
    }
}
