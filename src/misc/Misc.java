package misc;

import java.util.ArrayList;
import java.util.List;

public class Misc {
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
}