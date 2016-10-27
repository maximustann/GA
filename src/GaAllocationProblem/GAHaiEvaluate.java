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

import algorithms.Evaluate;
import algorithms.FitnessFunction;
/**
 *
 * @author Boxiong Tan (Maximus Tann)
 * @since PSO framework 1.0
 */
public class GAHaiEvaluate implements Evaluate{
	/** Evaluation function list */
	private ArrayList<FitnessFunction> funcList;
	
	/** two values in the array */
	private double[] weights;

	/**
	 * 
	 * @param funcList Evaluation function list
	 * @param weights weight is used to balance two objectives, cost and response time
	 */
	public GAHaiEvaluate(ArrayList<FitnessFunction> funcList, double[] weights){
		this.funcList = funcList;
		this.weights = weights;
	}

	public void setFuncList(ArrayList<FitnessFunction> funcList){
		this.funcList = funcList;
	}

	/**
	 * <ul>
	 * 	<li> 1. evaluate population with a list of fitness functions (two, in this case)</li>
	 *  <li> 2. add up n (two, in this case) fitness values multiply by their weights</li>
	 * </ul>
	 */
	public void evaluate(double[][] popVar, double[] fitness){
		ArrayList<double []> fitList = new ArrayList<double []>();
		for(int i = 0; i < funcList.size(); i++){
			fitList.add(funcList.get(i).normalizedFit(popVar));
		}

		for(int i = 0; i < fitness.length; i++){
			fitness[i] = 0.0;
			for(int j = 0; j < funcList.size(); j++){
				fitness[i] += weights[j] * fitList.get(j)[i];
			}
		}
	}
}
