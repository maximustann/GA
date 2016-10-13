/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * SinglePointCrossover.java - an implementation of crossover operator
 */
package commonOperators;

import algorithm.Chromosome;
import algorithm.TwoParentsCrossover;
/**
 * Single point crossover
 * 
 * @author Boxiong Tan (Maximus Tann) 
 * @since GA framework 1.0
 */
public class SinglePointCrossover implements TwoParentsCrossover {
    /**
     * update the Global best according to all the personal bests
     * 
     * @param father The selected chromosomes that involved in crossover.
     * @param mother The selected chromosomes that involved in crossover.
     * @param crossoverRate the probability of crossover.
     * @return An array of chromosome children
     */
	@Override
	public Chromosome[] update(
						Chromosome father, 
						Chromosome mother, 
						double crossoverRate
						) {
		return null;
	}

	
}
