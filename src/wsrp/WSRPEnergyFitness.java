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
import java.util.HashMap;

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

	public Object call() throws Exception {
		double[] vmUtil = vmUtilization((WSRP_IntChromosome) individual);
		ArrayList<ArrayList<Integer>> pms = vmsInPm((WSRP_IntChromosome) individual);
		ArrayList<Double> pmUtil = pmUtilization(pms, vmUtil);
		double en = round5(energy(pmUtil));
		return new double[]{en, 0};
	}

	/**
	 * calculate the utilization of each vm
	 *
	 * @param chromo individual
	 * @return an array of utilization, each entry's index is the number of the vm
	 */
	private double[] vmUtilization(WSRP_IntChromosome chromo){
		int totalVMNum = 0;
		for(int i = 0; i < taskNum; i++) {
			if(chromo.individual[i * 3 + 2] >= totalVMNum)
				totalVMNum = chromo.individual[i * 3 + 2] + 1;
		}
		double[] utilization = new double[totalVMNum];
		HashMap<Integer, Integer> numType = new HashMap<Integer, Integer>();
		for(int i = 0; i < taskNum; i++){
			if(!numType.containsKey(chromo.individual[i * 3 + 2])){
				numType.put(chromo.individual[i * 3 + 2], chromo.individual[i * 3 + 1]);
			}
		}

		// for the first service
		utilization[0] = taskCpu[chromo.individual[0]]
						* taskFreq[chromo.individual[0]]
							/ vmCpu[chromo.individual[1]];

		// for each service
		for(int i = 1; i < taskNum; i++){

			// If the vm has not been calculated
			if(utilization[chromo.individual[i * 3 + 2]] == 0){
					utilization[chromo.individual[i * 3 + 2]] = taskCpu[chromo.individual[i * 3]]
									* taskFreq[chromo.individual[i * 3]]
									/ vmCpu[chromo.individual[i * 3 + 1]];
			} else {
			// add up the utilization that the vm has, if it exceeds 1, set to 1
				double util = taskCpu[chromo.individual[i * 3]]
						* taskFreq[chromo.individual[i * 3]]
						/ vmCpu[chromo.individual[i * 3 + 1]];

				if(util + utilization[chromo.individual[i * 3 + 2]] > 1) utilization[chromo.individual[i * 3 + 2]] = 1;
				else utilization[chromo.individual[i * 3 + 2]] += util;
			}
		}
		// convert to pm utilization
		for(int i = 0; i < totalVMNum; i++){
//			try{
			utilization[i] = utilization[i] * vmCpu[numType.get(i)] / pmCpu;
//			} catch(NullPointerException e){
//				System.out.println("totalVMNum = " + totalVMNum
//									+ ", i = " + i +
//									", numType.size = " + numType.size() +
//									", utilization.length = " + utilization.length +
//									", numType.get(i) = " + numType.get(i));
//			}
		}
		return utilization;
	}


	private ArrayList<ArrayList<Integer>> vmsInPm(WSRP_IntChromosome chromo){
		ArrayList<ArrayList<Integer>> pm = new ArrayList<ArrayList<Integer>>();
		ArrayList<double[]> pmResource = new ArrayList<double[]>();
		ArrayList<Integer> vmNumList = new ArrayList<Integer>();

		pmResource.add(new double[]{pmCpu - vmCpu[chromo.individual[1]],
									pmMem - vmMem[chromo.individual[1]]});


		pm.add(new ArrayList<Integer>());
		pm.get(0).add(chromo.individual[2]);
		vmNumList.add(chromo.individual[2]);
		int pmCount = 1;
		for(int i = 1; i < taskNum; i++){
			int vmType = chromo.individual[i * 3 + 1];
			int vmNum = chromo.individual[i * 3 + 2];
			double leftCpu = pmResource.get(pmCount - 1)[0];
			double leftMem = pmResource.get(pmCount - 1)[1];
			if(vmNumList.contains(vmNum)) continue;

			// if the current PM is still capable of allocating the vm, update resource left in PM
			if(leftCpu - vmCpu[vmType] >= 0 && leftMem - vmMem[vmType] >= 0){
				leftCpu -= vmCpu[vmType];
				leftMem -= vmMem[vmType];
				pmResource.set(pmCount - 1, new double[]{leftCpu, leftMem});
				pm.get(pmCount - 1).add(vmNum);
			} else {
			// a new PM has to be launched
				pmResource.add(new double[]{pmCpu - vmCpu[vmType],
											pmMem - vmMem[vmType]});
				pm.add(new ArrayList<Integer>());
				pmCount += 1;
				pm.get(pmCount - 1).add(vmNum);
				vmNumList.add(vmNum);
			}
		}
		return pm;
	}

	private ArrayList<Double> pmUtilization(ArrayList<ArrayList<Integer>> pms, double[] vmUtil){
		ArrayList<Double> pmUtil = new ArrayList<Double>();
		for(ArrayList<Integer> vms : pms){
			double util = 0;
			for(int i = 0; i < vms.size(); i++){
				util += vmUtil[vms.get(i)];
			}
			pmUtil.add(util);
		}
		return pmUtil;
	}

	private double energy(ArrayList<Double> pmUtilization) {
		double totalEnergy = 0;
		for(int i = 0; i < pmUtilization.size(); i++){
			double pm = k * pmEnergy + (1 - k) * pmUtilization.get(i) * pmEnergy;
			totalEnergy += pm;
		}
		return totalEnergy;
	}






