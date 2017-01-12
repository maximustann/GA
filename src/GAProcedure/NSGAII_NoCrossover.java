package GAProcedure;

import java.util.ArrayList;

import algorithms.Chromosome;
import algorithms.GeneticAlgorithm;
import algorithms.TwoParentsCrossover;

public abstract class NSGAII_NoCrossover extends GeneticAlgorithm {
	public void run(int seed){
		collector.collectTime(0);
		initializeRand(seed);
		popVar = initPop.init(popSize, maxVar, lbound, ubound);

		for(int i = 0; i < maxGen; i++){
			int childrenCount = 0;
			ArrayList<double[]> newPopFit = new ArrayList<double[]>();
			ArrayList<double[]> combinedPopFit = new ArrayList<double[]>();
			Chromosome[] combinedPop = new Chromosome[popSize * 2];
			Chromosome[] newPop = new Chromosome[popSize];
			try {
				evaluate.evaluate(popVar, popFit);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			sort.sort(popVar, popFit);
//			collector.collect(popFit.get(0));
			while(true) {
				Chromosome child = popVar[selection.selected(popVar, popFit)];
				mutation.update(child, mutationRate);
				newPop[childrenCount] = child.clone();
				childrenCount++;
				if(childrenCount == popSize) break;
			}
			
			try {
				evaluate.evaluate(newPop, newPopFit);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for(int j = 0; j < popSize - 1; j++){
				combinedPop[j] = newPop[j].clone();
				combinedPopFit.add(newPopFit.get(j));
			}
			for(int j = popSize, k = 0; j < popSize * 2 - 1; j++, k++){
				combinedPop[j] = popVar[k].clone();
				combinedPopFit.add(popFit.get(k));
			}
			sort.sort(combinedPop, combinedPopFit);	
			
			// elitism
			for(int j = 0; j < popSize; j++){
				popVar[j] = combinedPop[j].clone();
				popFit.clear();
				popFit.add(combinedPopFit.get(j));
			}
		}
		collector.collectTime(1);
	}

	@Override
	protected abstract void prepare();
	
	/**
	 * Repeat experiment N times
	 */
	public void runNtimes(int seedStart, int nTimes) {
		for(int i = 0; i < nTimes; i++){
			run(seedStart);
			seedStart++;
		}
	}
}
