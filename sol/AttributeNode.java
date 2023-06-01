package sol;

import java.util.List;
import src.ITreeNode;
import src.Row;

/**
 * A class representing an inner node in the decision tree.
 */
public class AttributeNode implements ITreeNode {
    private String attribute;
    private String defaultValue;
    private List<ValueEdge> outgoingEdges;

    /**
     * Constructor of AttributeNode
     * @param attribute the attribute that this node has
     * @param defaultValue the default value
     * @param edges the value edges going from this attribute
     */
    public AttributeNode(String attribute, String defaultValue, List<ValueEdge> edges) {
        this.attribute = attribute;
        this.defaultValue = defaultValue;
        this.outgoingEdges = edges;
    }

    /**
     * Recursively traverses decision tree to return tree's decision for a row.
     * @param forDatum the datum to look up a decision for
     * @return the decision tree's decision
     */
    @Override
    public String getDecision(Row forDatum) {
        String value = forDatum.getAttributeValue(this.attribute);

        if (this.outgoingEdges == null) {
            throw new NullPointerException("Edges are not created");
        }
        for (ValueEdge edge : this.outgoingEdges) {
            if (edge.getValue().equals(value)) {
                return edge.getChild().getDecision(forDatum);
            }
        }
        return this.defaultValue;
    }

}
