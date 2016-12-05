/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective Genetic algorithm framework
 * Description:  Single-objective Genetic algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * InitBinaryChromosome.java - A binary type initialization implementation
 */
package commonOperators;

import commonRepresentation.IntValueChromosome;

/**
 * Initialization of population for binary GA
 * 
 * @author Boxiong Tan (Maximus Tann) 
 * @since GA framework 1.0
 */
public class InitBinaryChromosome extends InitIntChromosomes {

	/**
	 * binary chromosome is a subtype of int value chromosome, hence, we extends
	 * int value init method
	 * @param popSize 		population size
	 * @param maxVar 		size of a chromosome
	 * @param lbound 		override by 0
	 * @param ubound			override by 1
	 * @return
	 */
	public IntValueChromosome[] init(
								int popSize, 
								int maxVar, 
								double lbound, 
								double ubound
								){
		lbound = 0;
		ubound = 2;
		return super.init(popSize, maxVar, lbound, ubound);
	}
}
