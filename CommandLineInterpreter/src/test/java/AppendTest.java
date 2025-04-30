import org.example.CommandLineInterpreter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class AppendTest {

    @Test
    public void testAppendInvalidPath() throws IOException {
        CommandLineInterpreter cli = new CommandLineInterpreter();
        String text = "Some text";
        String invalidPath = "/invalid/path/to/file.txt"; // Invalid path

        String result = cli.append(text, invalidPath);

        assertTrue(result.startsWith("Error: File does not exist:"));
    }
    @Test
    public void testFileCreationAndExistence() {
        String currentDirectory = System.getProperty("user.dir");
        Path testFilePath = Paths.get(currentDirectory, "testfile.txt");
        File file = testFilePath.toFile();

        try {
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getAbsolutePath());
            }
            System.out.println("Does file exist? " + file.exists());
        } catch (IOException e) {
            fail("IOException: " + e.getMessage());
        } finally {
            if (file.exists()) {
                file.delete();
            }
        }
    }

    @Test
    public void testAppendNoPermission() {
        CommandLineInterpreter cli = new CommandLineInterpreter();
        String text = "Some text";

        String currentDirectory = System.getProperty("user.dir");

        Path testFilePath = Paths.get(currentDirectory, "f1.txt");
        File file = testFilePath.toFile();

        try {
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getAbsolutePath());
                file.setReadOnly();
            } else {
                System.out.println("File already exists: " + file.getAbsolutePath());
            }

            System.out.println("Does file exist before append? " + file.exists());

            String result = cli.append(text, file.getAbsolutePath());
            System.out.println("result " + result);

            assertTrue(result.startsWith("Error appending to file:"));
        } catch (IOException e) {
            fail("An unexpected IOException occurred: " + e.getMessage());
        } finally {

            if (file.exists()) {
                file.delete();
                System.out.println("Cleaned up test file: " + file.getAbsolutePath());
            } else {
                System.out.println("No file to clean up.");
            }
        }
    }


    @Test
    public void testAppendNullText() {
        CommandLineInterpreter cli = new CommandLineInterpreter();
        String filename = "testfile.txt";

        Exception exception = assertThrows(NullPointerException.class, () -> {
            cli.append(null, filename);
        });

        assertEquals("Text cannot be null", exception.getMessage());
    }

    @Test
    public void testAppendNullFilename() {
        CommandLineInterpreter cli = new CommandLineInterpreter();
        String text = "Some text";

        Exception exception = assertThrows(NullPointerException.class, () -> {
            cli.append(text, null);
        });

        assertEquals("Filename cannot be null", exception.getMessage());
    }
}
