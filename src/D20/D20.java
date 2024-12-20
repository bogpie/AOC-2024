package D20;

import D16.Direction;
import D18.PointStep;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static D16.RaceablePointType.*;

public class D20 {
    static int MIN_SAVE_LOOKUP = 100;

    public void main() {
        char[][] area;
        ArrayList<String> lines = new ArrayList<String>();
        try {
            Scanner scanner = new Scanner(new File("src/D20/input.txt"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lines.add(line);
                System.out.println(line);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        area = new char[lines.size()][lines.get(0).length()];
        Point start = new Point(0, 0);
        Point end = new Point(0, 0);
        findStartEnd(area, lines, start, end);

        int[][] steps = new int[area.length][area[0].length];
        ArrayList<Point> path = new ArrayList<>();
        fillSteps(area, start, end, steps, path);
        System.out.println("Steps to reach end: " + steps[end.x][end.y]);

        // For each point in the path, attempt to cheat by going through a wall
        int[] cheatsPerNoSteps = new int[10000];

        for (Point point : path) {
            // Look for a wall to go through, and see how many steps we save
            for (Direction direction : Direction.values()) {

                Point wallPoint = new Point(
                        point.x + direction.getValue().x,
                        point.y + direction.getValue().y
                );

                if (getIsOutOfBounds(area, wallPoint)) {
                    continue;
                }

                if (!getIsWallPoint(area, wallPoint)) {
                    continue;
                }

                Point nextPoint = new Point(
                        point.x + direction.getValue().x * 2,
                        point.y + direction.getValue().y * 2
                );
                if (getIsBadPoint(area, nextPoint)) continue;

                int noStepsSaved = steps[nextPoint.x][nextPoint.y] - steps[point.x][point.y] - 2;

                if (noStepsSaved < 0) {
                    continue;
                }

                cheatsPerNoSteps[noStepsSaved]++;
            }
        }

        for (int i = 0; i < cheatsPerNoSteps.length; i++) {
            if (cheatsPerNoSteps[i] < 0) {
                throw new RuntimeException("Negative cheatsPerNoSteps");
            }

            if (cheatsPerNoSteps[i] == 0) {
                continue;
            }

            System.out.println("There are " + cheatsPerNoSteps[i] + " cheats that save " + i + " picoseconds");
        }

        long noStepsSavedInLookup = 0;
        for (int i = MIN_SAVE_LOOKUP; i < cheatsPerNoSteps.length; i++) {
            noStepsSavedInLookup += cheatsPerNoSteps[i];
        }

        System.out.println("There are " + noStepsSavedInLookup + " cheats that save at least " + MIN_SAVE_LOOKUP + " " +
                "picoseconds");
        System.out.println("Final answer for part one: " + noStepsSavedInLookup);
    }

    private static boolean getIsBadPoint(char[][] area, Point nextPoint) {
        return getIsOutOfBounds(area, nextPoint) || getIsWallPoint(area, nextPoint);
    }

    private static boolean getIsWallPoint(char[][] area, Point nextPoint) {
        if (area[nextPoint.x][nextPoint.y] == WALL.getValue()) {
            return true;
        }
        return false;
    }

    private static boolean getIsOutOfBounds(char[][] area, Point nextPoint) {
        return nextPoint.x < 0 || nextPoint.x >= area.length
                || nextPoint.y < 0 || nextPoint.y >= area[0].length;
    }

    private static void findStartEnd(char[][] area, ArrayList<String> lines, Point start, Point end) {
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(i).length(); j++) {
                area[i][j] = lines.get(i).charAt(j);
                if (area[i][j] == START.getValue()) {
                    start.setLocation(i, j);
                } else if (area[i][j] == END.getValue()) {
                    end.setLocation(i, j);
                }
            }
        }
    }

    private static void fillSteps(
            char[][] area, Point start, Point end, int[][] steps, ArrayList<Point> path
    ) {
        for (int[] step : steps) {
            Arrays.fill(step, Integer.MAX_VALUE);
        }

        steps[start.x][start.y] = 0;

        PointStep startStep = new PointStep(start, 0);
        StepQueue queue = new StepQueue();
        queue.add(startStep);

        // There is a single path, according to the problem statement
        path.add(start);

        while (!queue.isEmpty()) {
            PointStep current = queue.poll();
            Point currentPoint = current.getPoint();
            int currentSteps = current.getNoSteps();

            if (currentPoint.equals(end)) {
                break;
            }

            for (Direction direction : Direction.values()) {
                Point nextPoint = new Point(
                        currentPoint.x + direction.getValue().x,
                        currentPoint.y + direction.getValue().y
                );

                if (getIsBadPoint(area, nextPoint)) continue;

                if (currentSteps + 1 >= steps[nextPoint.x][nextPoint.y]) {
                    continue;
                }

                steps[nextPoint.x][nextPoint.y] = currentSteps + 1;
                queue.add(new PointStep(nextPoint, currentSteps + 1));
                path.add(nextPoint);
            }
        }
    }
}
