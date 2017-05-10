package weka.filters.unsupervised.attribute;

import java.util.Arrays;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

/**
 * <!-- globalinfo-start --> Counts the amino acids in a protein sequence. 
 * Optionally scales counts to unit sum.
 * <p>
 * <!-- globalinfo-end -->
 * 
 * <!-- options-start --> Valid options are:
 * <pre>
 * -R &lt;index&gt;
 *  Sets the index of the string attribute.
 * </pre>
 * 
 * <pre>
 * -S
 *  Scales counts to sum to unity.
 * </pre>
 * <!-- options-end -->
 * 
 * @author danielhogan
 * @version $Revision$
 */
public class AminoAcidCounts extends AminoAcidFilter {
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
