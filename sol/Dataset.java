package sol;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import src.AttributeSelection;
import src.IDataset;
import src.Row;

/**
 * A class representing a training dataset for the decision tree
 */
public class Dataset implements IDataset {

    private List<String> attributeList;
    private List<Row> dataObjects;
    private AttributeSelection selectionType;

    /**
     * Constructor for a Dataset object
     * @param attributeList - a list of attributes
     * @param dataObjects -  a list of rows
     * @param attributeSelection - an enum for which way to select attributes
     */
    public Dataset(List<String> attributeList, List<Row> dataObjects, AttributeSelection attributeSelection) {
        this.attributeList = new ArrayList<String>(attributeList);
        this.dataObjects = new ArrayList<Row>(dataObjects);
        this.selectionType = attributeSelection;
    }


    /**
     * @return an attribute on which a tree splits based on the selection type of the dataset
     */
    public String getAttributeToSplitOn() {
        switch (this.selectionType) {
            case ASCENDING_ALPHABETICAL -> {
                return this.attributeList.stream().sorted().toList().get(0);
            }
            case DESCENDING_ALPHABETICAL -> {
                return this.attributeList.stream().sorted().toList().get(this.attributeList.size() - 1);
            }
            case RANDOM -> {
                return this.attributeList.get(new Random().nextInt(this.attributeList.size()));
            }
        }
        throw new RuntimeException("Non-Exhaustive Switch Case");
    }

    /**
     * Gets list of attributes in the dataset
     * @return a list of strings
     */
    @Override
    public List<String> getAttributeList() {
        return this.attributeList;
    }

    /**
     * Gets list of data objects (row) in the dataset
     * @return a list of Rows
     */
    @Override
    public List<Row> getDataObjects() {
        return this.dataObjects;
    }

    /**
     * Returns the attribute selection type (alphabetical, reverse alphabetical, random) for this Dataset
     * @return the attribute selection type
     */
    public AttributeSelection getSelectionType() {
        return this.selectionType;
    }

    /**
     * finds the size of the dataset (number of rows)
     * @return the number of rows in the dataset
     */
    public int size() {
        return this.dataObjects.size();
    }


    /**
     * Partition the dataset into new datasets for each attribute value
     * @param attribute the attribute to split on
     * @param attributeValues list of the distinct attribute values
     * @return list of dataset for each value
     */
    public List<Dataset> partition(String attribute, List<String> attributeValues) {
        List<Dataset> partitionedDataList = new ArrayList<>();
        for (String attributeValue : attributeValues) {
            List<Row> filteredDataObjects =
                    this.getDataObjects().stream().
                            filter(data -> data.getAttributeValue(attribute).equals(attributeValue)).toList();
            // create a new dataset with the partitioned data objects as well as a new attribute list whose
            // used attribute is removed
            partitionedDataList.
                    add(new Dataset(this.removeAttribute(attribute), filteredDataObjects, this.getSelectionType()));
        }
        return partitionedDataList;
    }


    /**
     * Compute the default value for the current attribute node
     * If more than one value have the max count, we choose the default value arbitrary.
     * @param targetAttribute the attribute for which we calculate the default
     * @return the most frequent value
     */
    public String computeDefault(String targetAttribute) {
        List<String> attributeValues = this.getDistinctAttributeValues(targetAttribute);
        List<String> listOfValues =
                this.getDataObjects().stream().map(e -> e.getAttributeValue(targetAttribute)).toList();

        String mostFrequentValue = null;
        int maxCount = 0;
        for (String attributeValue : attributeValues) {
            if (this.countFrequency(attributeValue, listOfValues) > maxCount) {
                mostFrequentValue = attributeValue;
                maxCount = this.countFrequency(attributeValue, listOfValues);
            }
        }
        return mostFrequentValue;
    }


    /**
     * This is a helper method of computeDefault. It counts the frequency of an attribute value
     * in the list of values in the parameter.
     * @param attributeValue the attribute value to count on
     * @param listOfValues the list of values
     * @return the frequency of the attribute
     */
    private int countFrequency(String attributeValue, List<String> listOfValues) {
        int count = 0;
        for (String value : listOfValues) {
            if (value.equals(attributeValue)) {
                count++;
            }
        }
        return count;
    }


    /**
     * @param targetAttribute the target attribute
     * @return true if the tree generator should make a leaf and false if not
     */
    public boolean ifMakeLeaf(String targetAttribute) {
        return this.getDistinctAttributeValues(targetAttribute).size() == 1;
    }


    /**
     * @param attribute an attribute
     * @return list of the distinct values in the dataset corresponding to the attribute
     */
    public List<String> getDistinctAttributeValues(String attribute) {
        return this.dataObjects.stream().map(e -> e.getAttributeValue(attribute)).distinct().toList();
    }


    /**
     * remove an attribute from the dataset's attribute list
     * @param attributeRemoved the attribute to be removed
     * @return a new attribute list
     */
    public List<String> removeAttribute(String attributeRemoved) {
        List<String> newAttributeList = new ArrayList<>();
        for (String attribute : this.attributeList) {
            if (!attribute.equals(attributeRemoved)) {
                newAttributeList.add(attribute);
            }
        }
        return newAttributeList;
    }
}
