/*
 * Boxiong Tan (Maximus Tann)
 * Title:        PSO algorithm framework
 * Description:  PSO algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * BPSOHaiTimeFitness.java - a response time fitness function for Hai's paper
 */
package GaAllocationProblem;
import algorithms.*;
/**
*
* @author Boxiong Tan (Maximus Tann)
* @author Hai Huang
* @since PSO framework 1.0
*/
public class GAHaiTimeFitness extends FitnessFunc {
	private double[] latency;
	private double[] frequency;
	private int noService;
	private int noLocation;
	private int noUser;
	private Constraint con;

	/**
	 * 
	 * @param normalize 		a user defined normalization method
	 * @param con 			a user defined constraint method
	 * @param latency		latency matrix
	 * @param frequency 		frequency matrix
	 * @param noService		number of services
	 * @param noLocation		number of locations
	 */
	public GAHaiTimeFitness(
								Normalize normalize, 
								Constraint con, 
								double[] latency, 
								double[] frequency,
								int noService, 
								int noLocation
								){
		super(normalize);
		this.con = con;
		this.latency = latency;
		this.frequency = frequency;
		this.noService = noService;
		this.noUser = latency.length / noLocation;
	}

	/**
	 * evaluate each population
	 */
	public double[] unNormalizedFit(double[][] popVar) {
		int popSize = popVar.length;
		noLocation = popVar[0].length / noService;
		double[] fitness = new double[popSize];

		for(int i = 0; i < popSize; i++){
			fitness[i] = fitnessIndividual(popVar[i], noService, noLocation);
		}
		return fitness;
	}

	/**
	 * Three Steps:
	 * <ul>
	 * 	<li>1. calculate fitness values</li>
	 *  <li>2. normalize fitness values</li>
	 *  <li>3. push fitness values	  </li>
	 * </ul>
	 */
	public double[] normalizedFit(double[][] popVar){
		double[] fitness = unNormalizedFit(popVar);
		normalize.doNorm(fitness);
		fitness = con.punish(popVar, fitness);
		return fitness;
	}

	/**
	 * To calculate the theoretically optimal response time 
	 * based on current deployment plan (this particle)
	 * Steps:
	 * <ul>
	 * 	<li>Turn both particle vector and latency vector into matrix form</li>
	 *  		<ul>Calculate response matrix
	 *  			<li> If there is only one copy of service deployed in a location, 
	 *  				response time is the latency value</li>
	 *  			<li> If there are multiple copies, the smallest will be chosen. </li>
	 *  		</ul>
	 *  	<li>calculate the fitness by summing up the product of response time of a service 
	 *  		and its invocation frequency</li>
	 * </ul>
	 * @param particle 		an instance from the population
	 * @param noService		the number of services
	 * @param noLocation		the number of locations
	 * @return fitness		the fitness value of this particle
	 */
	private double fitnessIndividual(double[] particle, int noService, int noLocation){
		double fitness = 0.0;
		double[][] particleMatrix = new double[noService][noLocation];
		double[][] latencyMatrix = new double[noUser][noLocation];
		double[][] temp = new double[noUser][noLocation];
		double[][] responseMatrix = new double[noUser][noService];
		double[] responseComp = new double[noService];


		// turn a particle vector into a particle matrix
		for(int i = 0; i < noService; i++){
			for(int j = 0; j < noLocation; j++){
				particleMatrix[i][j] = particle[i * noService + j];
			}
		}

		// turn latency vector into a matrix
		for(int i = 0; i < noUser; i++){
			for(int j = 0; j < noLocation; j++){
				latencyMatrix[i][j] = latency[i * noUser + j];
			}
		}


		// calculate the response matrix 
		for(int count = 0; count < noService; count++){
			for(int i = 0; i < noUser; i++){
				for(int j = 0; j < noLocation; j++) {
					temp[i][j] = particleMatrix[count][j] * latencyMatrix[i][j];
					responseMatrix[i][count] = min(temp[i]);
				}
			}
		}

		// sum up latency of users location for services
		/*	    s1   s2  s3
		 *  u1|0.5| 0.9 0.8
		 *  u2|0.4| 2.3 1.3
		 *  u3|1.6| 2.1 0.6
		 *   + ----
		 *      2.5
		 */
		for(int i = 0; i < noService; i++){
			for(int j = 0; j < noUser; j++) {
				responseComp[i] += responseMatrix[j][i];
			}
		}


		/*
		 * calculate the fitness by summing the product of response time of a web service 
		 * and its frequency
		 */
		for(int i = 0; i < noService; i++){
			fitness += responseComp[i] * frequency[i];
		}
		return fitness;
	}

	// select the smallest one but not zero
	private double min(double[] temp){
		double minimum = temp[0];
		for(int i = 0; i < temp.length; i++){
			if((minimum > temp[i] && temp[i] != 0) || (minimum == 0 && temp[i] != 0)) minimum = temp[i];
		}
		return minimum;
	}



}
