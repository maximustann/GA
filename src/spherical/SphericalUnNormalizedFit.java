/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective Genetic algorithm framework
 * Description:  Single-objective Genetic algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * SphericalUnNormalizedFit.java an implementation of unNormalizedFit
 */
package spherical;

import algorithms.Chromosome;
import algorithms.UnNormalizedFit;
import commonRepresentation.RealValueChromosome;
/**
 * An example of unNormalizedFit
 * 
 * @author Boxiong Tan (Maximus Tann) 
 * @since GA framework 1.0
 */
public class SphericalUnNormalizedFit extends UnNormalizedFit{
	/**
	 * @param individual the chromosome that you want to evaluate.
	 */
	public SphericalUnNormalizedFit(Chromosome individual){
		super(individual);
	}
	
	
	@Override
	/**
	 * a call() function need to be implemented, because unNormalizedFit 
	 * implement the Callable interface.
	 */
	public Object call() throws Exception {
		int maxVar = individual.size();
		
		double fit = 0.0;
		for(int j = 0; j < maxVar - 1; j++){
			fit += ((RealValueChromosome) individual).individual[j] * 
				   ((RealValueChromosome) individual).individual[j];
		}
		// just initialize the ranking with 0, because this call() will be executed in thread, 
		// therefore it is hard to initialize ranking here.
		
		return new double[]{fit, 0};
	}
}
