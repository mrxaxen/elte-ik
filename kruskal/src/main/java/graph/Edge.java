package graph;

public class Edge {
    public boolean directed;
    public boolean weighted;
    public Node v1;
    public Node v2;
    public int weight;

    public Edge(Node v1, Node v2) {
        this.weight = 1;
        this.v1 = v1;
        this.v2 = v2;
    }

    public Edge(Node v1, Node v2, boolean directed) {
        this.weight = 1;
        this.v1 = v1;
        this.v2 = v2;
        this.directed = directed;
    }

    public Edge(Node v1, Node v2, int weight) {
        this.weight = weight;
        this.v1 = v1;
        this.v2 = v2;
        this.weighted = true;
    }

    public Edge(Node v1, Node v2, int weight, boolean directed) {
        this.weight = weight;
        this.v1 = v1;
        this.v2 = v2;
        this.directed = directed;
        this.weighted = true;
    }

    public void transpose() {
        if (directed) {
            Node temp = v1;
            v1 = v2;
            v2 = temp;
        }
    }

    @Override
    public int hashCode() {
        return 31 * 17 + v1.hashCode() + v2.hashCode() + weight;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(o == this) return true;
        if(o.getClass() != Edge.class) return false;

        Edge e = (Edge) o;
        if(directed) {
            return (e.v1.equals(this.v1) && e.v2.equals(this.v2));
        } else {
            return (e.v1.equals(this.v1) && e.v2.equals(this.v2) || e.v1.equals(this.v2) && e.v2.equals(this.v1));
        }
    }

    @Override
    public String toString() {
        if(this.directed) {
            if (this.weighted) {
                return v1 + "--{" + weight + "}>>" + v2;
            } else {
                return v1 + "-->>" + v2;
            }
        } else {
            if (this.weighted) {
                return v1 + "--{" + weight + "}--" + v2;
            } else {
                return v1 + "----" + v2;
            }
        }
    }
}