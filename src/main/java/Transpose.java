import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.CmdLineException;

import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class Transpose {

    private boolean isA = false;
    private int numSym = 10;

    @Option(name = "-o", usage = "Output name")
    private String outputName;

    @Option(name = "-a", usage = "Number of symbols")
    private void setNum(int n) {
        numSym = n;
        isA = true;
    }

    @Option(name = "-t", usage = "Cut or do not cut")
    private boolean cut = true;

    @Option(name = "-r", usage = "Right")
    private boolean right = true;

    @Argument(usage = "Input file")
    private String inputName;

    public static void main(String[] args) throws IOException {
        new Transpose().launch(args);
    }

    private void launch(String[] args) throws IOException {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java -jar Transpose.jar options args");
            parser.printUsage(System.err);
            return;
        }

        transpose(inputName, outputName);
    }

    private void transpose(String inputName, String outputName) throws IOException {
        StringBuilder input = new StringBuilder();
        if (inputName == null) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter initial data or \"END\" to complete the input:");
            String str = sc.nextLine();
            while (sc.hasNext() && !str.equals("END")) {
                input.append(str).append("\n");
                str = sc.nextLine();
                if (str.equals("END")) break;
            }
        } else {
            try(BufferedReader reader = new BufferedReader(new FileReader(inputName))) {
                String str = reader.readLine();
                while (str != null) {
                    input.append(str).append("\n");
                    str = reader.readLine();
                }
            }
        }
        List<List<String>> matrix = new ArrayList<>();
        String[] st = input.toString().split("\n");
        int maxSize = 0;
        for (int i = 0; i < st.length; i++) {
            matrix.add(Arrays.asList((st[i].split(" "))));
            if (st[i].split(" ").length > maxSize) maxSize = st[i].split(" ").length;
        }
        /*if (outputName == null) {
            for (int i = 0; i < matrix.size(); i++) {
                System.out.println(matrix.get(i).toString().split(" "));
            }
        }*/
        if (outputName == null) {
            for (int i = 0; i < matrix.size(); i++) {
                for (int j = 0; j < matrix.get(i).size(); j++) {
                    System.out.print(matrix.get(i).get(j) + " ");
                }
                System.out.print("\n");
            }
        }
        transposeMatrix(matrix, maxSize);
    }
    List<String[]> result = new ArrayList<>();
    private void transposeMatrix(List<List<String>> matrix, int maxSize) {
        int k = 0;
        String str = new String();
        for (int i = 0; i < maxSize; i++) {
            //result.add(Collections.emptyList());
            for (List<String> strings : matrix) {
                if (strings.size() > k) {
                    str += strings.get(k) + " ";
                    //result.get(i).add(strings.get(k));
                } else {
                    str += " " + " ";
                    //result.get(i).add("");
                }
            }
            k++;
            //System.out.println(str);
            result.add(str.split(" "));
            str = "";
        }
        if (outputName == null) {
            for (int i = 0; i < result.size(); i++) {
                for (int j = 0; j < result.get(i).length; j++) {
                    System.out.print(result.get(i)[j] + " ");
                }
                System.out.print("\n");
            }
        }
        format(result, isA, numSym, cut, right);
        if (outputName == null) {
            for (int i = 0; i < result.size(); i++) {
                for (int j = 0; j < result.get(i).length; j++) {
                    System.out.print(result.get(i)[j] + " ");
                }
                System.out.print("\n");
            }
        }
    }
    private void format(List<String[]> result, boolean isA, int numSym, boolean cut, boolean right) {
        for (int i = 0; i < result.size(); i++) {
            for (int j = 0; j < result.get(i).length; j++) {
                if (result.get(i)[j].length() > numSym) {
                    if (right && cut) result.get(i)[j] = result.get(i)[j].substring(result.get(i)[j].length() - numSym);
                    if (cut) result.get(i)[j] = result.get(i)[j].substring(0, result.get(i)[j].length() - numSym);
                }
                else {
                    if (right) {
                        for (int y = 0; y < (numSym - result.get(i)[j].length() + 1); y++) {
                            result.get(i)[j] = " " + result.get(i)[j];
                        }
                    }
                    else if (isA) {
                        for (int y = 0; y < (numSym - result.get(i)[j].length() + 1); y++) {
                            result.get(i)[j] += " ";
                        }
                    }
                    }
                }
            }
    }
}
