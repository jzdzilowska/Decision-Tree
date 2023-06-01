package sol;

import src.ITreeGenerator;
import src.ITreeNode;
import src.Row;

import javax.xml.crypto.Data;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that implements the ITreeGenerator interface used to generate a decision tree
 */
public class TreeGenerator implements ITreeGenerator<Dataset> {
    private ITreeNode root;

    /**
     * Constructor of TreeGenerator
     */
    public TreeGenerator() {}

    /**
     * Generate a tree based on the dataset
     * @param trainingData    the dataset to train on
     * @param targetAttribute the attribute to predict
     */
    @Override
    public void generateTree(Dataset trainingData, String targetAttribute) {
        // create a new dataset whose attribute list does not have the target attribute
        Dataset dataset = new Dataset(trainingData.removeAttribute(targetAttribute),
                trainingData.getDataObjects(), trainingData.getSelectionType());
        // define the root attribute node
        this.root = this.generateTreeHelper(dataset, targetAttribute);
    }


    /**
     * This is a helper for generateTree. It recursively creates either attribute nodes or decision leafs
     * by getting an attribute to split on and partitioning the dataset. Edges connect each node.
     * @param dataset the dataset that the attribute refers to
     * @param targetAttribute the target attribute
     * @return an attribute node that takes in the attribute, the calculated default, and edges which
     * have either other attribute nodes and decision leafs in the parameter
     */
    private ITreeNode generateTreeHelper(Dataset dataset, String targetAttribute) {
        if (dataset.getAttributeList().size() == 0) {
            return new DecisionLeaf(dataset.getDistinctAttributeValues(targetAttribute).get(0));
        }

        // the attribute that the dataset splits on
        String attributeToSplitOn = dataset.getAttributeToSplitOn();

        // get all the possible attribute values in the dataset
        List<String> attributeValues = dataset.getDistinctAttributeValues(attributeToSplitOn);

        // partition the dataset
        List<Dataset> partitionedDataList = dataset.partition(attributeToSplitOn, attributeValues);

        // create edges for each possible attribute value
        List<ValueEdge> edges = new ArrayList<>();
        for (int i = 0; i < attributeValues.size(); i++) {
            ITreeNode child;

            if (partitionedDataList.get(i).ifMakeLeaf(targetAttribute)) { // decision leaf case
                String decision = partitionedDataList.get(i).getDistinctAttributeValues(targetAttribute).get(0);
                child = new DecisionLeaf(decision);

            } else { // attribute node case
                child = this.generateTreeHelper(partitionedDataList.get(i), targetAttribute); // recurse here
            }
            // add either a decision leaf or an attribute node to an edge
            edges.add(new ValueEdge(attributeValues.get(i), child));
        }
        return new AttributeNode(attributeToSplitOn, dataset.computeDefault(targetAttribute), edges);
    }


    /**
     * Looks up the decision for a datum in the decision tree.
     * @param datum the datum to look up a decision for
     * @return the decision of the row
     */
    @Override
    public String getDecision(Row datum) {
        return this.root.getDecision(datum);
    }
}
