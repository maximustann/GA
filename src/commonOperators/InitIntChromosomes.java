/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective Genetic algorithm framework
 * Description:  Single-objective Genetic algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * InitIntChromosomes.java - A int type initialization implementation
 */
package commonOperators;

import algorithm.*;

/**
 * Initialization of population for Int GA
 * 
 * @author Boxiong Tan (Maximus Tann) 
 * @since GA framework 1.0
 */
public class InitIntChromosomes implements InitPop{
    /**
     * Generate an array of chromosomes, 
     * 
     * @param popSize population size
     * @param maxVar the number of variable of a chromosome
     * @param lbound the lower boundary of a variable of a chromosome
     * @param ubound the upper boundary of a variable of a chromosome
     * @return array of population variables
     */	
	@Override
	public IntValueChromosome[] init(
						int popSize, 
						int maxVar, 
						double lbound, 
						double ubound
						) {
		IntValueChromosome[] popVar = new IntValueChromosome[popSize];
		
		// initialize population
		for(int i = 0; i < popSize; i++){
			popVar[i] = new IntValueChromosome(maxVar);
			for(int j = 0; j < maxVar; j++){
				popVar[i].individual[j] = StdRandom.uniform((int) lbound, (int) ubound);
			}
		}
		return popVar;
	}
	
}
