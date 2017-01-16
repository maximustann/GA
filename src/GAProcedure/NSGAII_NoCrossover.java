package GAProcedure;

import java.util.ArrayList;

import algorithms.Chromosome;
import algorithms.GeneticAlgorithm;
import wsrp.ResultCollector;

public abstract class NSGAII_NoCrossover extends GeneticAlgorithm {
	public void run(int seed){
		collector.collectTime(0);
		initializeRand(seed);
		popVar = initPop.init(popSize, maxVar, lbound, ubound);

		for(int i = 0; i < maxGen; i++){
			ArrayList<double[]> nonDominatedSet = new ArrayList<double[]>();
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

			constraint.evaluate(popVar, popFit);
			sort.sort(popVar, popFit);
			distance.calculate(popVar, popFit);
//			for(int j = 0; j < popSize; j++){
//				System.out.println("costFitness = " + popFit.get(j)[0] 
//						+ ", EnergyFitness = " + popFit.get(j)[1]
//						+ ", index = " + popFit.get(j)[2]
//						+ ", CD = " + popFit.get(j)[3]
//						+ ", ranking = " + popFit.get(j)[4]
//						+ ", violations = " + popFit.get(j)[5]);
//			}
//
//			int flag = 1;
//			while(flag == 1){}
			
			int count = 0;
			// copy the fitnesses of nonDominated individuals into nonDominatedSet
			while(true){
				if(popFit.get(count)[4] == 0){
					nonDominatedSet.add(popFit.get(count).clone());
					count++;
				} else{
					break;
				}
			}
			collector.collect(nonDominatedSet);
//			((ResultCollector) collector).printResult();
			
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
			
			for(int j = 0; j < popSize; j++){
				combinedPop[j] = newPop[j].clone();
				combinedPopFit.add(newPopFit.get(j));
			}
			for(int j = popSize, k = 0; j < popSize * 2; j++, k++){
				combinedPop[j] = popVar[k].clone();
				// you will have to adjust the index in the fitness, 
				// otherwise it will mess things up
				popFit.get(k)[2] = j;
				combinedPopFit.add(popFit.get(k));
			}

			sort.sort(combinedPop, combinedPopFit);	
			distance.calculate(combinedPop, combinedPopFit);
			popFit.clear();
			// elitism
			for(int j = 0; j < popSize; j++){
				popVar[j] = combinedPop[j].clone();
				popFit.add(combinedPopFit.get(j));
				// adjust the index
				popFit.get(j)[2] = j;
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
