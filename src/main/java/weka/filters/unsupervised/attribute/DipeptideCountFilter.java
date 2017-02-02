package weka.filters.unsupervised.attribute;

import java.util.Arrays;
import java.util.LinkedHashMap;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class DipeptideCountFilter extends AminoAcidFilter {
	private static final long serialVersionUID = -3796038204492101327L;

	public static LinkedHashMap<String,Integer> dipeptide2Int = new LinkedHashMap<String,Integer>();
	static {
		int i = 0;
		for (Character b1 : bases) {
			for (Character b2 : bases) {
				String dipeptide = new StringBuilder().append(b1)
						.append(b2).toString();
				dipeptide2Int.put(dipeptide, i);
				i += 1;
			}
		}
	}

	@Override
	protected Instances prepareOutputFormat(Instances instance) throws Exception {
		int numAttrs = instance.numAttributes();
		int i = 0;
		for (String dipeptide : dipeptide2Int.keySet()) {
			instance.insertAttributeAt(new Attribute(dipeptide), numAttrs + i);
			i += 1;
		}
		return instance;
	}

	@Override
	protected Instance processInstance(Instance instance) throws Exception {
		double[] counts = new double[dipeptide2Int.size()];
		Arrays.fill(counts, 0.0);
		String sequence = instance.stringValue(attrIndex.getIndex());
		for (int i = 0; i < sequence.length() - 1; i++) {
			String dipeptide = sequence.substring(i, i+2);
			counts[dipeptide2Int.get(dipeptide)] += 1;
		}
		if (scaleFlag) {
			for (int j = 0; j < counts.length; j++) {
				counts[j] = counts[j] / sequence.length();
			}
		}
		return new DenseInstance(1.0, counts);
	}
}
