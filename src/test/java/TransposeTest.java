import org.junit.jupiter.api.Test;
import java.io.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransposeTest {
    private boolean  assertFileContent(String[] expected, String result) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(result))) {
            String str = reader.readLine();
            int k = 0;
            while (str != null) {
                if (!expected[k].equals(str)) {
                    System.out.println("Expected line: " + expected[k]);
                    System.out.println("  Actual line: " + str);
                    return false;
                }
                k++;
                str = reader.readLine();
            }
        }
        return true;
    }

    @Test
    void test() throws IOException {
        Transpose.main("-a 3 -t -o out1.txt InputTest/in1.txt".split(" "));
        assertTrue(assertFileContent(new String[]{"fir st ", "cod e  ", "tes t  "},  "out1.txt"));
        Transpose.main("-a 5 -r -o out2.txt InputTest/in2.txt".split(" "));
        assertTrue(assertFileContent(new String[]{"    a bbbbb", "   aa  bbbb", "  aaa   bbb",
        " aaaa    bb", "aaaaa     b"},  "out2.txt"));
    }
}
