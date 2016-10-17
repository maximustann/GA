/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective Genetic algorithm framework
 * Description:  Single-objective Genetic algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * PolyMutation.java - A real value mutation method
 */
package commonOperators;

import algorithm.Chromosome;
import algorithm.Mutation;
import algorithm.StdRandom;

/**
 * A real value mutation method
 * @see <a href="http://www.mlahanas.de/MOEA/EAs.htm">Polynomial mutation</a>
 * @author Boxiong Tan (Maximus Tann)
 * @since GA framework 1.0
 */

public class PolyMutation implements Mutation {
	private double lbound, ubound;

	/**
	 * Constructor
	 * @param lbound lower bound of a variable
	 * @param ubound upper bound of a variable
	 */
	public PolyMutation(double lbound, double ubound){
		this.lbound = lbound;
		this.ubound = ubound;
	}
	/**
	 * please check the link above for the detailed description.
	 * polynomial mutation steps:
	 * <ul>
	 * 		<li> 1. randomly generate a number: u</li>
	 * 		<li> if u <= 0.5, calculate L, update current variable </li>
	 * 		<li> else u > 0.5, calculate R, update current variable </li>
	 * </ul>
	 * @param popVar population
	 * @param mutationRate It is not actually a "rate". It is a value which is suggested choose from [20, 100]
	 */
	public void update(Chromosome[] popVar, double mutationRate) {
		int popSize = popVar.length;
		for(int i = 0; i < popSize; i++){
			polynomialMutation((RealValueChromosome) popVar[i], mutationRate);
		}
	}

	/**
	 * @param individual an individual
	 * @param mutationRate
	 */
	private void polynomialMutation(RealValueChromosome individual, double mutationRate){
		double L, R, u;
		u = StdRandom.uniform();
		for(int j = 0; j < individual.size(); j++) {
			if(u <= 0.5) {
				L = Math.pow((2 * u), (1 / (1 + mutationRate))) - 1.0;
				individual.individual[j] = individual.individual[j] + L * (individual.individual[j] - lbound);
			} else {
				R = 1 - Math.pow((2 * (1 - u)), (1 / (1 + mutationRate)));
				individual.individual[j] = individual.individual[j] + R * (ubound - individual.individual[j]);
			}
		}
	}

}
