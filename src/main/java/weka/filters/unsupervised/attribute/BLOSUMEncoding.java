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
 * <!-- globalinfo-start --> Represents fixed-length sequences as sequences of vectors from BLOSUM62.
 * <p>
 * <!-- globalinfo-end -->
 * 
 * <!-- options-start --> Valid options are:
 * <pre>
 * -R &lt;index&gt;
 *  Sets the index of the string attribute.
 * </pre>
 * <!-- options-end -->
 * 
 * @author danielhogan
 * @version $Revision$
 */
public class BLOSUMEncoding extends ProteinFilter {
  private static final long serialVersionUID = 1L;

  public static final char[] bases = { 'A', 'R', 'N', 'D', 'C', 'Q', 'E', 'G', 'H', 'I', 'L', 'K', 'M', 'F', 'P', 'S', 'T', 'W', 'Y', 'V', 'B', 'Z', 'X', '*' };
  public static final double[][] blosum62 = {{ 4, -1, -2, -2,  0, -1, -1,  0, -2, -1, -1, -1, -1, -2, -1,  1,  0, -3, -2,  0, -2, -1,  0, -4 },
    { -1,  5,  0, -2, -3,  1,  0, -2,  0, -3, -2,  2, -1, -3, -2, -1, -1, -3, -2, -3, -1,  0, -1, -4 },
    { -2,  0,  6,  1, -3,  0,  0,  0,  1, -3, -3,  0, -2, -3, -2,  1,  0, -4, -2, -3,  3,  0, -1, -4 },
    { -2, -2,  1,  6, -3,  0,  2, -1, -1, -3, -4, -1, -3, -3, -1,  0, -1, -4, -3, -3,  4,  1, -1, -4 },
    { 0, -3, -3, -3,  9, -3, -4, -3, -3, -1, -1, -3, -1, -2, -3, -1, -1, -2, -2, -1, -3, -3, -2, -4 },
    { -1,  1,  0,  0, -3,  5,  2, -2,  0, -3, -2,  1,  0, -3, -1,  0, -1, -2, -1, -2,  0,  3, -1, -4 },
    { -1,  0,  0,  2, -4,  2,  5, -2,  0, -3, -3,  1, -2, -3, -1,  0, -1, -3, -2, -2,  1,  4, -1, -4 },
    { 0, -2,  0, -1, -3, -2, -2,  6, -2, -4, -4, -2, -3, -3, -2,  0, -2, -2, -3, -3, -1, -2, -1, -4 },
    { -2,  0,  1, -1, -3,  0,  0, -2,  8, -3, -3, -1, -2, -1, -2, -1, -2, -2,  2, -3,  0,  0, -1, -4 },
    { -1, -3, -3, -3, -1, -3, -3, -4, -3,  4,  2, -3,  1,  0, -3, -2, -1, -3, -1,  3, -3, -3, -1, -4 },
    { -1, -2, -3, -4, -1, -2, -3, -4, -3,  2,  4, -2,  2,  0, -3, -2, -1, -2, -1,  1, -4, -3, -1, -4 },
    { -1,  2,  0, -1, -3,  1,  1, -2, -1, -3, -2,  5, -1, -3, -1,  0, -1, -3, -2, -2,  0,  1, -1, -4 },
    { -1, -1, -2, -3, -1,  0, -2, -3, -2,  1,  2, -1,  5,  0, -2, -1, -1, -1, -1,  1, -3, -1, -1, -4 },
    { -2, -3, -3, -3, -2, -3, -3, -3, -1,  0,  0, -3,  0,  6, -4, -2, -2,  1,  3, -1, -3, -3, -1, -4 },
    { -1, -2, -2, -1, -3, -1, -1, -2, -2, -3, -3, -1, -2, -4,  7, -1, -1, -4, -3, -2, -2, -1, -2, -4 },
    { 1, -1,  1,  0, -1,  0,  0,  0, -1, -2, -2,  0, -1, -2, -1,  4,  1, -3, -2, -2,  0,  0,  0, -4 },
    { 0, -1,  0, -1, -1, -1, -1, -2, -2, -1, -1, -1, -1, -2, -1,  1,  5, -2, -2,  0, -1, -1,  0, -4 },
    { -3, -3, -4, -4, -2, -2, -3, -2, -2, -3, -2, -3, -1,  1, -4, -3, -2, 11,  2, -3, -4, -3, -2, -4 },
    { -2, -2, -2, -3, -2, -1, -2, -3,  2, -1, -1, -2, -1,  3, -3, -2, -2,  2,  7, -1, -3, -2, -1, -4 },
    { 0, -3, -3, -3, -1, -2, -2, -3, -3,  3,  1, -2,  1, -1, -2, -2,  0, -3, -1,  4, -3, -2, -1, -4 },
    { -2, -1,  3,  4, -3,  0,  1, -1,  0, -3, -4,  0, -3, -3, -2,  0, -1, -4, -3, -3,  4,  1, -1, -4 },
    { -1,  0,  0,  1, -3,  3,  4, -2,  0, -3, -3,  1, -1, -3, -1,  0, -1, -3, -2, -2,  1,  4, -1, -4 },
    { 0, -1, -1, -1, -2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -2,  0,  0, -2, -1, -1, -1, -1, -1, -4 },
    { -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4,  1 }};

  protected int stringLength = -1;

  @Override
  public String globalInfo() {
    return "A BLOSUM62 encoding filter.";
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
        // TODO: give informative names to columns
        outputFormat.insertAttributeAt(new Attribute(Integer.toString(20 * i + j)), outputFormat.numAttributes());
      }
    }
    return outputFormat;
  }

  @Override
  protected Instances process(Instances instances) throws Exception {
    Instances output = prepareOutputFormat(instances);

    for (int i = 0; i < instances.numInstances(); i++) {
      double[] vals = new double[output.numAttributes()]; // TODO: also change for OneHotEncoding
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
        int baseIdx = 0;
        for (; bases[baseIdx] != sequence.charAt(pos); baseIdx++) {
          // System.out.println("Does " + bases[baseIdx] + " equal " + sequence.charAt(pos) + "?");
        }
        assert baseIdx <= 20 : "Base index must be less than 20. Sequence base is " 
          + sequence.charAt(pos) + "; base index points to " + bases[baseIdx];
        System.arraycopy(blosum62[baseIdx], 0, vals, instances.numAttributes() + (20 * pos), 20);
      }
      output.add(new DenseInstance(1.0, vals));
    }

    System.err.println(output.toString());
    return output;
  }
}
