/*
 * Boxiong Tan (Maximus Tann)
 * Title:        PSO algorithm framework
 * Description:  PSO algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * Constraint.java - constraint functions for Hai's paper
 */
package GaAllocationProblem;
/**
*
* @author Boxiong Tan (Maximus Tann)
* @author Hai Huang
* @since PSO framework 1.0
*/
public class Constraint {
	private int noService;

	public Constraint(int noService){
		this.noService = noService;
	}

	/**
	 * Punish fitness values if the instance violates constraints
	 * If the instance violates a constraint, it will be set to maximum fitness value 1.
	 * @param popVar population
	 * @param fitness fitness values
	 * @return fitness fitness values after punishment
	 */
	public double[] punish(double[][] popVar, double[] fitness){
		int noLocation = popVar[0].length / noService;
		double[][] particle = new double[noService][noLocation];

		// transform vector into matrix
		for(int count = 0; count < popVar.length; count++){
			for(int i = 0; i < noService; i++){
				for(int j = 0; j < noLocation; j++){
					particle[i][j] = popVar[count][i * noService + j];
				}
			}

			// check if the service number constraint
			if(!checkService(particle)){
				fitness[count] = 1.0;
			}
		}
		return fitness;
	}

	/**
	 * 
	 * @param particle an instance
	 * @return boolean If the instance violates the constraint return false
	 */
	private boolean checkService(double[][] particle){
		for(int i = 0; i < particle.length; i++){
			if(rowSum(particle[i]) < 0.0001) return false;
		}
		return true;
	}
	
	/**
	 * rowSum
	 * @param row a deployment plan for a specific web service
	 * @return sum the count of the number of web services
	 */
	private double rowSum(double[] row){
		double sum = 0;
		for(int i = 0; i < row.length; i++) sum += row[i];
		return sum;
	}
}
