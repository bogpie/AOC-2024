package D23;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.*;

public class D23 {
    public void main() {
        Set<Node> nodes = new TreeSet<>();
        populate(nodes);

        Set<Cycle> cycles = new TreeSet<>();
        Set<Node> tNodes = new TreeSet<>();

        System.out.println("Part 1: ");
        doPartOne(nodes, cycles, tNodes);
        System.out.println(tNodes);
        System.out.println(cycles.stream().toList().subList(0, min(cycles.size(), 10)));
        System.out.println(cycles.size());

        System.out.println("Part 2: ");
        Set<Node> maximalClique = new TreeSet<>();
        doPartTwo(nodes, maximalClique);
        System.out.println(maximalClique);
        System.out.println(maximalClique.toString()
                .replace("[", "")
                .replace("]", "")
                .replace(", ", ","));
    }

    private void doPartTwo(Set<Node> nodes, Set<Node> maximalClique) {
        // For each node, start from a clique containing only that node
        for (Node node : nodes) {
            Set<Node> startClique = new TreeSet<>();
            startClique.add(node);

            var currentMaximalClique = findCurrentMaximalClique(nodes, startClique);
            if (currentMaximalClique.size() > maximalClique.size()) {
                maximalClique.clear();
                maximalClique.addAll(currentMaximalClique);
            }
        }
    }

    Set<Node> findCurrentMaximalClique(Set<Node> nodes, Set<Node> clique) {
        for (Node node : nodes) {
            if (clique.contains(node)) {
                continue;
            }

            // If node belongs to the clique, add it to the new clique
            if (clique.stream().allMatch(n -> n.getNext().contains(node))) {
                clique.add(node);
            }
        }

        return clique;
    }


    private void doPartOne(Set<Node> nodes, Set<Cycle> cycles, Set<Node> tNodes) {
        tNodes.addAll(nodes.stream().filter(node -> node.getValue().startsWith("t"))
                .collect(Collectors.toSet()));
        cycles.addAll(findCyclesForSubset(tNodes, 3));
    }

    private static void populate(Set<Node> nodes) {
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
