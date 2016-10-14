/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective Genetic algorithm framework
 * Description:  Single-objective Genetic algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * IntValueChromosome.java - A real value chromosome representation
 */

package commonOperators;
import algorithm.Chromosome;
/**
 * @author Boxiong Tan (Maximus Tann) 
 * @since GA framework 1.0
 */
public class IntValueChromosome extends Chromosome{
	/** We just want to inherent the type. Don't even need to encapsulate. 
	 * @param individual 
	 */
	public int [] individual;

	/**
	 * Constructor
	 * We need two genes to construct a chromosome.
	 * In this case, gene is tight coupling with chromosome. You will have to manually 
	 * define chromosome and gene together. 
	 * 
	 * This is just an example of how to define a chromosome class.
	 * 
	 * @param firstPart
	 * @param secondPart
	 *
	 */
	public IntValueChromosome(IntGene firstPart, IntGene secondPart){
		individual = new int[firstPart.size() + secondPart.size()];
		for(int i = 0; i < firstPart.size(); i++) {
			individual[i] = firstPart.gene[i];
		}
		for(int i = firstPart.size(), j = 0; j < secondPart.size(); j++, i++) {
			individual[i] = secondPart.gene[j];
		}	
	}
	
	/** get the size of  */
	public int size() {
		return individual.length;
	}
	
	/**  cut method 
	 *  @param cutPoint where to cut
	 *  @param geneIndicator first half (0) or second half (1) ?
	 *  @return return gene part
	 */
	public IntGene cut(int cutPoint, int geneIndicator){
		IntGene part;
		if(geneIndicator == 0) {
			part = new IntGene(cutPoint + 1); 
			for(int i = 0; i < cutPoint + 1; i++){
				part.gene[i] = individual[i];
			}
		} else {
			part = new IntGene(size() - (cutPoint + 1));
			for(int i = cutPoint + 1, j = 0; i < size(); i++, j++){
				part.gene[j] = individual[i];
			}
		}
		return part;
	}
	
}
