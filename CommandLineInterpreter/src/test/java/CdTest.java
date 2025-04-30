import org.junit.jupiter.api.Test;
import org.example.CommandLineInterpreter;
import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class CdTest {
    @Test
    public void testCd() throws IOException {
        CommandLineInterpreter cli = new CommandLineInterpreter();
        assertEquals(cli.cdCommand(".."),cli.printWorkingDirectory());
        assertEquals(cli.cdCommand(".."),cli.printWorkingDirectory());
        assertEquals(cli.cdCommand("Operating Systems"),cli.printWorkingDirectory());
    }
}
