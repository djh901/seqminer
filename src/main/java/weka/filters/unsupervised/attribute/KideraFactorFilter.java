package weka.filters.unsupervised.attribute;

import java.util.Arrays;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class KideraFactorFilter extends AminoAcidFilter {
	private static final long serialVersionUID = 7569364579668558625L;

	public static int nKideraFactors = 10;
	private static double[][] kideraFactors = {
			{ -1.56, -1.67, -0.97, -0.27, -0.93, -0.78, -0.20, -0.08, 0.21, -0.48 }, // A
			{ 0.12, -0.89, 0.45, -1.05, -0.71, 2.41, 1.52, -0.69, 1.13, 1.10 }, // C
			{ 0.58, -0.22, -1.58, 0.81, -0.92, 0.15, -1.52, 0.47, 0.76, 0.70 }, // D
			{ -1.45, 0.19, -1.61, 1.17, -1.31, 0.40, 0.04, 0.38, -0.35, -0.12 }, // E
			{ -0.21, 0.98, -0.36, -1.43, 0.22, -0.81, 0.67, 1.10, 1.71, -0.44 }, // F
			{ 1.46, -1.96, -0.23, -0.16, 0.10, -0.11, 1.32, 2.36, -1.66, 0.46 }, // G
			{ -0.41, 0.52, -0.28, 0.28, 1.61, 1.01, -1.85, 0.47, 1.13, 1.63 }, // H
			{ -0.73, -0.16, 1.79, -0.77, -0.54, 0.03, -0.83, 0.51, 0.66, -1.78 }, // I
			{ -0.34, 0.82, -0.23, 1.70, 1.54, -1.62, 1.15, -0.08, -0.48, 0.60 }, // K
			{ -1.04, 0.00, -0.24, -1.10, -0.55, -2.05, 0.96, -0.76, 0.45, 0.93 }, // L
			{ -1.40, 0.18, -0.42, -0.73, 2.00, 1.52, 0.26, 0.11, -1.27, 0.27 }, // M
			{ 1.14, -0.07, -0.12, 0.81, 0.18, 0.37, -0.09, 1.23, 1.10, -1.73 }, // N
			{ 2.06, -0.33, -1.15, -0.75, 0.88, -0.45, 0.30, -2.30, 0.74, -0.28 }, // P
			{ -0.47, 0.24, 0.07, 1.10, 1.10, 0.59, 0.84, -0.71, -0.03, -2.33 }, // Q
			{ 0.22, 1.27, 1.37, 1.87, -1.70, 0.46, 0.92, -0.39, 0.23, 0.93 }, // R
			{ 0.81, -1.08, 0.16, 0.42, -0.21, -0.43, -1.89, -1.15, -0.97, -0.23 }, // S
			{ 0.26, -0.70, 1.21, 0.63, -0.10, 0.21, 0.24, -1.15, -0.56, 0.19 }, // T
			{ -0.74, -0.71, 2.04, -0.40, 0.50, -0.81, -1.07, 0.06, -0.46, 0.65 }, // V
			{ 0.30, 2.10, -0.72, -1.57, -1.16, 0.57, -0.48, -0.40, -2.30, -0.60 }, // W
			{ 1.38, 1.48, 0.80, -0.56, -0.00, -0.68, -0.31, 1.03, -0.05, 0.53 }, // Y
	};

	@Override
	protected Instances prepareOutputFormat(Instances instances) throws Exception {
		int numAttrs = instances.numAttributes();
		for (int i = 0; i < nKideraFactors; i++) {
			instances.insertAttributeAt(new Attribute(Integer.toString(i)), numAttrs + i);
		}
		return instances;
	}

	@Override
	protected Instance processInstance(Instance instance) throws Exception {
		double[] scores = new double[nKideraFactors];
		Arrays.fill(scores, 0.0);
		String sequence = instance.stringValue(attrIndex.getIndex());
		int sequenceLength = sequence.length();
		for (int i = 0; i < sequenceLength; i++) {
			int baseIndex = AminoAcidCountFilter.baseMap.get(sequence.charAt(i));
			for (int j = 0; j < scores.length; j++) {
				scores[j] += kideraFactors[baseIndex][j];
			}
		}
		if (scaleFlag) {
			for (int i = 0; i < scores.length; i++) {
				scores[i] = scores[i] / sequenceLength;
			}
		}
		return new DenseInstance(1.0, scores);
	}

	@Override
	public String globalInfo() {
		// TODO Auto-generated method stub
		return "Calculate Kidera factors.";
	}
}
