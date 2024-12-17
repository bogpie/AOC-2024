package D16;

import java.awt.*;
import java.util.ArrayList;

public class ComplexNode extends Node {
    private ArrayList<Point> path;

    public ComplexNode(Node node, ArrayList<Point> path) {
        super(node.getPoint(), node.getDirection(), node.getWeight());
        this.path = path;
    }

    public ComplexNode(Point point, Direction direction, int weight, ArrayList<Point> path) {
        super(point, direction, weight);
        this.path = path;
    }

    public ComplexNode() {
        super();
        this.path = new ArrayList<>();
    }

    public ArrayList<Point> getPath() {
        return path;
    }

    public void setPath(ArrayList<Point> path) {
        this.path = path;
    }
}
