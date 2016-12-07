/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective Genetic algorithm framework
 * Description:  Single-objective Genetic algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * RosenbrockEvaluate.java evaluation function
 */
package Rosenbrock;

import java.util.ArrayList;

import algorithms.Chromosome;
import algorithms.Evaluate;
import algorithms.FitnessFunc;

public class RosenbrockEvaluate implements Evaluate{
	private ArrayList<FitnessFunc> funcList;

	/** constructor, initialize fitness function list */
	public RosenbrockEvaluate(ArrayList<FitnessFunc> funcList){
		this.funcList = funcList;
	}

	/** You can change the fitness function list */
	public void setFuncList(ArrayList<FitnessFunc> funcList){
		this.funcList = funcList;
	}

	@Override
	/*
	 * 
	 * an implementation of the evaluate interface.
	 * Basically, the process should evaluate all chromosomes in the popVar array and stores
	 * fitness values in popFit.
	 * 
	 * I did not write this as a template because you might want to use weighted sum with multiple
	 * objectives or single objective. So here I provide a list things that you need to do.
	 * 
	 * There are a few steps need to be done.
	 * <ul>
	 * 	<li>clean the popFit first</li>
	 * 	<li>execute fitness function, it will return an ArrayList<double[]> </li>
	 * 	<li>initialize the ranking, in the double[1] </li>
	 *  <li>add fitness value with its initialized ranking to the popFit</li>
	 * </ul>
	 */
	public void evaluate(Chromosome[] popVar, ArrayList<double[]> popFit) {
		// must clean the previous popFit first, otherwise the popFit list will be incremental.
		popFit.clear();
		
		// get fitness function list, just in case you have multiple objective functions.
		FitnessFunc fitnessFunction = funcList.get(0);
		ArrayList<double[]> tempFit = null;
		try {
			// execute the evaluation
			tempFit = fitnessFunction.execute(popVar);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(int i = 0; i < tempFit.size(); i++) {
			popFit.add(tempFit.get(i));
		}
	}
}
