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


	@Override
	public void evaluate(Chromosome[] popVar, ArrayList<double[]> popFit){
		int popSize = popVar.length;
		for(int i = 0; i < popSize; i++){
			popFit.get(i)[5] = countServiceViolations((WSRP_IntChromosome) popVar[i]);
		}
	}

	/**
	 * Count the violation of an individual
	 * @param individual
	 * @return the number of violations
	 */
	public int countServiceViolations(WSRP_IntChromosome individual){
		int violationNum = 0;
		int[] pmIndex = pmCount(individual);
		int pmNum = pmIndex[pmIndex.length - 1] + 1;
		int taskCount = 0;
		// for each pm, count the violations in its vms
		for(int i = 0; i < pmNum; i++){
			ArrayList[] pmVms = new ArrayList[vmTypes];
			ArrayList[] pmVmCount = new ArrayList[vmTypes];
			for(int j = 0; j < vmTypes; j++) {
				pmVms[j] = new ArrayList<Double>();
				pmVmCount[j] = new ArrayList<Integer>();
			}

			while(true){
				// if there is no more task or the task is allocated in a different
				// Pm, then break
				if(taskCount == taskNum || pmIndex[taskCount] != i) break;
				int taskNum = ((WSRP_IntChromosome) individual).individual[taskCount * 3];
				int vmType = ((WSRP_IntChromosome) individual).individual[taskCount * 3 + 1];
				int vmIndex = ((WSRP_IntChromosome) individual).individual[taskCount * 3 + 2];
				// if the vm has not been created, calculate the utility and add to list
				if(pmVms[vmType].isEmpty() || pmVms[vmType].size() < vmIndex + 1){
					double utility = taskCpu[taskNum] * taskFreq[taskNum] / vmCpu[vmType];

					if(utility > 1) {
						violationNum++;
						System.out.println("Wrong! constriant");
						System.out.println("taskNum = " + taskNum +
								", vmType = " + vmType +
								", vmIndex = " + vmIndex);
						System.out.println("taskCpu * taskFreq / vmCpu : " +
								taskCpu[taskNum] + " * " + taskFreq[taskNum] +
								" / " + vmCpu[vmType] + " = " + utility);
						utility = 1;
					}
					pmVmCount[vmType].add(1);
					pmVms[vmType].add(utility);
				} else {
					// the vm has been created, then add up the vm utility
					double utility = taskCpu[taskNum] * taskFreq[taskNum] / vmCpu[vmType];
					utility += (double) pmVms[vmType].get(vmIndex);

					// add up the
					if((double) pmVms[vmType].get(vmIndex) < 1 && utility > 1){
						violationNum += (int) pmVmCount[vmType].get(vmIndex);
					} else {
					// else, just increment one
						violationNum++;
					}
					if(utility > 1) {
						utility = 1;
					}
					int vmNum = (int) pmVmCount[vmType].get(vmIndex) + 1;
					pmVmCount[vmType].set(vmIndex, vmNum);
					pmVms[vmType].set(vmIndex, utility);
				}
				taskCount++;
			} // end While
		} // end for

		return violationNum;
	}


	/**
	 * Count the violation of an individual
	 * @param individual
	 * @return the number of violations
	 */
	public int countVmViolations(WSRP_IntChromosome individual){
		int violationNum = 0;
		int[] pmIndex = pmCount(individual);
		int pmNum = pmIndex[pmIndex.length - 1] + 1;
		int taskCount = 0;
		// for each pm, count the violations in its vms
		for(int i = 0; i < pmNum; i++){
			ArrayList[] pmVms = new ArrayList[vmTypes];
			for(int j = 0; j < vmTypes; j++) {
				pmVms[j] = new ArrayList<Double>();
			}

			while(true){
				// if there is no more task or the task is allocated in a different
				// Pm, then break
				if(taskCount == taskNum || pmIndex[taskCount] != i) break;
				int taskNum = ((WSRP_IntChromosome) individual).individual[taskCount * 3];
				int vmType = ((WSRP_IntChromosome) individual).individual[taskCount * 3 + 1];
				int vmIndex = ((WSRP_IntChromosome) individual).individual[taskCount * 3 + 2];
				// if the vm has not been created, calculate the utility and add to list
				if(pmVms[vmType].isEmpty() || pmVms[vmType].size() < vmIndex + 1){
					double utility = taskCpu[taskNum] * taskFreq[taskNum] / vmCpu[vmType];

					if(utility > 1) {
						violationNum++;
						System.out.println("Wrong!");
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

					// we only add the violation count for the first time which vm is violated
					if((double) pmVms[vmType].get(vmIndex) < 1 && utility > 1){
						violationNum++;
					}

					if(utility > 1) {
						utility = 1;
					}
					pmVms[vmType].set(vmIndex, utility);
				}
				taskCount++;
			} // end While
		} // end for

		return violationNum;
	}


	public ArrayList leftResource(WSRP_IntChromosome individual){
		int violationNum = 0;
		int[] pmIndex = pmCount(individual);
		int pmNum = pmIndex[pmIndex.length - 1] + 1;
		int taskCount = 0;
		ArrayList leftResource = new ArrayList();
		// for each pm, count the violations in its vms
		for(int i = 0; i < pmNum; i++){
			ArrayList[] pmVms = new ArrayList[vmTypes];
			ArrayList[] vmResource = new ArrayList[vmTypes];
			for(int j = 0; j < vmTypes; j++) {
				pmVms[j] = new ArrayList<Double>();
				vmResource[j] = new ArrayList<double[]>();
			}

			while(true){
				// if there is no more task or the task is allocated in a different
				// Pm, then break
				if(taskCount == taskNum || pmIndex[taskCount] != i) break;
				int taskNum = ((WSRP_IntChromosome) individual).individual[taskCount * 3];
				int vmType = ((WSRP_IntChromosome) individual).individual[taskCount * 3 + 1];
				int vmIndex = ((WSRP_IntChromosome) individual).individual[taskCount * 3 + 2];

				// if the vm has not been created, calculate the utility and add to list
				if(pmVms[vmType].isEmpty() || (pmVms[vmType].size() < vmIndex + 1)){
					double utility = taskCpu[taskNum] * taskFreq[taskNum] / vmCpu[vmType];
					double[] resource = new double[]{pmCpu - taskCpu[taskNum] * taskFreq[taskNum],
													  pmMem - taskMem[taskNum] * taskFreq[taskNum]};
					vmResource[vmType].add(resource);

					if(utility > 1) {
						violationNum++;
						System.out.println("Wrong!");
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

					// current resource left
					double cpu = ((double[]) vmResource[vmType].get(vmIndex))[0] - taskCpu[taskNum] * taskFreq[taskNum];
					double mem = ((double[]) vmResource[vmType].get(vmIndex))[1] - taskMem[taskNum] * taskFreq[taskNum];

					// we only add the violation count for the first time which vm is violated
					if((double) pmVms[vmType].get(vmIndex) < 1 && utility > 1){
						violationNum++;
					}

					if(utility > 1) {
						utility = 1;
					}
					vmResource[vmType].set(vmIndex, new double[]{cpu, mem});
					pmVms[vmType].set(vmIndex, utility);
				}
				taskCount++;
			} // end While
			leftResource.add(vmResource);
		} // end for

		return leftResource;
	}

	public int[] pmCount(WSRP_IntChromosome individual){
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
