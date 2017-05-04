package weka.filters.unsupervised.attribute;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import weka.core.Capabilities;
import weka.core.Capabilities.Capability;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.OptionMetadata;
import weka.core.SingleIndex;
import weka.core.Utils;
import weka.filters.SimpleStreamFilter;

public abstract class AminoAcidFilter extends SimpleStreamFilter {
	private static final long serialVersionUID = -1456442150601929952L;
	
	public static final char[] bases = { 'A', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'K', 'L', 
			'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'Y' };
	public static final HashMap<Character,Integer> baseMap = new HashMap<Character,Integer>();
	static {
		for (int i = 0; i < bases.length; i++) {
			baseMap.put(bases[i], i);
		}
	}

	protected SingleIndex attrIndex = new SingleIndex("last");
	protected boolean scaleFlag = false;

	public AminoAcidFilter() {
		super();
	}

	public Capabilities getCapabilities() {
		Capabilities result = super.getCapabilities();
		result.enableAllAttributes();
		result.enableAllClasses();
		result.enable(Capability.NO_CLASS);  //// filter doesn't need class to be set//
		return result;
	}

	@OptionMetadata(displayName = "Attribute index", description = "Index of string attribute.", commandLineParamName = "R", commandLineParamSynopsis = "-R <index>", displayOrder = 1)
	public String getAttributeIndex() {
		return attrIndex.getSingleIndex();
	}

	public void setAttributeIndex(String index) {
		attrIndex.setSingleIndex(index);
	}

	@OptionMetadata(displayName = "Scale", description = "Whether to scale to unit sum.", commandLineParamName = "S", commandLineParamSynopsis = "-S", commandLineParamIsFlag = true, displayOrder = 2)
	public boolean getScaleFlag() {
		return scaleFlag;
	}

	public void setScaleFlag(boolean flag) {
		scaleFlag = flag;
	}

	@Override
	public Enumeration<Option> listOptions() {
		Vector<Option> result = new Vector<Option>();
		
		result.addElement(new Option(
				"\tSet the index of the string attribute.", "C", 1,
				"-R <index>"));
		result.addElement(new Option(
				"\tSet whether to scale to unit sum.", "S", 0, 
				"-S"));
		return super.listOptions();
	}

	@Override
	public String[] getOptions() {
		Vector<String> options = new Vector<String>();
	
	    options.add("-R");
	    options.add("" + getAttributeIndex());
	    if (getScaleFlag()) {
			options.add("-S");
	    }
	
	    return options.toArray(new String[0]);
	}

	@Override
	public void setOptions(String[] options) throws Exception {
	    String attIndex = Utils.getOption('R', options);
	    if (attIndex.length() != 0) {
	      setAttributeIndex(attIndex);
	    } else {
	      setAttributeIndex("last");
	    }
	    
	    if (Utils.getFlag('S', options)) {
			setScaleFlag(true);
	    } else {
	    	setScaleFlag(false);
	    }
	    
	    Utils.checkForRemainingOptions(options);
	}

	@Override
	public String globalInfo() {
		return "A test stream filter.";
	}

	abstract protected Instances prepareOutputFormat(Instances instances) throws Exception; 
	abstract protected Instance processInstance(Instance instance) throws Exception;

	@Override
	protected Instances determineOutputFormat(Instances instances) throws Exception {
		attrIndex.setUpper(instances.numAttributes() - 1);

		int seqAttrIndex = attrIndex.getIndex();
		if (!instances.attribute(seqAttrIndex).isString()) {
			throw new Exception("Specified attribute index (" + seqAttrIndex + ") must be string type.");
		}

		Instances result = new Instances(instances, 0);
		return prepareOutputFormat(result);
	}

	@Override
	protected Instance process(Instance instance) throws Exception {
		Instance resultValues = processInstance(instance);
		double[] resultArray = Arrays.copyOf(instance.toDoubleArray(), 
				instance.numAttributes() + resultValues.numAttributes());
		System.arraycopy(resultValues.toDoubleArray(), 0, 
				resultArray, instance.numAttributes(), resultValues.numAttributes());
		Instance result = new DenseInstance(1.0, resultArray);
		copyValues(result, false, instance.dataset(), outputFormatPeek());
		return result;
	}

}
