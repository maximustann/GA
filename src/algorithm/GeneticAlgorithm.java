/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * GeneticAlgorithm.java - An abstract of Genetic algorithm. It does not contain the 
 * 							implementation of the algorithm
 */

package algorithm;

/**
 * The abstraction of GA
 * 
 * @author Boxiong Tan (Maximus Tann) 
 * @since GA framework 1.0
 */
import algorithm.StdRandom;
import dataCollector.DataCollector;

public abstract class GeneticAlgorithm {
	/** An InitPop object for initialization of population */
	protected InitPop initPop;

	/** An Evaluation object for Evaluation of fitness */
	protected Evaluate evaluate;

	/** An DataCollector object for collect result */
	protected DataCollector collector;

	/** an array of population variables, the size of array is the population size */	
	 protected Chromosome [] popVar;

	// Problem related parameter settings
	/** if optimization == 1, the algorithm maximize the fitness value 
	 *  else, the algorithm minimize the fitness value */
	protected int optimization;
	
	/**
	 * The probability of mutation
	 */
	protected double mutationRate;
	
	/**
	 * The probability of crossover
	 */
	protected double crossoverRate;
	
	/** size of population */
	protected int popSize;
	
	/** size of maximum generation */
	protected int maxGen;

    /**
     * Instead of using a constructor, we use a prepare() which do the 
     * initialization of GA including assignment of values, 
     * initialization of arrays.
     * This should be extended and accomplished in the subclass.
     */
	protected abstract void prepare();

    /**
     * Initialization of random generator.
     */
	public void initializeRand(int seed){
		StdRandom.setSeed(seed);
	}

    /**
     * The actual process of GA, but we do not implement the process here. 
     * @param seed the random seed
     */
	public abstract void run(int seed);

}
