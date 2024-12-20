package D16;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

import static D16.Direction.*;
import static D16.RaceablePointType.*;

public class D16 {

    public static final int TURN_AND_MOVE_PENALTY = 1001;
    public static final int FORWARD_PENALTY = 1;

    ArrayList<Direction> getTurns(Direction d) {
        if (d == Direction.NORTH || d == SOUTH) {
            return new ArrayList<>(List.of(WEST, EAST));
        } else {
            return new ArrayList<>(List.of(Direction.NORTH, SOUTH));
        }
    }

    ArrayList<Direction> getAllTurns() {
        return new ArrayList<>(List.of(NORTH, SOUTH, EAST, WEST));
    }


    public void main() {
        char[][] maze;

        try {
            File input = new File("src/D16/input.txt");
            Scanner scanner = new Scanner(input);
            List<String> lines = new ArrayList<>();
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
            scanner.close();
            maze = new char[lines.size()][lines.get(0).length()];
            for (int i = 0; i < lines.size(); i++) {
                maze[i] = lines.get(i).toCharArray();
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return;
        }

        int[][] filled = new int[maze.length][maze[0].length];

        for (int[] ints : filled) {
            Arrays.fill(ints, Integer.MAX_VALUE);
        }

        Point reindeer = new Point();
        Point goal = new Point();


        ArrayList<Node> lastNodes = new ArrayList<>();
        doPartOne(maze, filled, reindeer, goal, lastNodes);

//        printFilled(filled);

        System.out.println(filled[goal.x][goal.y]);

        // For part two just BFS, but store the paths, and if we reach the end with the same score, add the path's
        // nodes to a unique list
        doPartTwo(maze, filled[goal.x][goal.y], reindeer, goal);
    }

    private static void printFilled(int[][] filled) {
        for (int[] ints : filled) {
            for (int anInt : ints) {
                if (anInt == Integer.MAX_VALUE) {
                    System.out.print("X     ");
                } else {
                    // print anInt with a width of 5
                    System.out.printf("%-5d ", anInt);
                }
            }
            System.out.println();
        }
    }

    private void doPartTwo(char[][] maze, int goalScore, Point reindeer, Point goal) {
        ComplexQueue complexQueue = new ComplexQueue();

        HashSet<Point> partOfPath = new HashSet<>();
        partOfPath.add(reindeer);

        complexQueue.add(
                new ComplexNode(
                        new Node(reindeer, EAST, 0),
                        new ArrayList<>()
                )
        );

        var visitedScore = new HashMap<Direction, int[][]>();
        for (var direction : getAllTurns()) {
            visitedScore.put(direction, new int[maze.length][maze[0].length]);
        }
        for (var direction : getAllTurns()) {
            for (int i = 0; i < maze.length; i++) {
                for (int j = 0; j < maze[i].length; j++) {
                    visitedScore.get(direction)[i][j] = Integer.MAX_VALUE;
                }
            }
        }

        while (!complexQueue.isEmpty()) {
            ComplexNode complexNode = complexQueue.poll();

            Point point = complexNode.getPoint();

            // If reached the goal and the score is the same, remember the path's nodes
            if (point.equals(goal)) {
                if (complexNode.getWeight() == goalScore) {
                    partOfPath.addAll(complexNode.getPath());
                }
            }

            // Go forward first
            Point forward = new Point(
                    point.x + complexNode.getDirection().getValue().x,
                    point.y + complexNode.getDirection().getValue().y
            );

            // Check if forward is not worse than the goal score
            if (maze[forward.x][forward.y] != WALL.getValue()
                    && complexNode.getWeight() + FORWARD_PENALTY <= goalScore
                    && complexNode.getWeight() + FORWARD_PENALTY <= visitedScore.get(complexNode.getDirection())[forward.x][forward.y]
            ) {

                var newPath = new ArrayList<>(complexNode.getPath());
                newPath.add(forward);
                complexQueue.add(
                        new ComplexNode(forward, complexNode.getDirection(),
                                complexNode.getWeight() + FORWARD_PENALTY,
                                newPath
                        )
                );

                visitedScore.get(complexNode.getDirection())[forward.x][forward.y] =
                        complexNode.getWeight() + FORWARD_PENALTY;
            }

            // For each turn, check if we can go that way and if it's not worse than the goal score
            for (Direction turn : getTurns(complexNode.getDirection())) {
                Point newPoint = new Point(
                        point.x + turn.getValue().x,
                        point.y + turn.getValue().y
                );

                if (maze[newPoint.x][newPoint.y] != WALL.getValue() &&
                        complexNode.getWeight() + TURN_AND_MOVE_PENALTY <= goalScore
                        && complexNode.getWeight() + TURN_AND_MOVE_PENALTY <= visitedScore.get(turn)[newPoint.x][newPoint.y]
                ) {
                    ArrayList<Point> newPath = new ArrayList<>(complexNode.getPath());
                    newPath.add(newPoint);
                    complexQueue.add(
                            new ComplexNode(
                                    newPoint,
                                    turn,
                                    complexNode.getWeight() + TURN_AND_MOVE_PENALTY,
                                    newPath
                            )
                    );

                    visitedScore.get(turn)[newPoint.x][newPoint.y] =
                            complexNode.getWeight() + TURN_AND_MOVE_PENALTY;
                }
            }
        }

        System.out.println(partOfPath.size());
//        printMazeAndPath(maze, partOfPath);
    }

    private static void printMazeAndPath(char[][] maze, HashSet<Point> partOfPath) {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (partOfPath.contains(new Point(i, j))) {
                    System.out.print("Z ");
                } else {
                    System.out.print(maze[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    private void doPartOne(char[][] maze, int[][] filled, Point reindeer, Point goal, ArrayList<Node> lastNodes) {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j] == START.getValue()) {
                    reindeer.setLocation(i, j);
                    continue;
                }
                if (maze[i][j] == END.getValue()) {
                    goal.setLocation(i, j);
                }

            }
        }
        filled[reindeer.x][reindeer.y] = 0;

        WeightedQueue weightedQueue = new WeightedQueue();
        weightedQueue.add(new Node(reindeer, EAST, 0));

        while (!weightedQueue.isEmpty()) {
            Node node = weightedQueue.poll();
            Point point = node.getPoint();

            if (point.equals(goal)) {
                Node last = new Node();
                last.setDirection(node.getDirection());
                last.setPoint(node.getPoint());
                last.setWeight(node.getWeight());
                lastNodes.add(last);
            }

            // Go forward first
            Point forward = new Point(
                    point.x + node.getDirection().getValue().x,
                    point.y + node.getDirection().getValue().y
            );

            // Check if forward is better
            if (maze[forward.x][forward.y] != WALL.getValue()
                    && node.getWeight() + FORWARD_PENALTY <= filled[forward.x][forward.y]) {
                filled[forward.x][forward.y] = node.getWeight() + 1;
                weightedQueue.add(new Node(forward, node.getDirection(), node.getWeight() + 1));
            }

            // For each turn, check if we can go that way and if it's better
            for (Direction turn : getTurns(node.getDirection())) {
                Point newPoint = new Point(
                        point.x + turn.getValue().x,
                        point.y + turn.getValue().y
                );

                if (maze[newPoint.x][newPoint.y] != WALL.getValue() &&
                        node.getWeight() + TURN_AND_MOVE_PENALTY <= filled[newPoint.x][newPoint.y]) {
                    filled[newPoint.x][newPoint.y] = node.getWeight() + TURN_AND_MOVE_PENALTY;
                    weightedQueue.add(new Node(newPoint, turn, node.getWeight() + TURN_AND_MOVE_PENALTY));
                }
            }
        }
    }
}
