package D24;

public record Connection(String input1, String input2, Gate gate) {

    @Override
    public String toString() {
        return input1 + " " + gate.value() + " " + input2;
    }
}
