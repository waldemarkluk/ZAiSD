package list;

import java.util.LinkedList;

import exception.EdgeNotFoundException;
import exception.NodeNotFoundException;
import graph.Graf;
import graph.GrafE;
import graph.GrafV;

public class ListGraf implements Graf {

    private final int DEFAULT_INIT_SIZE = 100;
    private final int DEFAULT_STEP_SIZE = 10;
    private int actualSize = 0;

    private ListGrafV[] nodes;

    /* Constructors */
    public ListGraf(int initSize) {
        nodes = new ListGrafV[initSize];
    }

    public ListGraf() {
        nodes = new ListGrafV[DEFAULT_INIT_SIZE];
    }

    /* Private methods */

    private void makeNodesArrayBigger(int step) {
        ListGrafV[] newArray = new ListGrafV[actualSize + step];
        for (int i = 0; i < nodes.length; i++)
            newArray[i] = nodes[i];
        nodes = newArray;
    }

    private int findNodeArrayIndex(GrafV graphNode) throws NodeNotFoundException {
        int nodeInArrayIndex = 0;
        while (nodeInArrayIndex < actualSize && !graphNode.equals(nodes[nodeInArrayIndex].getGraphNode()))
            nodeInArrayIndex++;

        if (nodeInArrayIndex == actualSize)
            throw new NodeNotFoundException(graphNode);

        return nodeInArrayIndex;
    }

    /* Interface methods */
    @Override
    public void addV(GrafV graphNode) {
        ListGrafV listGraphNode = new ListGrafV(graphNode);

        try {
            findNodeArrayIndex(graphNode);
        } catch (NodeNotFoundException ex) {   //If not exist - add node
            if (nodes.length - 1 == actualSize)
                makeNodesArrayBigger(DEFAULT_STEP_SIZE);

            nodes[actualSize] = listGraphNode;

            actualSize++;
        }                                   //Else do nothing
    }

