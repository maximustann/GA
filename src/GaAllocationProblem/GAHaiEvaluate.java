/*
 * Boxiong Tan (Maximus Tann)
 * Title:        PSO algorithm framework
 * Description:  PSO algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * BPSOHaiEvaluate.java - evaluation for Hai's paper
 */
package GaAllocationProblem;

import java.util.ArrayList;

import algorithms.Chromosome;
import algorithms.Evaluate;
import algorithms.FitnessFunc;
import algorithms.Normalize;
/**
 *
 * @author Boxiong Tan (Maximus Tann)
 * @since PSO framework 1.0
 */
public class GAHaiEvaluate implements Evaluate{
	/** Evaluation function list */
	private ArrayList<FitnessFunc> funcList;
	
	/** two values in the array */
	private double[] weights;
	
	/** normalize function*/
	private Normalize[] normalizer;
	
	private Constraint[] constraints;

	/**
	 * 
	 * @param funcList Evaluation function list
	 * @param weights weight is used to balance two objectives, cost and response time
	 */
	public GAHaiEvaluate(
						ArrayList<FitnessFunc> funcList, 
						Normalize[] normalizer,
						Constraint[] constraints,
						double[] weights){
		this.funcList = funcList;
		this.weights = weights;
		this.normalizer = normalizer;
		this.constraints = constraints;
	}

	public void setFuncList(ArrayList<FitnessFunc> funcList){
		this.funcList = funcList;
	}

	/**
	 * <ul>
	 * 	<li> 1. evaluate population with a list of fitness functions (two, in this case)</li>
	 *  <li> 2. add up n (two, in this case) fitness values multiply by their weights</li>
	 * </ul>
	 */
	public void evaluate(Chromosome[] popVar, ArrayList<double[]> fitness){
		fitness.clear();
		ArrayList<ArrayList<double[]>> fitList = new ArrayList<ArrayList<double[]>>();
		
		for(int i = 0; i < funcList.size(); i++){
			ArrayList<double[]> tempFit = funcList.get(i).execute(popVar);
			tempFit = normalizer[i].doNorm(tempFit);
			tempFit = constraints[i].punish(popVar, tempFit);
			fitList.add(tempFit);
		}

		for(int i = 0; i < popVar.length; i++){
			double[] fit = new double[2];
			fit[0] = 0.0;
			fit[1] = i;
			for(int j = 0; j < funcList.size(); j++){
				fit[0] += weights[j] * fitList.get(j).get(i)[0];
			}
			fitness.add(fit);
		}
	}
}
