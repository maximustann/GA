package GAProcedure;

import algorithms.Chromosome;
import algorithms.Coevolution;
import algorithms.TwoParentsCrossover;
import algorithms.StdRandom;

public abstract class CoGA extends Coevolution {
    /**
     * Steps:
     * <ul>
     * <li> start timer </li>
     * <li> 1. initialize sub-populations </li>
     * <ul>
     * For each generation
     * <li> 2. evaluate each population, collect the best fitness value</li>
     * <li> 3. sort population </li>
     * <li> 4. select parents </li>
     * <li> 5. crossover </li>
     * <li> 6. store children </li>
     * <li> 7. mutation </li>
     * </ul>
     * <li> end timer </li>
     * </ul>
     *
     * @param seed Random seed
     */
    @Override
    public void run(int seed) {
        collectors[0].collectTime(0);
        initializeRand(seed);

        /**
         *
         * for each sub-population,
         * 1. initialize their population,
         * 2. initialize their representative
         *
         */
        for (int i = 0; i < numOfSubPop; i++) {
            popVars[numOfSubPop] = initPops[i].init(popSizes[i],
                    maxVars[i], lbounds[i], ubounds[i]);

            // randomly choose one individual as the representative in the first generation
            int u = StdRandom.uniform(popVars[i].length);
            representatives[i] = popVars[i][u];
        }

        /**
         * For each generation
         */
        for (int genCount = 0; genCount < maxGens; genCount++) {

            /**
             * For each sub-population, do the following:
             */
            for (int subPop = 0; subPop < numOfSubPop; subPop++) {
                Chromosome[] newPop = new Chromosome[popSizes[subPop]];
                int childrenCount = elitisms[subPop].getSize();
                try {
                    /**
                     * Evaluate
                     * The evaluation needs four parameters
                     * @param subPop indicates the number of the sub-population
                     * @param popVars indicates the population
                     * @param representatives indicates the best individuals
                     * @param popFits[subPop] indicates the fitness
                     */
                    evaluate.evaluate(subPop, popVars[subPop],
                                      representatives, popFits[subPop]);
                } catch (Exception e){
                    e.printStackTrace();
                }
                sorts[subPop].sort(popVars[subPop], popFits[subPop]);
                /**
                 * update the representative, assign the best individual to representative
                 */
                representatives[subPop] = popVars[subPop][0];
                elitisms[subPop].carryover(popVars[subPop], newPop);
                collectors[subPop].collect(popFits[subPop].get(0));

                while(true) {
                    int exitFlag = 0;
                    Chromosome father = popVars[subPop][selections[subPop].selected(popVars[subPop], popFits[subPop])];
                    Chromosome mother = popVars[subPop][selections[subPop].selected(popVars[subPop], popFits[subPop])];
                    Chromosome[] children =  ((TwoParentsCrossover) crossovers[subPop])
										.update(father, mother, crossoverRates[subPop]);
                    for(int j = 0; j < children.length; j++) {
                        mutations[j].update(children[j], mutationRates[subPop]);
                        newPop[childrenCount] = children[j].clone();
                        childrenCount++;
                        if(childrenCount == popSizes[subPop]){
                            exitFlag = 1;
                            break;
                        }
                    } // mutation for each individual ends
                    if(exitFlag == 1) break;
                } // generate new population ends
                popVars[subPop] = newPop.clone();
            } // sub-population loop ends
            collectors[0].collectTime(1);
        } // generation loop ends
    } // run ends

    @Override
    protected abstract void prepare();

    @Override
	public void runNtimes(int seedStart, int nTimes) {
		for(int i = 0; i < nTimes; i++){
			run(seedStart);
			seedStart++;
		}
	}
}
