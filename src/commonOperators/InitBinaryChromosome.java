package commonOperators;

import algorithm.Chromosome;
import algorithm.InitPop;
import algorithm.StdRandom;

public class InitBinaryChromosome implements InitPop{

	@Override
	public IntValueChromosome[] init(
					int popSize, 
					int maxVar, 
					double lbound, 
					double ubound
					) {
		IntValueChromosome[] popVar = new IntValueChromosome[popSize];
		for(int i = 0; i < popSize; i++){
			popVar[i] = new IntValueChromosome(maxVar);
			for(int j = 0; j < maxVar; j++){
				// The uniform() will return an integer value
				popVar[i].individual[j] = StdRandom.uniform((int) lbound, (int) ubound); 
			}
		}
		return popVar;
	}

}
