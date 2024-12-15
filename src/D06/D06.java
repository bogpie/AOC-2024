package D06;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class D06 {
    final char OBSTACLE = '#';
    final char EMPTY = '.';
    final char MARKED = 'X';
    final char START = '^';

    enum Direction {
        N, E, S, W
    }

    ArrayList<Direction> directions = new ArrayList<>(List.of(Direction.N, Direction.E, Direction.S, Direction.W));

    ArrayList<Point> offsets = new ArrayList<>(List.of(new Point(-1, 0), new Point(0, 1), new Point(1, 0), new Point(0, -1)));



    static class Point implements Comparable<Point> {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        @Override
        public int compareTo(Point o) {
            if (this.x != o.x) {
                return Integer.compare(this.x, o.x);
            }
            return Integer.compare(this.y, o.y);
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + x;
            result = 31 * result + y;
            return result;
        }
    }


    static class PointWithDirection implements Comparable<PointWithDirection> {
        Point point;
        Direction direction;

        public PointWithDirection(Point point, Direction direction) {
            this.point = point;
            this.direction = direction;
        }

        @Override
        public int compareTo(PointWithDirection o) {
            if (this.point.x != o.point.x) {
                return Integer.compare(this.point.x, o.point.x);
            }
            if (this.point.y != o.point.y) {
                return Integer.compare(this.point.y, o.point.y);
            }
            return Integer.compare(this.direction.ordinal(), o.direction.ordinal());
        }

        @Override
        public String toString() {
            return "(" + point.x + ", " + point.y + ", " + direction + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PointWithDirection that = (PointWithDirection) o;
            return point.equals(that.point) && direction == that.direction;
        }

    }
    public void main() {
        // Read all the input in a string matrix
        ArrayList<String> input = new ArrayList<>();
        try {
            Scanner sc = new Scanner(new File("src/D06/input.txt"));
            while (sc.hasNextLine()) {
                input.add(sc.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        TreeSet<Point> obstacles = new TreeSet<>();
        Point start = new Point(0, 0);

        for (int idLine = 0; idLine < input.size(); idLine++) {
            for (int idChr = 0; idChr < input.get(0).length(); idChr++) {
                String line = input.get(idLine);
                switch (line.charAt(idChr)) {
                    case OBSTACLE -> obstacles.add(new Point(idLine, idChr));
                    case START -> start = new Point(idLine, idChr);
                    default -> {
                    }
                }
            }
        }

        TreeSet<Point> visited = new TreeSet<>();
        getVisited(input, obstacles, start, visited);
        System.out.println(visited.size());

        int noLoops = 0;
        for (Point point : visited) {
            if (point == start) {
                continue;
            }


            // Place an obstacle
            input.set(
                    point.x,
                    input.get(point.x).substring(0, point.y)
                            + OBSTACLE
                            + input.get(point.x).substring(point.y + 1)
            );

            obstacles.add(point);

            TreeSet<Point> newVisited = new TreeSet<>();
            boolean isLoop = getVisited(input, obstacles, start, newVisited);

            if (isLoop) {
                noLoops++;
            }

            // De-place the obstacle
            input.set(
                    point.x,
                    input.get(point.x).substring(0, point.y)
                            + EMPTY
                            + input.get(point.x).substring(point.y + 1)
            );
            obstacles.remove(point);
        }

        System.out.println(noLoops);
        System.out.println();


    }

    private boolean getVisited(ArrayList<String> input, TreeSet<Point> obstacles, Point start, TreeSet<Point> visited) {
        TreeMap<PointWithDirection, Boolean> hasVisitedMap = new TreeMap<>();

        int currentDirectionIndex = 0;
        Point current = start;

        visited.add(current);
        while (true) {
            current = new Point(
                    current.x + offsets.get(currentDirectionIndex).x,
                    current.y + offsets.get(currentDirectionIndex).y
            );

            if (current.x < 0 || current.x >= input.get(0).length() || current.y < 0 || current.y >= input.size()) {
                return false;
            }

            // Check for infinte loops
            PointWithDirection currentPointWithDirection = new PointWithDirection(current, directions.get(currentDirectionIndex));
            if (hasVisitedMap.containsKey(currentPointWithDirection)) {
                return true;
            }
            hasVisitedMap.put(currentPointWithDirection, true);

            if (obstacles.contains(current)) {
                // Turn back and shift direction
                current = new Point(
                        current.x - offsets.get(currentDirectionIndex).x,
                        current.y - offsets.get(currentDirectionIndex).y
                );
                currentDirectionIndex = (currentDirectionIndex + 1) % directions.size();

                continue;
            }

            visited.add(current);
            input.set(current.x, input.get(current.x).substring(0, current.y) + MARKED + input.get(current.x).substring(current.y + 1));
        }

    }
}