package list;

import graph.GrafV;

public class ListGrafV {

    private ListGrafE firstEdge;
    private GrafV graphNode;

    public ListGrafV(GrafV graphNode) {
        this.graphNode = graphNode;
    }

    public GrafV getGraphNode() {
        return graphNode;
    }

    public ListGrafV(ListGrafE firstEdge, GrafV graphNode) {
        this.firstEdge = firstEdge;
        this.graphNode = graphNode;
    }

    public ListGrafE getFirstEdge() {
        return firstEdge;
    }

    public void setFirstEdge(ListGrafE firstEdge) {
        this.firstEdge = firstEdge;
    }
}
