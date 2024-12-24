package D24;

public record Gate(String value) {

    public boolean getOutput(boolean input1, boolean input2) {
        return switch (value) {
            case "AND" -> input1 && input2;
            case "OR" -> input1 || input2;
            case "XOR" -> input1 ^ input2;
            default -> throw new IllegalArgumentException("Unknown gate value: " + value);
        };
    }
}
