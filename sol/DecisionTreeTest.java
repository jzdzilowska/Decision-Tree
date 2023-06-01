package sol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import static org.junit.Assert.assertFalse;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import src.AttributeSelection;
import src.DecisionTreeCSVParser;
import src.ITreeNode;
import src.Row;

import javax.management.Attribute;
import java.util.ArrayList;
import java.util.List;

/**
 * A class containing the tests for methods in the TreeGenerator and Dataset classes
 */
public class DecisionTreeTest {

    TreeGenerator testGenerator;
    Dataset training;
    List<Row> dataObjects;
    List<String> attributeList;

    /**
     * Constructs the decision tree for testing based on the input file and the target attribute.
     */
    @Before
    public void buildTreeForTest() {
        String trainingPath = "C:\\CS200\\src\\pr01-decision-tree-zdzilowska-kana820\\data\\citiesdataset.csv";
        String targetAttribute = "result";

        this.dataObjects = DecisionTreeCSVParser.parse(trainingPath);
        this.attributeList = new ArrayList<>(this.dataObjects.get(0).getAttributes());
        this.training = new Dataset(this.attributeList, this.dataObjects, AttributeSelection.ASCENDING_ALPHABETICAL);
        // builds a TreeGenerator object and generates a tree for "foodType"
        this.testGenerator = new TreeGenerator();
        this.testGenerator.generateTree(this.training, targetAttribute);
    }

    /**
     * This test shows syntax for a basic assertEquals assertion -- can be deleted
     */
    @Test
    public void testAssertEqual() {
        assertEquals(1 + 1, 2);
    }

    /**
     * This test shows syntax for a basic assertTrue assertion -- can be deleted
     */
    @Test
    public void testAssertTrue() {
        assertTrue(true);
    }

    /**
     * This test shows syntax for a basic assertFalse assertion -- can be deleted
     */
    @Test
    public void testAssertFalse() {
        assertFalse(false);
    }

    /**
     * test for getAttributeList in IDataset
     */
    @Test
    public void testGetAttributeList() {
        Assert.assertEquals(this.attributeList, this.training.getAttributeList());
    }

    /**
     * test for getDataObjects in IDataset
     */
    @Test
    public void testGetDataObjects() {
        Assert.assertEquals(this.dataObjects, this.training.getDataObjects());
    }

    /**
     * test for getSelectionType in IDataset
     */
    @Test
    public void testGetSelectionType(){
        Assert.assertEquals(AttributeSelection.ASCENDING_ALPHABETICAL, this.training.getSelectionType());
    }

    /**
     * test for getAttributeToSplitOn in Dataset
     */
    @Test
    public void testGetAttributeToSplitOn() {
        List<String> attributeList = new ArrayList<>();
        attributeList.add("area");
        attributeList.add("lifestyle");
        attributeList.add("weather");

        Dataset set1 = new Dataset(attributeList, this.dataObjects, AttributeSelection.ASCENDING_ALPHABETICAL);
        Assert.assertEquals("area", set1.getAttributeToSplitOn());
        Dataset set2 = new Dataset(attributeList, this.dataObjects, AttributeSelection.DESCENDING_ALPHABETICAL);
        Assert.assertEquals("weather", set2.getAttributeToSplitOn());
    }

    /**
     * test for size in Dataset
     */
    @Test
    public void testDatasetSize() {
        Assert.assertEquals(5, this.training.size());
    }


    /**
     * Test for getDistinctAttributeValues
     */
    @Test
    public void testGetDistinctAttributeValues() {
        List<String> lifestyleList = new ArrayList<>();
        lifestyleList.add("fast-paced");
        lifestyleList.add("slow-paced");
        Assert.assertEquals(lifestyleList, this.training.getDistinctAttributeValues("lifestyle"));

        List<String> weatherList = new ArrayList<>();
        weatherList.add("warm");
        weatherList.add("cold");
        Assert.assertEquals(weatherList, this.training.getDistinctAttributeValues("weather"));

        List<String> areaList = new ArrayList<>();
        areaList.add("small");
        areaList.add("big");
        Assert.assertEquals(areaList, this.training.getDistinctAttributeValues("area"));
    }

    /**
     * Test for removeAttribute in Dataset
     */
    @Test
    public void testRemoveAttribute() {
        String trainingPath = "C:\\CS200\\src\\pr01-decision-tree-zdzilowska-kana820\\data\\citiesdataset.csv";
        List<Row> dataObjects2 = DecisionTreeCSVParser.parse(trainingPath);
        List<String> attributeList = new ArrayList<>(dataObjects2.get(0).getAttributes());
        Dataset data2 = new Dataset(attributeList, dataObjects2, AttributeSelection.ASCENDING_ALPHABETICAL);

        List<String> newAttributeList = new ArrayList<>();
        newAttributeList.add("weather");
        newAttributeList.add("result");
        newAttributeList.add("lifestyle");

        Assert.assertEquals(newAttributeList.stream().sorted().toList(),
                data2.removeAttribute("area").stream().sorted().toList());

        // checks for repeated deletion of the same object
        newAttributeList.add("area");
        newAttributeList.remove("lifestyle");
        Assert.assertEquals(newAttributeList.stream().sorted().toList(),
                data2.removeAttribute("lifestyle").stream().sorted().toList());
        Assert.assertEquals(newAttributeList.stream().sorted().toList(),
                data2.removeAttribute("lifestyle").stream().sorted().toList());

        // checks for deletion of a non-existent object
        Assert.assertEquals(data2.getAttributeList(), data2.removeAttribute("check"));
    }

