package D16;

public enum Type {
    WALL('#'),
    EMPTY('.'),
    START('S'),
    END('E');
    private final char value;

    Type(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }
}

