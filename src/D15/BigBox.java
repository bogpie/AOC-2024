package D15;

import java.awt.*;

public class BigBox implements Moveable {
    private Point left;
    private Point right;

    public BigBox(Point left, Point right) {
        this.left = left;
        this.right = right;
    }

    public Point getLeft() {
        return left;
    }

    public Point getRight() {
        return right;
    }

    public void setLeft(Point left) {
        this.left = left;
    }

    public void setRight(Point right) {
        this.right = right;
    }
}
