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
			ArrayList<Chromosome> nonDominatedSet = new ArrayList<Chromosome>();
			ArrayList<double[]> nonDominatedSetFit = new ArrayList<double[]>();
			int childrenCount = 0;
			ArrayList<double[]> newPopFit = new ArrayList<double[]>();
			ArrayList<double[]> combinedPopFit = new ArrayList<double[]>();
			Chromosome[] combinedPop = new Chromosome[popSize * 2];
			Chromosome[] newPop = new Chromosome[popSize];

			evaluate.evaluate(popVar, popFit);

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
			while(count < popFit.size()){
				if(popFit.get(count)[4] == 0){
					nonDominatedSetFit.add(popFit.get(count).clone());
					nonDominatedSet.add(popVar[(int) popFit.get(count)[2]].clone());
					count++;
				} else{
					break;
				}
			}
			collector.collectSet(nonDominatedSet);
			collector.collect(nonDominatedSetFit);
//
			int violations = 0;
			for(int j = 0; j < popSize; j++){
				violations += popFit.get(j)[5];
			}
			violations /= popSize;
			if(violations > 0.25){
				mutationRate = 0.9;
			} else
				mutationRate = 0.1;
//				mutationRate = 1 / (1 + Math.exp(-violations + popSize / 2));

			while(true) {

				Chromosome child = popVar[selection.selected(popVar, popFit)].clone();
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

//			for(int j = 0; j < popSize; j++){
//				if(popFit.get(j)[4] == 0){
//					nonDominatedSet.add(popVar[(int) popFit.get(j)[2]].clone());
//					j++;
//				} else{
//					break;
//				}
//			}
//			collector.collectSet(nonDominatedSet);

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
