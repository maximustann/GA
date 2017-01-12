/*
 * Boxiong Tan (Maximus Tann)
 * Title:        PSO algorithm framework
 * Description:  PSO algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * BPSOHaiTimeFitness.java - a response time fitness function for Hai's paper
 */
package wsrp;

import java.util.ArrayList;

import algorithms.*;
/**
*
* @author Boxiong Tan (Maximus Tann)
* @since PSO framework 1.0
*/
public class WSRPEnergyFitness extends UnNormalizedFit {
	/** These are data, therefore, they are defined as static values. */
	private static double k;
	private static int taskNum;	
	private static double pmCpu;
	private static double pmMem;
	private static double pmEnergy;
	private static double[] vmCpu;
	private static double[] vmMem;
	private static double[] taskCpu;
	private static double[] taskFreq;


	public WSRPEnergyFitness(
							int taskNum,
							double k,
							double pmCpu,
							double pmMem,
							double pmEnergy,
							double[] vmCpu,
							double[] vmMem,
							double[] taskCpu,
							double[] taskFreq
								){
		super(null);
		WSRPEnergyFitness.k = k;
		WSRPEnergyFitness.taskNum = taskNum;
		WSRPEnergyFitness.pmCpu = pmCpu;
		WSRPEnergyFitness.pmMem = pmMem;
		WSRPEnergyFitness.pmEnergy = pmEnergy;
		WSRPEnergyFitness.vmCpu = vmCpu;
		WSRPEnergyFitness.vmMem = vmMem;
		WSRPEnergyFitness.taskCpu = taskCpu;
		WSRPEnergyFitness.taskFreq = taskFreq;
	}
	
	public WSRPEnergyFitness(Chromosome individual){
		super(individual);
	}

	/**
	 * evaluate each population
	 */
	@Override
	public Object call() throws Exception {
		double[] fit = new double[2];
		int[] pmIndex = pmCount((WSRP_IntChromosome) individual);
		int pmNum = pmIndex[pmIndex.length - 1];
		int lastPm = 0;
		int pmCount = 0;
		double[] pmUtility = new double[pmNum];
		
		for(int i = 0; i < taskNum; i++){
			if(lastPm != pmIndex[i]) {
				pmCount++;
				lastPm = pmIndex[i];
			}
			pmUtility[pmCount] += (taskCpu[i] * taskFreq[i]) / pmCpu;
		}
		
		for(int i = 0; i < pmNum; i++){
			fit[0] = k * pmEnergy + pmUtility[i] * pmEnergy * (1 - k);
		}
		fit[1] = 0;
		return fit;
	}
	
	private int[] pmCount(WSRP_IntChromosome individual){
		int pmCount = 0;
		ArrayList<double[]> pmResource = new ArrayList<double[]>();
		int[] pmIndex = new int[taskNum];
		pmResource.add(new double[]{pmCpu, pmMem});

		for(int i = 0; i < taskNum; i++){
			// If there is enough resource in the current PM, then allocate it.
			if(pmResource.get(pmCount)[0] - vmCpu[individual.individual[i * 3 + 1]] >= 0
			&& pmResource.get(pmCount)[1] - vmMem[individual.individual[i * 3 + 1]] >= 0) {
				// If it is allocated in a new vm.
				if(individual.individual[i * 3 + 2] == 0) {
					pmResource.get(pmCount)[0] -= vmCpu[individual.individual[i * 3 + 1]];
					pmResource.get(pmCount)[1] -= vmMem[individual.individual[i * 3 + 1]];
				}
			} else {
				// if there is not enough resource, allocate a new PM
				pmCount++;
				pmResource.add(new double[]{pmCpu - vmCpu[individual.individual[i * 3 + 1]], 
											pmMem - vmMem[individual.individual[i * 3 + 1]]});
			}
			pmIndex[i] = pmCount;
		} // end for
		return pmIndex;
	}




}
