package D23;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class D23 {
    public void main() {
        Set<Node> nodes = new TreeSet<>();

        try {
            Scanner scanner = new Scanner(new File("src/D23/input.txt"));

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] split = line.split("-");

                Node left =
                        nodes.stream().filter(node -> node.getValue().equals(split[0]))
                                .findFirst().orElse(new Node(split[0]));
                Node right =
                        nodes.stream().filter(node -> node.getValue().equals(split[1]))
                                .findFirst().orElse(new Node(split[1]));

                left.getNext().add(right);
                right.getNext().add(left);

                nodes.add(left);
                nodes.add(right);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        var tNodes = nodes.stream().filter(node -> node.getValue().startsWith("t"))
                .collect(Collectors.toSet());

        System.out.println(tNodes);

        var cycles = findCyclesForSubset(tNodes, 3);
        System.out.println(cycles);
        System.out.println(cycles.size());
    }

    public Set<Cycle> findCyclesForSubset(Set<Node> tNodes, int length) {
        var cycles = new TreeSet<Cycle>();

        for (Node tNode : tNodes) {
            var cycle = new Cycle();
            cycle.getNodes().add(tNode);
            cycles.addAll(findCyclesForNode(tNode, tNode, length, cycle));
        }

        return cycles;
    }

    private Set<Cycle> findCyclesForNode(Node current, Node tNode, int length, Cycle cycle) {
        var cycles = new TreeSet<Cycle>();

        // If the length is 0, we have reached the end of the search for this node
        if (length == 1) {
            if (current.getNext().contains(tNode)) {
                cycle.getNodes().add(tNode);
                cycles.add(cycle);
            }
            return cycles;
        }

        for (Node next : current.getNext()) {
            var newCycle = new Cycle();
            newCycle.getNodes().addAll(cycle.getNodes());
            newCycle.getNodes().add(next);

            cycles.addAll(findCyclesForNode(next, tNode, length - 1, newCycle));
        }

        return cycles;
    }
}
