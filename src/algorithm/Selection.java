/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * Selection.java - an interface of selection operator. Please implement this interface.
 */

package algorithm;

import java.util.ArrayList;

/**
 * An interface of selection operator
 * 
 * @author Boxiong Tan (Maximus Tann) 
 * @since GA framework 1.0
 */
public interface Selection {
    /**
     * selection process
     * 
     * @param popVar whole population
     * @param fitness the fitness of whole population
     * @return return a selected chromosome
     */	
	public Chromosome selected (
			Chromosome [] popVar,
			ArrayList<Double[]> fitness
			);
}
