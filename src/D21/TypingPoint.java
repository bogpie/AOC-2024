package D21;

import java.awt.*;

public class TypingPoint {
    private Point point;
    private String path;
    private int distance;

    public TypingPoint(Point point, String path, int distance) {
        this.point = point;
        this.path = path;
        this.distance = distance;
    }

    public Point getPoint() {
        return point;
    }

    public String getPath() {
        return path;
    }

    public int getDistance() {
        return distance;
    }
}
