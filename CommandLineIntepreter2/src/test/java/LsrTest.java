import org.example.CommandLineInterpreter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class LsrTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUpStreams() {
        // Redirect System.out to capture output
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        // Restore System.out to its original state
        System.setOut(originalOut);
    }

    @Test
    public void testLsr() {
        // Build the path for the test directory within the current directory
        String currentDirectory = System.getProperty("user.dir");
        Path directoryPath = Paths.get(currentDirectory, "testLsr");  // Adjusted to "empty" as shown in the screenshot
        File testDirectory = directoryPath.toFile();

        // Run the lsr method on the test directory
        String res = CommandLineInterpreter.lsr(testDirectory);

        // Expected output string with absolute paths of files and directories
        StringBuilder expectedOutput = new StringBuilder();
        buildExpectedOutput(testDirectory, expectedOutput);

        // Normalize line endings in both expected and actual outputs
        String normalizedExpectedOutput = expectedOutput.toString().replace("\r\n", "\n").trim();
        String normalizedActualOutput = res.replace("\r\n", "\n").trim();

        // Assert that the normalized outputs match
        assertEquals(normalizedExpectedOutput, normalizedActualOutput);
    }


    private void buildExpectedOutput(File directory, StringBuilder output) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                output.append(file.getName()).append("\n");
                if (file.isDirectory()) {
                    buildExpectedOutput(file, output);
                }
            }
        }
    }
}
