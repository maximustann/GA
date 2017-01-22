/*
 * Boxiong Tan (Maximus Tann)
 * Title:        PSO algorithm framework
 * Description:  PSO algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * Constraint.java - constraint functions for wsrp
 */
package wsrp;

import java.util.ArrayList;
import java.util.HashMap;

import algorithms.Chromosome;
import algorithms.Constraint;

/**
*
* @author Boxiong Tan (Maximus Tann)
* @since PSO framework 1.0
*/
public class WSRP_Constraint implements Constraint {
	/** These are data, therefore, they are defined as static values. */
	private int taskNum;
	private double pmCpu;
	private double pmMem;
	private int vmTypes;
	private double[] vmCpu;
	private double[] vmMem;
	private double[] taskCpu;
	private double[] taskMem;
	private double[] taskFreq;

	public WSRP_Constraint(
				int taskNum,
				int vmTypes,
				double pmCpu,
				double pmMem,
				double pmEnergy,
				double[] vmCpu,
				double[] vmMem,
				double[] taskCpu,
				double[] taskMem,
				double[] taskFreq
				){
		this.taskNum = taskNum;
		this.vmTypes = vmTypes;
		this.pmMem = pmMem;
		this.pmCpu = pmCpu;
		this.vmMem = vmMem;
		this.vmCpu = vmCpu;
		this.taskCpu = taskCpu;
		this.taskMem = taskMem;
		this.taskFreq = taskFreq;

	}


	public void evaluate(Chromosome[] popVar, ArrayList<double[]> popFit){
		for(int i = 0; i < popVar.length; i++){
			popFit.get(i)[5] = violationCount((WSRP_IntChromosome) popVar[i]);
		}
	}


	/**
	 * calculate the utilization of each vm
	 *
	 * @param chromo individual
	 * @return an array of utilization, each entry's index is the number of the vm
	 */
	private int violationCount(WSRP_IntChromosome chromo){
		int violations = 0;
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

				if(utilization[chromo.individual[i * 3 + 2]] + util > 1) {
					utilization[chromo.individual[i * 3 + 2]] = 1;
				} else{
					utilization[chromo.individual[i * 3 + 2]] += util;
				}
			}
		}
		for(int i = 0; i < totalVMNum; i++){
			if(utilization[i] == 1){
				for(int j = 0; j < taskNum; j++){
					if(chromo.individual[j * 3 + 2] == i) violations++;
				}
			}
		}

		return violations;
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

	/**
	 * 
	 *
	 */
	public double[] emptinessMean(WSRP_IntChromosome chromo){
		ArrayList<ArrayList<Integer>> pms = vmsInPm(chromo);
		double cpuMeanEmptiness = 0;
		double memMeanEmptiness = 0;
		
		HashMap<Integer, Integer> numType = new HashMap<Integer, Integer>();
		for(int i = 0; i < taskNum; i++){
			if(!numType.containsKey(chromo.individual[i * 3 + 2])){
				numType.put(chromo.individual[i * 3 + 2], chromo.individual[i * 3 + 1]);
			}
		}
		
		// for each pm
		for(int i = 0; i < pms.size(); i++){
			double pmCpuEmptiness = 0;
			double pmMemEmptiness = 0;
			// for each vm
			for(int j = 0; j < pms.get(i).size(); j++){
				pmCpuEmptiness += vmCpu[numType.get(pms.get(i).get(j))];
				pmMemEmptiness += vmMem[numType.get(pms.get(i).get(j))];
			}
			pmCpuEmptiness /= pmCpu;
			pmMemEmptiness /= pmMem;
			
			cpuMeanEmptiness += pmCpuEmptiness;
			memMeanEmptiness += pmMemEmptiness;
		}
		cpuMeanEmptiness /= pms.size();
		memMeanEmptiness /= pms.size();
		
		return new double[]{cpuMeanEmptiness, memMeanEmptiness};
	}
	
	
	public double pmUtilization(WSRP_IntChromosome chromo){
		double[] vmU = vmUtilization(chromo);
		ArrayList<ArrayList<Integer>> pms = new ArrayList<ArrayList<Integer>>();
		pms = vmsInPm(chromo);
		
		double util = 0;
		for(int i = 0; i < pms.size(); i++){
			double pmUtil = 0;
			for(int j = 0; j < pms.get(i).size(); j++){
				pmUtil += vmU[pms.get(i).get(j)];
			}
			util += pmUtil;
		}
		util /= pms.size();
		return util;
		
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

}
