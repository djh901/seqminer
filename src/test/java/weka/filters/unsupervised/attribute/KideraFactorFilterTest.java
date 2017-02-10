package weka.filters.unsupervised.attribute;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import weka.core.Instances;
import weka.filters.Filter;

public class KideraFactorFilterTest extends AminoAcidFilterTest {

	public KideraFactorFilterTest(String name) {
		super(name);
	}

	@Override
	public Filter getFilter() {
		KideraFactors filter = new KideraFactors();
		return filter;
	}
	
	public void testTypical() {
		Instances result = useFilter();
		System.out.println(result.toString());
		assertEquals(result.numAttributes(), m_Instances.numAttributes() + KideraFactors.nKideraFactors);
		
		// 4th Kidera factor for 1st sequence
		double kid4 = 1.17 + -0.73 + 0.28 + -1.57 + 0.81 + -1.10 + -0.40 + 0.28 + 0.63 + 1.10 + 0.81 + -0.56 + -0.56 + -0.75 + -0.27 + -0.40 + -0.77 + -1.43 + -1.10 + -0.77;
		// 9th Kidera factor for 2nd sequence
		double kid9 = 1.10 + 0.21 + -0.97 + -1.27 + -0.48 + 0.21 + -2.30 + -0.97 + -0.35 + -0.48 + -0.05 + -1.66 + 0.74 + -0.46 + 0.76 + 0.74 + -2.30 + -0.05 + -0.48 + 1.13;
		assertEquals(result.instance(0).value(6), kid4);
		assertEquals(result.instance(1).value(11), kid9);
		System.out.println(result.toString());
	}
	
	public void testUnitScaling() {
		((AminoAcidFilter) m_Filter).setScaleFlag(true);
		Instances result = useFilter();
		System.out.println(result.toString());
		
		// 4th Kidera factor for 1st sequence
		double kid4 = 1.17 + -0.73 + 0.28 + -1.57 + 0.81 + -1.10 + -0.40 + 0.28 + 0.63 + 1.10 + 0.81 + -0.56 + -0.56 + -0.75 + -0.27 + -0.40 + -0.77 + -1.43 + -1.10 + -0.77;
		// 9th Kidera factor for 2nd sequence
		double kid9 = 1.10 + 0.21 + -0.97 + -1.27 + -0.48 + 0.21 + -2.30 + -0.97 + -0.35 + -0.48 + -0.05 + -1.66 + 0.74 + -0.46 + 0.76 + 0.74 + -2.30 + -0.05 + -0.48 + 1.13;
		assertEquals(result.instance(0).value(6), kid4 / 20);
		assertEquals(result.instance(1).value(11), kid9 / 20);
		System.out.println(result.toString());
	}

	public static Test suite() {
		return new TestSuite(KideraFactorFilterTest.class);
	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

}