    /**
     * Test for partition
     */
    @Test
    public void testPartition() {
        List<String> valueList = new ArrayList<>();
        valueList.add("small");
        valueList.add("big");

        Dataset smallDataset = this.training.partition("area", valueList).get(0);
        Dataset bigDataset = this.training.partition("area", valueList).get(1);
        Assert.assertEquals("warm", smallDataset.getDataObjects().get(0).getAttributeValue("weather"));
        Assert.assertEquals("cold", smallDataset.getDataObjects().get(1).getAttributeValue("weather"));
        Assert.assertEquals("fast-paced", bigDataset.getDataObjects().get(0).getAttributeValue("lifestyle"));
        Assert.assertEquals("fast-paced", bigDataset.getDataObjects().get(1).getAttributeValue("lifestyle"));
    }

    /**
     * Test for computeDefault in Dataset
     */
    @Test
    public void testComputeDefault() {
        Assert.assertEquals("east coast", this.training.computeDefault("result"));
    }

    /**
     * Test for ifMakeLeaf in Dataset
     */
    @Test
    public void testIfMakeLeaf() {
        List<String> attributeList = new ArrayList<>();
        attributeList.add("lifestyle");
        attributeList.add("weather");

        Row city1 = new Row("test row (city 1 - east)");
        city1.setAttributeValue("lifestyle", "slow-paced");
        city1.setAttributeValue("weather", "warm");
        Row city2 = new Row("test row (city 2 - west)");
        city2.setAttributeValue("lifestyle", "fast-paced");
        city2.setAttributeValue("weather", "warm");
        List<Row> dataObjects = new ArrayList<>();
        dataObjects.add(city1);
        dataObjects.add(city2);

        Dataset dataset = new Dataset(attributeList, dataObjects, AttributeSelection.ASCENDING_ALPHABETICAL);
        Assert.assertTrue(dataset.ifMakeLeaf("weather"));
        Assert.assertFalse(dataset.ifMakeLeaf("lifestyle"));
    }


    /**
     * Tests the expected classification of cities; checks both dataset-included objects, and ones
     * with attribute values not included in the dataset (calculated based on defaults
     * for each node)
     */
    @Test
    public void testGetDecision() {
        Row city1 = new Row("test row (city 1 - east)");
        city1.setAttributeValue("lifestyle", "fast-paced");
        city1.setAttributeValue("weather", "warm");
        city1.setAttributeValue("area", "small");

        Row city2 = new Row("test row (city 2 - west)");
        city2.setAttributeValue("lifestyle", "fast-paced");
        city2.setAttributeValue("weather", "warm");
        city2.setAttributeValue("area", "big");

        Row city3 = new Row("test row (city 3 - east)");
        city3.setAttributeValue("lifestyle", "fast-paced");
        city3.setAttributeValue("weather", "cold");
        city3.setAttributeValue("area", "big");

        Row city4 = new Row("test row (city 4 - east)");
        city4.setAttributeValue("lifestyle", "fast-paced");
        city4.setAttributeValue("weather", "cold");
        city4.setAttributeValue("area", "small");

        Row city5 = new Row("test row (city 5 - west)");
        city5.setAttributeValue("lifestyle", "slow-paced");
        city5.setAttributeValue("weather", "warm");
        city5.setAttributeValue("area", "big");

        Row city6 = new Row("test row (city 6 - east)");
        city6.setAttributeValue("lifestyle", "slow-paced");
        city6.setAttributeValue("weather", "mild");
        city6.setAttributeValue("area", "medium");

        Row city7 = new Row("test row (city 7 - west)");
        city7.setAttributeValue("lifestyle", "random");
        city7.setAttributeValue("weather", "cold");
        city7.setAttributeValue("area", "big");

        Assert.assertEquals("east coast", this.testGenerator.getDecision(city1));
        Assert.assertEquals("west coast", this.testGenerator.getDecision(city2));
        Assert.assertEquals("east coast", this.testGenerator.getDecision(city3));
        Assert.assertEquals("east coast", this.testGenerator.getDecision(city4));
        Assert.assertEquals("west coast", this.testGenerator.getDecision(city5));
        Assert.assertEquals("east coast", this.testGenerator.getDecision(city6));
        Assert.assertEquals("west coast", this.testGenerator.getDecision(city7));
    }
}
