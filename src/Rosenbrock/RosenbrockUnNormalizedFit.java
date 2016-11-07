package Rosenbrock;

import algorithm.Chromosome;
import algorithm.UnNormalizedFit;
import commonOperators.RealValueChromosome;

public class RosenbrockUnNormalizedFit extends UnNormalizedFit{
	Chromosome individual;
	public RosenbrockUnNormalizedFit(Chromosome individual){
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
