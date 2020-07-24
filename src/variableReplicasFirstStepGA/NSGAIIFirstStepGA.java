package variableReplicasFirstStepGA;

import ProblemDefine.ParameterSettings;
import ProblemDefine.ProblemParameterSettings;
import algorithms.Chromosome;
import algorithms.GeneticAlgorithm;
import algorithms.TwoParentsCrossover;
import gaFactory.GAFactory;
import multiobjectiveMicroService.MultiGroupGAChromosome;

import java.util.ArrayList;

public class NSGAIIFirstStepGA extends GeneticAlgorithm {
	GAFactory factory;
	ParameterSettings pars;
	ProblemParameterSettings proSet;


	public void run(int seed){
		collector.collectTime(0);
		initializeRand(seed);
		popVar = initPop.init(popSize, maxVar, lbound, ubound);
//		ArrayList<ArrayList<double[]>> genNonDomFit = new ArrayList<>();


		for(int i = 0; i < maxGen; i++){
			ArrayList<Chromosome> nonDominatedSet = new ArrayList<>();
			ArrayList<double[]> nonDominatedSetFit = new ArrayList<>();
			int childrenCount = 0;
			ArrayList<double[]> newPopFit = new ArrayList<>();
			ArrayList<double[]> combinedPopFit = new ArrayList<>();
			Chromosome[] combinedPop = new Chromosome[popSize * 2];
			Chromosome[] newPop = new Chromosome[popSize];

			evaluate.evaluate(popVar, popFit);

//			constraint.evaluate(popVar, popFit);
			distance.calculate(popVar, popFit);
			sort.sort(popVar, popFit);


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
//			genNonDomFit.add(nonDominatedSetFit);
//			collector.collectSet(nonDominatedSet);
//			collector.collect(nonDominatedSetFit);
            collector.collect(nonDominatedSet);
//			System.out.println(nonDominatedSet.size());
//			System.out.println(nonDominatedSet.size());
//
//			int violations = 0;
//			for(int j = 0; j < popSize; j++){
//				violations += popFit.get(j)[5];
//			}
//			violations /= popSize;
//			if(violations > 0.1 * popVar[0].size() / 4){
//				mutationRate = 0.9;
//			} else
//				mutationRate = 0.1;
//				mutationRate = 1 / (1 + Math.exp(-violations + popSize / 2));

			while(true) {

//				Chromosome child = popVar[selection.selected(popVar, popFit)].clone();
                int exitFlag = 0;
                Chromosome father = popVar[selection.selected(popVar, popFit)];
                Chromosome mother = popVar[selection.selected(popVar, popFit)];
                Chromosome[] children = ((TwoParentsCrossover) crossover)
										.update(father, mother, crossoverRate);

				for(int j = 0; j < children.length; j++) {
					mutation.update(children[j], mutationRate);
					newPop[childrenCount] = children[j];
					childrenCount++;
					if(childrenCount == popSize) {
						exitFlag = 1;
						break;
					}
				}
				if(exitFlag == 1) break;
			}

			try {
				evaluate.evaluate(newPop, newPopFit);
			} catch (Exception e) {
				e.printStackTrace();
			}

			for(int j = 0; j < popSize; j++){
                combinedPop[j] = newPop[j];
				combinedPopFit.add(newPopFit.get(j));
			}

			for(int j = popSize, k = 0; j < popSize * 2; j++, k++){
				combinedPop[j] = popVar[k];
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
				popVar[j] = combinedPop[j];
				popFit.add(combinedPopFit.get(j));
				// adjust the index
				popFit.get(j)[2] = j;
			}
//			detectViolations(popVar); // debug

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

//		collector.collect(genNonDomFit);
		collector.collectTime(1);
	}


	private void detectViolations(Chromosome[] pop){
		for(Chromosome individual:pop){
			((MultiGroupGAChromosome) individual).checkViolations();
		}
	}

	public NSGAIIFirstStepGA(ParameterSettings pars, ProblemParameterSettings proSet, GAFactory factory){
		this.factory = factory;
		this.pars = pars;
		this.proSet = proSet;
		prepare();
	}


	protected void prepare(){
		maxGen = pars.getMaxGen();
		maxVar = pars.getMaxVar();
		popSize = pars.getPopSize();
		mutationRate = pars.getMutationRate();
		crossoverRate = pars.getCrossoverRate();
		lbound = pars.getLbound();
		ubound = pars.getUbound();
		optimization = pars.getOptimization();
		tournamentSize = pars.getTournamentSize();
		popFit = new ArrayList<double[]>();

		initPop = factory.getInitPopMethod();
		mutation = factory.getMutation();
		crossover = factory.getCrossover();
		selection = factory.getSelection(tournamentSize, optimization);
		elitism = factory.getElitism(pars.getElitSize(), optimization);
		evaluate = proSet.getEvaluate();
		collector = factory.getDataCollector();
		sort = factory.getSort();
		distance = factory.getDistance();
	};

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
