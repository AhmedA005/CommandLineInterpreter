package org.example;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class CommandLineInterpreter {
    private static Scanner scanner = new Scanner(System.in);
    File initialDirectory = new File("");
    String currPath = initialDirectory.getAbsoluteFile().getAbsolutePath();

    // Function to check if a directory is empty
    private boolean isDirectoryEmpty(File directory) {
        File[] contents = directory.listFiles();
        return contents == null || contents.length == 0;
    }

    // mkdir Command
    public void makeDirectories(String... directoryNames) {
        // Check if no directory names are provided
        if (directoryNames == null || directoryNames.length == 0) {
            System.out.println("No directory names provided.");
            return;
        }

        // Iterate through all provided directory names
        for (String directoryName : directoryNames) {
            // Validate directory name
            if (directoryName == null || directoryName.trim().isEmpty()) {
                System.out.println("Invalid directory name: " + directoryName);
                continue;
            }

            // Get the current directory
            String currentDirectory = currPath;
            Path directoryPath = Paths.get(currentDirectory, directoryName);

            // Create the directory and handle possible exceptions
            try {
                Files.createDirectories(directoryPath);
                System.out.println("Directory created successfully at: " + directoryPath);
            } catch (FileAlreadyExistsException e) {
                System.out.println("Directory already exists: " + directoryPath);
            } catch (IOException e) {
                System.out.println("I/O error occurred while creating directory at: " + directoryPath);
            } catch (SecurityException e) {
                System.out.println("Permission denied: Unable to create directory at " + directoryPath);
            }
        }
    }

    // rmdir Command
    public void deleteDirectory(String... directoryNames) {
        // Check if no directory names are provided
        if (directoryNames == null || directoryNames.length == 0) {
            System.out.println("No directory names provided.");
            return;
        }

        // Iterate through all provided directory names
        for (String directoryName : directoryNames) {
            // Validate directory name
            if (directoryName == null || directoryName.trim().isEmpty()) {
                System.out.println("Invalid directory name: " + directoryName);
                continue;
            }

            // Get the current directory
            String currentDirectory = currPath;
            Path directoryPath = Paths.get(currentDirectory, directoryName);
            File directory = directoryPath.toFile();

            // Check if directory exists
            if (directory.exists()) {
                // If directory is empty, delete the directory
                if (isDirectoryEmpty(directory)) {
                    try {
                        Files.delete(directoryPath);
                        System.out.println("Directory deleted successfully at: " + directoryPath);
                    } catch (IOException e) {
                        System.out.println("Failed to delete directory: " + directoryPath + ". Reason: " + e.getMessage());
                    }
                } else {
                    System.out.println("Directory is not empty. Cannot delete non-empty directories.");
                }
            } else {
                System.out.println("Directory does not exist: " + directoryPath);
            }
        }
    }

    // cat command
    public String cat(String... fileNames) throws FileNotFoundException {
        if (fileNames == null || fileNames.length == 0) {
            System.out.println("No file names provided.");
            return "No file names provided.";
        }

        String currentDirectory = currPath;
        StringBuilder result = new StringBuilder();

        for (String fileName : fileNames) {
            if (fileName == null || fileName.trim().isEmpty()) {
                System.out.println("Invalid file name: " + fileName);
                continue;
            }

            Path filePath = Paths.get(currentDirectory, fileName);
            if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
                try {
                    Files.lines(filePath).forEach(line -> {
                        result.append(line).append("\n");
                    });
                } catch (IOException e) {
                    System.out.println("I/O error occurred while reading file: " + filePath);
                } catch (SecurityException e) {
                    System.out.println("Permission denied: Unable to access file: " + filePath);
                }
            } else {
                System.out.println("File not found: " + filePath);
            }
        }

        return result.toString();
    }

    // > command
    public void redirectInput(String input, String outputFileName) throws IOException {
        String currentDirectory = currPath;
        Path outputFilePath = Paths.get(currentDirectory, outputFileName);

        String outputText = "";

        // Handle exact matches for 'ls', 'lsa', and 'lsr'
        if (input.equals("ls")) {
            outputText = ls(".");
        } else if (input.startsWith("ls ")) {
            // "ls <directory>"
            String[] inputParts = input.split(" ", 2);
            outputText = ls(inputParts[1]);
        } else if (input.equals("lsa")) {
            outputText = lsa(".");
        } else if (input.startsWith("lsa ")) {
            // "lsa <directory>"
            String[] inputParts = input.split(" ", 2);
            outputText = lsa(inputParts[1]);
        } else if (input.equals("lsr")) {
            outputText = lsr(new File("."));
        } else if (input.startsWith("lsr ")) {
            // "lsr <directory>"
            String[] inputParts = input.split(" ", 2);
            outputText = lsr(new File(inputParts[1]));
        } else if (input.startsWith("cat")) {
            // Handle "cat file1 file2 ..."
            String[] fileNames = input.substring(4).trim().split("\\s+");
            outputText = cat(fileNames);
        } else {
            // Check if input is an existing file path
            Path inputFilePath = Paths.get(currentDirectory, input);
            if (Files.exists(inputFilePath) && Files.isRegularFile(inputFilePath)) {
                outputText = new String(Files.readAllBytes(inputFilePath)); // Read file content
            } else {
                // Input does not match any known command or file
                System.out.println("Error: Command or file not found - " + input);
                return;
            }
        }

        // Write the determined output text to the output file
        try (FileWriter myWriter = new FileWriter(outputFilePath.toFile())) {
            myWriter.write(outputText + System.lineSeparator());
            System.out.println("Command output has been redirected to " + outputFileName);
        }
    }


    public String ls(String directoryName) {

        String currentDirectory = currPath;
        Path directoryPath = Paths.get(currentDirectory, directoryName);
        File directory = directoryPath.toFile();

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                StringBuilder nonHiddenFiles = new StringBuilder();

                for (File file : files) {
                    if (!file.isHidden()) { // Only process non-hidden files
                        nonHiddenFiles.append(file.getName()).append("\n");
                    }
                }

                // Return non-hidden files as a string
                return nonHiddenFiles.length() > 0 ? nonHiddenFiles.toString() : "Empty";
            } else {
                return "Empty"; // Directory is empty
            }
        } else {
            return "Invalid directory"; // Directory does not exist
        }
    }


    public String lsa(String directoryName) {
        String currentDirectory = currPath;
        Path directoryPath = Paths.get(currentDirectory, directoryName);
        File directory = directoryPath.toFile();

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                StringBuilder allFiles = new StringBuilder();
                for (File file : files) {
                    if (file.isHidden()) {
                        allFiles.append("." + file.getName()).append("\n");
                    } else {
                        allFiles.append(file.getName()).append("\n");
                    }
                }
                return allFiles.length() > 0 ? allFiles.toString() : "Empty";
            } else {
                return "Empty"; // empty
            }
        } else {
            return "Invalid directory"; // not valid directory
        }
    }

    public static String lsr(File path) {
        StringBuilder filePaths = new StringBuilder();
        listFilesRecursively(path, filePaths);
        return filePaths.toString();
    }

    private static void listFilesRecursively(File directory, StringBuilder filePaths) {
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (!file.isHidden()) {
                    filePaths.append(file.getName()).append("\n");

                    // Recurse if it's a directory
                    if (file.isDirectory()) {
                        listFilesRecursively(file, filePaths);
                    }
                }
            }
        }
    }

    public void rm(String... names) {
        if (names == null || names.length == 0) {
            System.out.println("No names provided.");
            return;
        }

        for (String name : names) {
            if (name == null || name.trim().isEmpty()) {
                System.out.println("Invalid name: " + name);
                continue;
            }

            String currentDirectory = currPath;
            Path path = Paths.get(currentDirectory, name);
            File fileOrDir = path.toFile();

            if (fileOrDir.exists()) {
                try {
                    if (fileOrDir.isDirectory()) {
                        Files.delete(fileOrDir.toPath());
                        System.out.println("Directory deleted successfully: " + path);
                    } else {
                        Files.delete(fileOrDir.toPath());
                        System.out.println("File deleted successfully: " + path);
                    }
                } catch (IOException e) {
                    System.out.println("Failed to delete: " + path + ". Reason: " + e.getMessage());
                }
            } else {
                System.out.println("File or directory does not exist: " + path);
            }
        }
    }


    public String append(String commandOrText, String filename) throws IOException {
        if (commandOrText == null) {
            throw new NullPointerException("Text cannot be null");
        }
        if (filename == null) {
            throw new NullPointerException("Filename cannot be null");
        }

        String text = "";
        String[] commandParts = commandOrText.split("\\s+", 2); // Split into command and optional argument
        String command = commandParts[0];
        String argument = commandParts.length > 1 ? commandParts[1] : null; // Use null if no argument provided

        // Determine the output based on the command and optional argument
        switch (command) {
            case "ls":
                text += ls(argument == null ? "." : argument); // Pass argument if provided
                break;
            case "cat":
                if (argument != null) {
                    text += cat(argument); // Pass argument as filename
                } else {
                    System.out.println("Error: No file specified for cat command.");
                    return "Error: No file specified for cat command.";
                }
                break;
            case "lsa":
                text += lsa(argument == null ? "." : argument);
                break;
            case "lsr":
                text += lsr(new File(argument == null ? "." : argument));
                break;
            case "pwd":
                text+=printWorkingDirectory();
                break;
            default:
                text += commandOrText + "\n"; // Treat as direct text if command is not recognized
        }

        // Append the result to the specified file
//        File file = new File(currPath + File.separator + filename);
         File file = new File(filename);

//        System.out.println("Checking existence for: " + file.getAbsolutePath());
//        System.out.println("File exists? " + file.exists());
//        System.out.println("Can write? " + file.canWrite());
        if (!file.exists()) {
            System.out.println("File does not exist: " + filename);
            return "Error: File does not exist: " + filename;
        }
        if (!file.canWrite()) {
            return "Error appending to file: " + filename;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(text);
            writer.newLine(); // Add a new line after the text
            System.out.println("Appended to " + filename);
            return "Text appended to " + filename;
        } catch (IOException e) {
            return "Error appending to file: " + e.getMessage();
        }
    }

    public static void exit() {
        System.exit(0);
    }

    public void help() {
        System.out.println("List of available commands:");
        System.out.println("pwd - Print the current working directory");
        System.out.println("cd - Change the current working directory");
        System.out.println("ls - List files and directories in current directory");
        System.out.println("lsa - List files and directories in current directory, including hidden files");
        System.out.println("lsr - List files and directories recursively in current directory");
        System.out.println("mkdir - Create a new directory");
        System.out.println("rmdir - Remove a directory");
        System.out.println("touch - Create a new file");
        System.out.println("mv - Moves one or more files/directories to a directory.");
        System.out.println("rm - Remove a file or directory");
        System.out.println("cat - Concatenates the content of the files and prints it.");
        System.out.println("> - Redirects the output of the first command to be written to a file.");
        System.out.println(">> - Like > but appends to the file if it exists.");
        System.out.println("| - redirect the output of the previous command as in input to another command.");
        System.out.println("exit - exits the terminal");
    }

    public String cdCommand(String directory) throws IOException {
        File newFile;

        // Handle case when directory is "."
        if (directory.equals(".")) {
            return currPath;
        }

        // Handle ".." for moving up one directory
        if (directory.equals("..")) {
            File file = new File(currPath);
            String parentPath = file.getParent();
            if (parentPath != null) {
                currPath = parentPath;
            }
            return currPath;
        }

        // Handle absolute and relative paths
        if (Paths.get(directory).isAbsolute()) {
            newFile = new File(directory); // Absolute path
        } else {
            newFile = new File(currPath, directory); // Relative path
        }

        // Check if newFile is a directory that exists
        if (newFile.exists() && newFile.isDirectory()) {
            currPath = newFile.getAbsolutePath(); // Change to new path
        } else {
            System.out.println("Directory does not exist: " + directory);
        }

        return currPath;
    }



    public void printPath() {
        System.out.println(currPath);
    }

    public String getPath() {
        return currPath;
    }

    public void createFile(Scanner operand) throws IOException {
        String fileName = operand.nextLine().strip();
        System.out.println(fileName);
        printPath();
        touchCommand(fileName);
    }

    public void touchCommand(String... fileNames) throws IOException {
        if (fileNames == null || fileNames.length == 0) {
            System.out.println("No file names provided.");
            return;
        }

        for (String fileName : fileNames) {
            if (fileName == null || fileName.trim().isEmpty()) {
                System.out.println("Invalid file name: " + fileName);
                continue;
            }

            File file = new File(currPath + File.separator + fileName);
            try {
                if (file.createNewFile()) {
                    System.out.println("File created: " + file.getName());
                } else {
                    System.out.println("File already exists: " + file.getName());
                }
            } catch (IOException e) {
                System.out.println("An error occurred while creating file: " + file.getName());
                e.printStackTrace();
            }
        }
    }

    public void mv(String[] operands) throws IOException {
        if (operands == null || operands.length == 0) {
            System.out.println("No file names provided.");
            return;
        }
        if (operands.length == 2) {
            String old = operands[0];
            String newer = operands[1];
            if(old.equals(newer))return;
            mvCommand(old, newer);
        }
        else if (operands.length > 2) {
            String newDirectoryName = operands[operands.length - 1];
            File newDirectory = new File(currPath + File.separator + newDirectoryName);
            if (newDirectory.isDirectory()) {
                for(int i = 0 ; i < operands.length-1 ; i++) {
                    mvCommand(operands[i], newDirectoryName);
                }
            }
            else {
                System.out.println("Directory does not exist.");
            }
        }
    }

    public void mvCommand(String old, String newer) throws IOException {
        File file = new File(currPath + File.separator + old);
        File newFile = new File(currPath + File.separator + newer);
        if (!file.exists()) {
            System.out.println("Source file does not exist: " + old);
            return;
        }
        if (newFile.exists()) moveFile(currPath + File.separator + old, currPath + File.separator + newer);
        else renameFile(file, newFile);
    }

    public void moveFile(String src, String dist) throws IOException {
        Path sourcePath = Paths.get(src);
        Path destinationPath = Paths.get(dist);
        try {
            Files.move(sourcePath, destinationPath.resolve(sourcePath.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File moved successfully!");
        } catch (Exception e) {
            System.out.println("Error moving file: " + e.getMessage());
        }
    }

    public void renameFile(File one, File two) throws IOException {

        if (!one.exists()) {
            System.out.println("File does not exist.");
        } else {
            if (one.renameTo(two)) {
                System.out.println("File renamed successfully.");
            } else {
                System.out.println("Renaming failed.");
            }
        }
    }

    public String printWorkingDirectory() throws IOException {
        File file = new File(currPath);
//        System.out.println(file.getAbsoluteFile().getAbsolutePath());
        return file.getAbsoluteFile().getAbsolutePath();
    }


    public String piping(String command1, String command2) throws IOException {
        if (command1 == null || command2 == null) {
            throw new NullPointerException("Text cannot be null");
        }

        String firstCommandOutput = executePipingFirstCommand(command1);

        if (firstCommandOutput == null || firstCommandOutput.isEmpty()) {
            return "No output from the first command.";
        }
        String secondCommand = executePipingsecondCommand(firstCommandOutput, command2);
        if (firstCommandOutput == null || firstCommandOutput.isEmpty()) {
            return "No output from the first command.";
        }
        if (secondCommand == null || secondCommand.isEmpty()) {
            return "No output from the second command.";
        }
        return secondCommand;
    }

    public String pipingCat(String catPipingText) throws FileNotFoundException {
        if(catPipingText==null) {
            return "No input provided";
        }
        return catPipingText;

    }


    private String executePipingFirstCommand(String command1) throws IOException {

        String[] commandParts = command1.split("\\s+", 2);
        String mainCommand = commandParts[0];
        String argument = (commandParts.length > 1) ? commandParts[1] : ".";

        switch (mainCommand) {
            case "ls":
                return ls(argument);
            case "lsa":
                return lsa(argument);
            case "lsr":
                return lsr(new File(argument));
            case "pwd":
                return printWorkingDirectory();
            case ">>" :
                String appendFile=commandParts[1];
                return append(argument,appendFile);
            case "cat":
//                return pipingCat(argument);
                return cat(argument);
            default:
                System.out.println("Command not recognized for piping: " + mainCommand);
                return "";
        }
    }
    private String executePipingsecondCommand(String outputPipetext,String command2) throws IOException {

        String[] commandParts;
        String secondArgument = "";
        if(command2.contains(">>")){
            commandParts = command2.split("\\s+", 2);
            command2=commandParts[0];
            secondArgument = commandParts[1];      
        }
        switch (command2) {

            case "pwd":
                return printWorkingDirectory();
            case ">>" :
                return append(outputPipetext,secondArgument);
            case "cat":
                return pipingCat(outputPipetext);
            default:
                return "No output from the second command.";
        }
    }

}