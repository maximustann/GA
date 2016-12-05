/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective Genetic algorithm framework
 * Description:  Single-objective Genetic algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * BinaryFlipCoinMutation.java - An implementation of int mutation
 */
package commonOperators;

import algorithms.Chromosome;
import algorithms.Mutation;
import algorithms.StdRandom;
import commonRepresentation.IntValueChromosome;

/**
 * The most commonly used mutation. It flips a binary value chromosome.
 * @author Boxiong Tan (Maximus Tann)
 * @since GA framework 1.0
 */
public class BinaryFlipCoinMutation implements Mutation{

	/**
	 * If a variable of a chromosome equals 0, then, with a mutationRate of probability, flip
	 * this variable to 1. visa versa.
	 */
	@Override
	public void update(Chromosome individual, double mutationRate) {
		int chromoSize = individual.size();
		for(int i = 0; i < chromoSize; i++){
			if(StdRandom.uniform() <= mutationRate){
				if(((IntValueChromosome) individual).individual[i] == 1){
					((IntValueChromosome) individual).individual[i] = 0;
				} else {
					((IntValueChromosome) individual).individual[i] = 1;
				}
			}
		}
	}

}
