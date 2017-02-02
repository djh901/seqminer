package weka.filters.unsupervised.attribute;

import java.util.Arrays;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class AminoAcidCountFilter extends AminoAcidFilter {
	private static final long serialVersionUID = 8367780873661135860L;

	@Override
	protected Instances prepareOutputFormat(Instances instances) throws Exception {
		int numAttrs = instances.numAttributes();
		for (int i = 0; i < bases.length; i++) {
			instances.insertAttributeAt(new Attribute(Character.toString(bases[i])), numAttrs + i);
		}
		return instances;
	}

	@Override
	protected Instance processInstance(Instance instance) throws Exception {
		double[] counts = new double[20];
		Arrays.fill(counts, 0.0);
		int seqAttrIndex = attrIndex.getIndex();
		String sequence = instance.stringValue(seqAttrIndex);
		int sequenceLength = sequence.length();
		for (int i = 0; i < sequenceLength; i++) {
			counts[baseMap.get(sequence.charAt(i))] += 1.0;
		}
		if (scaleFlag) {
			for (int i = 0; i < bases.length; i++) {
				counts[i] = counts[i] / sequenceLength;
			}
		}
		return new DenseInstance(1.0, counts);
	}
}