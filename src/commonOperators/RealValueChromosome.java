/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective Genetic algorithm framework
 * Description:  Single-objective Genetic algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * RealValueChromosome.java - A real value chromosome representation
 */

package commonOperators;
import algorithm.Chromosome;
import algorithm.Gene;
/**
 * 
 * 
 * @author Boxiong Tan (Maximus Tann) 
 * @since GA framework 1.0
 */
public class RealValueChromosome extends Chromosome{
	/** We just want to inherent the type. Don't even need to encapsulate. 
	 * @param individual 
	 */
	public double [] individual;
	
	@Override
	public int size() {
		return individual.length;
	}

	@Override
	public Gene cut() {
		return null;
	}
}
