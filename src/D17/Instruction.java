package D17;

import java.util.Arrays;

public enum Instruction {
    ADV(0),
    BXL(1),
    BST(2),
    JNZ(3),
    BXC(4),
    OUT(5),
    BDV(6),
    CDV(7);

    private final long value;

    Instruction(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public static Instruction fromValue(long value) {
        return Arrays.stream(Instruction.values()).filter(
                instruction -> instruction.getValue() == value
        ).findFirst().orElseThrow();
    }
}
