/*
 * Boxiong Tan (Maximus Tann)
 * Title:        PSO algorithm framework
 * Description:  PSO algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * ParameterSettings.java - A parameter package
 */
package ProblemDefine;
/**
 * Parameter settings
 * 
 * @author Boxiong Tan (Maximus Tann) 
 * @since PSO framework 1.0
 */
public class ParameterSettings {
	private double w, c1, c2, lbound, ubound, clampFactor;
	private int optimization, popSize, maxGen, maxVar;
    /**
     * Prepare a package of parameter settings
     * 
     * @param w inertia
     * @param c1 cognitive parameter
     * @param c2 social parameter
     * @param clampFactor clamp factor
     * @param lbound the lower boundary of a variable of a particle
     * @param ubound the upper boundary of a variable of a particle
     * @param maxVar the number of variable of a particle
     * @return 2D-array of population variables
     */	
	public ParameterSettings(
						double w, 
						double c1, 
						double c2, 
						double lbound, 
						double ubound, 
						double clampFactor,
						int optimization, 
						int popSize, 
						int maxGen, 
						int maxVar
						){
		this.c1 = c1;
		this.c2 = c2;
		this.lbound = lbound;
		this.ubound = ubound;
		this.clampFactor = clampFactor;
		this.optimization = optimization;
		this.popSize = popSize;
		this.maxGen = maxGen;
		this.maxVar = maxVar;
	}

	public int getMaxVar(){
		return maxVar;
	}
	public double getW() {
		return w;
	}
	public double getC1() {
		return c1;
	}
	public double getC2() {
		return c2;
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
	public double getClampFactor(){
		return clampFactor;
	}

}
