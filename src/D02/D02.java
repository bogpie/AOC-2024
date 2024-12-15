package D02;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Math.abs;

public class D02 {
    public static void main() {
        Scanner sc = null;
        D02 d02 = new D02();

        try {
            sc = new Scanner(new File("src/D02/input.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int noSafePartOne = 0;
        int noSafePartTwo = 0;
        assert sc != null;
        while (sc.hasNextLine()) {
            String line = sc.nextLine();

            ArrayList <Integer> numbers = new ArrayList<>();
            for (String s : line.split(" ")) {
                numbers.add(Integer.parseInt(s));
            }

            noSafePartOne = (d02.computeSafety(numbers) ? noSafePartOne + 1 : noSafePartOne);

            // Try removing one number until safe
            for (int i = 0; i < numbers.size(); i++) {
                ArrayList<Integer> newNumbers = new ArrayList<>(numbers);
                newNumbers.remove(i);
                boolean isSafe = d02.computeSafety(newNumbers);
                if (isSafe) {
                    noSafePartTwo = noSafePartTwo + 1;
                    break;
                }
            }
        }

        System.out.println(noSafePartOne);
        System.out.println(noSafePartTwo);
    }

    private boolean computeSafety(ArrayList<Integer> numbers) {
        int oldValue = numbers.get(0);
        int newValue = numbers.get(1);
        boolean isIncreasing = oldValue < newValue;

        if (isFar(oldValue, newValue)) {
            return false;
        }

        boolean isSafe = true;
        oldValue = newValue;
        for (int i = 2; i < numbers.size(); i++) {
            newValue = numbers.get(i);

            if (newValue > oldValue && !isIncreasing) {
                isSafe = false;
                break;
            }
            if (newValue < oldValue && isIncreasing) {
                isSafe = false;
                break;
            }

            if (isFar(oldValue, newValue)) {
                isSafe = false;
                break;
            }

            oldValue = newValue;
        }

        return isSafe;
    }

    private boolean isFar(int oldValue, int newValue) {
        return abs(oldValue - newValue) < 1 || abs(oldValue - newValue) > 3;
    }
}