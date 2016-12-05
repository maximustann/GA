/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * Elitism.java - an interface of elitism operator, please implement this interface.
 */
package algorithms;
/**
 * An abstraction of elitism.
 * @author Boxiong Tan (Maximus Tann) 
 * @since GA framework 1.0
 */
public abstract class Elitism {
	/** Carry over an elitSize of chromosomes to the next generation */
	protected int elitSize = -1;

	/** Carry over an elitPercent of chromosomes to the next generation */
	protected double elitPercent = -1;
	
	/** Optimization direction, 0 indicates minimization, 1 indicates maximization */
	protected int optimization;
	
	/** constructor 
	 * @param elitSize the size of population that will be carried over to the next generation
	 * @param optimization Optimization direction
	 * */
	public Elitism(int elitSize, int optimization){
		this.elitSize = elitSize;
		this.optimization = optimization;
	}
	
	
	/**  constructor 
	 * @param elitPercent the percentage of population that will be carried over to the next generation
	 * @param optimization Optimization direction
	 * */
	public Elitism(double elitPercent, int optimization){
		this.elitPercent = elitPercent;
		this.optimization = optimization;
	}
	
	
	/** 
	 * carry over a number of chromosomes to the next generation 
	 * according to the elitSize or the elitPercent.
	 */
	public abstract void carryover(Chromosome[] popVar, Chromosome[] newPop);
	
	
	/**
	 * @return elitSize the number of chromosomes that will be carried over to the next generation, 
	 * 					return -1 if this value has not been set.
	 */
	public int getSize(){
		return elitSize;
	}

	/**
	 * @return elitPercent the percentage of chromosomes that will be carried over to the next generation, 
	 * 					return -1 if this value has not been set.
	 */
	public double getPercent(){
		return elitPercent;
	}
}
