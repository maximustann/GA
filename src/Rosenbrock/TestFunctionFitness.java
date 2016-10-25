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
				fit += 
						100 * (
							   Math.pow(
									      (
											   Math.pow(
											   		((RealValueChromosome) popVar[i]).individual[j], 2
													   ) 
											   - ((RealValueChromosome) popVar[i]).individual[j + 1]
										   ), 2
									    )
							   ) + Math.pow(
									   (((RealValueChromosome) popVar[i]).individual[j] - 1), 
									   2
									   );
			}
			fitness.add(new double[] {fit, 0});
		}
		return fitness;
	}

	public ArrayList<double[]> normalizedFit(Chromosome[] popVar){
		return unNormalizedFit(popVar);
	}

}
