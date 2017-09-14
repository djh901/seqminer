package weka.filters.unsupervised.attribute;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

import weka.core.Capabilities;
import weka.core.Capabilities.Capability;
import weka.core.Instances;
import weka.core.Option;
import weka.core.OptionMetadata;
import weka.core.SingleIndex;
import weka.core.Utils;
import weka.filters.SimpleBatchFilter;

public abstract class ProteinFilter extends SimpleBatchFilter {
	protected SingleIndex attrIndex = new SingleIndex("last");

	public ProteinFilter() {
		super();
	}

	public Capabilities getCapabilities() {
		Capabilities result = super.getCapabilities();
		result.enableAllAttributes();
		result.enableAllClasses();
		result.enable(Capability.NO_CLASS);  // filter doesn't need class to be set
		return result;
	}

	@OptionMetadata(displayName = "Attribute index", description = "Index of string attribute.", commandLineParamName = "R", commandLineParamSynopsis = "-R <index>", displayOrder = 1)
	public String getAttributeIndex() {
		return attrIndex.getSingleIndex();
	}

	public void setAttributeIndex(String index) {
		attrIndex.setSingleIndex(index);
	}

	@Override
	public Enumeration<Option> listOptions() {
		Vector<Option> options = new Vector<Option>();
		options.add(new Option(
				"\tSet the index of the string attribute.", "C", 1,
				"-R <index>"));
        options.addAll(Collections.list(super.listOptions()));
		return options.elements();
	}

	@Override
	public String[] getOptions() {
		Vector<String> options = new Vector<String>();
	    options.add("-R");
	    options.add("" + getAttributeIndex());
        Collections.addAll(options, super.getOptions());
	    return options.toArray(new String[options.size()]);
	}

	@Override
	public void setOptions(String[] options) throws Exception {
	    String attIndex = Utils.getOption('R', options);
	    if (attIndex.length() != 0) {
	      setAttributeIndex(attIndex);
	    } else {
	      setAttributeIndex("last");
	    }
	    super.setOptions(options); 
	    Utils.checkForRemainingOptions(options);
	}

	abstract protected Instances prepareOutputFormat(Instances instances) throws Exception; 

	@Override // TODO: is this method deprecated by prepareOutputFormat
	protected Instances determineOutputFormat(Instances instances) throws Exception {
		attrIndex.setUpper(instances.numAttributes() - 1);

		int seqAttrIndex = attrIndex.getIndex();
		if (!instances.attribute(seqAttrIndex).isString()) {
			throw new Exception("Specified attribute index (" + seqAttrIndex + ") must be string type.");
		}

		Instances result = new Instances(instances, 0);
		return prepareOutputFormat(result);
	}
}
