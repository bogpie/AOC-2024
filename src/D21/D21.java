package D21;

import D16.Direction;

import java.awt.*;
import java.io.File;
import java.util.*;

import static D16.Direction.*;
import static D20.D20.getIsBadPoint;

public class D21 {
    public void main() {
        ArrayList<String> codes = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File("src/D21/input.txt"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                codes.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 789/456/123/#0A
        char[][] numericKeypad = {
                {'7', '8', '9'},
                {'4', '5', '6'},
                {'1', '2', '3'},
                {'#', '0', 'A'}
        };

        // - ^ A / < v >, but renaming A to B
        char[][] directionalKeypad = {
                {'#', '^', 'B'},
                {'<', 'v', '>'}
        };

        // Remember the positions of each
        HashMap<Character, Point> positions = new HashMap<>();

        for (int i = 0; i < numericKeypad.length; i++) {
            for (int j = 0; j < numericKeypad[i].length; j++) {
                positions.put(numericKeypad[i][j], new Point(i, j));
            }
        }

        for (int i = 0; i < directionalKeypad.length; i++) {
            for (int j = 0; j < directionalKeypad[i].length; j++) {
                positions.put(directionalKeypad[i][j], new Point(i, j));
            }
        }

        long total = 0;
        for (var code : codes) {
            System.out.println(code);

            // Robot 1: Starts on the directional, at A, types the code, when finds looked up char, types A
            Point startPoint = new Point(
                    positions.get('A').x,
                    positions.get('A').y
            );
            String resultedPath = goRobot(numericKeypad, positions, code, startPoint);
            System.out.println(resultedPath);

            // Robot 2: Starts on the numeric, at B, types the code, when finds looked up char, types A
            startPoint = new Point(
                    positions.get('B').x,
                    positions.get('B').y
            );

            // For the directional keypad of the 2nd robot, we need to change the desired point from 'A' to 'B'
            resultedPath = resultedPath.replace('A', 'B');
            resultedPath = goRobot(directionalKeypad, positions, resultedPath, startPoint);
            System.out.println(resultedPath);

            // Robot 3: Like robot 2, but using their result as the code
            startPoint = new Point(
                    positions.get('B').x,
                    positions.get('B').y
            );

            // For the directional keypad of the 3rd robot, we need to change the desired point from 'A' to 'B'
            resultedPath = resultedPath.replace('A', 'B');
            resultedPath = goRobot(directionalKeypad, positions, resultedPath, startPoint);
            System.out.println(resultedPath);


            // For the resultedPath, calculate the score, it is length * parse_numeric(code
            long score = resultedPath.length() * Long.parseLong(code.replaceAll("[^0-9]", ""));
            total += score;
            System.out.println(score);
            System.out.println();
        }
        System.out.println(total);
    }

    private static String goRobot(
            char[][] keypad, HashMap<Character, Point> positions,
            String code, Point initialStartPoint
    ) {
        // Try each letter of the code and apply an A* algorithm to find the shortest path
        StringBuilder resultedPathBuilder = new StringBuilder();
        Point startPoint = initialStartPoint;

        for (char desiredChr : code.toCharArray()) {
            int[][] stepGrid = new int[keypad.length][keypad[0].length];
            resetStepGrid(keypad, stepGrid);
            stepGrid[startPoint.x][startPoint.y] = 0;

            TypingPointQueue queue = new TypingPointQueue();

            var desiredPoint = positions.get(desiredChr);

            if (startPoint.equals(desiredPoint)) {
                resultedPathBuilder.append("A");
                continue;
            }

            var distance = getManhattanDistance(
                    startPoint.x, startPoint.y,
                    desiredPoint.x, desiredPoint.y
            );

            TypingPoint start = new TypingPoint(startPoint, "", distance);

            queue.add(start);
            String chrResultedPath = "";
            while (!queue.isEmpty()) {
                var current = queue.poll();
                var currentPoint = current.getPoint();

                // If we reached the desired point, we can press 'A' and stop
                if (currentPoint.equals(desiredPoint)) {
                    chrResultedPath = current.getPath() + "A";
                    break;
                }

                // Otherwise we need to move towards the desired point
                for (var direction : directions) {
                    var newPoint = new Point(
                            currentPoint.x + direction.getValue().x,
                            currentPoint.y + direction.getValue().y
                    );

                    // Avoid out of bounds and '#' points
                    if (getIsBadPoint(keypad, newPoint)) {
                        continue;
                    }

                    // Avoid already visited points
                    if (current.getPath().length() + 1 >= stepGrid[newPoint.x][newPoint.y]) {
                        continue;
                    }

                    var newTypingPoint = new TypingPoint(
                            newPoint,
                            current.getPath() + direction.getChar(),
                            getManhattanDistance(
                                    newPoint.x, newPoint.y,
                                    desiredPoint.x, desiredPoint.y
                            )
                    );

                    queue.add(newTypingPoint);
                    stepGrid[newPoint.x][newPoint.y] = current.getPath().length() + 1;
                }
            }
            resultedPathBuilder.append(chrResultedPath);

            // Update the start point
            startPoint = desiredPoint;
        }
        return resultedPathBuilder.toString();
    }

    private static int getManhattanDistance(int x, int y, int x1, int y1) {
        return Math.abs(x - x1) + Math.abs(y - y1);
    }

    private static void resetStepGrid(char[][] keypad, int[][] stepGrid) {
        for (int i = 0; i < keypad.length; i++) {
            for (int j = 0; j < keypad[i].length; j++) {
                stepGrid[i][j] = Integer.MAX_VALUE;
            }
        }
    }
}
