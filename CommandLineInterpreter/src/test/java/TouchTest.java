import org.example.CommandLineInterpreter;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TouchTest {
    @Test
    public void testTouch() throws IOException {
        CommandLineInterpreter cli = new CommandLineInterpreter();
        cli.touchCommand("filename.txt");
        File file = new File(  cli.getPath() + File.separator + "filename.txt");
        assertTrue(file.exists());
    }
}
