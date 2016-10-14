package commonOperators;

import algorithm.Gene;

public class DoubleGene implements Gene {
	public double[] gene;

	@Override
	public int size() {
		return gene.length;
	}
}
