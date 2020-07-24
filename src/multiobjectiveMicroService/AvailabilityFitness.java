package multiobjectiveMicroService;

import algorithms.Chromosome;
import algorithms.UnNormalizedFit;


public class AvailabilityFitness extends UnNormalizedFit {
	public AvailabilityFitness(){
		super(null);
	}

	public AvailabilityFitness(Chromosome individual){
		super(individual);
	}

	public Object call() throws Exception {
	    double[] fit = new double[2];

		((MultiGroupGAChromosome) individual).updateAppList();
        fit[0] = ((MultiGroupGAChromosome) individual).updateAverageAvailability();
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
