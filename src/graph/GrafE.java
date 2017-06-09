package graph;

public class GrafE {
    private GrafV firstNode;
    private GrafV secondNode;
    private int weight;

    public GrafE(GrafV firstNode, GrafV secondNode, int weight) {
        this.firstNode = firstNode;
        this.secondNode = secondNode;
        this.weight = weight;
    }
    
    public void setWeight(int w) {
    	this.weight = w;
    }

    public GrafV getFirstV() {
        return firstNode;
    }

    public GrafV getSecondV() {
        return secondNode;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "(" + firstNode + "," + secondNode + "," + weight + ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;

        GrafE edge = (GrafE) o;

        if (weight != edge.weight) return false;
        if (firstNode != null ? !firstNode.equals(edge.firstNode) : edge.firstNode != null) return false;
        return !(secondNode != null ? !secondNode.equals(edge.secondNode) : edge.secondNode != null);

    }

    @Override
    public int hashCode() {
    	int result = 1000000*this.getFirstV().getVId() + 1000*this.getSecondV().getVId() + this.getWeight();
//        result = 31 * result + weight;
        return result;
    }
}
