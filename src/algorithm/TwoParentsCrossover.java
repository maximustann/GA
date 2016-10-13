/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * TwoParentsCrossover.java - an interface of crossover operator, please implement this interface.
 */
package algorithm;

/**
 * An interface of crossover operator
 * 
 * @author Boxiong Tan (Maximus Tann) 
 * @since GA framework 1.0
 */
public interface TwoParentsCrossover extends Crossover {
    /**
     * update the Global best according to all the personal bests
     * 
     * @param mother a chromosome
     * @param father a chromosome
     * @param crossoverRate the probability of crossover.
     * @return An array of chromosome children
     */	
	public Chromosome[] update(
						Chromosome father,
						Chromosome mother,
						double crossoverRate
						);
}
