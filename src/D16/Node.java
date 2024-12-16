package D16;

import java.awt.*;

public class Node {
    private Point point;
    private Direction direction;
    private int weight;

    public Node(Point point, Direction direction, int weight) {
        this.point = point;
        this.direction = direction;
        this.weight = weight;
    }

    public Node() {
        this.point = new Point();
        this.direction = Direction.NORTH;
        this.weight = 0;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getWeight() {
        return weight;
    }

    public Point getPoint() {
        return point;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
