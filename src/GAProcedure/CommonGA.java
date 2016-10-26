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
 * 		<li> 2. evaluate population, collect the best fitness value</li> 
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
			int childrenCount = elitism.getSize();
			Chromosome[] newPop = new Chromosome[popSize];
			evaluate.evaluate(popVar, popFit);

			sort.sort(popVar, popFit);
			elitism.carryover(popVar, newPop);
			collector.collect(popFit.get(0));
			collector.collectChromosome(popVar);
			
			// ============Debug==============
//			System.out.println("After sorting");
//			for(int j = 0; j < popSize; j++){
//				System.out.println(popFit.get(j)[0] + " : " + popFit.get(j)[1]);
//			}
//			System.out.println();
			// ============Debug==============

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

			mutation.update(newPop, mutationRate);
			popVar = newPop;
		}
	}

	@Override
	protected abstract void prepare();
}
