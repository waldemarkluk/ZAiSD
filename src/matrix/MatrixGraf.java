package matrix;

import java.util.LinkedList;

import exception.EdgeNotFoundException;
import exception.NodeNotFoundException;
import graph.Graf;
import graph.GrafE;
import graph.GrafV;

public class MatrixGraf implements Graf {

    private final int DEFAULT_INIT_SIZE = 100;
    private final int DEFAULT_STEP_SIZE = 10;
    private int actualSize = 0;

    private Object[][] matrix;

    /* Constructors */
    public MatrixGraf(int initSize) {
        matrix = new Object[initSize][initSize];
    }

    public MatrixGraf() {
        matrix = new Object[DEFAULT_INIT_SIZE][DEFAULT_INIT_SIZE];
    }

    /* Private methods */

    private void makeMatrixBigger(int step) {
        Object[][] newMatrix = new Object[matrix.length + step][matrix.length + step];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[i].length; j++)
                newMatrix[i][j] = matrix[i][j];
        matrix = newMatrix;
    }

    private int findNodeMatrixIndex(GrafV graphNode) throws NodeNotFoundException {
        int nodeInMatrixIndex = 0;
        while (nodeInMatrixIndex < matrix.length && !graphNode.equals(matrix[nodeInMatrixIndex][0]))
            nodeInMatrixIndex++;

        if (nodeInMatrixIndex == matrix.length)
            throw new NodeNotFoundException(graphNode);

        return nodeInMatrixIndex;
    }

    private int getNeighborsCount(GrafV graphNode) throws NodeNotFoundException {
        int nodeMatrixIndex = findNodeMatrixIndex(graphNode);
        int neighborsCount = 0;
        for (int i = 1; i <= actualSize; i++)
            if (matrix[nodeMatrixIndex][i] != null)
                neighborsCount++;
        return neighborsCount;
    }

    /* Implementation of interface Graph */
    @Override
    public void addV(GrafV graphNode) {
        try {
            findNodeMatrixIndex(graphNode);
        } catch (NodeNotFoundException ex) {
            if (matrix.length - 1 == actualSize)
                makeMatrixBigger(DEFAULT_STEP_SIZE);

            matrix[actualSize + 1][0] = graphNode;
            matrix[0][actualSize + 1] = graphNode;

            actualSize++;
        }
    }

    @Override
    public void deleteV(GrafV graphNode) throws NodeNotFoundException {
        int nodeInMatrixIndex = findNodeMatrixIndex(graphNode);

        for (int i = nodeInMatrixIndex; i <= actualSize; i++)
            for (int j = 0; j <= actualSize; j++)
                if (i != actualSize)
                    matrix[i][j] = matrix[i + 1][j];
                else
                    matrix[i][j] = null;

        for (int i = 0; i <= actualSize; i++)
            for (int j = nodeInMatrixIndex; j <= actualSize; j++)
                if (j != actualSize)
                    matrix[i][j] = matrix[i][j + 1];
                else
                    matrix[i][j] = null;

        actualSize--;
    }

    @Override
    public void addE(GrafE graphEdge) {
        int firstNodeMatrixIndex = 0;
        int secondNodeMatrixIndex = 0;

        try {
            firstNodeMatrixIndex = findNodeMatrixIndex(graphEdge.getFirstV());
            secondNodeMatrixIndex = findNodeMatrixIndex(graphEdge.getSecondV());
        } catch (NodeNotFoundException ex) {
            addV(graphEdge.getFirstV());
            firstNodeMatrixIndex = actualSize;
            addV(graphEdge.getSecondV());
            secondNodeMatrixIndex = actualSize;
        } finally {
            if (matrix[firstNodeMatrixIndex][secondNodeMatrixIndex] == null || ((GrafE) matrix[firstNodeMatrixIndex][secondNodeMatrixIndex]).getWeight() > graphEdge.getWeight())
                matrix[firstNodeMatrixIndex][secondNodeMatrixIndex] = graphEdge; // If lower weight then replace current edge.
        }
    }

    @Override
    public void deleteE(GrafE graphEdge) throws NodeNotFoundException, EdgeNotFoundException {
        int firstNodeMatrixIndex = findNodeMatrixIndex(graphEdge.getFirstV());
        int secondNodeMatrixIndex = findNodeMatrixIndex(graphEdge.getSecondV());
        if (matrix[firstNodeMatrixIndex][secondNodeMatrixIndex] == null) throw new EdgeNotFoundException(graphEdge);
        else matrix[firstNodeMatrixIndex][secondNodeMatrixIndex] = null;
    }

    @Override
    public GrafV[] getNeighborV(GrafV graphNode) throws NodeNotFoundException {
        int nodeMatrixIndex = findNodeMatrixIndex(graphNode);
        int neighborsCount = getNeighborsCount(graphNode);
        GrafV[] neighbors = new GrafV[neighborsCount];

        int actualNeighborIndex = 0;

        for (int i = 1; i <= actualSize; i++)
            if (matrix[nodeMatrixIndex][i] != null) {
                neighbors[actualNeighborIndex] = ((GrafE) matrix[nodeMatrixIndex][i]).getSecondV();
                actualNeighborIndex++;
            }

        return neighbors;
    }

    @Override
    public GrafE[] getOutE(GrafV graphNode) throws NodeNotFoundException {
        int incidentalCount = 0;
        int index = findNodeMatrixIndex(graphNode);
        for (int i = 1; i < actualSize + 1; i++)
            if (matrix[index][i] != null && (((GrafE) matrix[index][i]).getFirstV().equals(graphNode)))
                incidentalCount++;


        GrafE[] incidentalList = new GrafE[incidentalCount];
        int actualElementIndex = 0;
        for (int i = 1; i < actualSize + 1; i++) {
            if (matrix[index][i] != null && (((GrafE) matrix[index][i]).getFirstV().equals(graphNode))) {
                incidentalList[actualElementIndex] = (GrafE) matrix[index][i];
                actualElementIndex++;
            }
        }

        return incidentalList;
    }

    @Override
    public GrafE[] getInE(GrafV graphNode) throws NodeNotFoundException {
        int incidentalCount = 0;
        int index = findNodeMatrixIndex(graphNode);
        for (int i = 1; i < actualSize + 1; i++)
            if (matrix[i][index] != null && (((GrafE) matrix[i][index]).getSecondV().equals(graphNode)))
                incidentalCount++;

        GrafE[] incidentalList = new GrafE[incidentalCount];
        int actualElementIndex = 0;
        for (int i = 1; i < actualSize + 1; i++) {
            if (matrix[i][index] != null && (((GrafE) matrix[i][index]).getSecondV().equals(graphNode))) {
                incidentalList[actualElementIndex] = (GrafE) matrix[i][index];
                actualElementIndex++;
            }
        }

        return incidentalList;
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
        int size = 0;
        for (int i = 1; i <= actualSize; i++)
            for (int j = 1; j <= actualSize; j++)
                if (matrix[i][j] != null)
                    size++;
        return size;
    }

    @Override
    public boolean isVNeighbors(GrafV graphNode1, GrafV graphNode2) {
        try {
            int firstNodeMatrixIndex = findNodeMatrixIndex(graphNode1);
            int secondNodeMatrixIndex = findNodeMatrixIndex(graphNode2);

            return matrix[firstNodeMatrixIndex][secondNodeMatrixIndex] != null || matrix[secondNodeMatrixIndex][firstNodeMatrixIndex] != null;
        } catch (NodeNotFoundException e) {
            return false;
        }

    }

    @Override
    public LinkedList<GrafV> getV() {
        LinkedList<GrafV> newNodes = new LinkedList<>();
        for (int i = 1; i < actualSize + 1; i++)
            newNodes.add(((GrafV) matrix[0][i]));
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
