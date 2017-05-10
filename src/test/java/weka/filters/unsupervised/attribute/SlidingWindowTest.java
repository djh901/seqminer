package weka.filters.unsupervised.attribute;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import weka.core.Instances;
import weka.filters.Filter;

public class SlidingWindowTest extends AminoAcidFilterTest {
    public SlidingWindowTest(String name) {
        super(name);
    }

    @Override
    public Filter getFilter() {
        SlidingWindow filter = new SlidingWindow();
        return filter;
    }

    public void testTypical() {
        Instances result = useFilter();
        System.out.println(result.toString());
    }

    public static Test suite() {
        return new TestSuite(SlidingWindowTest.class);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }
}
