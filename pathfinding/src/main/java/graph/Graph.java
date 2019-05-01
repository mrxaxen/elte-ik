package graph;

import java.util.ArrayList;
import java.util.Iterator;

public class Graph {

    public static final int INFINITY = 2147483647;
    public ArrayList<Node> nodes;

    public ArrayList<Edge> edges;
    public Graph() {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
    }
    //Example from lesson

    public void genPremade() {
        for (int i = 0; i < 5; i++) {
            nodes.add(new Node(i+""));
        }
        //a
        edges.add(new Edge(nodes.get(0), nodes.get(1), 6,true));
        edges.add(new Edge(nodes.get(0), nodes.get(3), 7,true));
        //b
        edges.add(new Edge(nodes.get(1), nodes.get(3),8, true));
        edges.add(new Edge(nodes.get(1), nodes.get(4),-4, true));
        edges.add(new Edge(nodes.get(1), nodes.get(2),5, true));
        //c
        edges.add(new Edge(nodes.get(2), nodes.get(1),-2, true));
        //d
        edges.add(new Edge(nodes.get(3), nodes.get(2),-3,true));
        edges.add(new Edge(nodes.get(3), nodes.get(4),9, true));
        //e
        edges.add(new Edge(nodes.get(4), nodes.get(2),7, true));
    }

    /**
     * Generates and adjacency vector from the adjMatrix of the graph.
     * @return
     */
    public AdjNode[] adjVec() {
        int[][] adjMx = createAdjMx();
        AdjNode[] adjVec = new AdjNode[adjMx.length];
        for(int i = 0; i < adjMx.length;++i) {
            for(int j = adjMx[i].length-1; j >= 0;--j) {
                int value;
                if((value = adjMx[i][j]) > 0) {
                    for(int k = 0; k < value; ++k) {
                        AdjNode node = new AdjNode(nodes.get(j),adjVec[i]);
                        adjVec[i] = node;
                    }
                }
            }
            adjVec[i] = new AdjNode(nodes.get(i),adjVec[i]);
        }
        return adjVec;
    }

    public int[][] createAdjMx() {
        Iterator it = edges.iterator();
        int[][] adjMx = new int[nodes.size()][nodes.size()];
        while(it.hasNext()) {
            Edge e = (Edge) it.next();
            adjMx[Integer.parseInt(e.v1.name)][Integer.parseInt(e.v2.name)]++;
            if(!e.directed)
                adjMx[Integer.parseInt(e.v2.name)][Integer.parseInt(e.v1.name)]++;
        }
        return adjMx;
    }

    /**
     * Builds a random graph with parallel and loop edges which are directed and weighted. Node/Edgecount: 1-51
     */
    public void buildRandomGraph() {
        buildRandomGraph(true,true,true,true,50,50,50);
    }

    /**
     * Builds a random simple graph which can be directed. Edge/Nodecount: 1-51
     * @param directed If true the generated graph is directed
     */
    public void buildRandomGraph(boolean directed) {
        buildRandomGraph(false,false,directed,false);
    }

    /**
     * Builds a random graph with the given paramenters. Edge/Nodecount: 1-51.
     * @param parallelEdges If true the graph is able to have parallel edges.
     * @param loops If true the graph is able to have loops.
     * @param directed If true the graph becomes directed.
     * @param weighted If true the edges become weighted. Weight values 1-51 (The weight is 1 by default even if it is disabled)
     */
    public void buildRandomGraph(boolean parallelEdges, boolean loops, boolean directed, boolean weighted) {
        buildRandomGraph(parallelEdges, loops, directed, weighted, 50, 50, 50);
    }

