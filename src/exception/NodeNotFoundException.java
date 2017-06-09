package exception;

import graph.GrafV;

public class NodeNotFoundException extends Exception {

    public NodeNotFoundException() {
        super("Node not found. ");
    }

    public NodeNotFoundException(String message) {
        super("Node not found. " + message);
    }

    public NodeNotFoundException(GrafV graphNode) {
        super("Node " + graphNode.getVId() + " not found!");
    }
    
}
