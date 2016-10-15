/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * Normalize.java - An interface of normalization
 */
package algorithm;

import java.util.ArrayList;

/**
 * Normalization function
 * 
 * @author Boxiong Tan (Maximus Tann) 
 * @since GA framework 1.0
 */

public interface Normalize {
	
    /**
     * Normalize the given fitness values 
     * 
     * @param fitness the array of fitness values
     * @return an array of normalized fitness values
     */
	public ArrayList<double[]> doNorm(ArrayList<double[]> fitness);
}
