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
import java.util.HashSet;

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
	private double consolidationFactor;
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
			double consolidationFactor
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
		this.consolidationFactor = consolidationFactor;
	}

	public void update(Chromosome individual, double mutationRate){
//		individual.print();
		for(int i = 0; i < taskNum; i++){
			if(StdRandom.uniform() <= mutationRate){
//				System.out.println("Before");
//				individual.print();
//				System.out.println(" , index = " + i);
				mutateVMTypes((WSRP_IntChromosome) individual, i);
//				System.out.println("After");
//				individual.print();
//				System.out.println();
			}
		}
		if(!validation((WSRP_IntChromosome) individual)){
			System.out.println("wrong");
		}
	}


	private boolean validation(WSRP_IntChromosome chromo){
		HashSet<Integer> vmSet = new HashSet<Integer>();
		int topNum = 0;
		for(int i = 0; i < taskNum; i++){
			vmSet.add(chromo.individual[i * 2 + 1]);
			if(topNum < chromo.individual[i * 2 + 1])
				topNum = chromo.individual[i * 2 + 1];
		}
		if(vmSet.size() != topNum + 1){
			chromo.print();
			return false;
		}
		return true;

	}


	private void mutateVMTypes(WSRP_IntChromosome chromo, int index){
		ArrayList<Integer>[] existingVMTypes = findExistingVms(chromo);

		int totalVMNum = 0;
		int currentVM = chromo.individual[index * 2 + 1];
		int currentVMCount = 0;
		for(int i = 0; i < taskNum; i++) {
			if(chromo.individual[i * 2 + 1] >= totalVMNum)
				totalVMNum = chromo.individual[i * 2 + 1] + 1;
			if(chromo.individual[i * 2 + 1] == currentVM)
				currentVMCount++;
		}

//		System.out.println("currentVMCount = " + currentVMCount);
		int task = index;
		int suitableType = suitableVM(task);
		int vmType = StdRandom.uniform(suitableType, vmTypes);


		// Check if there is an existing VM with the vmType, if no, launch a new VM with vmType
		if(existingVMTypes[vmType].isEmpty()){
			int insertPoint = StdRandom.uniform(totalVMNum);
			for(int i = 0; i < taskNum; i++){
				if(chromo.individual[i * 2 + 1] >= insertPoint)
					chromo.individual[i * 2 + 1] += 1;
			}


			existingVMTypes[vmType].add(totalVMNum);
			chromo.individual[index * 2] = vmType;
			chromo.individual[index * 2 + 1] = insertPoint;
			totalVMNum += 1;

			// If after last change the existing vm disappeared
			if(currentVMCount == 1){
				for(int i = 0; i < taskNum; i++){
					if(chromo.individual[i * 2 + 1] > currentVM){
						chromo.individual[i * 2 + 1] = chromo.individual[i * 2 + 1] - 1;
					}
				}
				totalVMNum -= 1;
			}


		} else {
			// else, generate a random number u, check if it smaller than consolidation factor
			double u = StdRandom.uniform();
			// If yes, randomly pick an existing VM with the type of vmType
			if(u < consolidationFactor){
				// pickedVMIndex is an index, not the vm number
				int pickedVMIndex = StdRandom.uniform(existingVMTypes[vmType].size());
				int pickedVM = existingVMTypes[vmType].get(pickedVMIndex);
				chromo.individual[index * 2] = vmType;
				chromo.individual[index * 2 + 1] = pickedVM;

				// After the changing, the current VM is gone, therefore adjustment is needed
				if(currentVMCount == 1 && pickedVM != currentVM){
//					System.out.println("currentVM = " + currentVM +
//										", pickedVM = " + pickedVM +
//										", suitableType = " + suitableType
//										);
					for(int i = 0; i < taskNum; i++){
						if(chromo.individual[i * 2 + 1] > currentVM){
							chromo.individual[i * 2 + 1] = chromo.individual[i * 2 + 1] - 1;
						}
					}
				}
			} else {
				// If no, launch a new VM with vmType, but we need to insert this new VM in somewhere
				int insertPoint = StdRandom.uniform(totalVMNum);
				for(int i = 0; i < taskNum; i++){
					if(chromo.individual[i * 2 + 1] >= insertPoint)
						chromo.individual[i * 2 + 1] += 1;
				}

				existingVMTypes[vmType].add(totalVMNum);
				chromo.individual[index * 2] = vmType;
				chromo.individual[index * 2 + 1] = insertPoint ;
				totalVMNum += 1;

				// If after last change the existing vm disappeared
				if(currentVMCount == 1){
					for(int i = 0; i < taskNum; i++){
						if(chromo.individual[i * 2 + 1] > currentVM){
							chromo.individual[i * 2 + 1] = chromo.individual[i * 2 + 1] - 1;
						}
					}
					totalVMNum -= 1;
				}

			}
		}
	}

	private ArrayList<Integer>[] findExistingVms(WSRP_IntChromosome chromo){
		ArrayList<Integer>[] existingVMTypes = new ArrayList[vmTypes];
		for(int i = 0; i < vmTypes; i++) existingVMTypes[i] = new ArrayList<Integer>();
		for(int i = 0; i < taskNum; i++){
			int type = chromo.individual[i * 2];
			if(existingVMTypes[type].isEmpty())
				existingVMTypes[type].add(chromo.individual[i * 2 + 1]);
			if(!existingVMTypes[type].contains(chromo.individual[i * 2 + 1])){
				existingVMTypes[type].add(chromo.individual[i * 2 + 1]);
			}
		}

		return existingVMTypes;
	}

	private int suitableVM(int taskNo){
		int vmType = 0;
		for(int k = 0; k < vmTypes; k++){
			if(vmCpu[k] - taskCpu[taskNo] * taskFreq[taskNo] >= 0
			&& vmMem[k] - taskMem[taskNo] * taskFreq[taskNo] >= 0){
				vmType = k;
				break;
			}
		}
		return vmType;
	}

}