//	/**
//	 * evaluate each population
//	 */
//	@Override
//	public Object call() throws Exception {
//		double[] fit = new double[2];
//		int[] pmIndex = pmCount((WSRP_IntChromosome) individual);
//		int pmNum = pmIndex[pmIndex.length - 1] + 1;
//		double[] pmUtility = new double[pmNum];
//		int lastPm = 0;
//		int taskCount = 0;
//		for(int i = 0; i < pmNum; i++){
//			ArrayList[] pmVms = new ArrayList[vmTypes];
//			for(int j = 0; j < vmTypes; j++) pmVms[j] = new ArrayList<Double>();
//			while(true){
//				if(taskCount == taskNum || pmIndex[taskCount] != i) break;
//				int taskNum = ((WSRP_IntChromosome) individual).individual[taskCount * 3];
//				int vmType = ((WSRP_IntChromosome) individual).individual[taskCount * 3 + 1];
//				int vmIndex = ((WSRP_IntChromosome) individual).individual[taskCount * 3 + 2];
//				// if the vm has not been created, calculate the utility and add to list
//				if(pmVms[vmType].isEmpty() || pmVms[vmType].size() < vmIndex + 1){
//					double utility = taskCpu[taskNum] * taskFreq[taskNum] / vmCpu[vmType];
//					if(utility > 1) {
//						System.out.println("Wrong! evaluate");
//						System.out.println("taskNum = " + taskNum +
//											", vmType = " + vmType +
//											", vmIndex = " + vmIndex);
//						System.out.println("taskCpu * taskFreq / vmCpu : " +
//											taskCpu[taskNum] + " * " + taskFreq[taskNum] +
//											" / " + vmCpu[vmType] + " = " + utility);
//						utility = 1;
//					}
//					pmVms[vmType].add(utility);
//				} else {
//					// the vm has been created, then add up the vm utility
//					double utility = taskCpu[taskNum] * taskFreq[taskNum] / vmCpu[vmType];
//					utility += (double) pmVms[vmType].get(vmIndex);
//					if(utility > 1) {
//						utility = 1;
//					}
//					pmVms[vmType].set(vmIndex, utility);
//				}
//				taskCount++;
//			} // end While
//
//			// calculate the pmUtility
//			for(int j = 0; j < vmTypes; j++){
//				while(!pmVms[j].isEmpty()){
//					pmUtility[i] += ((double) pmVms[j].get(0)) * vmCpu[j] / pmCpu;
//					pmVms[j].remove(0);
//				}
//			} // end for
//		} // end for
//
//		// calculate the total energy
//		for(int i = 0; i < pmNum; i++){
//			fit[0] += k * pmEnergy + (1 - k) * pmEnergy * pmUtility[i];
//		}
//		fit[0] = round5(fit[0]);
//		return fit;
//	}
//
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
//
//	private int[] pmCount(WSRP_IntChromosome individual){
//		int pmCount = 0;
//		ArrayList<double[]> pmResource = new ArrayList<double[]>();
//		int[] pmIndex = new int[taskNum];
//		pmResource.add(new double[]{pmCpu, pmMem});
//
//		for(int i = 0; i < taskNum; i++){
//			// If there is enough resource in the current PM, then allocate it.
//			if(pmResource.get(pmCount)[0] - vmCpu[individual.individual[i * 3 + 1]] >= 0
//			&& pmResource.get(pmCount)[1] - vmMem[individual.individual[i * 3 + 1]] >= 0) {
//				// If it is allocated in a new vm.
//				if(individual.individual[i * 3 + 2] == 0) {
//					pmResource.get(pmCount)[0] -= vmCpu[individual.individual[i * 3 + 1]];
//					pmResource.get(pmCount)[1] -= vmMem[individual.individual[i * 3 + 1]];
//				}
//			} else {
//				// if there is not enough resource, allocate a new PM
//				pmCount++;
//				pmResource.add(new double[]{pmCpu - vmCpu[individual.individual[i * 3 + 1]],
//											pmMem - vmMem[individual.individual[i * 3 + 1]]});
//			}
//			pmIndex[i] = pmCount;
//		} // end for
//		return pmIndex;
//	}

}
