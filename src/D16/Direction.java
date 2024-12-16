package D16;

import lombok.Getter;

import java.awt.*;

public enum Direction {
    NORTH(new Point(-1, 0)),
    SOUTH(new Point(1, 0)),
    WEST(new Point(0, -1)),
    EAST(new Point(0, 1));

    private final Point value;


    Direction(Point direction) {
        this.value = direction;
    }

    public Point getValue() {
        return value;
    }

    public Direction getOpposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case WEST -> EAST;
            case EAST -> WEST;
        };
    }
}