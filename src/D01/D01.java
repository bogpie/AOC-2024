package D01;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class D01 {
    public static void main() {
        ArrayList<Integer> leftList = new ArrayList<>();
        ArrayList<Integer> rightList = new ArrayList<>();

        try {
            Scanner sc = new Scanner(new File("src/D01/input.txt"));
            while (sc.hasNextLine()) {
                leftList.add(sc.nextInt());
                rightList.add(sc.nextInt());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        leftList.sort(null);
        rightList.sort(null);

        int sum = 0;
        for (int i = 0; i < leftList.size(); i++) {
            sum += Math.abs(leftList.get(i) - rightList.get(i));
        }
        System.out.println(sum);

        int[] appears = new int[99999];
        for (Integer integer : rightList) {
            appears[integer]++;
        }

        sum = 0;
        for (Integer integer : leftList) {
            sum += appears[integer] * integer;
        }
        System.out.println(sum);
    }
}