package D23;

import java.util.ArrayList;

public class Node implements Comparable<Node> {
    private final String value;
    private final ArrayList<Node> next;

    public Node(String value) {
        this.value = value;
        this.next = new ArrayList<>();
    }

    public String getValue() {
        return value;
    }

    public ArrayList<Node> getNext() {
        return next;
    }

    @Override
    public String toString() {
        return value;
    }


    public String toStringWithNext() {
        var nextValues = next.stream().map(node -> node.value).toList();
        return value + " -> " + nextValues;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Node other = (Node) obj;
        if (value == null) {
            return other.value == null;
        } else return value.equals(other.value);
    }

    @Override
    public int compareTo(Node o) {
        return value.compareTo(o.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
