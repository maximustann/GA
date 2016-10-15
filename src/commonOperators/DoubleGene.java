/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective Genetic algorithm framework
 * Description:  Single-objective Genetic algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * DoubleGene.java - A double type gene implementation
 */
package commonOperators;

import algorithm.Gene;
/**
 * @author Boxiong Tan (Maximus Tann) 
 * @since GA framework 1.0
 */
public class DoubleGene implements Gene {
	/** actual content */
	public double[] gene;

	/** Constructor 
	 * @param size the size of gene 
	 */	
	public DoubleGene(int size){
		gene = new double[size];
	}

	@Override
	/** get size of gene */
	public int size() {
		return gene.length;
	}
}