    @Override
    public void deleteV(GrafV graphNode) throws NodeNotFoundException {
        //Deleting edges connected to node
        for (int i = 0; i < actualSize; i++) {
            ListGrafE actualEdge = nodes[i].getFirstEdge();
            while (actualEdge != null) {
                if (actualEdge.getSecondV().equals(graphNode)) {
                    try {
                        deleteE(actualEdge);
                    } catch (EdgeNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                actualEdge = actualEdge.getNextEdge();
            }
        }

        int nodeIndex = findNodeArrayIndex(graphNode);
        for (int i = nodeIndex; i < actualSize; i++)
            nodes[i] = nodes[i + 1];
        nodes[actualSize - 1] = null;
        actualSize--;
    }

    @Override
    public void addE(GrafE graphEdge) {
        int primaryNodeIndex = 0;
        ListGrafE newEdge = new ListGrafE(graphEdge.getFirstV(), graphEdge.getSecondV(), graphEdge.getWeight());

        try {
            primaryNodeIndex = findNodeArrayIndex(graphEdge.getFirstV());
        } catch (NodeNotFoundException ex) {   //If not exist first node - add it
            addV(graphEdge.getFirstV());
            primaryNodeIndex = actualSize - 1;
        } finally {
            ListGrafE prevEdge = null;
            ListGrafE actualEdge = nodes[primaryNodeIndex].getFirstEdge();
            while (actualEdge != null) {
                prevEdge = actualEdge;
                actualEdge = actualEdge.getNextEdge();
            }

            if (prevEdge == null) nodes[primaryNodeIndex].setFirstEdge(newEdge);
            else {
                prevEdge.setNextEdge(newEdge);
                newEdge.setPrevEdge(prevEdge);
            }
        }
    }

    @Override
    public void deleteE(GrafE graphEdge) throws NodeNotFoundException, EdgeNotFoundException {
        int primaryNodeIndex = findNodeArrayIndex(graphEdge.getFirstV());
        ListGrafE actualEdge = nodes[primaryNodeIndex].getFirstEdge();
        while (actualEdge != null && !actualEdge.getSecondV().equals(graphEdge.getSecondV()))
            actualEdge = actualEdge.getNextEdge();

        if (actualEdge == null) throw new EdgeNotFoundException(graphEdge);
        else {
            if (actualEdge.getPrevEdge() != null) actualEdge.getPrevEdge().setNextEdge(actualEdge.getNextEdge());
            else nodes[primaryNodeIndex].setFirstEdge(actualEdge.getNextEdge());
            if (actualEdge.getNextEdge() != null) actualEdge.getNextEdge().setPrevEdge(actualEdge.getPrevEdge());
        }
    }

    @Override
    public GrafV[] getNeighborV(GrafV graphNode) throws NodeNotFoundException {
        int nodesCount = 0;
        int primaryNodeIndex = findNodeArrayIndex(graphNode);

        ListGrafE actualEdge = nodes[primaryNodeIndex].getFirstEdge();
        while (actualEdge != null) {
            actualEdge = actualEdge.getNextEdge();
            nodesCount++;
        }

        GrafV[] neighbors = new GrafV[nodesCount];
        int actualPosition = 0;
        actualEdge = nodes[primaryNodeIndex].getFirstEdge();
        while (actualEdge != null) {
            neighbors[actualPosition] = actualEdge.getSecondV();
            actualEdge = actualEdge.getNextEdge();
            actualPosition++;
        }

        return neighbors;
    }

    @Override
    public GrafE[] getOutE(GrafV graphNode) throws NodeNotFoundException {
        int edgesCount = 0;

        int index = findNodeArrayIndex(graphNode);

        ListGrafE actualEdge = nodes[index].getFirstEdge();
        while (actualEdge != null) {
            edgesCount++;
            actualEdge = actualEdge.getNextEdge();
        }

        GrafE[] neighbors = new GrafE[edgesCount];
        int actualPosition = 0;
        actualEdge = nodes[index].getFirstEdge();
        while (actualEdge != null) {
            neighbors[actualPosition] = actualEdge;
            actualPosition++;
            actualEdge = actualEdge.getNextEdge();
        }

        return neighbors;
    }

    @Override
    public GrafE[] getInE(GrafV graphNode) throws NodeNotFoundException {
        int edgesCount = 0;

        for (int i = 0; i < actualSize; i++) {
            ListGrafE actualEdge = nodes[i].getFirstEdge();
            while (actualEdge != null) {
                if (actualEdge.getSecondV().equals(graphNode))
                    edgesCount++;
                actualEdge = actualEdge.getNextEdge();
            }
        }

        GrafE[] neighbors = new GrafE[edgesCount];
        int actualPosition = 0;
        for (int i = 0; i < actualSize; i++) {
            ListGrafE actualEdge = nodes[i].getFirstEdge();
            while (actualEdge != null) {
                if (actualEdge.getSecondV().equals(graphNode)) {
                    neighbors[actualPosition] = actualEdge;
                    actualPosition++;
                }
                actualEdge = actualEdge.getNextEdge();
            }
        }

        return neighbors;
    }

    @Override
    public GrafE[] getIncidentalE(GrafV graphNode) throws NodeNotFoundException {
        GrafE[] inEdges = getInE(graphNode);
        GrafE[] outEdges = getOutE(graphNode);
        GrafE[] incidentalEdges = new GrafE[inEdges.length + outEdges.length];
        for (int i = 0; i < outEdges.length; i++)
            incidentalEdges[i] = outEdges[i];
        for (int i = 0; i < inEdges.length; i++) {
            incidentalEdges[i + outEdges.length] = inEdges[i];
        }

        return incidentalEdges;
    }

    @Override
    public int getVCount() {
        return actualSize;
    }

    @Override
    public int getECount() {
        int count = 0;
        for (int i = 0; i < actualSize; i++) {
            ListGrafE actualEdge = nodes[i].getFirstEdge();
            while (actualEdge != null) {
                count++;
                actualEdge = actualEdge.getNextEdge();
            }
        }
        return count;
    }

    @Override
    public boolean isVNeighbors(GrafV graphNode1, GrafV graphNode2) {
        try {
            int firstNodeIndex = findNodeArrayIndex(graphNode1);
            ListGrafE actualEdge = nodes[firstNodeIndex].getFirstEdge();
            while (actualEdge != null && !actualEdge.getSecondV().equals(graphNode2)) {
                actualEdge = actualEdge.getNextEdge();
            }
            return (actualEdge != null);
        } catch (NodeNotFoundException ex) {
            return false;
        }
    }

    @Override
    public LinkedList<GrafV> getV() {
        LinkedList<GrafV> newNodes = new LinkedList<>();
        for (ListGrafV node : nodes)
            if (node != null && node.getGraphNode() != null)
                newNodes.add(node.getGraphNode());

        return newNodes;
    }

    @Override
    public LinkedList<GrafE> getE() throws NodeNotFoundException {
        LinkedList<GrafE> edges = new LinkedList<>();
        for (GrafV node : getV())
            for (GrafE edge : getOutE(node))
                edges.add(edge);

        return edges;
    }
}
