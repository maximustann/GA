package algorithms;


import java.util.ArrayList;

import algorithms.*;
import dataCollector.DataCollector;

public abstract class Coevolution {

    /**
     * Everything is array instead of one.
     */
    /**
     * Number of sub-populations
     */
    protected int numOfSubPop;

    /** An Evaluation object for Evaluating fitness */
    protected CoEvaluate evaluate;

    /**
     * An InitPop object for initializing a population
     */
    protected InitPop[] initPops;

    /** selection operators */
    protected Selection[] selections;

    /** Crossover operators */
    protected Crossover[] crossovers;

    /** mutation operators */
    protected Mutation[] mutations;

    /** Elitism operators */
    protected Elitism[] elitisms;

    /** Constraint methods */
    protected Constraint[] constraints;

    /** distance methods */
    protected Distance[] distances;

    /** DataCollector object for collecting results */
    protected DataCollector collector;






    /** a double array of population variables,
     * the first entry is the index of sub-population
     * and the second entry is the population size */
    protected Chromosome[][] popVars;

    /**
     * Representative is the best individual in a sub-population.
     * You can think of representatives as pBest in PSO.
     */
    protected Chromosome[] representatives;

    /**
     * An array of double, each entry stands for the fitness value of a sub-population
     */
    protected double[] repFits;

    /** An array of List, each list includes
     * fitness values of that sub-population,
     * e.g ArrayList<double[]>,
     * A list of [fitness values, their rankings] */
    protected ArrayList<double[]>[] popFits;

    protected  Sort[] sorts;

    /**
     * optimization = 0: minimization
     * 1: maximization
     */
    protected int optimization;

    protected double[] mutationRates;
    protected int[] elitSizes;

    protected int[] tournamentSizes;
    protected double[] crossoverRates;

    /** populations' sizes */
    protected int[] popSizes;

    protected int maxGen;

    /** chromosome size */
    protected int[] maxVars;

    /** the upper and lower boundaries of a variable*/
    protected double[] lbounds;
    protected double[] ubounds;

    /**
     * Instead of using a constructor, we use a prepare() which does the
     * initialization of GA including assignment of values,
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
     * This run is quite different with the orginal GA, therefore, it needs to
     * be re-implemented later on.
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
