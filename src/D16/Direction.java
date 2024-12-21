package D16;

import lombok.Getter;

import java.awt.*;
import java.util.List;

public enum Direction {
    WEST(new Point(0, -1)),
    NORTH(new Point(-1, 0)),
    SOUTH(new Point(1, 0)),
    EAST(new Point(0, 1));


    public static Direction charValueOf(char c) {
        return switch (c) {
            case '^' -> NORTH;
            case 'v' -> SOUTH;
            case '<' -> WEST;
            case '>' -> EAST;
            default -> null;
        };
    }

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