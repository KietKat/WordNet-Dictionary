package ngordnet.main;

import edu.princeton.cs.algs4.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Graph {
    private Bag<Edge>[] adjList; // adjacency list
    int nodeCount;
    //build in adjacency list
    //variables : what is our graph representation?
    //adjList, adjMatrix
    public Graph(int _count)
    {
        this.nodeCount = _count;
        adjList = (Bag<Edge>[]) new Bag[this.nodeCount];
        for (int i = 0; i < _count; i++) {
            adjList[i]= new Bag<Edge>();
        }
    }

    public void addEdge (Node from, Node to)
    {
        Edge temp = new Edge(from, to);
        int index = from.from;
        adjList[index].add(temp);
    }


/*    public void addEdge (Node nodeFrom, Node nodeTo)
    {
        if (!adjList.containsKey(nodeFrom)) {
            adjList.put(nodeFrom, new ArrayList<>());
        }
        adjList.get(nodeFrom).add(nodeTo);
    }*/

    // implements Edge class
    class Edge {
        //an edge will have its number, content and where it points to
        Node from;
        Node to;

        // Constructor
        Edge(Node _node, Node _to)
        {
            this.from = _node;
            this.to = _to;
        }

        //String[] getContent() { return node.content; }

    }

}
