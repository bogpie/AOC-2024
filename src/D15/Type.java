package D15;

public enum Type {
    WALL('#'),
    EMPTY('.'),
    BOX('O'),
    ROBOT('@'),
    NORTH('^'),
    EAST('>'),
    SOUTH('v'),
    WEST('<');

    private final char value;

    Type(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }
}
