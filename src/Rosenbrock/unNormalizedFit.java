package Rosenbrock;

import java.util.concurrent.Callable;

import algorithm.Chromosome;
import commonOperators.RealValueChromosome;

public class unNormalizedFit implements Callable{
	Chromosome individual;
	public unNormalizedFit(Chromosome individual){
		this.individual = individual;
	}
	@Override
	public Object call() throws Exception {
		int maxVar = individual.size();
		
		double fit = 0.0;
		for(int j = 0; j < maxVar - 1; j++){
			double firstTerm, secondTerm;
			firstTerm = ((RealValueChromosome) individual).individual[j + 1] - 
					Math.pow(((RealValueChromosome) individual).individual[j], 2);
			secondTerm = Math.pow(((RealValueChromosome) individual).individual[j] - 1, 2);
			fit += 100 * (firstTerm * firstTerm) + secondTerm;
		}
		return new double[]{fit, 0};
	}
}
