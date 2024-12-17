package D16;

import java.util.LinkedList;
import java.util.PriorityQueue;

public class ComplexQueue extends PriorityQueue<ComplexNode> {
    public ComplexQueue() {
        super(
                (n1, n2) -> n1.getWeight() - n2.getWeight()
        );
    }

}
