package D07;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class D07 {
    private static final long LONG_MAX = 9223372036854775807L;

    enum Operator {
        ADD, MULTIPLY, CONCAT
    }

    public void main() {
        // Scan input txt
        List<String> input;
        try {
            input = List.of
                    (new Scanner(new File("src/D07/input.txt")).useDelimiter("\\Z").next().split("\n"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        long partOneSum = 0;
        long partTwoSum = 0;
        for (String line : input) {
            // Find first number - the result
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(line);
            long result = 0;
            if (matcher.find()) {
                result = Long.parseLong(matcher.group());
            }

            // Find the numbers
            List<Long> numbers = new ArrayList<>();
            while (matcher.find()) {
                numbers.add(Long.parseLong(matcher.group()));
            }

            // Backtracking algorithm, + and * between numbers
            ArrayList<Operator> stack = new ArrayList<>();
            boolean found = backtracking(numbers, result, stack, List.of(Operator.ADD, Operator.MULTIPLY));
            if (found) {
                partOneSum += result;
            }

            found = backtracking(numbers, result, stack, List.of(Operator.ADD, Operator.MULTIPLY, Operator.CONCAT));
            if (found) {
                if (LONG_MAX - partTwoSum < 100000) {
                    throw new RuntimeException("Overflow");
                }
                partTwoSum += result;
            }
        }

        System.out.println(partOneSum);
        System.out.println(partTwoSum);
    }

    private boolean backtracking(List<Long> numbers, long desiredResult, ArrayList<Operator> stack,
                                 List<Operator> possibleOperators) {
        // End condition
        if (stack.size() == numbers.size() - 1) {
            // Compute the result
            long computedResult = numbers.get(0);
            for (int idOperator = 0; idOperator < stack.size(); idOperator++) {
                Operator operator = stack.get(idOperator);
                long nextNumber = numbers.get(idOperator + 1);
                switch (operator) {
                    case ADD -> computedResult += nextNumber;
                    case MULTIPLY -> computedResult *= nextNumber;
                    case CONCAT -> computedResult = Long.parseLong(String.valueOf(computedResult) + nextNumber);
                }
            }

            return computedResult == desiredResult;
        }

        // Recursive case
        boolean found;
        for (Operator operator : possibleOperators) {
            stack.add(operator);
            found = backtracking(numbers, desiredResult, stack, possibleOperators);
            if (found) {
                return true;
            }
            stack.remove(stack.size() - 1);
        }

        return false;
    }
}
