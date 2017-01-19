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
	private static int vmTypes;
	private static double[] vmCpu;
	private static double[] vmMem;
	private static double[] taskCpu;
	private static double[] taskFreq;


	public WSRPEnergyFitness(
							int taskNum,
							int vmTypes,
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
		WSRPEnergyFitness.vmTypes = vmTypes;
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
		int pmNum = pmIndex[pmIndex.length - 1] + 1;
		double[] pmUtility = new double[pmNum];
		int lastPm = 0;
		int taskCount = 0;
		for(int i = 0; i < pmNum; i++){
			ArrayList[] pmVms = new ArrayList[vmTypes];
			for(int j = 0; j < vmTypes; j++) pmVms[j] = new ArrayList<Double>();
			while(true){
				if(taskCount == taskNum || pmIndex[taskCount] != i) break;
				int taskNum = ((WSRP_IntChromosome) individual).individual[taskCount * 3];
				int vmType = ((WSRP_IntChromosome) individual).individual[taskCount * 3 + 1];
				int vmIndex = ((WSRP_IntChromosome) individual).individual[taskCount * 3 + 2];
				// if the vm has not been created, calculate the utility and add to list
				if(pmVms[vmType].isEmpty() || pmVms[vmType].size() < vmIndex + 1){
					double utility = taskCpu[taskNum] * taskFreq[taskNum] / vmCpu[vmType];
					if(utility > 1) {
						System.out.println("Wrong! evaluate");
						System.out.println("taskNum = " + taskNum +
											", vmType = " + vmType +
											", vmIndex = " + vmIndex);
						System.out.println("taskCpu * taskFreq / vmCpu : " +
											taskCpu[taskNum] + " * " + taskFreq[taskNum] +
											" / " + vmCpu[vmType] + " = " + utility);
						utility = 1;
					}
					pmVms[vmType].add(utility);
				} else {
					// the vm has been created, then add up the vm utility
					double utility = taskCpu[taskNum] * taskFreq[taskNum] / vmCpu[vmType];
					utility += (double) pmVms[vmType].get(vmIndex);
					if(utility > 1) {
						utility = 1;
					}
					pmVms[vmType].set(vmIndex, utility);
				}
				taskCount++;
			} // end While

			// calculate the pmUtility
			for(int j = 0; j < vmTypes; j++){
				while(!pmVms[j].isEmpty()){
					pmUtility[i] += ((double) pmVms[j].get(0)) * vmCpu[j] / pmCpu;
					pmVms[j].remove(0);
				}
			} // end for
		} // end for

		// calculate the total energy
		for(int i = 0; i < pmNum; i++){
			fit[0] += k * pmEnergy + (1 - k) * pmEnergy * pmUtility[i];
		}
		fit[0] = round5(fit[0]);
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
