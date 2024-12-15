package D05;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class D05 {
    private static ArrayList<ArrayList<Boolean>> canFollow;

    public void main() {
        ArrayList<ArrayList<Integer>> incorrectLists = new ArrayList<>();
        Long sum = doPartOne(incorrectLists);
        System.out.println(sum);

        sum = doPartTwo(incorrectLists);
        System.out.println(sum);
    }

    private Long doPartTwo(ArrayList<ArrayList<Integer>> incorrectLists) {
        Long sum = 0L;
        for (var incorrectList : incorrectLists) {
            ArrayList<Integer> reordered = reorder(incorrectList);
            sum += reordered.get(reordered.size() / 2);
        }
        return sum;
    }

    private ArrayList<Integer> reorder(ArrayList<Integer> incorrectList) {
        ArrayList<Integer> reordered = new ArrayList<>(incorrectList);

        boolean ordered = false;
        while (!ordered) {
            ordered = true;
            for (int i = 0; i < reordered.size() - 1; i++) {
                if (!canFollow.get(reordered.get(i)).get(reordered.get(i + 1))) {
                    int temp = reordered.get(i + 1);
                    reordered.set(i + 1, reordered.get(i));
                    reordered.set(i, temp);
                    ordered = false;
                    break;
                }
            }
        }

        return reordered;
    }

    private static Long doPartOne(ArrayList<ArrayList<Integer>> incorrectLists) {
        // Read all the input in a string
        String input = "";
        try {
            input = new Scanner(new File("src/D05/input.txt")).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Split string by lines
        String[] lines = input.split("\n");

        canFollow = IntStream.range(0, 100)
                .mapToObj(i -> new ArrayList<>(Collections.nCopies(100, false)))
                .collect(Collectors.toCollection(ArrayList::new));

        // Read rules
        int idLine = 0;
        while (idLine < lines.length) {
            // If not a rule, the updates are next
            String line = lines[idLine++];
            Pattern rulePattern = Pattern.compile("(\\d+)\\|(\\d+)");
            Matcher ruleMatcher = rulePattern.matcher(line);
            if (!ruleMatcher.find()) {
                break;
            }

            int left = Integer.parseInt(ruleMatcher.group(1));
            int right = Integer.parseInt(ruleMatcher.group(2));

            canFollow.get(left).set(right, true);
        }

        // Default rule : 0->any true
        IntStream.range(0, 100).forEach(i -> canFollow.get(0).set(i, true));

        // Read updates
        Long sum = 0L;
        while (idLine < lines.length) {
            // Line is a list of numbers
            String line = lines[idLine++];
            Pattern numberPattern = Pattern.compile("(\\d+)");
            Matcher numberMatcher = numberPattern.matcher(line);
            ArrayList<Integer> numbers = new ArrayList<>();
            while (numberMatcher.find()) {
                numbers.add(Integer.parseInt(numberMatcher.group(1)));
            }

            int old = 0;
            boolean isCorrect = true;
            for (Integer number : numbers) {
                if (!canFollow.get(old).get(number)) {
                    isCorrect = false;
                    break;
                }
                old = number;
            }

            if (isCorrect) {
                Integer middleNumber = numbers.get(numbers.size() / 2);
                sum += middleNumber;
            }
            else {
                incorrectLists.add(numbers);
            }
        }
        return sum;
    }
}