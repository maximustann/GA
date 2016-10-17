/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective Genetic algorithm framework
 * Description:  Single-objective Genetic algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * IntReverseSequenceMutation.java - An implementation of int mutation
 */
package commonOperators;

import algorithm.Chromosome;
import algorithm.Mutation;
import algorithm.StdRandom;
/**
 * Please find detailed explanation in this paper:
 * Analyzing the Performance of Mutation Operators to Solve the Travelling Salesman Problem
 *
 * @author Boxiong Tan (Maximus Tann)
 * @since GA framework 1.0
 */
public class IntReverseSequenceMutation implements Mutation {

	@Override
	/**
	 * Steps:
	 * 	1.Check if a random number in [0, 1] less than mutationRate, if yes, mutate
	 * 	2. Do reverse sequence mutation
	 */
	public void update(Chromosome[] popVar, double mutationRate) {
		int popSize = popVar.length;

		for(int i = 0; i < popSize; i++){
			if(StdRandom.uniform() <= mutationRate) {
				reverseSequence((IntValueChromosome) popVar[i]);
			}
		}
	}

	/**
	 * Steps:
	 * <ul>
	 * 	<li> 1. randomly selected two points, startPoint < endPoint </li>
	 * 	<li> 2. reverse the sequence between these two points.</li>
	 * <ul>
	 *
	 * @param chromosome an individual
	 */
	private void reverseSequence(IntValueChromosome chromosome){
		int startPoint, endPoint;
		int chromoSize = chromosome.size();
		endPoint = StdRandom.uniform(0, chromoSize);
		startPoint = StdRandom.uniform(0, endPoint);

		int[] temp = new int[endPoint - startPoint];

		for(int i = startPoint, j = 0; i < endPoint; i++, j++)
			temp[j] = chromosome.individual[i];

		for(int i = startPoint, j = endPoint; i < endPoint; i++, j--){
			chromosome.individual[i] = temp[j];
		}
	}

}
