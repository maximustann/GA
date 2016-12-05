/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective Genetic algorithm framework
 * Description:  Single-objective Genetic algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * SimulatedBinaryCrossover.java - A real value crossover method
 */
package commonOperators;

import algorithms.Chromosome;
import algorithms.StdRandom;
import algorithms.TwoParentsCrossover;
import commonRepresentation.RealValueChromosome;

/**
 * A real value mutation method
 * @see <a href="http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.33.7291&rep=rep1&type=pdf">SBX original paper</a>
 * @author Boxiong Tan (Maximus Tann)
 * @since GA framework 1.0
 */

public class SimulatedBinaryCrossover implements TwoParentsCrossover {
	/** distributionIndex is set to a fixed number 1 according to the above paper */
	private double distributionIndex = 1;

	/**
	 * please check the link above for the detailed description.
	 * SBX steps:
	 * <ul>
	 *	<li> For each variable, if a random number smaller than crossoverRate, then we apply crossover </li>
	 *  <li> create a random number u between 0 and 1</li>
	 *  <li> calculate a beta using a polynomial probability distribution</li>
	 *  <li> apply crossover, child1 = 0.5 * ((father + mother) - beta * abs(father - mother))</li>
	 *  <li> apply crossover, child2 = 0.5 * ((father + mother) + beta * abs(father - mother))</li>
	 * </ul>
	 * @param father
	 * @param mother
	 * @param crossoverRate a crossoverRate
	 */
	@Override
	public Chromosome[] update(Chromosome father, Chromosome mother, double crossoverRate) {
		int chromoSize = father.size();
		RealValueChromosome[] children = new RealValueChromosome[2];
		double beta;

		RealValueChromosome child1 = new RealValueChromosome(chromoSize);
		RealValueChromosome child2 = new RealValueChromosome(chromoSize);
		for(int i = 0; i < chromoSize; i++){
			double randomNum = StdRandom.uniform();
			if(randomNum < crossoverRate){
				double u = StdRandom.uniform();
				if(u <= 0.5){
					beta = Math.pow((2 * u), (1 / (distributionIndex + 1)));
				} else {
					beta = Math.pow((1 / (2 * (1 - u))), (1 / (distributionIndex + 1)));
				}
				double process1 = (((RealValueChromosome) father).individual[i] +
								    ((RealValueChromosome) mother).individual[i]);

				double process2 =  (((RealValueChromosome) father).individual[i] -
								    ((RealValueChromosome) mother).individual[i]);

				if(process2 < 0) process2 = - process2;
				child1.individual[i] = 0.5 * (process1 + beta * process2);
				child2.individual[i] = 0.5 * (process1 - beta * process2);
			} 
		else {
				child1.individual[i] = ((RealValueChromosome) father).individual[i];
				child2.individual[i] = ((RealValueChromosome) mother).individual[i];
			}
		}
		children[0] = child1;
		children[1] = child2;
		return children;
	}

}
