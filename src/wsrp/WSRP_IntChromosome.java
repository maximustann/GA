package wsrp;

import algorithms.Gene;
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
			if(((i + 1) % 3 == 0) && ((i + 1) != 0)) System.out.print(" | ");
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


}
