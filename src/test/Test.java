package test;

import java.util.ArrayList;

import commonOperators.InitIntChromosomes;
import commonRepresentation.IntValueChromosome;
import multi_objective_operators.CrowdingDistance;
import multi_objective_operators.FastNonDominatedSorting;

public class Test {
	public static void main(String[] arg){
		IntValueChromosome[] chromosomes;
		int popSize = 5;
		int optimization = 0;
		InitIntChromosomes init = new InitIntChromosomes();
		chromosomes = init.init(popSize, 1, 0, 10);
		ArrayList<double[]> fitness = new ArrayList<double[]>();
		FastNonDominatedSorting sorting = new FastNonDominatedSorting(optimization);
		CrowdingDistance crowding = new CrowdingDistance(optimization);
		fitness.add(new double[]{5, 2, 0, 0, 0});
		fitness.add(new double[]{1, 8, 1, 0, 0});
		fitness.add(new double[]{2, 6, 2, 0, 0});
		fitness.add(new double[]{3, 3, 3, 0, 0});
		fitness.add(new double[]{6, 1, 4, 0, 0});
		
//		fitness.add(new double[]{5, 4, 0, 0, 0});
//		fitness.add(new double[]{3, 8, 1, 0, 0});
//		fitness.add(new double[]{2, 6, 2, 0, 0});
//		fitness.add(new double[]{1, 3, 3, 0, 0});
//		fitness.add(new double[]{1, 1, 4, 0, 0});	
		
//		fitness.add(new double[]{15, 14, 0, 0, 0});
//		fitness.add(new double[]{13, 18, 1, 0, 0});
//		fitness.add(new double[]{12, 14.5, 2, 0, 0});
//		fitness.add(new double[]{11, 13, 3, 0, 0});
//		fitness.add(new double[]{11, 11, 4, 0, 0});	
//		fitness.add(new double[]{11, 15, 5, 0, 0});	
		
		sorting.sort(chromosomes, fitness);
		crowding.calculate(fitness);

		
		for(int i = 0; i < popSize; i++){
			double[] fit = fitness.get(i);
			System.out.println("fit1: " + fit[0] + ", fit2: " + fit[1] + ", index: " + fit[2] + ", crowdingD: " + fit[3]
					+ ", ranking: " + fit[4]);
		}
		
	}
}
