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
import commonRepresentation.IntValueChromosome;
/**
*
* @author Boxiong Tan (Maximus Tann)
* @author Hai Huang
* @since PSO framework 1.0
*/
public class GAHaiTimeFitness extends UnNormalizedFit {
	/** These are data, therefore, they are defined as static values. */
	private static double[] latency;
	private static double[] frequency;
	private static int noService;
	private static int noLocation;
	private static int noUser;


	/**
	 * 
	 * @param latency		latency matrix
	 * @param frequency 		frequency matrix
	 * @param noService		number of services
	 * @param noLocation		number of locations
	 */
	public GAHaiTimeFitness(
								double[] latency, 
								double[] frequency,
								int noService, 
								int noLocation
								){
		super(null);
		GAHaiTimeFitness.latency = latency;
		GAHaiTimeFitness.frequency = frequency;
		GAHaiTimeFitness.noService = noService;
		GAHaiTimeFitness.noUser = latency.length / noLocation;
	}
	
	public GAHaiTimeFitness(Chromosome individual){
		super(individual);
		GAHaiTimeFitness.noLocation = individual.size() / noService;
	}

	/**
	 * evaluate each population
	 */
	@Override
	public Object call() throws Exception {
		double[] fit = new double[2];
		fit[0] = fitnessIndividual((IntValueChromosome) individual);
		fit[1] = 0;
		return fit;
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
	 * @return fitness		the fitness value of this particle
	 */
	private double fitnessIndividual(
					IntValueChromosome individual){
		double fitness = 0.0;
		double[][] latencyMatrix = new double[noUser][noLocation];
		double[][] temp = new double[noUser][noLocation];
		double[][] responseMatrix = new double[noUser][noService];
		double[] responseComp = new double[noService];


		// turn a particle vector into a particle matrix
		individual.toMatrix(noService);

		// turn latency vector into a matrix
		for(int i = 0; i < noUser; i++){
			for(int j = 0; j < noLocation; j++){
				latencyMatrix[i][j] = latency[i * noLocation + j];
			}
		}


		// calculate the response matrix 
		for(int count = 0; count < noService; count++){
			for(int i = 0; i < noUser; i++){
				for(int j = 0; j < noLocation; j++) {
					temp[i][j] = individual.matrixIndividual[count][j] * latencyMatrix[i][j];
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
			if((	 minimum > temp[i] && temp[i] != 0) || 
				(minimum == 0 && temp[i] != 0)) 
				 minimum = temp[i];
		}
		return minimum;
	}



}
