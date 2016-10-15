/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * FitnessFunc.java - An abstract of common fitness function.
 */

package algorithm;

import java.util.ArrayList;

/**
 *  For writing any Fitness Functions this class should be extend.
 */
public abstract class FitnessFunc implements FitnessFunction{
	
	/** A normalization method */
	public Normalize normalize;
	
    /**
     * Creates a new FitnessFunc object.
     * @param normalize
     */
	public FitnessFunc(Normalize normalize){
		this.normalize = normalize;
	}
	
    /**
     * Generate an array of normalized fitness values
     * Two steps: 1. apply fitness function on popVar
     * 			  2. apply normalization function on fitness values
     *
     * @param popVar population variables
     * @return array of normalized fitness values
     */
	@Override
	public ArrayList<double[]> normalizedFit(Chromosome [] popVar) {
		ArrayList<double[]> fitness = unNormalizedFit(popVar);
		normalize.doNorm(fitness);
		return fitness;
	}

    /**
     * Generate an array of unnormalized fitness values
     * Apply fitness function on popVar
     *
     * @param popVar population variables
     * @return array of unnormalized fitness values, which is often used 
     * 			by normalizedFit()
     */
	public abstract ArrayList<double[]> unNormalizedFit(Chromosome [] popVar);

}
