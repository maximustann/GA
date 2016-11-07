/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * Sort.java - an interface of sort method. Please implement this interface.
 */
package algorithm;

import java.util.ArrayList;
/**
 * An interface of sort
 * 
 * @author Boxiong Tan (Maximus Tann) 
 * @since GA framework 1.0
 */
public interface Sort {
	/**
	 * @param popVar population
	 * @param popFit fitness
	 */
	public void sort(Chromosome[] popVar, ArrayList<double[]> popFit);
}
