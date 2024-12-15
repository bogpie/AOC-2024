package D04;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.abs;

public class D04 {
    public static void main() {
        D04 d04 = new D04();

        // Read all the input in a string
        String input = "";
        try {
            input = new Scanner(new File("src/D04/input.txt")).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        int count = doFirstPart(input);
        System.out.println(count);

        count = doSecondPart(input);
        System.out.println(count);
    }

    private static int doSecondPart(String input) {
        int count = 0;
        List<String> lines = List.of(input.split("\n"));

        // Lines contain \r at the end of the line
        for (int idLine = 1; idLine < lines.size() - 1; idLine++) {
            for (int idChr = 1 ; idChr < lines.get(0).length() - 2; idChr++) {

                char crt = lines.get(idLine).charAt(idChr);
                char upLeft = lines.get(idLine - 1).charAt(idChr - 1);
                char upRight = lines.get(idLine - 1).charAt(idChr + 1);
                char downLeft = lines.get(idLine + 1).charAt(idChr - 1);
                char downRight = lines.get(idLine + 1).charAt(idChr + 1);

                // Look for M A S crossed with M A S, or reverse

                if (crt != 'A') {
                    continue;
                }

                if (isNotMs(upLeft) || isNotMs(upRight) || isNotMs(downLeft) || isNotMs(downRight)) {
                    continue;
                }

                if (upLeft == downRight || upRight == downLeft) {
                    continue;
                }

                count++;
            }
        }
        return count;
    }

    private static boolean isNotMs(char upLeft) {
        return upLeft != 'M' && upLeft != 'S';
    }

    private static int doFirstPart(String input) {
        int count = 0;
        List<String> lines = List.of(input.split("\n"));
        count += countXmas(lines);

        // Flip the input and count again
        List<String> flippedLines = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            flippedLines.add("");
        }
        for (String line : lines) {
            for (int idChr = 0; idChr < lines.get(0).length() - 1; idChr++) {
                flippedLines.set(idChr, flippedLines.get(idChr) + line.charAt(idChr));
            }
        }
        count += countXmas(flippedLines);

        // Get left to right diagonal
        List<String> leftToRightDiagonal = getLeftToRightDiagonal(lines);

        count += countXmas(leftToRightDiagonal);

        // Get right to left diagonal
        List<String> rightToLeftDiagonal = getRightToLeftDiagonal(lines);

        count += countXmas(rightToLeftDiagonal);
        return count;
    }

    private static List<String> getRightToLeftDiagonal(List<String> lines) {
        List<String> rightToLeftDiagonal = new ArrayList<>();
        for (int i = 0; i < lines.size() * 2; i++) {
            rightToLeftDiagonal.add("");
        }

        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(0).length() - 1; j++) {
                int idDiag = i + j;
                var currentLine = rightToLeftDiagonal.get(idDiag);
                rightToLeftDiagonal.set(idDiag, currentLine + lines.get(i).charAt(j));
            }
        }
        return rightToLeftDiagonal;
    }

    private static List<String> getLeftToRightDiagonal(List<String> lines) {
        List<String> leftToRightDiagonal = new ArrayList<>();
        for (int i = 0; i < lines.size() * 2; i++) {
            leftToRightDiagonal.add("");
        }

        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(0).length() - 1; j++) {
                int idDiag = i - j + lines.size() - 1;
                var currentLine = leftToRightDiagonal.get(idDiag);
                leftToRightDiagonal.set(idDiag, currentLine + lines.get(i).charAt(j));
            }
        }
        return leftToRightDiagonal;
    }

    private static int countXmas(List<String> lines) {
        int count = 0;
        for (String line : lines) {
            // Find the word XMAS or SAMX

            Matcher xmasMatcher = Pattern.compile("(XMAS)").matcher(line);
            count += xmasMatcher.results().count();

            Matcher samxMatcher = Pattern.compile("(SAMX)").matcher(line);
            count += samxMatcher.results().count();
        }
        return count;
    }
}