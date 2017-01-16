/*
 * Boxiong Tan (Maximus Tann)
 * Title:        GA framework
 * Description:  GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * GAHaiEvaluate.java - evaluation for Hai's paper
 */
package wsrp;

import java.util.ArrayList;

import algorithms.Chromosome;
import algorithms.Evaluate;
import algorithms.FitnessFunc;
/**
 *
 * @author Boxiong Tan (Maximus Tann)
 * @since PSO framework 1.0
 */
public class WSRPEvaluate implements Evaluate{
	/** Evaluation function list */
	private ArrayList<FitnessFunc> funcList;
	
	/**
	 * 
	 * @param funcList Evaluation function list
	 * @param weights weight is used to balance two objectives, cost and response time
	 */
	public WSRPEvaluate(
						ArrayList<FitnessFunc> funcList){
		this.funcList = funcList;
	}

	public void setFuncList(ArrayList<FitnessFunc> funcList){
		this.funcList = funcList;
	}

	/**
	 * <ul>
	 * 	<li> 1. evaluate population with a list of fitness functions (two, in this case)</li>
	 * </ul>
	 */
	public void evaluate(Chromosome[] popVar, ArrayList<double[]> fitness){
		fitness.clear();
		ArrayList<ArrayList<double[]>> fitList = new ArrayList<ArrayList<double[]>>();
		
		// evaluate each objective
		for(int i = 0; i < funcList.size(); i++){
			ArrayList<double[]> tempFit = funcList.get(i).execute(popVar);
			fitList.add(tempFit);
		}

		for(int i = 0; i < popVar.length; i++){
			// fit[0]: costFitness, fit[1]: energyFitness, fit[2]: index, fit[3]: crowding distance
			// fit[4]: ranking, fit[5]: violations
			double[] fit = new double[6];
			fit[0] = fitList.get(0).get(i)[0];
			fit[1] = fitList.get(1).get(i)[0];
			fit[2] = i;
			fit[3] = 0;
			fit[4] = 0;
			fit[5] = 0;
			fitness.add(fit);
		}
	}
}
