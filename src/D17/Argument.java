package D17;

import java.util.Arrays;

public enum Argument {
    VALUE(),
    REGISTER_A(4),
    REGISTER_B(5),
    REGISTER_C(6),
    RESERVED(7);
    private final long value;

    Argument(long value) {
        this.value = value;
    }

    Argument() {
        // Value will be extracted later
        this.value = -1;
    }

    public long getValue() {
        return value;
    }

    public static Argument fromValue(long value) {
        return Arrays.stream(Argument.values()).filter(
                argument -> argument.getValue() == value
        ).findFirst().orElse(Argument.VALUE);
    }
}
