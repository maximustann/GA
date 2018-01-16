package ProblemDefine;
import java.util.Arrays;
/**
 * Parameter settings of cooperative coevolution
 *
 * @author Boxiong Tan (Maximus Tann)
 * @since GA framework 2.0
 */
public class CoGAParameterSettings {
    private double[] mutationRates, crossoverRates, lbounds, ubounds;
    private int optimization, maxGens, numOfSubPop;
	private int[] tournamentSizes, elitSizes, popSizes,
                 maxVars;
     /**
     * Prepare a package of parameter settings
      *
      * ParameterSettings is about algorithm not the problem related.
      *
     * @param mutationRates mutation rates
     * @param crossoverRates crossover rates
     * @param lbounds the lower boundary of a variable of particles
     * @param ubounds the upper boundary of a variable of particles
     * @param tournamentSizes sizes of tournament selection
     * @param elitSizes sizes of elitism
     * @param optimization minimize (0) or maximize (1)
     * @param popSizes population sizes
     * @param maxGens maximum generations
     * @param maxVars the numbers of variable of a particle
     */
	public CoGAParameterSettings(
						double[] mutationRates,
						double[] crossoverRates,
						double[] lbounds,
						double[] ubounds,
						int[] tournamentSizes,
						int[] elitSizes,
						int[] popSizes,
						int[] maxVars,
						int numOfSubPop,
						int optimization,
						int maxGens
						) {
		this.mutationRates = mutationRates;
		this.crossoverRates = crossoverRates;
		this.lbounds = lbounds;
		this.ubounds = ubounds;
		this.tournamentSizes = tournamentSizes;
		this.elitSizes = elitSizes;
		this.optimization = optimization;
		this.popSizes = popSizes;
		this.maxGens = maxGens;
		this.maxVars = maxVars;
		this.numOfSubPop = numOfSubPop;
	}

	public int getNumOfSubPop(){
		return numOfSubPop;
	}
	public int[] getMaxVar(){
		return maxVars;
	}
	public double[] getLbound() {
		return lbounds;
	}
	public double[] getUbound() {
		return ubounds;
	}
	public int getOptimization() {
		return optimization;
	}
	public int[] getPopSize() {
		return popSizes;
	}
	public int getMaxGen() {
		return maxGens;
	}

	public double[] getMutationRate() {
		return mutationRates;
	}

	public double[] getCrossoverRate() {
		return crossoverRates;
	}

	public int[] getTournamentSize() {
		return tournamentSizes;
	}

	public int[] getElitSize() {
		return elitSizes;
	}
}
