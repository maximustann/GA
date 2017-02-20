/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Genetic algorithm framework
 * Description:  Genetic algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * InitWSRP_FirstFit.java - An initialization method for WSRP
 */
package wsrp;

import java.util.ArrayList;
import java.util.HashSet;

import algorithms.*;

/**
 *
 * @author Boxiong Tan (Maximus Tann)
 * @since GA framework 1.0
 */
public class InitWSRP_FirstFit implements InitPop{
	private int taskNum;
	private int vmTypes;
	private double pmMem;
	private double pmCpu;
	private double[] vmMem;
	private double[] vmCpu;
	private double[] taskCpu;
	private double[] taskMem;
	private double[] taskFreq;
	private double consolidationFactor;

	public InitWSRP_FirstFit(
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
		this.taskNum = taskNum;
		this.vmTypes = vmTypes;
		this.pmMem = pmMem;
		this.pmCpu = pmCpu;
		this.vmMem = vmMem;
		this.vmCpu = vmCpu;
		this.taskCpu = taskCpu;
		this.taskMem = taskMem;
		this.taskFreq = taskFreq;
		this.consolidationFactor = consolidationFactor;
	}
    /**
     * Generate an array of chromosomes,
     *
     * @param popSize population size
     * @param maxVar the number of variables of a chromosome
     * @param lbound the lower boundary of a variable of a chromosome
     * @param ubound the upper boundary of a variable of a chromosome
     * @return an array of population variables
     */
	@Override
	public WSRP_IntChromosome[] init(
						int popSize,
						int maxVar,
						double lbound,
						double ubound
						) {
		WSRP_IntChromosome[] popVar = new WSRP_IntChromosome[popSize];


		// initialize population
//		popVar[0] = firstFitChromosome();
		for(int i = 0; i < popSize; i++){
			popVar[i] = generateChromosome();
		}
		return popVar;
	}


	private WSRP_IntChromosome generateChromosome(){
		WSRP_IntChromosome chromo = new WSRP_IntChromosome(taskNum * 2);
//		int[] taskSequence = generateRandomSequence();
		ArrayList<Integer>[] existingVMTypes = new ArrayList[vmTypes];
		int totalVMNum = 0;

		for(int i = 0; i < vmTypes; i++) existingVMTypes[i] = new ArrayList<Integer>();

		// for each service
		for(int i = 0; i < taskNum; i++){
			// find its most suitable VM
//			int suitableType = suitableVM(taskSequence[i]);
			int suitableType = suitableVM(i);
			int vmType = StdRandom.uniform(suitableType, vmTypes);
			// Check if there is an existing VM with the vmType, if no, launch a new VM with vmType
			if(existingVMTypes[vmType].isEmpty()){
				existingVMTypes[vmType].add(totalVMNum);
//				chromo.individual[i * 2] = taskSequence[i];
				chromo.individual[i * 2] = vmType;
				chromo.individual[i * 2 + 1] = totalVMNum;
				totalVMNum += 1;
			} else{
			// else, generate a random number u, check if it smaller than consolidation factor
				double u = StdRandom.uniform();
				// If yes, randomly pick an existing VM with the type of vmType
				if(u < consolidationFactor){
					// pickedVMIndex is an index, not the vm number
					int pickedVMIndex = StdRandom.uniform(existingVMTypes[vmType].size());
					int pickedVM = existingVMTypes[vmType].get(pickedVMIndex);
					chromo.individual[i * 2] = vmType;
					chromo.individual[i * 2 + 1] = pickedVM;
				} else {
				// If no, launch a new VM with vmType
					existingVMTypes[vmType].add(totalVMNum);
					chromo.individual[i * 2] = vmType;
					chromo.individual[i * 2 + 1] = totalVMNum;
					totalVMNum += 1;
				}
			}
		}
		return chromo;
	}



	private int suitableVM(int taskNo){
		int vmType = 0;
//		System.out.println("taskNum = " + taskNo);
		for(int k = 0; k < vmTypes; k++){
			if(vmCpu[k] - taskCpu[taskNo] * taskFreq[taskNo] >= 0
			&& vmMem[k] - taskMem[taskNo] * taskFreq[taskNo] >= 0){
//				System.out.println("taskNo = " + taskNo +
//									", vmCpu = " + vmCpu[k] +
//									" ,vmMem = " + vmMem[k] +
//									" ,taskCpu = " + taskCpu[taskNo] +
//									" ,taskMem = " + taskMem[taskNo] +
//									", taskFreq = " + taskFreq[taskNo]);
				vmType = k;
				break;
			}
		}
		return vmType;
	}


	/**
	 * Generate a random sequence of task
	 * Basic idea is, first initialize a sequence which has a length of task
	 * then, randomly draw one each time until the array list is empty.
	 * @return a random sequence
	 */
//	private int[] generateRandomSequence(){
//		int taskCount = 0;
//		int[] taskSequence = new int[taskNum];
//		ArrayList<Integer> dummySequence = new ArrayList<Integer>();
//		for(int i = 0; i < taskNum; i++) dummySequence.add(i);
//		while(!dummySequence.isEmpty()){
//			int index = StdRandom.uniform(dummySequence.size());
//			taskSequence[taskCount++] = dummySequence.get(index);
//			dummySequence.remove(index);
//		}
//		return taskSequence;
//	}

}
