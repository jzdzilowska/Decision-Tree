package sol;

import org.junit.Assert;
import org.junit.Test;
import src.AttributeSelection;
import src.DecisionTreeCSVParser;
import src.Row;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;

/**
 * A class to test basic decision tree functionality on a basic training dataset
 */
public class BasicDatasetTest {
    // IMPORTANT: for this filepath to work, make sure the project is open as the top-level directory in IntelliJ
    // (See the first yellow information box in the handout testing section for details)

    String trainingPath = "data/citiesdataset.csv"; // TODO: replace with your own input file
    String targetAttribute = "result"; // TODO: replace with your own target attribute
    TreeGenerator testGenerator;
    Dataset training;

    /**
     * Constructs the decision tree for testing based on the input file and the target attribute.
     */
    @Before
    public void buildTreeForTest() {
        List<Row> dataObjects = DecisionTreeCSVParser.parse(this.trainingPath);
        List<String> attributeList = new ArrayList<>(dataObjects.get(0).getAttributes());
        this.training = new Dataset(attributeList, dataObjects, AttributeSelection.ASCENDING_ALPHABETICAL);
        // builds a TreeGenerator object and generates a tree for "foodType"
        this.testGenerator = new TreeGenerator();

//        TODO: Uncomment this once you've implemented generateTree
        this.testGenerator.generateTree(this.training, this.targetAttribute);
    }

    /**
     *
     */
    @Test
    public void testDecisionTreeCSVParser() {
        List<Row> dataObjects = DecisionTreeCSVParser.parse(this.trainingPath);
        Assert.assertEquals(5,dataObjects.size());
        Assert.assertEquals("fast-paced", dataObjects.get(1).getAttributeValue("lifestyle"));
        Assert.assertEquals("east coast", dataObjects.get(3).getAttributeValue("result"));
    }


    /**
     * Tests the expected classification of cities
     */
    @Test
    public void testClassification() {
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

        Assert.assertEquals("east coast", this.testGenerator.getDecision(city1));
        Assert.assertEquals("west coast", this.testGenerator.getDecision(city2));
        Assert.assertEquals("east coast", this.testGenerator.getDecision(city3));
        Assert.assertEquals("east coast", this.testGenerator.getDecision(city4));
        Assert.assertEquals("west coast", this.testGenerator.getDecision(city5));
    }
}
