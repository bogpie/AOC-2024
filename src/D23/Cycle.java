package D23;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class Cycle implements Comparable<Cycle> {
    private Set<Node> nodes;

    public Cycle() {
        nodes = new TreeSet<>();
    }

    public Set<Node> getNodes() {
        return nodes;
    }

    public void setNodes(HashSet<Node> nodes) {
        this.nodes = nodes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cycle cycle = (Cycle) o;

        return Objects.equals(nodes, cycle.nodes);
    }

    @Override
    public int hashCode() {
        return nodes != null ? nodes.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Cycle{" +
                "nodes=" + nodes +
                '}' + "\n";
    }

    @Override
    public int compareTo(Cycle other) {
        return this.toString().compareTo(other.toString());
    }

}
