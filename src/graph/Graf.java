package graph;

import java.util.LinkedList;

import exception.EdgeNotFoundException;
import exception.NodeNotFoundException;

public interface Graf {
	
	void addV(GrafV graphNode);
	
	void deleteV(GrafV graphNode) throws NodeNotFoundException, EdgeNotFoundException;

    void addE(GrafE graphEdge);

    void deleteE(GrafE graphEdge) throws NodeNotFoundException, EdgeNotFoundException;

    GrafV[] getNeighborV(GrafV graphNode) throws NodeNotFoundException;

    GrafE[] getOutE(GrafV graphNode) throws NodeNotFoundException;

    GrafE[] getInE(GrafV graphNode) throws NodeNotFoundException;

    GrafE[] getIncidentalE(GrafV graphNode) throws NodeNotFoundException;

    int getVCount();

    int getECount();

    boolean isVNeighbors(GrafV graphNode1, GrafV graphNode2) throws NodeNotFoundException;

    LinkedList<GrafV> getV();

    LinkedList<GrafE> getE() throws NodeNotFoundException;

}
