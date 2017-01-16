package multi_objective_operators;

import java.util.ArrayList;

import algorithms.Chromosome;
import algorithms.StdRandom;
import commonOperators.TournamentSelection;

public class BinaryTournamentSelection extends TournamentSelection{

	public BinaryTournamentSelection(int optimization) {
		super(2, optimization);
	}
	
	@Override
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
	}
}
