/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * CommonGA.java - An common procedure for GA
 */

package GAProcedure;
import algorithm.Chromosome;
import algorithm.GeneticAlgorithm;
import algorithm.TwoParentsCrossover;

/**
 * The abstraction of common GA procedure
 * @author Boxiong Tan (Maximus Tann) 
 * @since GA framework 1.0
 */
public abstract class CommonGA extends GeneticAlgorithm{
/**
 * Steps:
 * <ul>
 * 	<li> 1. initialize population </li>
 * 	<ul>
 * 		For each generation
 * 		<li> 2. evaluate population </li> 
 * 		<li> 3. sort population </li> 
 * 		<ul> while (not enough children)
 * 			<li> 4. select parents </li> 
 * 			<li> 5. crossover </li> 
 * 			<li> 6. store children </li>
 * 		<ul>
 * 		<li> 7. mutation </li> 
 * 	<ul> 
 * <ul>
 * 
 * @param seed Random seed
 */
	public void run(int seed){
		initializeRand(seed);
		popVar = initPop.init(popSize, maxVar, lbound, ubound);

		for(int i = 0; i < maxGen; i++){
			int childrenCount = 0;
			Chromosome[] newPop = new Chromosome[popSize];
			evaluate.evaluate(popVar, popFit);
			sorting.sort(popVar, popFit);

			while(childrenCount < popSize) {
				Chromosome father = popVar[selection.selected(popVar, popFit)];
				Chromosome mother = popVar[selection.selected(popVar, popFit)];
				Chromosome[] children = ((TwoParentsCrossover) crossover)
										.update(father, mother, crossoverRate);
				for(int j = 0; j < children.length; j++) {
					newPop[childrenCount] = children[j];
					childrenCount++;
				}
			}
			popVar = newPop;
			mutation.update(popVar, mutationRate);
//			collector.collect(gBestFit);
		}
	}

	@Override
	protected abstract void prepare();
}
