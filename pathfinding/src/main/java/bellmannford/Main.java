package bellmannford;

import graph.AdjNode;
import graph.Graph;
import graph.Node;

import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {
        Graph graph = new Graph();
        graph.genPremade();
        bellmannFord(graph, graph.nodes.get(0));
    }

    public static void bellmannFord(Graph graph, Node start) throws RuntimeException {
        int round = 1;
        int qsize = 1;
        AdjNode[] adjVec = graph.adjVec();
        LinkedList<Node> queue = new LinkedList<>();

        for (Node node : graph.nodes) {
            node.distance = graph.INFINITY;
            node.parent = null;
        }
        start.distance = 0;
        queue.add(start);

        while (!queue.isEmpty() && round <= graph.nodes.size()) {
            Node current = queue.pop();
            qsize--;
            AdjNode neighbour = adjVec[Integer.parseInt(current.name)].next;

            while (neighbour != null) {
                int distanceFromCurrent = current.distance + graph.getEdge(current, neighbour.node,true).weight;
                if(neighbour.node.distance > distanceFromCurrent) {
                    neighbour.node.distance = distanceFromCurrent;
                    neighbour.node.parent = current;
                    if (round == graph.nodes.size()) {
                        throw new RuntimeException("Round count exceeded the maximum!");
                    }
                    if(!queue.contains(neighbour.node)) queue.add(neighbour.node);
                }
                neighbour = neighbour.next;
            }

            if (qsize == 0) {
                round++;
                qsize = queue.size();
            }
        }

        for (Node node : graph.nodes) {
            System.out.println(node + " distance: " + node.distance + " , parent: " + node.parent);
        }
    }
}