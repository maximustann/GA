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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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
		for(int i = 0; i < taskNum; i++){
			if(StdRandom.uniform() <= mutationRate){
				mutateVMTypes3((WSRP_IntChromosome) individual, i);
			}
		}
		if(!validation((WSRP_IntChromosome) individual)){
			throw(new IllegalStateException());
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


	// This is a greedy approach in selecting the consolidation vm
	private void mutateVMTypes3(WSRP_IntChromosome chromo, int index){
		ArrayList<Integer>[] existingVMTypes = findExistingVms(chromo);
		ArrayList<double[]> suitableVmAndFit = new ArrayList<double[]>();
		double[] vmUtil = vmUtilization(chromo);
		double[] vmFit = vmFitness(vmUtil);


		HashMap<Integer, Integer> numType = new HashMap<Integer, Integer>();
		for(int i = 0; i < taskNum; i++){
			if(!numType.containsKey(chromo.individual[i * 2 + 1])){
				numType.put(chromo.individual[i * 2 + 1], chromo.individual[i * 2]);
			}
		}

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
		int suitableVmNum = 0;

		for(int i = suitableType; i < vmTypes; i++){
			for(int j = 0; j < existingVMTypes[i].size(); j++){
				suitableVmNum++;
				suitableVmAndFit.add(new double[]{existingVMTypes[i].get(j), vmFit[existingVMTypes[i].get(j)]});
			}
		}

	// If there is some suitable vm existed, then whether you want to create a new one, or consolidate into old ones
		double u = StdRandom.uniform();
		// If yes, randomly pick an existing VM with the type of vmType
		if(u < consolidationFactor){
			// Here we implement a roulette wheel method to choose an existing suitable vm to consolidate
			// the vm with lower utility get higher chance to be selected
//			suitableVmAndFit = rouletteWheel(suitableVmAndFit);
			// sort the vm Fitness
			Collections.sort(suitableVmAndFit, new Comparator<double[]>() {
				@Override
				public int compare(double[] fitness1, double[] fitness2) {
					int condition = 0;
					if(fitness1[1] - fitness2[1] > 0.0) condition = 1;
					else if(fitness1[1] - fitness2[1] < 0.0) condition = -1;
					else condition = 0;
					return condition;
				}
			});

			// choose the best in terms of fitness
			int chosenVm = (int) suitableVmAndFit.get(0)[0];
//			for(int i = 0; i < suitableVmNum; i++){
//				if(suitableVmAndFit.get(i)[1] > p) {
//					chosenVm = (int) suitableVmAndFit.get(i)[0];
//					break;
//				}
//			}

			chromo.individual[index * 2] = numType.get(chosenVm);
			chromo.individual[index * 2 + 1] = chosenVm;

			// After the changing, the current VM is gone, therefore adjustment is needed
			if(currentVMCount == 1 && chosenVm!= currentVM){
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

			existingVMTypes[suitableType].add(totalVMNum);
			chromo.individual[index * 2] = suitableType;
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

	private void mutateVMTypes4(WSRP_IntChromosome chromo, int index){
		ArrayList<Integer>[] existingVMTypes = findExistingVms(chromo);
		ArrayList<double[]> suitableVmAndFit = new ArrayList<double[]>();
		double[] vmUtil = vmUtilization(chromo);
		double[] vmFit = vmFitness(vmUtil);


		HashMap<Integer, Integer> numType = new HashMap<Integer, Integer>();
		for(int i = 0; i < taskNum; i++){
			if(!numType.containsKey(chromo.individual[i * 2 + 1])){
				numType.put(chromo.individual[i * 2 + 1], chromo.individual[i * 2]);
			}
		}

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
		int suitableVmNum = 0;

		for(int i = suitableType; i < vmTypes; i++){
			for(int j = 0; j < existingVMTypes[i].size(); j++){
				suitableVmNum++;
				suitableVmAndFit.add(new double[]{existingVMTypes[i].get(j), vmFit[existingVMTypes[i].get(j)]});
			}
		}

	// If there is some suitable vm existed, then whether you want to create a new one, or consolidate into old ones
		double u = StdRandom.uniform();
		// If yes, randomly pick an existing VM with the type of vmType
		if(u < consolidationFactor){
			// Here we implement a roulette wheel method to choose an existing suitable vm to consolidate
			// the vm with lower utility get higher chance to be selected
			suitableVmAndFit = rouletteWheel(suitableVmAndFit);
			// sort the vm Fitness
			Collections.sort(suitableVmAndFit, new Comparator<double[]>() {
				@Override
				public int compare(double[] fitness1, double[] fitness2) {
					int condition = 0;
					if(fitness2[1] - fitness1[1] > 0.0) condition = 1;
					else if(fitness2[1] - fitness1[1] < 0.0) condition = -1;
					else condition = 0;
					return condition;
				}
			});

			// else, generate a random number
			double p = StdRandom.uniform();
			int chosenVm = 0;
			for(int i = 0; i < suitableVmNum; i++){
				if(suitableVmAndFit.get(i)[1] > p) {
					chosenVm = (int) suitableVmAndFit.get(i)[0];
					break;
				}
			}

			chromo.individual[index * 2] = numType.get(chosenVm);
			chromo.individual[index * 2 + 1] = chosenVm;

			// After the changing, the current VM is gone, therefore adjustment is needed
			if(currentVMCount == 1 && chosenVm!= currentVM){
				for(int i = 0; i < taskNum; i++){
					if(chromo.individual[i * 2 + 1] > currentVM){
						chromo.individual[i * 2 + 1] = chromo.individual[i * 2 + 1] - 1;
					}
				}
			}
		} else {

			int vmType = StdRandom.uniform(suitableType, vmTypes);
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


	private void mutateVMTypes2(WSRP_IntChromosome chromo, int index){
		ArrayList<Integer>[] existingVMTypes = findExistingVms(chromo);
		ArrayList<double[]> suitableVmAndFit = new ArrayList<double[]>();
		double[] vmUtil = vmUtilization(chromo);
		double[] vmFit = vmFitness(vmUtil);


		HashMap<Integer, Integer> numType = new HashMap<Integer, Integer>();
		for(int i = 0; i < taskNum; i++){
			if(!numType.containsKey(chromo.individual[i * 2 + 1])){
				numType.put(chromo.individual[i * 2 + 1], chromo.individual[i * 2]);
			}
		}

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
		int suitableVmNum = 0;

		for(int i = suitableType; i < vmTypes; i++){
			for(int j = 0; j < existingVMTypes[i].size(); j++){
				suitableVmNum++;
				suitableVmAndFit.add(new double[]{existingVMTypes[i].get(j), vmFit[existingVMTypes[i].get(j)]});
			}
		}

	// If there is some suitable vm existed, then whether you want to create a new one, or consolidate into old ones
		double u = StdRandom.uniform();
		// If yes, randomly pick an existing VM with the type of vmType
		if(u < consolidationFactor){
			// Here we implement a roulette wheel method to choose an existing suitable vm to consolidate
			// the vm with lower utility get higher chance to be selected
			suitableVmAndFit = rouletteWheel(suitableVmAndFit);
			// sort the vm Fitness
			Collections.sort(suitableVmAndFit, new Comparator<double[]>() {
				@Override
				public int compare(double[] fitness1, double[] fitness2) {
					int condition = 0;
					if(fitness2[1] - fitness1[1] > 0.0) condition = 1;
					else if(fitness2[1] - fitness1[1] < 0.0) condition = -1;
					else condition = 0;
					return condition;
				}
			});

			// else, generate a random number
			double p = StdRandom.uniform();
			int chosenVm = 0;
			for(int i = 0; i < suitableVmNum; i++){
				if(suitableVmAndFit.get(i)[1] > p) {
					chosenVm = (int) suitableVmAndFit.get(i)[0];
					break;
				}
			}

			chromo.individual[index * 2] = numType.get(chosenVm);
			chromo.individual[index * 2 + 1] = chosenVm;

			// After the changing, the current VM is gone, therefore adjustment is needed
			if(currentVMCount == 1 && chosenVm!= currentVM){
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

			existingVMTypes[suitableType].add(totalVMNum);
			chromo.individual[index * 2] = suitableType;
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

	private ArrayList<double[]> rouletteWheel(ArrayList<double[]> vmAndFit){
		ArrayList<double[]> vmAndPro = new ArrayList<double[]>();
		int num = vmAndFit.size();
		double sumOfUtil = 0;
		double previous_pro = 0;
		for(int i = 0; i < num; i++){
			sumOfUtil += vmAndFit.get(i)[1];
		}

		for(int i = 0; i < num; i++){
			double fit = previous_pro + vmAndFit.get(i)[1] / sumOfUtil;
			previous_pro = fit;
			vmAndPro.add(new double[]{vmAndFit.get(i)[0], fit});
		}
		return vmAndPro;
	}

	private double[] vmFitness(double[] vmUtil){
		double[] vmFit = new double[vmUtil.length];
		for(int i = 0; i < vmUtil.length; i++) vmFit[i] = 1 / vmUtil[i];
		return vmFit;
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


	/**
	 * calculate the utilization of each vm which is not converted to Pm utilization
	 *
	 * @param chromo individual
	 * @return an array of utilization, each entry's index is the number of the vm
	 */
	private double[] vmUtilization(WSRP_IntChromosome chromo){
		int totalVMNum = 0;
		// the total VM Num of vms must be plus one higher than the vm count
		for(int i = 0; i < taskNum; i++) {
			if(chromo.individual[i * 2 + 1] >= totalVMNum)
				totalVMNum = chromo.individual[i * 2 + 1] + 1;
		}
		double[] utilization = new double[totalVMNum];
		HashMap<Integer, Integer> numType = new HashMap<Integer, Integer>();
		for(int i = 0; i < taskNum; i++){
			if(!numType.containsKey(chromo.individual[i * 2 + 1])){
				numType.put(chromo.individual[i * 2 + 1], chromo.individual[i * 2]);
			}
		}

		// for each service
		for(int i = 0; i < taskNum; i++){

			// If the vm has not been calculated
			if(utilization[chromo.individual[i * 2 + 1]] == 0){
					utilization[chromo.individual[i * 2 + 1]] = taskCpu[i] * taskFreq[i] / vmCpu[chromo.individual[i * 2]];
			} else {
			// add up the utilization that the vm has, if it exceeds 1, set to 1
				double util = taskCpu[i] * taskFreq[i] / vmCpu[chromo.individual[i * 2]];

				if(util + utilization[chromo.individual[i * 2 + 1]] > 1) utilization[chromo.individual[i * 2 + 1]] = 1;
				else utilization[chromo.individual[i * 2 + 1]] += util;
			}
		}

		return utilization;
	}

	// return a array of ArrayList,
	// each entry of the array is a type of vm
	// each entry of the ArrayList is the number of vm
	private ArrayList<Integer>[] findExistingVms(WSRP_IntChromosome chromo){
		ArrayList<Integer>[] existingVMTypes = new ArrayList[vmTypes];
		for(int i = 0; i < vmTypes; i++) existingVMTypes[i] = new ArrayList<Integer>();
		for(int i = 0; i < taskNum; i++){
			int type = chromo.individual[i * 2];
			// If the there is no machine or the current list has no such vm number, then add the vm number in the list
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
