package graph;

public class AdjNode {
    public Node node;
    public AdjNode next = null;

    public AdjNode(Node node) {
        this.node = node;
    }

    public AdjNode(Node node, AdjNode next) {
        this.node = node;
        this.next = next;
    }

    @Override
    public String toString() {
        if(next != null)
            return node.toString() + ": " + next.toString();
        return node.toString();
    }
}