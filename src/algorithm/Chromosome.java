/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective Genetic algorithm framework
 * Description:  Single-objective Genetic algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * Chromosome.java 	The interface of chromosome. You can define your own chromosome 
 * by implement this interface.
 */

package algorithm;
/**
 * An abstract class of chromosome
 * 
 * @author Boxiong Tan (Maximus Tann) 
 * @since GA framework 1.0
 */
public abstract class Chromosome {
	public Chromosome() {
		
	}
	public Chromosome(Gene firstPart, Gene secondPart)
	{
		
	}
	/** get size of chromosome */
	public abstract int size();
	
	/** cut method */
	public abstract Gene cut(int cutPoint, int geneIndicator);
	
}
