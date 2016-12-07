/*
 * Boxiong Tan (Maximus Tann)
 * Title:        GA framework
 * Description:  GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * LinearScaling.java - a normalization method
 */
package GaAllocationProblem;

import java.util.ArrayList;

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
	 * @param fitness unnormalized fitness values, the inner array contains two item,
	 * fitness value and the ranking of the current chromosome. 
	 */
	@Override
	public ArrayList<double[]> doNorm(ArrayList<double[]> fitness){
		for(int i = 0; i < fitness.size(); i++){
			fitness.get(i)[0] = (fitness.get(i)[0] - min) / (max - min);
		}
		return fitness;
	}
}
