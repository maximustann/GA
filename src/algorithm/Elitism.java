/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * Elitism.java - an interface of elitism operator, please implement this interface.
 */
package algorithm;
/**
 * An abstraction of elitism.
 * @author Boxiong Tan (Maximus Tann) 
 * @since GA framework 1.0
 */
public abstract class Elitism {
	/** Carry over elitSize of chromosomes to next generation */
	protected int elitSize = -1;
	/** Carry over elitPercent of chromosomes to next generation */
	protected double elitPercent = -1;
	/** Optimization direction */
	protected int optimization;
	
	/** ElitSize as the parameter of the constructor */
	public Elitism(int elitSize, int optimization){
		this.elitSize = elitSize;
		this.optimization = optimization;
	}
	/**  ElitPercent as the parameter of the constructor */
	public Elitism(double elitPercent, int optimization){
		this.elitPercent = elitPercent;
		this.optimization = optimization;
	}
	
	
	/** 
	 * carry over a number of chromosomes to the next generation 
	 * according to elitSize or elitPercent
	 */
	public abstract void carryover(Chromosome[] popVar, Chromosome[] newPop);
	/**
	 * @return elitSize the number of carryover chromosomes, return -1 if this value has not
	 * been set.
	 */
	public int getSize(){
		return elitSize;
	}

	/**
	 * @return elitPercent the percent of carryover chromosomes, return -1 if this value has not
	 * been set.
	 */
	public double getPercent(){
		return elitPercent;
	}
}
