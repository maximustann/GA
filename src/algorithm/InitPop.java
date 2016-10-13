/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * InitPop.java - An interface of initialization of population
 */
package algorithm;

/**
 * Initialization of population
 * 
 * @author Boxiong Tan (Maximus Tann) 
 * @since GA framework 1.0
 */
public interface InitPop {
	
    /**
     * Generate an array of population, 
     * 
     * @param popSize population size
     * @param maxVar the number of variable of a particle
     * @param lbound the lower boundary of a variable of a chromosome
     * @param ubound the upper boundary of a variable of a chromosome
     * @return 2D-array of population variables
     */	
	public Chromosome[] init(
							int popSize, 
							int maxvar, 
							double lbound, 
							double ubound
							);
}
