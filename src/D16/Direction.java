package D16;

import lombok.Getter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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

    public char getChar() {
        return switch (this) {
            case NORTH -> '^';
            case SOUTH -> 'v';
            case WEST -> '<';
            case EAST -> '>';
        };
    }

    public static final List<Direction> directions = List.of(EAST, NORTH, WEST, SOUTH);
}