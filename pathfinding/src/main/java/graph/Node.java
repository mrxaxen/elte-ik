package graph;

public class Node {
    public String name;
    public Color color;
    public Node parent;
    public int componentId = -1;
    public int discoveryTime;
    public int finishTime;
    public int distance;

    public Node(String name) {
        this.name = name;
        color = Color.WHITE;
    }

    @Override
    public int hashCode() {
        return 31 * 17 + name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(o == this) return true;
        if(o.getClass() != Node.class) return false;

        Node n = (Node) o;
        if(name.equals(n.name)) return true;
        return false;
    }

    @Override
    public String toString() {
        return "["+name+"]";
    }
}