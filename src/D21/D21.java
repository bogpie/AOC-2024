package D21;

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
        TreeMap<PointPair, ArrayList<String>> memo = new TreeMap<>();

        for (var code : codes) {
            System.out.println(code);

            // Human: Starts on the directional, at A, types the code, when finds looked up char, types A
            Point startPoint = new Point(
                    positions.get('A').x,
                    positions.get('A').y
            );


            // Don t use memo for the first robot since it is the only one that starts from a different point
            ArrayList<String> humanPaths = goRobot(numericKeypad, positions, code, startPoint, new HashMap<>());

            // Robot 1: Starts on the numeric, at B, types the code, when finds looked up char, types A
            var robotPaths = getRobotPaths(directionalKeypad, positions, humanPaths, memo);

            // Robot 2 -> 25: The same, each output is a new input
            for (int i = 2; i <= 25; i++) {
                robotPaths = getRobotPaths(directionalKeypad, positions, robotPaths, memo);

                System.out.println(memo.size());
            }

            // For the resultedPaths length, calculate the score, it is length * parse_numeric(code)
            long score = robotPaths.get(0).length() * Long.parseLong(code.replaceAll("[^0-9]", ""));
            total += score;
        }
        System.out.println(total);
    }

    private static ArrayList<String> getRobotPaths(char[][] directionalKeypad, HashMap<Character, Point> positions,
                                                   ArrayList<String> firstRobotPaths,
                                                   Map<PointPair, ArrayList<String>> memo) {
        Point startPoint;
        startPoint = new Point(
                positions.get('B').x,
                positions.get('B').y
        );
        var secondRobotPaths = new ArrayList<String>();

        // For the directional keypad of the 2nd robot, we need to change the desired point from 'A' to 'B'
        firstRobotPaths.replaceAll(s -> s.replaceAll("A", "B"));

        int threshold = 1;
        for (var firstRobotPath : firstRobotPaths) {
            secondRobotPaths.addAll(goRobot(directionalKeypad, positions, firstRobotPath, startPoint, memo));
            if (secondRobotPaths.size() > threshold) {
                secondRobotPaths = new ArrayList<>(secondRobotPaths.subList(0, threshold));
            }
        }

        // Only keep secondRobotPaths that are the shortest
        int shortestLength = secondRobotPaths.stream().mapToInt(String::length).min().orElseThrow();
        secondRobotPaths.removeIf(path -> path.length() > shortestLength);

        return secondRobotPaths;
    }

    private static ArrayList<String> goRobot(
            char[][] keypad, HashMap<Character, Point> positions,
            String code, Point initialStartPoint,
            Map<PointPair, ArrayList<String>> memo
    ) {
        // Try each letter of the code and apply an A* algorithm to find the shortest path
        Point startPoint = initialStartPoint;

        ArrayList<String> codePaths = new ArrayList<>();
        codePaths.add("");

        for (char desiredChar : code.toCharArray()) {
            int threshold = 1;
            if (codePaths.size() > threshold) {
                codePaths = new ArrayList<>(codePaths.subList(0, threshold));
            }

            var desiredPoint = positions.get(desiredChar);

            ArrayList<String> charPaths;

            charPaths = getCharPathsBetween(keypad, startPoint, desiredPoint, memo);

            // For each code path, for each char path, append the char path to the code path
            ArrayList<String> newCodePaths = new ArrayList<>();
            for (var currentCodePath : codePaths) {
                for (var currentCharPath : charPaths) {
                    newCodePaths.add(currentCodePath + currentCharPath);
                    if (newCodePaths.size() > threshold) {
                        break;
                    }
                }
            }
            codePaths = newCodePaths;

            // Update the start point
            startPoint = desiredPoint;

        }

        return codePaths;
    }

    private static ArrayList<String> getCharPathsBetween(char[][] keypad, Point startPoint, Point desiredPoint,
                                                         Map<PointPair, ArrayList<String>> memo) {
        // See if already memoized
        var pointPair = new PointPair(startPoint, desiredPoint);
        if (memo.containsKey(pointPair)) {
            return memo.get(pointPair);
        }

        ArrayList<String> charPaths;
        charPaths = new ArrayList<>();

        int[][] stepGrid = new int[keypad.length][keypad[0].length];
        resetStepGrid(keypad, stepGrid);
        stepGrid[startPoint.x][startPoint.y] = 0;

        TypingPointQueue queue = new TypingPointQueue();


        var distance = getManhattanDistance(
                startPoint.x, startPoint.y,
                desiredPoint.x, desiredPoint.y
        );

        TypingPoint start = new TypingPoint(startPoint, "", distance);

        queue.add(start);
        String charPath = "";
        while (!queue.isEmpty()) {
            var current = queue.poll();
            var currentPoint = current.getPoint();

            // If we reached the desired point, we can press 'A' and stop

            int threshold = 1;
            if (currentPoint.equals(desiredPoint)) {
                charPath = current.getPath() + "A";
                charPaths.add(charPath);

                if (charPaths.size() > threshold) {
                    break;
                }
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
                if (current.getPath().length() + 1 > stepGrid[newPoint.x][newPoint.y]) {
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

        memo.put(pointPair, charPaths);
        return charPaths;
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
