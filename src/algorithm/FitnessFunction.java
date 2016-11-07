/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * FitnessFunction.java - An interface of fitnessFunction, please implement this interface
 * when you define your fitness functions.
 */
package algorithm;

import java.util.ArrayList;
/**
 * An interface of fitnessFunction, please implement this interface
 * when you define your fitness functions.
 * @author Boxiong Tan (Maximus Tann) 
 * @since GA framework 1.0
 */
public interface FitnessFunction {
	public ArrayList<double[]> normalizedFit(Chromosome [] popVar);
	public ArrayList<double[]> unNormalizedFit(Chromosome [] popVar);
}
