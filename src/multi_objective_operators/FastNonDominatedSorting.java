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

import java.util.*;

import LNS_FF.Initialization;
import algorithms.Chromosome;
import algorithms.Sort;

import javax.net.ssl.SSLEngineResult;

/**
 * @author Boxiong Tan (Maximus Tann) 
 * @since GA framework 1.0
 */
public class FastNonDominatedSorting implements Sort{
	private int optimization;
	public FastNonDominatedSorting(int optimization){
		this.optimization = optimization;
	}


//	public void sort(Chromosome[] popVar, ArrayList<double[]> popFit){
//		ArrayList<ArrayList<Integer>> fronts = new ArrayList<>();
//		ArrayList<ArrayList<Integer>> dominatedSet = new ArrayList<>();
//		ArrayList<Integer> dominationList = new ArrayList<>();
//		ArrayList<Integer> currentFront = new ArrayList<>();
////		ArrayList<double[]> sortedList = new ArrayList<double[]>();
//		Chromosome[] newPop = new Chromosome[popVar.length];
//		int ranking = 0;
//
//		for(int i = 0; i < popVar.length; i++) {
//			int domination = 0;
//			ArrayList<Integer> individualSet = new ArrayList<>();
//			double[] currentFit = popFit.get(i);
//			for (int j = 0; j < popVar.length; j++) {
//				double[] targetFit = popFit.get(j);
//				if (dominated(currentFit, targetFit)) {
//					individualSet.add(j);
//				} else if (dominated(targetFit, currentFit)) {
//					domination++;
//				}
//			}
//
//			if (domination == 0) {
//				currentFront.add(i);
//				popFit.get(i)[4] = 0;
//			}
//
//			dominatedSet.add(individualSet);
//			dominationList.add(domination);
//		}
//		fronts.add(currentFront);
//
//		int counter = 1;
//		while(currentFront.size() != 0) {
//			ArrayList<Integer> h = new ArrayList<>();
//			for(int i = 0; i < currentFront.size(); i++) {
//				Integer pIndex = currentFront.get(i);
//				ArrayList<Integer> individualSet = dominatedSet.get(pIndex);
//				for(int j = 0; j < individualSet.size(); j++) {
//					Integer qIndex = individualSet.get(j);
//					dominationList.set(qIndex, dominationList.get(qIndex) - 1);
//					if(dominationList.get(qIndex) == 0) {
//						popFit.get(qIndex)[4] = counter + 1;
//						h.add(qIndex);
//					}
//				}
//			}
//			counter++;
//			currentFront = h;
//			if(!h.isEmpty()) fronts.add(currentFront);
//		}
//
//		// sort each front with crowding distance
//		for(int i = 0; i < fronts.size(); i++){
//			ArrayList<Integer> eachFront = fronts.get(i);
//			Collections.sort(eachFront, new Comparator<Integer>() {
//				@Override
//				public int compare(Integer pIndex, Integer qIndex) {
//					int condition = 0;
//					double pCrowding = popFit.get(pIndex)[3];
//					double qCrowding = popFit.get(qIndex)[3];
//					if(pCrowding - qCrowding > 0.0) condition = 1;
//					else if(pCrowding - qCrowding < 0.0) condition = -1;
//					else condition = 0;
//					return condition;
//				}
//			});
//			// for crowding distance, bigger the better. Therefore,
//			// we use descendant order.
//			Collections.reverse(eachFront);
//		}
//
//		int counter2 = 0;
//		for(int i = 0; i < fronts.size(); i++){
//			ArrayList<Integer> front = fronts.get(i);
//			for(int j = 0; j < front.size(); j++){
//				Integer pIndex = front.get(j);
//				popFit.get(pIndex)[2] = counter2;
//				counter2++;
//			}
//		}
//
//		for(int i = 0; i < popFit.size(); i++){
//			newPop[(int) popFit.get(i)[2]] = popVar[i].clone();
//		}
//
//		popFit.sort(new Comparator<double[]>() {
//			@Override
//			public int compare(double[] o1, double[] o2) {
//				int condition = 0;
//				if(o1[2] < o2[2]) condition = -1;
//				else if(o1[2] > o2[2]) condition = 1;
//				else condition = 0;
//				return condition;
//			}
//		});
//
//		for(int i = 0; i < popVar.length; i++){
//			popVar[i] = newPop[i];
//		}
//	}
//
//	private boolean dominated(double[] x, double[] y){
//		boolean dominated = false;
//		if(x[0] <= y[0] && x[1] <= y[1]){
//			if(x[0] < y[0] || x[1] < y[1]){
//				return true;
//			}
//		}
//		return dominated;
//	}

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
			newPop[i] = popVar[(int) sortedList.get(i)[2]].clone();
			sortedList.get(i)[2] = i;
		}

		// copy the sorted population back to original pop
		for(int i = 0; i < popVar.length; i++){
			popVar[i] = newPop[i];
		}
//        popVar = newPop;
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
				if(targetFitness[0] < currentFitness[0] || targetFitness[1] < currentFitness[1]){
					dominatedNo++;
				}
			} else if((optimization == 0 && targetFitness[0] >= currentFitness[0] && targetFitness[1] >= currentFitness[1]) ||
				(optimization == 1 && targetFitness[0] <= currentFitness[0] && targetFitness[1] <= currentFitness[1])){
				if(currentFitness[0] < targetFitness[0] || currentFitness[1] < targetFitness[1]){
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
