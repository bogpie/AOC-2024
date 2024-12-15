package D03;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class D03 {
    public static void main() {
        D03 d03 = new D03();

        // Read all the input in a string
        String input = "";
        try {
            input = new Scanner(new File("src/D03/input.txt")).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        System.out.println(d03.doPartOne(input));
        System.out.println(d03.doPartTwo(input));
    }

    private long doPartTwo(String input) {
        // Initialize state: by default, mul is enabled
        boolean isEnabled = true;
        long sum = 0;

        // Pattern to match all relevant instructions (do(), don't(), mul(x, y))
        String regex = "(do\\(\\)|don't\\(\\)|mul\\((\\d+),(\\d+)\\))";
        Matcher matcher = Pattern.compile(regex).matcher(input);

        while (matcher.find()) {
            String match = matcher.group();

            if (match.equals("do()")) {
                // Enable mul instructions
                isEnabled = true;
            } else if (match.equals("don't()")) {
                // Disable mul instructions
                isEnabled = false;
            } else if (match.startsWith("mul") && isEnabled) {
                // Extract numbers from the mul instruction
                long x = Long.parseLong(matcher.group(2));
                long y = Long.parseLong(matcher.group(3));
                sum += x * y;
            }
        }

        return sum;
    }


    private long doPartOne(String input) {
        // Split string by "mul(number,number)" appearances using a regex
        String regex = "mul\\((\\d+),(\\d+)\\)";
        Matcher matcher = Pattern.compile(regex).matcher(input);

        long sum = 0;
        while (matcher.find()) {
            sum += Long.parseLong(matcher.group(1)) * Long.parseLong(matcher.group(2));
        }

        return sum;
    }
}