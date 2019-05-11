import graph.Edge;
import graph.Graph;
import graph.Node;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Graph graph = new Graph();
        graph.genPremade();
        System.out.println(graph);
        kruskal(graph);
        System.out.println(graph);
    }

    public static void kruskal(Graph graph){
        graph.sortByWeight();
        ArrayList<Edge> mstEdges = new ArrayList<>();
        for (Node node : graph.nodes) {
            node.parent = null;
            node.size = 1;
        }
        for (Edge e : graph.edges) {
            Node v1 = find(e.v1);
            Node v2 = find(e.v2);
            if (!v1.equals(v2)) {
                union(v1, v2);
                mstEdges.add(new Edge(e.v1, e.v2, e.weight));
            }
        }
        graph.edges = mstEdges;
    }

    private static Node find(Node node) {
        if(node.parent != null) {
            return find(node.parent);
        }
        return node;
    }

    private static void union(Node v1, Node v2) {
        if (v1.size < v2.size) {
            v1.parent = v2;
            v2.size+= v1.size;
        } else {
            v2.parent = v1;
            v2.size += v1.size;
        }
    }
}
