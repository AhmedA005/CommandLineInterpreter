import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import org.example.CommandLineInterpreter;

class PipingTest {

    private final CommandLineInterpreter commandLineInterpreter = new CommandLineInterpreter();

    @Test
    void testPipingWithValidCommands() throws IOException {
        String result = commandLineInterpreter.piping("ls", "cat");
        String result2 = commandLineInterpreter.piping("lsa", ">> file1.txt");
        String result3 = commandLineInterpreter.piping("lsr", "cat");
        String result4 = commandLineInterpreter.piping("pwd", "cat");
        String result5 = commandLineInterpreter.piping("pwd", ">> filename.txt");
        assertNotNull(result);
        assertNotNull(result2);
        assertNotNull(result3);
        assertNotNull(result4);
        assertNotNull(result5);

    }

    @Test
    void testPipingWithNullFirstCommand() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            commandLineInterpreter.piping(null, "cat");
        });
        assertEquals("Text cannot be null", exception.getMessage());
    }

    @Test
    void testPipingWithNullSecondCommand() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            commandLineInterpreter.piping("ls", null);
        });
        assertEquals("Text cannot be null", exception.getMessage());
    }

    @Test
    void testPipingWithEmptyFirstCommandOutput() throws IOException {
        String result = commandLineInterpreter.piping("invalidCommand", "cat");
        assertEquals("No output from the first command.", result);
    }

    @Test
    void testPipingWithEmptySecondCommandOutput() throws IOException {

        String result = commandLineInterpreter.piping("ls", "invalidCommand");
        assertEquals("No output from the second command.", result);
    }

    @Test
    void testPipingCatWithNullInput() throws FileNotFoundException {
        String result = commandLineInterpreter.pipingCat(null);
        assertEquals("No input provided", result);
    }

    @Test
    void testPipingCatWithValidInput() throws FileNotFoundException {
        String result = commandLineInterpreter.pipingCat("Hello, World!");
        assertEquals("Hello, World!", result);
    }

}
