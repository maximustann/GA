package GAProcedure;
import algorithm.Chromosome;
import algorithm.GeneticAlgorithm;
public abstract class CommonGA extends GeneticAlgorithm{

	public void run(int seed){
		initializeRand(seed);
		popVar = initPop.init();

		for(int i = 0; i < maxGen; i++){
			evaluate.evaluate(popVar, popFit);
			Chromosome father = selection.selected(popVar, popFit);
			Chromosome mother = selection.selected(popVar, popFit);
			
			crossover.
			
			
			collector.collect(gBestFit);
			collector.collectParticle(popVar);
		}
	}

	@Override
	protected abstract void prepare();
}
