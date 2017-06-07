package weka.filters.unsupervised.attribute;

import junit.framework.Assert.*;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.rules.ZeroR;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.TestInstances;
import weka.filters.AbstractFilterTest;
import weka.filters.Filter;

public class OneHotEncodingTest extends AbstractFilterTest {
  public OneHotEncodingTest(String name) {
    super(name);
  }

  @Override
  protected FilteredClassifier getFilteredClassifier() {
    return null;
  }

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
    Attribute nomAttr = new Attribute("nomAttr");
    Attribute strAttr = new Attribute("strAttr");
    Attribute classAttr = new Attribute("classAttr");
    FastVector nomVals = new FastVector(2);
    nomVals.addElement("black");
    nomVals.addElement("blue");
    FastVector classVals = new FastVector(2);
    classVals.addElement("pos");
    classVals.addElement("neg");
    FastVector attrs = new FastVector();
    attrs.addElement(new Attribute("nomAttr", nomVals));
    attrs.addElement(new Attribute("strAttr", (FastVector) null));
    attrs.addElement(new Attribute("classAttr", classVals));
    Instances data = new Instances("TestData", attrs, 0);
    double vals[] = new double[3];
    vals[0] = 0;
    vals[1] = data.attribute(1).addStringValue("ACDEF");
    vals[2] = 1;
    data.add(new DenseInstance(1.0, vals));
    vals = new double[3];
    vals[0] = 1;
    vals[1] = data.attribute(1).addStringValue("STVWY");
    vals[2] = 0;
    data.add(new DenseInstance(1.0, vals));
    m_Instances = data;

		((OneHotEncoding) m_Filter).setAttributeIndex("2");
	}

  @Override
  public Filter getFilter() {
    OneHotEncoding filter = new OneHotEncoding();
    return filter;
  }

  public void testDefault() {
    Instances result = useFilter();
    assertEquals(result.numInstances(), 12);
    assertEquals(result.numAttributes(), m_Instances.numAttributes());
    assertEquals(result.get(0).toString(1), "YTA");
    assertEquals(result.get(2).toString(1), "AVW");
    assertEquals(result.get(10).toString(1), "AYY");
  }

  public static Test suite() {
    return new TestSuite(OneHotEncodingTest.class);
  }

  public static void main(String[] args) {
    TestRunner.run(suite());
  }
}
