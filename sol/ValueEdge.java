package sol;

import src.ITreeNode;

/**
 * A class that represents the edge of an attribute node in the decision tree
 */
public class ValueEdge {
    private ITreeNode child;
    private String value;

    /**
     * Constructor of ValueEdge
     * @param value the attribute value
     * @param child an attribute node or a decision leaf connected to this edge
     */
    public ValueEdge(String value, ITreeNode child) {
        this.child = child;
        this.value = value;
    }

    /**
     * An accessor method to the value of the edge
     * @return the edge value
     */
    public String getValue() {
        return this.value;
    }

    /**
     * An accessor method to the child node of the edge
     * @return the child node
     */
    public ITreeNode getChild() {
        return this.child;
    }
}
