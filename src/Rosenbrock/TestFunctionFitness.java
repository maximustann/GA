package Rosenbrock;

import java.util.ArrayList;

import algorithm.*;
import commonOperators.RealValueChromosome;
public class TestFunctionFitness implements FitnessFunction{

	public ArrayList<double[]> unNormalizedFit(Chromosome[] popVar){
		ArrayList<double[]> fitness = new ArrayList<double[]>();
		
		for(int i = 0; i < popVar.length; i++){
			double fit = 0.0;
			for(int j = 0; j < popVar[0].size() - 1; j++){
				double firstTerm, secondTerm;
				firstTerm = ((RealValueChromosome) popVar[i]).individual[j + 1] - 
						Math.pow(((RealValueChromosome) popVar[i]).individual[j], 2);
				secondTerm = Math.pow(((RealValueChromosome) popVar[i]).individual[j] - 1, 2);
				fit += 100 * (firstTerm * firstTerm) + secondTerm;
			}

			fitness.add(new double[] {fit, i});
		}
		return fitness;
	}

	public ArrayList<double[]> normalizedFit(Chromosome[] popVar){
		return unNormalizedFit(popVar);
	}

}
