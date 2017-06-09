package graph;

public class GrafV {
    private int nodeId;

    public GrafV(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getVId() {
        return nodeId;
    }

    @Override
    public String toString() {
        return nodeId + "";
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof GrafV && ((GrafV) obj).getVId() == nodeId);
    }

    @Override
    public int hashCode() {
        return nodeId;
    }
}
