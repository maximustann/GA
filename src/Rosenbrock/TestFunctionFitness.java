package Rosenbrock;

import java.util.ArrayList;

import algorithm.*;
public class TestFunctionFitness implements FitnessFunction{

	public ArrayList<double[]> unNormalizedFit(Chromosome[] popVar){
		ArrayList<double[]> fitness = new ArrayList<double[]>();
		for(int i = 0; i < popVar.length; i++){
			for(int j = 0; j < popVar[0].size() - 1; j++){
				double fit += 100 * (((double) popVar[i].individual[j]) * popVar[i][j] - popVar[i][j + 1]) *
						(popVar[i][j] * popVar[i][j] - popVar[i][j + 1]) + (popVar[i][j] - 1) * (popVar[i][j] - 1);
			}
		}
		return fitness;
	}

	public ArrayList<double[]> normalizedFit(Chromosome[] popVar){
		return unNormalizedFit(popVar);
	}

}
