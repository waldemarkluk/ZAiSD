package exception;

import graph.GrafE;

public class EdgeNotFoundException extends Exception {

    public EdgeNotFoundException() {
        super("Edge not found. ");
    }

    public EdgeNotFoundException(String message) {
        super("Edge not found. " + message);
    }

    public EdgeNotFoundException(GrafE graphEdge) {
        super("Edge (" + graphEdge.getFirstV() + "," + graphEdge.getSecondV() + "," + graphEdge.getWeight() + ") not found!");
    }

}
