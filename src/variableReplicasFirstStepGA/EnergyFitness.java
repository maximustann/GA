package variableReplicasFirstStepGA;

import algorithms.Chromosome;
import algorithms.UnNormalizedFit;


public class EnergyFitness extends UnNormalizedFit {
	public EnergyFitness(){
		super(null);
	}

	public EnergyFitness(Chromosome individual){
		super(individual);
	}

	public Object call() throws Exception {
	    double[] fit = new double[2];
        fit[0] = ((FirstStepGAChromosome) individual).calEnergyFitness();
        fit[1] = 0;
        return fit;
	}

	/**
	 *
	 * keep 5 decimal points
	 * @param number
	 * @return
	 */
	private double round5(double number){
		number = Math.floor(number * 100000) / 100000;
		return number;
	}


}
