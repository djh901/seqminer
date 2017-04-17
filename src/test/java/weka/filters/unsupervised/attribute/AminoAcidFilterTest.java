package weka.filters.unsupervised.attribute;


import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.rules.ZeroR;
import weka.core.Instances;
import weka.core.TestInstances;
import weka.filters.AbstractFilterTest;

public abstract class AminoAcidFilterTest extends AbstractFilterTest {

	public AminoAcidFilterTest(String name) {
		super(name);
	}

	@Override
	protected FilteredClassifier getFilteredClassifier() {
		FilteredClassifier result = new FilteredClassifier();
		AminoAcidFilter filter = (AminoAcidFilter) getFilter();
		filter.setAttributeIndex("2");
		result.setFilter(filter);
		result.setClassifier(new ZeroR());
		return result;
	}

	@Override
	protected Instances getFilteredClassifierData() throws Exception {
		TestInstances test = new TestInstances(); 
		test.setNumString(1);
		test.setWords("A,C,D,E,F,G,H,I,K,L,M,N,P,Q,R,S,T,V,W,Y");
		test.setWordSeparators("");
		Instances data = test.generate();
		return data;
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		TestInstances test = new TestInstances(); 
		test.setNumString(1);
		test.setWords("A,C,D,E,F,G,H,I,K,L,M,N,P,Q,R,S,T,V,W,Y");
		test.setWordSeparators("");
		test.setSeed(1989);
		test.setNumInstances(3);
		m_Instances = test.generate();
		
		((AminoAcidFilter) m_Filter).setAttributeIndex("2");
	}

}