package wsrp;

import algorithms.Gene;
import commonRepresentation.IntGene;
import commonRepresentation.IntValueChromosome;
public class WSRP_IntChromosome extends IntValueChromosome {

	public WSRP_IntChromosome(Gene firstPart, Gene secondPart) {
		super(firstPart, secondPart);
	}

	public WSRP_IntChromosome(int size){
		super(size);
	}

	@Override
	/**
	 * Print in one line
	 */
	public void print() {
		for(int i = 0; i < size(); i++){
			System.out.print(individual[i] + " ");
			if(((i + 1) % 2 == 0) && ((i + 1) != 0)) System.out.print(" | ");
		}
		System.out.println();
	}

	public WSRP_IntChromosome clone(){
		WSRP_IntChromosome copy = new WSRP_IntChromosome(size());
		for(int i = 0; i < size(); i++){
			copy.individual[i] = individual[i];
		}
		return copy;
	}
	
	public IntGene cut(int cutPoint, int geneIndicator){
		IntGene part;
		if(geneIndicator == 0){
			part = new IntGene(cutPoint + 1);
			for(int i = 0; i < cutPoint + 1; i++){
				part.gene[i] = individual[i];
			}
		} else{
			part = new IntGene(size() - (cutPoint + 1));
			for(int i = cutPoint + 1, j = 0; i < size(); i++, j++){
				part.gene[j] = individual[i];
			}
		}
		return part;
	}



}
