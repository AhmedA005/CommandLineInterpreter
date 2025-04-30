import org.example.CommandLineInterpreter;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class LsaTest {
    @Test
    public void lsaemptytest() {
        String currentDirectory = System.getProperty("user.dir");


        CommandLineInterpreter cli = new CommandLineInterpreter();
        String res = cli.lsa("empty");

        // Compare the expected non-hidden files list with the ls output
        assertEquals("Empty", res.trim());
    }


    @Test
    public void lsavalidtest() {
        CommandLineInterpreter cli = new CommandLineInterpreter();

        String currentDirectory = System.getProperty("user.dir");
        Path directoryPath = Paths.get(currentDirectory, "dir1");
        File path = new File(directoryPath.toAbsolutePath().toString());

        // Generate a string of non-hidden file names, joined by newline
        String expectedNonHiddenFiles = Arrays.stream(path.listFiles())
                .filter(file -> !file.isHidden())
                .map(File::getName)
                .collect(Collectors.joining("\n"));

        String res = cli.lsa("dir1");

        // Compare the expected non-hidden files list with the ls output
        assertEquals(expectedNonHiddenFiles, res.trim());
    }
}
