package weka.filters.unsupervised.attribute;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

import weka.core.SingleIndex;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Capabilities;
import weka.core.Capabilities.Capability;
import weka.core.Option;
import weka.core.OptionMetadata;
import weka.filters.SimpleBatchFilter;
import weka.core.Utils;

/**
 * FIXME!!!
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
public class OneHotEncoding extends ProteinFilter {
  private static final long serialVersionUID = 1L;

  public static final double[] BLOSUM62 = {
  }

  protected int stringLength = -1;

  @Override
  public String globalInfo() {
    return "A one-hot encoding filter.";
  }

  @Override
  protected Instances prepareOutputFormat(Instances inputFormat) throws Exception {
    attrIndex.setUpper(inputFormat.numAttributes() - 1);
    int seqAttrIndex = attrIndex.getIndex();
    if (!inputFormat.attribute(seqAttrIndex).isString()) {
      throw new Exception("Specified attribute index (" + seqAttrIndex + ") must be string type.");
    }

    Attribute seqAttribute = inputFormat.attribute(seqAttrIndex);
    for (int i = 0; i < seqAttribute.numValues(); i++) {
      if (stringLength < 0) { stringLength = seqAttribute.value(i).length(); }
      if (seqAttribute.value(i).length() != stringLength) {
        throw new Exception("All strings must have the same length.");
      }
    }

    Instances outputFormat = new Instances(inputFormat, 0);
    for (int i = 0; i < stringLength; i++) {
      for (int j = 0; j < 20; j++) {
        outputFormat.insertAttributeAt(new Attribute(Integer.toString(20 * i + j)), outputFormat.numAttributes());
      }
    }
    return outputFormat;
  }

  @Override
  protected Instances process(Instances instances) throws Exception {
    Instances output = prepareOutputFormat(instances);
    double[] vals = new double[output.numAttributes()];

    for (int i = 0; i < instances.numInstances(); i++) {
      String sequence = instances.get(i).toString(attrIndex.getIndex());
      for (int j = 0; j < instances.numAttributes(); j++) {
        if (instances.attribute(j).isString()) {
          // TODO: handle nominal and relational
          vals[j] = output.attribute(j).addStringValue(instances.get(i).toString(j));
        } else {
          vals[j] = instances.get(i).value(j);    
        }
      }
      for (int pos = 0; pos < sequence.length(); pos++) {
        for (int baseIdx = 0; baseIdx < 20; baseIdx++) {
          if (sequence.charAt(pos) == AminoAcidFilter.bases[baseIdx]) {
             vals[instances.numAttributes() + ((20 * pos) + baseIdx)] = 1;
          } else {
             vals[instances.numAttributes() + ((20 * pos) + baseIdx)] = 0; 
          }
        }
      }
      Instance obj = new DenseInstance(1.0, vals);
      output.add(new DenseInstance(1.0, vals));
      vals = new double[output.numAttributes()];
    }

    return output;
  }
}
