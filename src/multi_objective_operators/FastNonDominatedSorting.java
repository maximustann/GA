package multi_objective_operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import algorithms.Chromosome;
import algorithms.Sort;

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
			ArrayList temp = count(i, popFit);
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
			if(sortedList.size() == popFit.size()) break;
			for(int i = 0; i < popFit.size(); i++){
				// If the dominationCount is 0, 
				// then assign its ranking to the current ranking.
				if(dominationCount.get(i) == 0){
					// index 5 is the ranking
					popFit.get(i)[4] = ranking;
					zeroRankList.add(popFit.get(i));
					// For the sorted chromosomes, set their dominationCount to -1
					dominationCount.set(i, -1);
				}
			}
			ranking++;
			
			// for each item in the zeroRankList, find its dominatedSet
			for(int j = 0; j < zeroRankList.size(); j++){
				ArrayList<double[]> dominatedChromosomes = dominatedSet.get((int) zeroRankList.get(j)[2]);
				for(double[] chr:dominatedChromosomes){
					// minus 1 for each dominated chromosome's dominationCount
					dominationCount.set((int) chr[2], dominationCount.get((int) chr[2]) - 1);
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
		
		for(int i = 0; i < popVar.length; i++){
			newPop[i] = popVar[(int) sortedList.get(i)[2]];
			sortedList.get(i)[2] = i;
		}
		
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
	 * @return the number of chromosome that dominate current one
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ArrayList count(int index, ArrayList<double[]> popFit){
		double[] currentFitness = popFit.get(index);
		ArrayList<double[]> dominatedList = new ArrayList<double[]>();
		Integer dominatedNo = 0;
		int popSize = popFit.size();
		for(int i = 0; i < popSize; i++){
			if(i == index) continue;
			double[] targetFitness = popFit.get(i);
			// count the number of chromosomes that dominate current one
			if((optimization == 0 && targetFitness[0] <= currentFitness[0] && targetFitness[1] <= currentFitness[1]) ||
				(optimization == 1 && targetFitness[0] >= currentFitness[0] && targetFitness[1] >= currentFitness[1])){
				dominatedNo++;
			}
			
			// put the chromosome that domainted by current one into an ArrayList
			if((optimization == 0 && targetFitness[0] >= currentFitness[0] && targetFitness[1] >= currentFitness[1]) ||
				(optimization == 1 && targetFitness[0] <= currentFitness[0] && targetFitness[1] <= currentFitness[1])){
				dominatedList.add(targetFitness);
			}
		}

		ArrayList temp = new ArrayList();
		temp.add(dominatedNo);
		temp.add(dominatedList);
		return temp;
	}
	
	

}
