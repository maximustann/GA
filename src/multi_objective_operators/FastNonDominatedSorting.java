/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * FastNonDominatedSorting.java
 */
package multi_objective_operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import algorithms.Chromosome;
import algorithms.Sort;
/**
 * @author Boxiong Tan (Maximus Tann) 
 * @since GA framework 1.0
 */
public class FastNonDominatedSorting implements Sort{
	private int optimization;
	
	public FastNonDominatedSorting(int optimization){
		this.optimization = optimization;
	}
	
	/**
	 * Sort
	 * Steps
	 * <ul>
	 * 	<li> 1.for each chromosome, calculate its domination number and dominated set. </li>
	 *  <li> 2.insert the chromosome according to its ranking until all chromosomes have been inserted  </li>
	 *  <li> 3.sort according to their ranking  </li>
	 * </ul>
	 * @param popVar population
	 * @param popFit fitness
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void sort(Chromosome[] popVar, ArrayList<double[]> popFit) {
		int ranking = 0;
		Chromosome[] newPop = new Chromosome[popVar.length];
		ArrayList<Integer> dominationCount = new ArrayList<Integer>();
		ArrayList<ArrayList<double[]>> dominatedSet = new ArrayList<ArrayList<double[]>>();
		ArrayList<double[]> sortedList = new ArrayList<double[]>();
		
		// for each chromosome, calculate its domination number and dominated set.
		for(int i = 0; i < popFit.size(); i++){
			ArrayList temp = count(i, popFit, popVar.length);
			// 0 means the dominationCount
			dominationCount.add((Integer) temp.get(0));
			
			// 1 means the dominatedSet
			dominatedSet.add((ArrayList<double[]>) temp.get(1));
		}


		// insert the chromosome according to its ranking until all chromosomes 
		// have been inserted.
		while(true){
			ArrayList<double[]> zeroRankList = new ArrayList<double[]>();
			// all the fitness has been sorted, break
			if(sortedList.size() == popVar.length) break;
			for(int i = 0; i < popVar.length; i++){
				// If the dominationCount is 0, 
				// then assign its ranking to the current ranking.
				if(dominationCount.get(i) == 0){
					// index 4 is the ranking
					popFit.get(i)[4] = ranking;
					zeroRankList.add(popFit.get(i));
					// For the sorted chromosomes, set their dominationCount to -1
					dominationCount.set(i, -1);
				}
			}
			ranking++;
			
			// for each item in the zeroRankList, find its dominated
			for(int j = 0; j < zeroRankList.size(); j++){
				
				ArrayList<double[]> dominatedChromosomes = dominatedSet.get((int) zeroRankList.get(j)[2]);
				for(int k = 0; k < dominatedChromosomes.size(); k++){
					// minus 1 for each dominated chromosome's dominationCount
//					dominationCount.set((int) chr[2], dominationCount.get((int) chr[2]) - 1);
					// get the index of the dominated Chromosome
					int dominatedChromosomeIndex = (int) dominatedChromosomes.get(k)[2];
					dominationCount.set(dominatedChromosomeIndex, dominationCount.get(dominatedChromosomeIndex) - 1);
				}
			}
			
			// sort according to their crowding distance.
			Collections.sort(zeroRankList, new Comparator<double[]>() {
				@Override
				public int compare(double[] fitness1, double[] fitness2) {
					int condition = 0;
					if(fitness1[3] - fitness2[3] > 0.0) condition = 1;
					else if(fitness1[3] - fitness2[3] < 0.0) condition = -1;
					else condition = 0;
					return condition;
				}
			});		
			// for crowding distance, bigger the better. Therefore, 
			// we use descendant order.
			Collections.reverse(zeroRankList);
			sortedList.addAll(zeroRankList);
		}
		
		// sort according to their ranking.
		Collections.sort(sortedList, new Comparator<double[]>() {
			@Override
			public int compare(double[] fitness1, double[] fitness2) {
				int condition = 0;
				if(fitness1[4] - fitness2[4] > 0.0) condition = 1;
				else if(fitness1[4] - fitness2[4] < 0.0) condition = -1;
				else condition = 0;
				return condition;
			}
		});
		
		// adjust the position value
		for(int i = 0; i < popVar.length; i++){
			newPop[i] = popVar[(int) sortedList.get(i)[2]];
			sortedList.get(i)[2] = i;
		}
		
		// copy the sorted population back to original pop
		for(int i = 0; i < popVar.length; i++){
			popVar[i] = newPop[i];
		}
		popFit.clear();
		popFit.addAll(sortedList);
	}
	
	/**
	 * calculate its domination number
	 * We only support for two objectives.
	 * 
	 * @param index the index of current chromosome
	 * @param popFit fitness
	 * @return the domination number and the current chromosome's dominate set
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ArrayList count(int index, ArrayList<double[]> popFit, int popSize){
		double[] currentFitness = popFit.get(index);
		// dominatedList is the list of the chromosomes' fitness which are dominated by
		// the current chromosome
		ArrayList<double[]> dominatedList = new ArrayList<double[]>();
		
		
		Integer dominatedNo = 0;
		for(int i = 0; i < popSize; i++){
			if(i == index) continue;
			double[] targetFitness = popFit.get(i);
			// count the number of chromosomes that dominate current one
			if((optimization == 0 && targetFitness[0] <= currentFitness[0] && targetFitness[1] <= currentFitness[1]) ||
				(optimization == 1 && targetFitness[0] >= currentFitness[0] && targetFitness[1] >= currentFitness[1])){
				// if two chromosomes have the same fitness, they are nondominated
				if(targetFitness[0] == currentFitness[0] && targetFitness[1] == currentFitness[1]){
				} else {
					dominatedNo++;
				}
			}


			// put the chromosome that dominated by current one into an ArrayList
			if((optimization == 0 && targetFitness[0] >= currentFitness[0] && targetFitness[1] >= currentFitness[1]) ||
				(optimization == 1 && targetFitness[0] <= currentFitness[0] && targetFitness[1] <= currentFitness[1])){
				if(targetFitness[0] == currentFitness[0] && targetFitness[1] == currentFitness[1]){
				} else {
					dominatedList.add(targetFitness);
				}
			}
		} // end for

		// pack the dominationNumber and dominatedSet into a list
		ArrayList temp = new ArrayList();
		temp.add(dominatedNo);
		temp.add(dominatedList);
		return temp;
	}
	

}
