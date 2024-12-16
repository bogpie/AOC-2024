package D16;

import java.util.PriorityQueue;

public class WeightedQueue extends PriorityQueue<Node> {
    public WeightedQueue() {
        super(
                (n1, n2) -> n1.getWeight() - n2.getWeight()
        );
    }
}
