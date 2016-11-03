package debugTools;

import algorithm.Chromosome;

public interface ChromosomePairwiseComparison {
	public boolean compare(Chromosome v1, Chromosome v2);
	public void comparePop(Chromosome[] pop1, Chromosome[] pop2);
}
