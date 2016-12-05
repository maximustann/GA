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
import algorithms.*;
import commonRepresentation.IntValueChromosome;
/**
 * AllocationParameterSettings for Hai's Paper
 *
 * @author Boxiong Tan (Maximus Tann)
 * @author Hai Huang
 * @since GA framework 1.0
 */
public class GAHaiCostFitness extends UnNormalizedFit{
	private static double[] costMatrix;

	
	public GAHaiCostFitness(Chromosome individual){
		super(individual);
	}

	/**
	 * 
	 * @param con a user defined constraint, in this case user define a max cost constraint
	 * @param costMatrix cost matrix, read from file, generate from normal distribution [20,100]
	 */
	public GAHaiCostFitness(double[] costMatrix){
		super(null);
		GAHaiCostFitness.costMatrix = costMatrix;
	}
	

	public Object call() throws Exception {
		double[] fit = new double[2];
		int maxVar = individual.size();
		for(int j = 0; j < maxVar; j++){
			 fit[0] += costMatrix[j] * ((IntValueChromosome) individual).individual[j];
		}
		fit[1] = 0;
		return fit;
	}


}
