/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * ParameterSettings.java - common GA parameter settings
 */
package ProblemDefine;
/**
 * Parameter settings
 *
 * @author Boxiong Tan (Maximus Tann)
 * @since GA framework 1.0
 */
public class ParameterSettings {
	private double mutationRate, crossoverRate, lbound, ubound;
	private int optimization, tournamentSize, elitSize, popSize, maxGen, maxVar;
    /**
     * Prepare a package of parameter settings
     *
     * @param mutationRate mutation rate
     * @param crossoverRate crossover rate
     * @param lbound the lower boundary of a variable of a particle
     * @param ubound the upper boundary of a variable of a particle
     * @param tournamentSize size of tournament selection
     * @param elitSize size of elitism
     * @param optimization minimize (0) or maximize (1)
     * @param popSize population size
     * @param maxGen maximum generation
     * @param maxVar the number of variable of a particle
     */
	public ParameterSettings(
						double mutationRate,
						double crossoverRate,
						double lbound,
						double ubound,
						int tournamentSize,
						int elitSize,
						int optimization,
						int popSize,
						int maxGen,
						int maxVar
						){
		this.mutationRate = mutationRate;
		this.crossoverRate = crossoverRate;
		this.lbound = lbound;
		this.ubound = ubound;
		this.tournamentSize = tournamentSize;
		this.elitSize = elitSize;
		this.optimization = optimization;
		this.popSize = popSize;
		this.maxGen = maxGen;
		this.maxVar = maxVar;
	}

	public int getMaxVar(){
		return maxVar;
	}
	public double getLbound() {
		return lbound;
	}
	public double getUbound() {
		return ubound;
	}
	public int getOptimization() {
		return optimization;
	}
	public int getPopSize() {
		return popSize;
	}
	public int getMaxGen() {
		return maxGen;
	}

	public double getMutationRate() {
		return mutationRate;
	}

	public double getCrossoverRate() {
		return crossoverRate;
	}

	public int getTournamentSize() {
		return tournamentSize;
	}

	public int getElitSize() {
		return elitSize;
	}
}
