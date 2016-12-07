/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective Genetic algorithm framework
 * Description:  Single-objective Genetic algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * IntGene.java - A int type gene implementation
 */
package commonRepresentation;

import algorithms.Gene;
/**
 * @author Boxiong Tan (Maximus Tann) 
 * @since GA framework 1.0
 */
public class IntGene implements Gene {
	/** actual content */
	public int[] gene;

	/** Constructor 
	 * @param size the size of gene 
	 */
	public IntGene(int size){
		gene = new int[size];
	}

	@Override
	/** get size of gene */
	public int size() {
		return gene.length;
	}
}
