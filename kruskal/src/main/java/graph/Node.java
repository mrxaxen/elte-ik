package graph;

public class Node implements Cloneable{
    public String name;
    public Color color;
    public Node parent;
    public int componentId = -1;
    public int discoveryTime;
    public int finishTime;
    public int size;

    public Node(String name) {
        this.name = name;
        color = Color.WHITE;
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
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