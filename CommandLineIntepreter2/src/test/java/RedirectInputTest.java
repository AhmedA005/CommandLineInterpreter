import org.example.CommandLineInterpreter;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RedirectInputTest {
    @Test
    public void testRedirectInput() throws IOException {
        CommandLineInterpreter cli = new CommandLineInterpreter();
        String currentDirectory = System.getProperty("user.dir");

        File outputFile = new File(currentDirectory, "output.txt");
        File inputFile = new File(currentDirectory, "input.txt");

        assertTrue("File should exist", Files.exists(outputFile.toPath()));
        assertTrue("File should exist", Files.exists(inputFile.toPath()));

        // case 1: redirect input from file
        cli.redirectInput("input.txt", "output.txt");

        String inputContent = Files.readString(inputFile.toPath()).trim();
        String outputContent = Files.readString(outputFile.toPath()).trim();
        assertEquals("The output file should contain the same content as the input file", inputContent, outputContent);

    }
}
