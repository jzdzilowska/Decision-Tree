package sol;

import src.ITreeNode;
import src.Row;

/**
 * A class representing a leaf in the decision tree.
 */
public class DecisionLeaf implements ITreeNode  {
    private String decision;

    /**
     * Constructor of DecisionLeaf
     * @param leafDecision the value of the leaf
     */
    public DecisionLeaf(String leafDecision) {
        this.decision = leafDecision;
    }

    /**
     * Recursively traverses decision tree to return tree's decision for a row.
     * @param forDatum the datum to look up a decision for
     * @return the decision tree's decision
     */
    @Override
    public String getDecision(Row forDatum) {
        return this.decision;
    }

}
