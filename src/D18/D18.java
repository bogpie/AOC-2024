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
        int linesMax = MAX_LINES;

        var points = new ArrayList<Point>();
        try {
            Scanner scanner = new Scanner(new File("src/D18/input.txt"));

            int countedLines = 0;
            while (scanner.hasNextLine() && countedLines < linesMax) {
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
        for (int i = 0; i <= xMax; i++) {
            for (int j = 0; j <= yMax; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();

        while (!queue.isEmpty()) {
            var currentStep = queue.poll();
            var current = currentStep.getPoint();
            var currentNoSteps = currentStep.getNoSteps();

//            printStepGrid(xMax, yMax, stepGrid);

            if (current.equals(end)) {
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

        System.out.println("No of steps: " + stepGrid[end.x][end.y]);
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
