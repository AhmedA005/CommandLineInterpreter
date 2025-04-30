import org.example.CommandLineInterpreter;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

public class MakeDirectoryTest {
    @Test
    public void testMkdir() {
        CommandLineInterpreter cli = new CommandLineInterpreter();
        cli.makeDirectories("fol1");
        String currentDirectory = System.getProperty("user.dir");
        Path directoryPath = Paths.get(currentDirectory, "fol1");
        assertTrue("Directory Created", Files.exists(directoryPath));
    }
}
