package algorithm;

import algorithm.StdRandom;
import dataCollector.DataCollector;

public abstract class GeneticAlgorithm {
	protected InitPop initPop;
	protected Evaluate evaluate;
	protected DataCollector collector;

	protected double[][] popVar;
	// Problem related parameter settings
	protected int optimization;
	protected int popSize;
	protected int maxGen;

	protected abstract void prepare();

	// Initialize random
	public void initializeRand(int seed){
		StdRandom.setSeed(seed);
	}

	// Run the algorithm
	public abstract void run(int seed);

}
