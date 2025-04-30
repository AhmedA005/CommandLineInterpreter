import org.example.CommandLineInterpreter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class CatTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() {
        // Redirect System.out to capture output in outContent
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void tearDown() {
        // Restore original System.out after the test
        System.setOut(originalOut);
    }

    @Test
    public void testCat() throws IOException {
        CommandLineInterpreter cli = new CommandLineInterpreter();
        // Get the current working directory and f1.txt file path
        String currentDirectory = System.getProperty("user.dir");
        Path testFilePath = Paths.get(currentDirectory, "testing.txt");

        // Ensure the file exists before running the test
        assertTrue("File should exist", Files.exists(testFilePath));

        // Normalize the expected content to use LF line separators
        String expectedContent = new String(Files.readAllBytes(testFilePath)).replace("\r\n", "\n").trim();

        // Execute the cat command and normalize its output
        String actualOutput = cli.cat("testing.txt").replace("\r\n", "\n").trim();

        // Compare the normalized contents
        assertEquals("Output should match the content of the file", expectedContent, actualOutput);
    }

}
