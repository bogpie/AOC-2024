package D18;

import java.awt.*;

public class PointStep {
    private Point point;
    private int noSteps;

    public PointStep() {
        this.point = new Point();
        this.noSteps = 0;
    }

    public PointStep(Point point, int noSteps) {
        this.point = point;
        this.noSteps = noSteps;
    }

    public Point getPoint() {
        return point;
    }

    public int getNoSteps() {
        return noSteps;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public void setNoSteps(int noSteps) {
        this.noSteps = noSteps;
    }
}
