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

import algorithm.Chromosome;
import algorithm.Mutation;
import algorithm.StdRandom;

/**
 * Most commonly used mutation, flip coin mutation of an int value chromosome
 * @author Boxiong Tan (Maximus Tann)
 * @since GA framework 1.0
 */
public class BinaryFlipCoinMutation implements Mutation{

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
