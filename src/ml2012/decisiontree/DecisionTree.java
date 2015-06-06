package ml2012.decisiontree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.GainRatioAttributeEval;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class DecisionTree {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		//this is used to test Github
		long start = System.currentTimeMillis();
		
		Instances data = new Instances(  
	            new BufferedReader(
	            		new FileReader("data.arff")));
		System.out.println("instances# = " + data.numInstances());
//		for(int i = 0; i < 12; i++){
//			data.remove(i);
//		}
		System.out.println("instances# after remove = " + data.numInstances());
		data.setClassIndex(data.numAttributes()-1);
		AttributeSelection attsel = new AttributeSelection();   
		InfoGainAttributeEval ig = new InfoGainAttributeEval(); 
//		GainRatioAttributeEval ig = new GainRatioAttributeEval();
		Ranker rk = new Ranker();  
		attsel.setEvaluator(ig);  
		attsel.setSearch(rk);
		attsel.SelectAttributes(data);  
		data = attsel.reduceDimensionality(data);
		// obtain the attribute indices that were selected 
		int[] indices = attsel.selectedAttributes();  
		System.out.println(attsel.toResultsString());
		System.out.println("selected attributes#" + indices.length);
		System.out.println("-----------------begin classify...------------------");
		J48 classifier = new J48();
//		classifier.setUnpruned(true);
	    Evaluation eval = new Evaluation(data);
//	    Object classificationOutput[] = new Object[1];
//	    AbstractOutput ao = new FOutput();
//	    StringBuffer predsBuff = new StringBuffer();
//	    ao.setBuffer(predsBuff);
//	    classificationOutput[0] = ao;
//	    eval.crossValidateModel(classifier, data, 4, new Random(1), classificationOutput);
	    eval.crossValidateModel(classifier, data, 4, new Random(1));
	    System.out.println(eval.toClassDetailsString());
	    System.out.println(eval.toSummaryString());
	    System.out.println(eval.toMatrixString());
		
		long end = System.currentTimeMillis();
		System.out.println("time consumed: " + (end - start) / 1000.0 + " s");
	}

}
