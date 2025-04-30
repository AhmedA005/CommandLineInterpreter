import org.example.CommandLineInterpreter;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertFalse;

public class RemoveTest {
    @Test
    public void rmexistencetest() {
        CommandLineInterpreter cli = new CommandLineInterpreter();
        cli.rm("test.txt");
        String currentDirectory = System.getProperty("user.dir");
        Path directoryPath = Paths.get(currentDirectory, "test.txt");
        assertFalse("Directory Should Not Exist", Files.exists(directoryPath));
    }


    @Test
    public void rmvalidtest() {
        CommandLineInterpreter cli = new CommandLineInterpreter();
        cli.rm("test1.txt", "test2.txt");
        String currentDirectory = System.getProperty("user.dir");
        Path directoryPath = Paths.get(currentDirectory, "test.txt");
        assertFalse("Directory Should Not Exist", Files.exists(directoryPath));
    }
}
