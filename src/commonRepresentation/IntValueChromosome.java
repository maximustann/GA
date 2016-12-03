/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective Genetic algorithm framework
 * Description:  Single-objective Genetic algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * IntValueChromosome.java - A real value chromosome representation
 */

package commonRepresentation;


import algorithm.Chromosome;
import algorithm.Gene;
/**
 * @author Boxiong Tan (Maximus Tann) 
 * @since GA framework 1.0
 */
public class IntValueChromosome extends Chromosome{
	/** We just want to inherent the type. Don't even need to encapsulate. 
	 */
	public int [] individual;
	public int [][] matrixIndividual;
	
	/**
	 * Constructor
	 * @param size
	 */
	public IntValueChromosome(int size){
		individual = new int[size];
	}
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
	public IntValueChromosome(Gene firstPart, Gene secondPart){
		individual = new int[firstPart.size() + secondPart.size()];
		for(int i = 0; i < firstPart.size(); i++) {
			individual[i] = ((IntGene) firstPart).gene[i];
		}
		for(int i = firstPart.size(), j = 0; j < secondPart.size(); j++, i++) {
			individual[i] = ((IntGene)secondPart).gene[j];
		}	
	}
	
	/** get the size of chromosome */
	public int size() {
		return individual.length;
	}
	
	/**  cut method 
	 *  @param cutPoint where to cut
	 *  @param geneIndicator first half (0) or second half (1)
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

	@Override
	/**
	 * Print in one line
	 */
	public void print() {
		for(int i = 0; i < size(); i++){
			System.out.print(individual[i] + " ");
		}
	}
	
	/**
	 * Print matrix form
	 */
	public void printMatrix(){
		int rowNum = matrixIndividual.length;
		int colNum = matrixIndividual[0].length;
		for(int i = 0; i < rowNum; i++){
			for(int j = 0; j < colNum; j++){
				System.out.print(matrixIndividual[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	/**
	 * Transit to matrix representation
	 */
	public void toMatrix(int row){
		int chromoSize = size();
		int col = chromoSize / row;
		matrixIndividual = new int[row][col];
		for(int i = 0; i < row; i++){
			for(int j = 0; j < col; j++){
				matrixIndividual[i][j] = individual[i * col + j];
			}
		}
	}
	/**
	 * Get an exact copy of current chromosome
	 */
	public IntValueChromosome clone() {
		IntValueChromosome copy = new IntValueChromosome(size());
		for(int i = 0; i < size(); i++){
			copy.individual[i] = individual[i];
		}
		return copy;
	}
	
	private boolean equals(IntValueChromosome chromo){
		int chromoSize = size();
		for(int i = 0; i < chromoSize; i++){
			if(individual[i] != chromo.individual[i]){
				return false;
			}
		}
		return true;
	}
	@Override
	public boolean equals(Chromosome chromos) {
		return equals((IntValueChromosome) chromos);
	}
}
