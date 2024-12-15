package D15;

public enum Type {
    WALL('#'),
    EMPTY('.'),
    BOX('O'),
    ROBOT('@'),
    NORTH('^'),
    EAST('>'),
    SOUTH('v'),
    WEST('<'),
    BIG_BOX_LEFT('['),
    BIG_BOX_RIGHT(']');

    private final char value;

    Type(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }
}
