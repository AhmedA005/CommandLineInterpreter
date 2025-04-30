import org.example.CommandLineInterpreter;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertFalse;

public class RemoveDirectoryTest {
    @Test
    public void testRmdir() throws IOException {
        CommandLineInterpreter cli = new CommandLineInterpreter();
        cli.deleteDirectory("fol1");
        String currentDirectory = System.getProperty("user.dir");
        Path directoryPath = Paths.get(currentDirectory, "fol1");
        assertFalse("Directory Should Not Exist", Files.exists(directoryPath));
    }
}
