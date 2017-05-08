package weka.filters.unsupervised.attribute;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import weka.core.Instances;
import weka.filters.Filter;

public class AminoAcidCountTest extends AminoAcidFilterTest {
	public AminoAcidCountTest(String name) {
		super(name);
	}

	@Override
	public Filter getFilter() {
		AminoAcidFilter filter = new AminoAcidCounts();
		return filter;
	}
	
	public void testTypical() {
		Instances result = useFilter();
		Assert.assertEquals(result.numAttributes(), m_Instances.numAttributes() + AminoAcidFilter.bases.length);
		Assert.assertEquals(result.instance(0).value(3), 1.0); // A
		Assert.assertEquals(result.instance(0).value(22), 2.0); // Y
		Assert.assertEquals(result.instance(2).value(3), 4.0); // A
		Assert.assertEquals(result.instance(2).value(22), 1.0); // Y
		System.out.println(result.toString());
	}
	
	public void testUnitScaling() {
		((AminoAcidFilter) m_Filter).setScaleFlag(true);
		Instances result = useFilter();
		Assert.assertEquals(result.instance(0).value(3), 0.05); // A
		Assert.assertEquals(result.instance(0).value(22), 0.1); // Y
		Assert.assertEquals(result.instance(2).value(3), 0.2); // A
		Assert.assertEquals(result.instance(2).value(22), 0.05); // Y
		System.out.println(result.toString());
	}

	public static Test suite() {
		return new TestSuite(AminoAcidCountTest.class);
	}
	
	public static void main(String[] args) {
		TestRunner.run(suite());
	}
}
