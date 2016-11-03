package debugTools;

import algorithm.Chromosome;
import commonOperators.IntValueChromosome;

public class IntValueCompare implements ChromosomePairwiseComparison {

	private boolean compare(IntValueChromosome v1, IntValueChromosome v2) {
		int chromoSize = v1.size();
		for(int i = 0; i < chromoSize; i++){
			if(v1.individual[i] != v2.individual[i]){
				System.out.println("Error detected");
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean compare(Chromosome v1, Chromosome v2) {
		return compare((IntValueChromosome) v1, (IntValueChromosome) v2);
	}

	@Override
	public void comparePop(Chromosome[] pop1, Chromosome[] pop2) {
		int popSize = pop1.length;
		for(int i = 0; i < popSize; i++){
			if(!compare(pop1[i], pop2[i])){
				System.out.println("Error instance: " + i);
				pop1[i].print();
				System.out.println();
				pop2[i].print();
			}
		}

	}

}
