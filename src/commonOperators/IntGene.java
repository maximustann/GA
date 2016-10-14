package commonOperators;

import algorithm.Gene;

public class IntGene implements Gene {
	public int[] gene;

	public IntGene(int size){
		gene = new int[size];
	}
	@Override
	public int size() {
		return gene.length;
	}
}