    /**
     * Builds a random graph with given parameters.
     * @param parallelEdges If true the graph is able to have parallel edges.
     * @param loops If true the graph is able to have loops.
     * @param directed If true the graph becomes directed.
     * @param weighted If true the edges become weighted.
     * @param maxNodeCount The maximum amount of nodes the graph can have.
     * @param maxEdgeCount The maximum amount of edges the graph can have.
     * @param maxWeight The maximum weight an edge can have.
     */
    public void buildRandomGraph(boolean parallelEdges, boolean loops, boolean directed, boolean weighted, int maxNodeCount, int maxEdgeCount, int maxWeight) {
        int nodeCount = (int) (Math.random() * maxNodeCount + 1);
        int edgeCount = (int)(Math.random() * maxEdgeCount + 1);
        System.out.println("NodeCount: " + nodeCount + " MaxEdgeCount: " + edgeCount);
        for(int n = 0;n < nodeCount;++n) {
            nodes.add(new Node(n + ""));
        }
            if(loops || nodes.size() > 1) {
                for(int e = 0; e < edgeCount;++e) {
                    int v1Ind = (int) (Math.random() * nodes.size());
                    int v2Ind = (int) (Math.random() * nodes.size());
                    Edge edge;
                    if(loops) {
                        if(directed) {
                            if(weighted) {
                                int weight = (int)(Math.random() * maxWeight + 1);
                                edge = new Edge(nodes.get(v1Ind), nodes.get(v2Ind), weight, true);
                            }else {
                                edge = new Edge(nodes.get(v1Ind), nodes.get(v2Ind),true);
                            }
                        } else {
                            edge = new Edge(nodes.get(v1Ind), nodes.get(v2Ind));
                        }
                        if(!parallelEdges) {
                            if(!edges.contains(edge)) edges.add(edge);
                        } else {
                            edges.add(edge);
                        }
                    } else {
                        while(v1Ind == v2Ind){
                            v1Ind = (int) (Math.random() * nodes.size());
                            if(v1Ind % 2 == 0) // step every 2nd it.
                                v2Ind = (int) (Math.random() * nodes.size());
                        }
                        if(directed) {
                            if(weighted) {
                                int weight = (int)(Math.random() * maxWeight + 1);
                                edge = new Edge(nodes.get(v1Ind), nodes.get(v2Ind), weight, true);
                            }else {
                                edge = new Edge(nodes.get(v1Ind), nodes.get(v2Ind), true);
                            }
                        } else {
                            if(weighted) {
                                int weight = (int)(Math.random() * maxWeight + 1);
                                edge = new Edge(nodes.get(v1Ind), nodes.get(v2Ind), weight);
                            } else {
                                edge = new Edge(nodes.get(v1Ind), nodes.get(v2Ind));
                            }
                        }
                        if(!parallelEdges) {
                            if(!edges.contains(edge)) edges.add(edge);
                        } else {
                            edges.add(edge);
                        }
                    }
                }
            }

        System.out.println("Actual Node Count: " + nodes.size() + " Actual Edge Count: " + edges.size());
    }

    public void transposeGraph() {
        edges.forEach(edge -> {
            if(edge.directed) edge.transpose();
        });
    }

    public Edge getEdge(Node v1, Node v2, boolean isDirected) {
        int index = edges.indexOf(new Edge(v1,v2, isDirected));
        Edge edge = edges.get(index);
        return edge;
    }

    public boolean addEdge(Edge e) {
        boolean v1;
        boolean v2;
        if((v1 = nodes.contains(e.v1)) & (v2 = nodes.contains(e.v2))) {
            return edges.add(e);
        } else {
            if(v1 && v2) {
                v1 = nodes.add(e.v1);
                v2 = nodes.add(e.v2);
            } else {
                if(v1) v1 = nodes.add(e.v1);
                if(v2) v2 = nodes.add(e.v2);
            }
        }
        return v1&&v2;
    }

    public boolean addNode(Node n) {
        if(!nodes.contains(n)) return nodes.add(n);
        return true;
    }

    public Node remNode(Node n) {
        Iterator it = edges.iterator();
        while(it.hasNext()) {
            Edge e = (Edge) it.next();
            if(e.v1 == n || e.v2 == n) remEdge(e);
        }
        return nodes.remove(nodes.indexOf(n));
    }

    public Edge remEdge(Edge e) {
        return edges.remove(edges.indexOf(e));
    }

    @Override
    public String toString() {
        Iterator it = edges.iterator();
        StringBuilder sb = new StringBuilder();
        while(it.hasNext()) {
            sb.append(it.next().toString() + "\n");
        }
        return sb.toString();
    }
}