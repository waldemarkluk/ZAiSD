package list;

import graph.GrafE;
import graph.GrafV;

public class ListGrafE extends GrafE{

    private ListGrafE prevEdge;
    private ListGrafE nextEdge;

    public ListGrafE(GrafV firstNode, GrafV secondNode, int weight) {
        super(firstNode, secondNode, weight);
    }

    public ListGrafE getPrevEdge() {
        return prevEdge;
    }

    public void setPrevEdge(ListGrafE prevEdge) {
        this.prevEdge = prevEdge;
    }

    public ListGrafE getNextEdge() {
        return nextEdge;
    }

    public void setNextEdge(ListGrafE nextEdge) {
        this.nextEdge = nextEdge;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
