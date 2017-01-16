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

package algorithms;

import java.util.ArrayList;

import algorithms.StdRandom;
import dataCollector.DataCollector;

public abstract class GeneticAlgorithm {
	/** An InitPop object for initializing a population */
	protected InitPop initPop;

	/** An Evaluation object for Evaluating fitness */
	protected Evaluate evaluate;

	/** A selection operator */
	protected Selection selection;

	/** A Crossover operator */
	protected Crossover crossover;

	/** A Mutation operator */
	protected Mutation mutation;
	
	/** An Elitism operator */
	protected Elitism elitism;
	
	/** A Constraint method */
	protected Constraint constraint;
	
	/** distance method */
	protected Distance distance;

	/** An DataCollector object for collecting results */
	protected DataCollector collector;

	/** an array of population variables, the size of array is the population size */
	 protected Chromosome[] popVar;

	 /** An list of [fitness values,their rankings] */
	 protected ArrayList<double[]> popFit;
	 
	 /** A sort method */
	 protected Sort sort;

	// Problem related parameter settings
	/** if optimization == 1, the algorithm maximizes the fitness value
	 *  else, the algorithm minimizes the fitness value */
	protected int optimization;

	/**
	 * The probability of mutation
	 */
	protected double mutationRate;

	/**
	 * the size of the chromosomes that carried over to the next generation
	 */
	protected int elitSize;

	/** size of tournament */
	protected int tournamentSize;
	
	
	/** The probability of crossover */
	protected double crossoverRate;

	/** size of population */
	protected int popSize;

	/** size of maximum generation */
	protected int maxGen;

	/** chromosome size */
	protected int maxVar;

	/** the lower boundary of a variable of a chromosome */
	protected double lbound;

	/** the upper boundary of a variable of a chromosome */
	protected double ubound;

    /**
     * Instead of using a constructor, we use a prepare() which does the
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
	
	/**
	 * Repeat experiments for N times
	 * 
	 * @param seedStart the random seeds start from this seedStart, increasing 1 by each time.
	 * @param nTimes run experiments for nTimes
	 */
	public abstract void runNtimes(int seedStart, int nTimes);

}
