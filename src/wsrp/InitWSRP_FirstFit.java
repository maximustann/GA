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

import algorithms.*;

/**
 * Bin Pack (First Fit) Initialization of population for WSRP
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
	
	public InitWSRP_FirstFit(
							int taskNum,
							int vmTypes,
							double pmMem,
							double pmCpu,
							double[] vmMem,
							double[] vmCpu,
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
		for(int i = 0; i < popSize; i++){
			popVar[i] = generateChromosome();
		}
		return popVar;
	}
	
	private WSRP_IntChromosome generateChromosome(){
		WSRP_IntChromosome chromo = new WSRP_IntChromosome(taskNum * 3);
		int[] taskSequence = generateRandomSequence();
		ArrayList<double[]> pmResource = new ArrayList<double[]>();
		ArrayList<int[]> vmTypesInPm = new ArrayList<int[]>();
		int pmCount = 0;
		boolean allocationSuccFlag = false;
		pmResource.add(new double[]{pmCpu, pmMem});
		vmTypesInPm.add(new int[vmTypes]);
		
		
		for(int i = 0; i < taskNum; i++){
			int vmType = 0;
			// Repeat generate vmType until the vm has the capacity to hold the current task
			while(true) {
				vmType = StdRandom.uniform(vmTypes);
				if(vmCpu[vmType] - taskCpu[taskSequence[i]] * taskFreq[taskSequence[i]] >= 0 
				&& vmMem[vmType] - taskMem[taskSequence[i]] * taskFreq[taskSequence[i]] >= 0)
					break;
			}
			for(int j = 0; j < pmCount; j++){
				// First scenario is the current Pm has the capacity for the vm
				if(
					pmResource.get(j)[0] - vmCpu[vmType] >= 0
				&&	pmResource.get(j)[1] - vmMem[vmType] >= 0){
					pmResource.get(j)[0] -= vmCpu[vmType];
					pmResource.get(j)[1] -= vmMem[vmType];
					chromo.individual[i * 3] = taskSequence[i];
					chromo.individual[i * 3 + 1] = vmType;
					if(vmTypesInPm.get(pmCount)[vmType] == 0) {
						vmTypesInPm.get(pmCount)[vmType]++;
						chromo.individual[i * 3 + 2] = 0;
					} else {
						int indexSelect = StdRandom.uniform(vmTypesInPm.get(pmCount)[vmType] + 1);
						if(indexSelect == vmTypesInPm.get(pmCount)[vmType] + 1){
							vmTypesInPm.get(pmCount)[vmType]++;
							chromo.individual[i * 3 + 2] = indexSelect;
						} else {
							chromo.individual[i * 3 + 2] = indexSelect;
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
				chromo.individual[i * 3] = taskSequence[i];
				chromo.individual[i * 3 + 1] = vmType;
				vmTypesInPm.get(pmCount)[vmType]++;
				chromo.individual[i * 3 + 2] = 0;
			}
			allocationSuccFlag = false;
		}
		return chromo;
	}
	
	/**
	 * Generate a random sequence of task
	 * Basic idea is, first initialize a sequence which has a length of task
	 * then, randomly draw one each time until the array list is empty.
	 * @return a random sequence
	 */
	private int[] generateRandomSequence(){
		int taskCount = 0;
		int[] taskSequence = new int[taskNum];
		ArrayList<Integer> dummySequence = new ArrayList<Integer>();
		for(int i = 0; i < taskNum; i++) dummySequence.add(i);
		while(!dummySequence.isEmpty()){
			int index = StdRandom.uniform(dummySequence.size());
			taskSequence[taskCount++] = dummySequence.get(index);
			dummySequence.remove(index);
		}
		return taskSequence;
	}
	
}
