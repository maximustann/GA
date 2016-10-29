/*
 * Boxiong Tan (Maximus Tann)
 * Title:        GA framework
 * Description:  GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * BPSOHaiCostFitness.java - cost fitness from Hai's Paper
 */
package GaAllocationProblem;
import java.util.ArrayList;
import algorithm.*;
import commonOperators.IntValueChromosome;
/**
 * AllocationParameterSettings for Hai's Paper
 *
 * @author Boxiong Tan (Maximus Tann)
 * @author Hai Huang
 * @since GA framework 1.0
 */
public class GAHaiCostFitness extends FitnessFunc{
	private double[] costMatrix;
	Constraint con;

	/**
	 * 
	 * @param normalize a user defined normalization method
	 * @param con a user defined constraint, in this case user define a max cost constraint
	 * @param costMatrix cost matrix, read from file, generate from normal distribution [20,100]
	 */
	public GAHaiCostFitness(Normalize normalize, Constraint con, double[] costMatrix){
		super(normalize);
		this.con = con;
		this.costMatrix = costMatrix;
	}

	/**
	 * Simply sum all the deployed services
	 */
	public ArrayList<double[]> unNormalizedFit(Chromosome[] popVar){
		int popSize = popVar.length;
		int maxVar = popVar[0].size();
		ArrayList<double[]> fitness = new ArrayList<double[]>();
		for(int i= 0; i < popSize; i++){
			double[] fit = new double[2];
			fit[1] = i;
			for(int j = 0; j < maxVar; j++){
				 fit[0] += costMatrix[j] * ((IntValueChromosome) popVar[i]).individual[j];
			}
			fitness.add(fit);
		}
		return fitness;
	}

	/**
	 * 
	 * Steps:
	 * <ul>
	 * 	<li>1. calculate fitness values</li>
	 * 	<li>2. normalize fitness values</li>
	 * 	<li>3. punish fitness values</li>
	 * </ul>
	 */
	public ArrayList<double[]> normalizedFit(Chromosome[] popVar){
		ArrayList<double[]> fitness = unNormalizedFit(popVar);
		normalize.doNorm(fitness);
		fitness = con.punish(popVar, fitness);
		return fitness;

	}

}
