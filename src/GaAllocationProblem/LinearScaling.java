/*
 * Boxiong Tan (Maximus Tann)
 * Title:        PSO algorithm framework
 * Description:  PSO algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * LinearScaling.java - a normalization method
 */
package GaAllocationProblem;

import algorithms.Normalize;
/**
*
* @author Boxiong Tan (Maximus Tann)
* @since PSO framework 1.0
*/
public class LinearScaling implements Normalize{
	private double max, min;
	
	/**
	 * maximum and minimum values are needed
	 * @param max the maximum value
	 * @param min the minimum value
	 */
	public LinearScaling(double max, double min){
		this.max = max;
		this.min = min;
	}

	/**
	 * normalize all fitness values
	 * @param fitness unnormalized fitness values
	 */
	public double[] doNorm(double[] fitness){

		for(int i = 0; i < fitness.length; i++){
			fitness[i] = (fitness[i] - min) / (max - min);
		}
		return fitness;
	}
}
