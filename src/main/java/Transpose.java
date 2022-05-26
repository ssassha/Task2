import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.CmdLineException;

import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class Transpose {

    @Option(name = "-o", usage = "Output name")
    private String outputName;

    @Option(name = "-a", usage = "Number of symbols")
    private int numSym;

    @Option(name = "-t", usage = "Cut or do not cut")
    private boolean cut;

    @Option(name = "-r", usage = "Right")
    private boolean right;

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

    int maxSize = 0;
    private void transpose(String inputName, String outputName) throws IOException {
        StringBuilder input = new StringBuilder();
        if (inputName == null) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter initial data or \"END\" to complete the input:");
            String str = sc.nextLine();
            while (sc.hasNext() && !str.equals("END")) {
                if (maxSize < str.split(" ").length) maxSize = str.split(" ").length;
                input.append(str).append("\n");
                str = sc.nextLine();
                if (str.equals("END")) break;
            }
        } else {
            try(BufferedReader reader = new BufferedReader(new FileReader(inputName))) {
                String str = reader.readLine();
                while (str != null) {
                    if (maxSize < str.split(" ").length) maxSize = str.split(" ").length;
                    input.append(str).append("\n");
                    str = reader.readLine();
                }
            }
        }
        ArrayList<ArrayList<String>> matrix = new ArrayList<>();
        String[] st = input.toString().split("\n");
        for (String s : st) {
            matrix.add(new ArrayList<>(Arrays.asList(s.split(" "))));
        }
        transposeMatrix(matrix, maxSize);
    }
    ArrayList<ArrayList<String>> result = new ArrayList<>();
    private void transposeMatrix(ArrayList<ArrayList<String>> matrix, int maxSize) {
        for (int i = 0; i < maxSize; i++) {
            result.add(new ArrayList<>());
            for (ArrayList<String> strings : matrix) {
                if (strings.size() > i) result.get(i).add(strings.get(i));
                else result.get(i).add(" ");
            }
        }
        if (numSym == 0 && (cut || right)) numSym = 10;
        format(result, numSym, cut, right);
        if (outputName == null) {
            for (int i = 0; i < result.size(); i++) {
                StringBuilder str = new StringBuilder();
                for (int j = 0; j < result.get(i).size(); j++) {
                    System.out.print(result.get(i).get(j));
                    if (j + 1 < result.get(i).size()) System.out.print(" ");
                }
                if (i + 1 < result.size()) System.out.print("\n");

            }
        }
        else {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputName))){
                for (int i = 0; i < result.size(); i++) {
                    for (int j = 0; j < result.get(i).size(); j++) {
                        writer.write(result.get(i).get(j));
                        if (j + 1 < result.get(i).size()) writer.write(" ");
                    }
                    if (i + 1 < result.size()) writer.write("\n");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    private void format(ArrayList<ArrayList<String>> result, int numSym, boolean cut, boolean right) {
        for (ArrayList<String> strings : result) {
            for (int j = 0; j < strings.size(); j++) {
                String word = strings.get(j);
                if (word.length() > numSym && cut) word = cut(word, numSym);
                if (word.length() < numSym && right) word = rightSpaces(word, numSym);
                if (word.length() < numSym) word = leftSpaces(word, numSym);
                strings.set(j, word);
            }
        }
    }

    private String cut(String word, int numSym) {
        String newWord;
        if (right) newWord = word.substring(word.length() - numSym);
        else newWord = word.substring(0, numSym);
        return newWord;
    }
    private String rightSpaces(String word, int numSym) {
        StringBuilder newWord = new StringBuilder(word);
        while (newWord.length() < numSym) newWord.insert(0, " ");
        return newWord.toString();
    }
    private String leftSpaces(String word, int numSym) {
        StringBuilder newWord = new StringBuilder(word);
        while (newWord.length() < numSym) newWord.append(" ");
        return newWord.toString();
    }
}
