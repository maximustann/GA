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

import java.lang.reflect.InvocationTargetException;

import algorithm.Chromosome;
import algorithm.Gene;
import algorithm.StdRandom;
import algorithm.TwoParentsCrossover;
/**
 * Single point crossover
 * NOTICES: 
 * 		CHROMOSOME HAS TO HAVE A CONSTRUCTOR THAT CONTAINS TWO GENE TYPE PARAMETERS
 * @author Boxiong Tan (Maximus Tann) 
 * @since GA framework 1.0
 */
public class SinglePointCrossover implements TwoParentsCrossover {
    /**
     * update the Global best according to all the personal bests
     * Steps:
     * 0. If a random number greater than crossover rate, return the same parents.
     * 1. First get the sub-type of chromosome.
     * 2. Get the constructor in which it allows two genes to be composed
     * 3. Cut the chromosomes into pieces and reconstruct two children
     * 4. return an array of children
     * 
     * @param father The selected chromosome that involved in crossover.
     * @param mother The selected chromosome that involved in crossover.
     * @param crossoverRate the probability of crossover.
     * @return An array of chromosome children
     */
	@Override
	public Chromosome[] update(
					Chromosome father, 
					Chromosome mother, 
					double crossoverRate
					) {
		Chromosome[] children = new Chromosome[2];
		/* If random number greater than crossover rate. Do not crossover. */
		if(StdRandom.uniform() > crossoverRate) {
			children[0] = father.clone();
			children[1] = mother.clone();
			return children;
		}
		
		/* random choose cutPoint */
		int cutPoint = StdRandom.uniform(father.size());
		
		/* Get the chromosome's type */
		Class<? extends Chromosome> childrenType = father.getClass();
		try {
		/* Use the constructor to generate two children */
			Chromosome child1 = childrenType
									.getConstructor(Gene.class, Gene.class)
									.newInstance(
												father.cut(cutPoint, 0), 
												mother.cut(cutPoint, 1)
												);
			Chromosome child2 = childrenType
									.getConstructor(Gene.class, Gene.class)
									.newInstance(
												father.cut(cutPoint, 0), 
												mother.cut(cutPoint, 1)
												); 
			children[0] = child1;
			children[1] = child2;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return children;
	}

	
}
