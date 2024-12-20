package D16;

public enum RaceablePointType {
    WALL('#'),
    EMPTY('.'),
    START('S'),
    END('E');
    private final char value;

    RaceablePointType(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }
}

