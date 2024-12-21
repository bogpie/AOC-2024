package D21;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class TypingPointQueue extends PriorityQueue<TypingPoint> {
    // Prioritize nodes with the smallest path, then the smallest distance
    public TypingPointQueue() {

        super(
                (a, b) -> {
                    if (a.getPath().length() == b.getPath().length()) {
                        if (a.getDistance() == b.getDistance()) {
                            return a.getPath().compareTo(b.getPath()); // Tie-break by lexicographical order of paths
                        }
                        return Double.compare(a.getDistance(), b.getDistance());
                    }
                    return Integer.compare(a.getPath().length(), b.getPath().length());

                }
        );
    }
}
