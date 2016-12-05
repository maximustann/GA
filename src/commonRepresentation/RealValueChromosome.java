/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective Genetic algorithm framework
 * Description:  Single-objective Genetic algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * RealValueChromosome.java - A real value chromosome representation
 */

package commonRepresentation;
import algorithms.Chromosome;
import algorithms.Gene;
/**
 *
 * @author Boxiong Tan (Maximus Tann)
 * @since GA framework 1.0
 */
public class RealValueChromosome extends Chromosome{
	public double [] individual;

	public RealValueChromosome(int size){
		individual = new double[size];
	}
	@Override
	/** get the size of chromosome */
	public int size() {
		return individual.length;
	}


	/**  cut method
	 *  @param cutPoint where to cut
	 *  @param geneIndicator first half (0) or second half (1) 
	 *  @return return gene part
	 */
	@Override
	public Gene cut(int cutPoint, int geneIndicator) {
		DoubleGene part;
		if(geneIndicator == 0) {
			part = new DoubleGene(cutPoint + 1);
			for(int i = 0; i < cutPoint + 1; i++){
				part.gene[i] = individual[i];
			}
		} else {
			part = new DoubleGene(size() - (cutPoint + 1));
			for(int i = cutPoint + 1, j = 0; i < size(); i++, j++){
				part.gene[j] = individual[i];
			}
		}
		return part;
	}
	
	/**
	 * Print in one line
	 */
	public void print() {
		for(int i = 0; i < size(); i++){
			System.out.print(individual[i] + " ");
		}
		System.out.println();
	}
	
	/**
	 * return an exact copy
	 */
	public RealValueChromosome clone() {
		RealValueChromosome copy = new RealValueChromosome(size());
		for(int i = 0; i < size(); i++){
			copy.individual[i] = individual[i];
		}
		return copy;
	}

	/** compare with a target chromosome */
	private boolean equals(RealValueChromosome target){
		int chromoSize = size();
		for(int i = 0; i < chromoSize; i++){
			if(individual[i] != target.individual[i]){
				return false;
			}
		}
		return true;
	}
	@Override
	public boolean equals(Chromosome target) {
		return equals((RealValueChromosome) target);
	}
	
}
