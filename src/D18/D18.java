package D18;

import D16.Direction;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class D18 {
    private final int MAX_X = 70;
    private final int MAX_Y = 70;
    private final int MAX_LINES = 1024;

    private final int DEMO_MAX_X = 6;
    private final int DEMO_MAX_Y = 6;
    private final int DEMO_MAX_LINES = 12;

    public void main() {
        int xMax = MAX_X;
        int yMax = MAX_Y;
        int maxLines = MAX_LINES;

        ArrayList<Point> points = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new File("src/D18/input.txt"));

            int countedLines = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // parse line, in the format of number,number

                int[] coordinates = Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray();

                Point point = new Point(coordinates[1], coordinates[0]);
                points.add(point);
                countedLines++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (points.size() < MAX_LINES) {
            xMax = DEMO_MAX_X;
            yMax = DEMO_MAX_Y;
            maxLines = DEMO_MAX_LINES;
        }

        PointStep endStep = new PointStep(new Point(xMax, yMax), Integer.MAX_VALUE);
        getEndStep(xMax, yMax, new ArrayList<>(points.subList(0, maxLines)), endStep);
        System.out.println("-------------------------");
        System.out.println();
        System.out.println();


        // Do a binary search on max lines
        int left = 0;
        int right = points.size();
        int mid = left + (right - left) / 2;

        while (left < right) {
            endStep = new PointStep(new Point(xMax, yMax), Integer.MAX_VALUE);

            mid = left + (right - left) / 2;
            endStep = getEndStep(xMax, yMax, new ArrayList<>(points.subList(0, mid)), endStep);

            // If we blocked the path, we need to look for a smaller number of points
            if (endStep.getNoSteps() == Integer.MAX_VALUE) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }

        // Get point that blocked the path, in the format of y,x
        int blockIndex = right - 1;
        if (blockIndex < 0 || blockIndex >= points.size()) {
            throw new RuntimeException("No block found");
        }
        System.out.println(points.get(blockIndex).y + "," + points.get(blockIndex).x);
    }

    private PointStep getEndStep(int xMax, int yMax, ArrayList<Point> points, PointStep endStep) {
        char[][] grid = new char[xMax + 1][yMax + 1];
        for (int i = 0; i <= xMax; i++) {
            for (int j = 0; j <= yMax; j++) {
                grid[i][j] = '.';
            }
        }

        for (Point point : points) {
            grid[point.x][point.y] = '#';
        }

        int[][] stepGrid = new int[xMax + 1][yMax + 1];

        for (int i = 0; i <= xMax; i++) {
            for (int j = 0; j <= yMax; j++) {
                stepGrid[i][j] = Integer.MAX_VALUE;
            }
        }

        Point start = new Point(0, 0);
        Point end = new Point(xMax, yMax);


        PointStep startStep = new PointStep(start, 0);

        Queue<PointStep> queue = new LinkedList<>();
        queue.add(startStep);
        stepGrid[start.x][start.y] = 0;

        // print grid
//        printGrid(xMax, yMax, grid);

        while (!queue.isEmpty()) {
            var currentStep = queue.poll();
            var current = currentStep.getPoint();
            var currentNoSteps = currentStep.getNoSteps();

            if (current.equals(end)) {
                endStep = currentStep;
                break;
            }

            for (Direction direction : Direction.values()) {
                Point next = new Point(
                        current.x + direction.getValue().x,
                        current.y + direction.getValue().y
                );

                if (next.x < 0 || next.x > xMax || next.y < 0 || next.y > yMax) {
                    continue;
                }

                if (grid[next.x][next.y] == '#') {
                    continue;
                }

                if (currentNoSteps + 1 >= stepGrid[next.x][next.y]) {
                    continue;
                }

                stepGrid[next.x][next.y] = currentNoSteps + 1;

                PointStep nextStep = new PointStep(next, currentNoSteps + 1);
                queue.add(nextStep);
            }
        }

        System.out.println("No of steps: " + endStep.getNoSteps() + " for " + points.size() + " points " +
                "and blocked path at " + points.get(points.size() - 1).y + "," + points.get(points.size() - 1).x);

        System.out.println();
        return endStep;
    }

    private static void printGrid(int xMax, int yMax, char[][] grid) {
        for (int i = 0; i <= xMax; i++) {
            for (int j = 0; j <= yMax; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void printStepGrid(int xMax, int yMax, int[][] stepGrid) {
        for (int i = 0; i <= xMax; i++) {
            for (int j = 0; j <= yMax; j++) {
                if (stepGrid[i][j] == Integer.MAX_VALUE) {
                    System.out.print(0);
                } else {
                    System.out.print(stepGrid[i][j]);
                }
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }
}
