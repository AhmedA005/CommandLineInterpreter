import org.example.CommandLineInterpreter;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MvTest {
    CommandLineInterpreter cli = new CommandLineInterpreter();
    @Test
    public void testMv() throws IOException {
        cli.touchCommand("file1.txt");
        cli.mvCommand("file1.txt","newName.txt");
        File file = new File(  cli.getPath() + File.separator + "newName.txt");
        assertTrue(file.exists());
        cli.mvCommand("newName.txt","fol");
        File newPlace = new File(  cli.getPath() + File.separator + "fol\\newName.txt");
        assertTrue(newPlace.exists());
    }
}
