package D15;

import java.awt.*;

public class Robot implements Moveable {
    private Point point;

    public Robot() {
        this.point = new Point(0, 0);
    }

    public Robot(Point point) {
        this.point = point;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }
}
