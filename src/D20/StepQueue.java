package D20;

import D18.PointStep;

import java.util.PriorityQueue;

public class StepQueue extends PriorityQueue<PointStep> {
    public StepQueue() {
        super(
                (PointStep a, PointStep b) -> {
                    if (a.getNoSteps() == b.getNoSteps()) {
                        if (a.getPoint().x == b.getPoint().x) {
                            return a.getPoint().y - b.getPoint().y;
                        }
                        return a.getPoint().x - b.getPoint().x;
                    }
                    return a.getNoSteps() - b.getNoSteps();
                }
        );
    }
}
