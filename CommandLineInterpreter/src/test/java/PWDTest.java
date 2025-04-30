import org.example.CommandLineInterpreter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class PWDTest {
    @Test
    public void testPwd() throws IOException {
        CommandLineInterpreter cli = new CommandLineInterpreter();
        assertEquals(cli.getPath(),cli.printWorkingDirectory());
        cli.cdCommand("..");
        assertEquals(cli.getPath(),cli.printWorkingDirectory());
    }
}
