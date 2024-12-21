package D21;

import D16.Direction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import static D16.Direction.directions;

public class TypingPointQueue extends PriorityQueue<TypingPoint> {
    // Prioritize nodes with the smallest path, then the smallest distance
    public TypingPointQueue() {
        super(
                (a, b) -> {
                    if (a.getPath().length() == b.getPath().length()) {
                        if (a.getDistance() == b.getDistance()) {
                            // Avoid turns

                            var aLetter = a.getPath().charAt(a.getPath().length() - 1);
                            int aDirection = Direction.charValueOf(aLetter).ordinal();

                            var bLetter = b.getPath().charAt(b.getPath().length() - 1);
                            int bDirection = Direction.charValueOf(bLetter).ordinal();

                            // If taking a turn for a, prioritize b
                            var aPrevLetter = a.getPath().length() > 1 ?
                                    a.getPath().charAt(a.getPath().length() - 2)
                                    : aLetter;

                            var bPrevLetter = b.getPath().length() > 1 ?
                                    b.getPath().charAt(b.getPath().length() - 2)
                                    : bLetter;

                            boolean aIsTurn = aPrevLetter != aLetter;
                            boolean bIsTurn = bPrevLetter != bLetter;

                            // Prioritize <, ^, >, v
                            if (!aIsTurn) {
                                if (!bIsTurn) {
                                    return Integer.compare(aDirection, bDirection);
                                }
                                return -1;
                            }
                            return 1;
                        }
                        return Double.compare(a.getDistance(), b.getDistance());
                    }
                    return Integer.compare(a.getPath().length(), b.getPath().length());

                }
        );
    }
}
