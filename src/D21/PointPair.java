package D21;

import java.awt.*;

public class PointPair implements Comparable<PointPair> {
    private final Point start;
    private final Point desired;

    @Override
    public int compareTo(PointPair o) {
        if (start.equals(o.start)) {
            if (desired.equals(o.desired)) {
                return 0;
            }
            return desired.x == o.desired.x ?
                    Integer.compare(desired.y, o.desired.y) :
                    Integer.compare(desired.x, o.desired.x);
        }

        return start.x == o.start.x ?
                Integer.compare(start.y, o.start.y) :
                Integer.compare(start.x, o.start.x);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PointPair pointPair)) return false;
        return start.equals(pointPair.start) && desired.equals(pointPair.desired);
    }

    public PointPair(Point start, Point desired) {
        this.start = start;
        this.desired = desired;
    }

    public Point getDesired() {
        return desired;
    }

    public Point getStart() {
        return start;
    }
}
