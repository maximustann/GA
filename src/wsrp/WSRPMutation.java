/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective Genetic algorithm framework
 * Description:  Single-objective Genetic algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * WSRPMutation.java - An implementation of int mutation for WSRP
 */
package wsrp;
import java.util.ArrayList;

import algorithms.Chromosome;
import algorithms.Mutation;
import algorithms.StdRandom;
public class WSRPMutation implements Mutation {
	private static int taskNum;
	private static int vmTypes;
	private static double pmMem;
	private static double pmCpu;
	private static double[] vmMem;
	private static double[] vmCpu;
	private static double[] taskCpu;
	private static double[] taskMem;
	private static double[] taskFreq;
	private double consolidationCoefficient;
//	
//	public WSRPMutation(double consolidationCoefficient) {
//		this.consolidationCoefficient = consolidationCoefficient;
//	}
	public WSRPMutation(
			int taskNum,
			int vmTypes,
			double pmMem,
			double pmCpu,
			double[] vmMem,
			double[] vmCpu,
			double[] taskCpu,
			double[] taskMem,
			double[] taskFreq,
			double consolidationCoefficient
			){
		WSRPMutation.taskNum = taskNum;
		WSRPMutation.vmTypes = vmTypes;
		WSRPMutation.pmMem = pmMem;
		WSRPMutation.pmCpu = pmCpu;
		WSRPMutation.vmMem = vmMem;
		WSRPMutation.vmCpu = vmCpu;
		WSRPMutation.taskCpu = taskCpu;
		WSRPMutation.taskMem = taskMem;
		WSRPMutation.taskFreq = taskFreq;
		this.consolidationCoefficient = consolidationCoefficient;
	}
	public void update(Chromosome individual, double mutationRate) {
		// for each task, check if it is satisfied the mutation condition
		for(int i = 0; i < taskNum; i++) {
			if(StdRandom.uniform() <= mutationRate) {
				// mutate vm types and swap task between PMs share equal probability!
				if(StdRandom.uniform() <= 0.5)
					mutateVMTypes((WSRP_IntChromosome) individual, i);
				else {
					swapTasksBetweenPm((WSRP_IntChromosome) individual, i);
					adjustVMIndex((WSRP_IntChromosome) individual);
				}
			}
		}
	}
	
	
	private void adjustVMIndex(WSRP_IntChromosome individual){
		int pmCount = 0;
		ArrayList<double[]> pmResource = new ArrayList<double[]>();
		ArrayList<int[]> vmTypesInPm = new ArrayList<int[]>();
		boolean allocationSuccFlag = false;
		pmResource.add(new double[]{pmCpu, pmMem});
		vmTypesInPm.add(new int[vmTypes]);
		
		
		for(int i = 0; i < taskNum; i++){
			// get the vm type from the chromosome
			int vmType = individual.individual[i * 3 + 1];

			for(int j = 0; j < pmCount; j++){
				// First scenario is the current Pm has the capacity for the vm
				if(
					pmResource.get(j)[0] - vmCpu[vmType] >= 0
				&&	pmResource.get(j)[1] - vmMem[vmType] >= 0){
					pmResource.get(j)[0] -= vmCpu[vmType];
					pmResource.get(j)[1] -= vmMem[vmType];
					// If it is the first vmType in the current PM,assign its index as 0.
					// vmTypesInpm.get(pmCount[vmType] is the number of vm
					if(vmTypesInPm.get(pmCount)[vmType] == 0) {
						vmTypesInPm.get(pmCount)[vmType]++;
						individual.individual[i * 3 + 2] = 0;
					} else {
						// If the vmType exists, then generate its index 
						// according to consolidation coefficient, if less than the coefficient
						// then select from existing vms
						if(StdRandom.uniform() <= consolidationCoefficient){
							int indexSelect = StdRandom.uniform(vmTypesInPm.get(pmCount)[vmType]);
							individual.individual[i * 3 + 2] = indexSelect;
						} else {
							vmTypesInPm.get(pmCount)[vmType]++;
							individual.individual[i * 3 + 2] = vmTypesInPm.get(pmCount)[vmType] - 1;
						}
					}
					allocationSuccFlag = true;
					break;
				
				} // End If
			} // End for
			
			
			// There is no suitable pm for the current vm, therefore, we
			// need to create a new pm.
			if(!allocationSuccFlag){
				pmCount++;
				pmResource.add(new double[]{pmCpu, pmMem});
				vmTypesInPm.add(new int[vmTypes]);
				pmResource.get(pmCount)[0] = pmResource.get(pmCount)[0] - vmCpu[vmType];
				pmResource.get(pmCount)[1] = pmResource.get(pmCount)[1] - vmMem[vmType];
				vmTypesInPm.get(pmCount)[vmType]++;
				individual.individual[i * 3 + 2] = 0;
			}
			allocationSuccFlag = false;
		}	
	}
	
	private void swapTasksBetweenPm(WSRP_IntChromosome individual, int index){
		int[] pmIndex = pmCount(individual);
		int chosenIndex, temp;
		// select a random index which is not allocated at the same PM with the current 
		// task
		while(true){
			chosenIndex = StdRandom.uniform(taskNum);
			if(pmIndex[chosenIndex] != pmIndex[index]) break;
		}
		
		// swap service
		temp = individual.individual[chosenIndex * 3];
		individual.individual[chosenIndex * 3] = individual.individual[index * 3];
		individual.individual[index * 3] = temp;
		
		// swap vm type
		temp = individual.individual[chosenIndex * 3 + 1];
		individual.individual[chosenIndex * 3 + 1] = individual.individual[index * 3 + 1];
		individual.individual[index * 3 + 1] = temp;
		
	}
	
	private void mutateVMTypes(WSRP_IntChromosome individual, int index){
		int newType = 0;
		while(true){
			newType = StdRandom.uniform(vmTypes);
			// If the new VM type does not violate the basic requirement of task
			// then it is fine.
			if(vmCpu[newType] - taskCpu[individual.individual[index * 3]] 
								* taskFreq[individual.individual[index * 3]] >= 0
			&& vmMem[newType] - taskMem[individual.individual[index * 3]] 
								* taskFreq[individual.individual[index * 3]] >= 0)
				break;
		}

		// mutate to new type
		individual.individual[index * 3 + 1] = newType;
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
