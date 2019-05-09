/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * BinaryTournamentSelection.java - A binary tournament selection procedure
 */
package multi_objective_operators;

import java.util.ArrayList;

import algorithms.Chromosome;
import algorithms.StdRandom;
import commonOperators.TournamentSelection;

/**
 * @author Boxiong Tan (Maximus Tann) 
 * @since GA framework 1.0
 */
public class BinaryTournamentSelection extends TournamentSelection{

	public BinaryTournamentSelection(int optimization, int seed) {
		super(2, optimization, seed);
	}

	@Override
	/**
	 * Randomly select two chomosomes' fitness 
	 * the fitness is constructed as follows:
	 * <ul>
	 * 	<li> [0] obj1 value </li>
	 *  <li> [1] obj2 value </li>
	 *  <li> [2] position in the population </li>
	 *  <li> [3] crowding distance </li>
	 *  <li> [4] rank </li> 
	 *  <li> [5] number of violations </li>
	 * </ul>
	 * 
	 * This implementation using a ranked domination to select better chromosome 
	 * in the binary selection. It first considers the number of violations.
	 * A chromosome with a lower number of violations will always be selected.
	 * A chromosome with better ranking is always better.
	 * If two chromosomes are in the same ranking, the one with smaller Crowding distance
	 * is better.
	 */
	public int selected(Chromosome[] popVar, ArrayList<double[]> fitness) {
		double[] rank 				= new double[2];
		double[] crowdingDistance 	= new double[2];
		double[] violations			= new double[2];
		ArrayList<double[]> chosen 	= new ArrayList<double[]>();
		int[] chosenIndex			= new int[2];

		// randomly choose two chromosomes
		chosenIndex[0] = StdRandom.uniform(fitness.size());
		chosen.add(fitness.get(chosenIndex[0]));

		chosenIndex[1] = StdRandom.uniform(fitness.size());
		chosen.add(fitness.get(chosenIndex[1]));

		rank[0] 				= chosen.get(0)[4];
		rank[1] 				= chosen.get(1)[4];
		crowdingDistance[0] 	= chosen.get(0)[3];
		crowdingDistance[1] 	= chosen.get(1)[3];
		violations[0]		= chosen.get(0)[5];
		violations[1]		= chosen.get(1)[5];

		if(violations[0] < violations[1]){
			return chosenIndex[0];
		} else if(violations[0] > violations[1]){
			return chosenIndex[1];
		} else {
			if(rank[0] < rank[1]){
				return chosenIndex[0];
			} else if(rank[0] > rank[1]){
				return chosenIndex[1];
			} else{
				if(crowdingDistance[0] > crowdingDistance[1]){
					return chosenIndex[0];
				} else {
					return chosenIndex[1];
				}
			}
		}
	} // selected ends
//
//	@Override
//	public int selected(Chromosome[] popVar, ArrayList<double[]> fitness) {
//		double[] rank 				= new double[2];
//		double[] crowdingDistance 	= new double[2];
//		double[] violations			= new double[2];
//		ArrayList<double[]> chosen 	= new ArrayList<double[]>();
//		int[] chosenIndex			= new int[2];
//
//		// randomly choose two chromosomes
//		chosenIndex[0] = StdRandom.uniform(fitness.size());
//		chosen.add(fitness.get(chosenIndex[0]));
//
//		chosenIndex[1] = StdRandom.uniform(fitness.size());
//		chosen.add(fitness.get(chosenIndex[1]));
//
//		rank[0] 				= chosen.get(0)[4];
//		rank[1] 				= chosen.get(1)[4];
//		crowdingDistance[0] 	= chosen.get(0)[3];
//		crowdingDistance[1] 	= chosen.get(1)[3];
//		violations[0]		= chosen.get(0)[5];
//		violations[1]		= chosen.get(1)[5];
//
////		if(violations[0] < violations[1]){
////			return chosenIndex[0];
////		} else if(violations[0] > violations[1]){
////			return chosenIndex[1];
////		} else {
//			if(rank[0] < rank[1]){
//				return chosenIndex[0];
//			} else if(rank[0] > rank[1]){
//				return chosenIndex[1];
//			} else{
//				if(crowdingDistance[0] > crowdingDistance[1]){
//					return chosenIndex[0];
//				} else {
//					return chosenIndex[1];
//				}
//			}
////		}
//	} // selected ends


}
