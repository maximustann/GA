/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective Genetic algorithm framework
 * Description:  Single-objective Genetic algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * Chromosome.java 	The interface of chromosome. You can define your own chromosome 
 * by implementing this interface.
 */

package algorithms;
/**
 * An abstract class of chromosome
 * 
 * @author Boxiong Tan (Maximus Tann) 
 * @since GA framework 1.0
 */
public abstract class Chromosome implements Cloneable{
	/** get the size of this chromosome */
	public abstract int size();
	
	/** cut method, "gene cut" is used in the crossover operator
	 * @param cutPoint indicates where to cut
	 * @param geneIndicator denotes which part of chromosome should it returns, 0 denotes
	 * the part before cutPoint, 1 denotes the part after cutPoint 
	 */
	public abstract Gene cut(int cutPoint, int geneIndicator);
	
	/** print current chromosome */
	public abstract void print();

	/** 
	 * get an extract copy of the current chromosome, 
	 * 
	 * NOTICE!!! MUST PROVIDE A DEEP COPY otherwise the methods depend on this method 
	 * will FAIL!!! Such as elitism, mutation. 
	 * 
	 */
	public abstract Chromosome clone();
	
	/** compare the current chromosome with target*/
	public abstract boolean equals(Chromosome target);
}
