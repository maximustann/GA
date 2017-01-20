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
		WSRP_IntChromosome chromo = new WSRP_IntChromosome(taskNum * 3);
		int[] taskSequence = generateRandomSequence();
		ArrayList<Integer>[] existingVMTypes = new ArrayList[vmTypes];
		int totalVMNum = 0;

		for(int i = 0; i < vmTypes; i++) existingVMTypes[i] = new ArrayList<Integer>();

		// for each service
		for(int i = 0; i < taskNum; i++){
			// find its most suitable VM
			int suitableType = suitableVM(taskSequence[i]);
			int vmType = StdRandom.uniform(suitableType, vmTypes);
			// Check if there is an existing VM with the vmType, if no, launch a new VM with vmType
			if(existingVMTypes[vmType].isEmpty()){
				existingVMTypes[vmType].add(totalVMNum);
				chromo.individual[i * 3] = taskSequence[i];
				chromo.individual[i * 3 + 1] = vmType;
				chromo.individual[i * 3 + 2] = totalVMNum;
				totalVMNum += 1;
			} else{
			// else, generate a random number u, check if it smaller than consolidation factor
				double u = StdRandom.uniform();
				// If yes, randomly pick an existing VM with the type of vmType
				if(u < consolidationFactor){
					// pickedVMIndex is an index, not the vm number
					int pickedVMIndex = StdRandom.uniform(existingVMTypes[vmType].size());
					int pickedVM = existingVMTypes[vmType].get(pickedVMIndex);
					chromo.individual[i * 3] = taskSequence[i];
					chromo.individual[i * 3 + 1] = vmType;
					chromo.individual[i * 3 + 2] = pickedVM;
				} else {
				// If no, launch a new VM with vmType
					existingVMTypes[vmType].add(totalVMNum);
					chromo.individual[i * 3] = taskSequence[i];
					chromo.individual[i * 3 + 1] = vmType;
					chromo.individual[i * 3 + 2] = totalVMNum;
					totalVMNum += 1;
				}
			}
		}
		return chromo;
	}

//	private WSRP_IntChromosome firstFitChromosome(){
//		WSRP_IntChromosome chromo = new WSRP_IntChromosome(taskNum * 3);
//		ArrayList<int[]> allocation = new ArrayList<int[]>();
//		int[] taskSequence = generateRandomSequence();
//		ArrayList<double[]> pmResource = new ArrayList<double[]>();
//		ArrayList<int[]> vmTypesInPm = new ArrayList<int[]>();
//		int pmCount = 1;
//		boolean allocationSuccFlag = false;
//		pmResource.add(new double[]{pmCpu, pmMem});
//		vmTypesInPm.add(new int[vmTypes]);
//		ArrayList<Integer> pmBound = new ArrayList<Integer>();
//		pmBound.add(0);
//
//		// for each service
//		for(int i = 0; i < taskNum; i++){
//			boolean flag = false;
//			int suitableType = suitableVM(taskSequence[i]);
//			// for each existing PM, j is the index of PM
//			System.out.println(pmCount);
//			for(int j = 0; j < pmCount; j++){
//				// check if the task has been successfully allocated
//				if(flag) {
//					break;
//				}
//				// check if has enough resource for a VM, if yes, then create a VM
//				if(pmResource.get(j)[0] - vmCpu[suitableType] >= 0
//				&& pmResource.get(j)[1] - vmMem[suitableType] >= 0){
//
//					pmResource.get(j)[0] -= vmCpu[suitableType];
//					pmResource.get(j)[1] -= vmMem[suitableType];
//					allocation.add(pmBound.get(j),
//							new int[]{taskSequence[i], suitableType, vmTypesInPm.get(j)[suitableType]});
//					System.out.println("task = " + taskSequence[i] + ", vmType = " + suitableType +
//							", index = " + vmTypesInPm.get(j)[suitableType]);
//					System.out.println("pmBound" + j + " = " + pmBound.get(j));
//					pmBound.set(j, pmBound.get(j) + 1);
//					vmTypesInPm.get(j)[suitableType] += 1;
//					flag = true;
//				}
//			} // end for each PM
//			// no suitable PM
//			if(!flag){
//				pmCount++;
//				pmResource.add(new double[]{pmCpu, pmMem});
//				vmTypesInPm.add(new int[vmTypes]);
//				pmResource.get(pmCount - 1)[0] = pmResource.get(pmCount - 1)[0] - vmCpu[suitableType];
//				pmResource.get(pmCount - 1)[1] = pmResource.get(pmCount - 1)[1] - vmMem[suitableType];
//				allocation.add(new int[]{taskSequence[i], suitableType, vmTypesInPm.get(pmCount - 1)[suitableType]});
//				pmBound.add(pmCount - 1);
//				vmTypesInPm.get(pmCount - 1)[suitableType] += 1;
//				flag = true;
//			}
//		}
//
//		for(int i = 0; i < taskNum; i++){
//			chromo.individual[i * 3] = allocation.get(i)[0];
//			chromo.individual[i * 3 + 1] = allocation.get(i)[1];
//			chromo.individual[i * 3 + 2] = allocation.get(i)[2];
//		}
//		return chromo;
//	}

//	private WSRP_IntChromosome generateChromosome(){
//		WSRP_IntChromosome chromo = new WSRP_IntChromosome(taskNum * 3);
//		int[] taskSequence = generateRandomSequence();
//		ArrayList<double[]> pmResource = new ArrayList<double[]>();
//		ArrayList<int[]> vmTypesInPm = new ArrayList<int[]>();
//		int pmCount = 1;
//		boolean allocationSuccFlag = false;
//		pmResource.add(new double[]{pmCpu, pmMem});
//		vmTypesInPm.add(new int[vmTypes]);
//		boolean flag = false;
//
//		// for each service
//		for(int i = 0; i < taskNum; i++){
//			flag = false;
//			System.out.println("i = " + i);
//			int suitableType = suitableVM(taskSequence[i]);
//			// for each existing PM, j is the index of PM
//			for(int j = 0; j < pmCount; j++){
//				// check if the task has been successfully allocated
//				if(flag) {
//					break;
//				}
//				// check if has enough resource for a VM, if yes, then create a VM
//				if(pmResource.get(j)[0] - vmCpu[suitableType] >= 0
//				&& pmResource.get(j)[1] - vmMem[suitableType] >= 0){
//
//					pmResource.get(j)[0] -= vmCpu[suitableType];
//					pmResource.get(j)[1] -= vmMem[suitableType];
//					chromo.individual[i * 3] = taskSequence[i];
//					chromo.individual[i * 3 + 1] = suitableType;
//					chromo.individual[i * 3 + 2] = vmTypesInPm.get(j)[suitableType];
//					vmTypesInPm.get(j)[suitableType] += 1;
//					flag = true;
//				}
////				else {
////				// else, check if there is an existing VM which is capable of run the service
////					for(int k = suitableType; k < vmTypes; k++){
////						if(vmTypesInPm.get(j)[k] > 0){
////							int index = StdRandom.uniform(vmTypesInPm.get(j)[k]);
////							System.out.println("index = " + index);
////							chromo.individual[i * 3] = taskSequence[i];
////							chromo.individual[i * 3 + 1] = suitableType;
////							chromo.individual[i * 3 + 2] = index;
////							flag = true;
////						}
////					}
////				}
//			} // end for each PM
//			// no suitable PM
//			if(!flag){
//				pmCount++;
//				pmResource.add(new double[]{pmCpu, pmMem});
//				vmTypesInPm.add(new int[vmTypes]);
//				pmResource.get(pmCount - 1)[0] = pmResource.get(pmCount - 1)[0] - vmCpu[suitableType];
//				pmResource.get(pmCount - 1)[1] = pmResource.get(pmCount - 1)[1] - vmMem[suitableType];
//				chromo.individual[i * 3] = taskSequence[i];
//				chromo.individual[i * 3 + 1] = suitableType;
//				chromo.individual[i * 3 + 2] = vmTypesInPm.get(pmCount - 1)[suitableType];
//				vmTypesInPm.get(pmCount - 1)[suitableType] += 1;
//			}
//		}
//		return chromo;
//	}

//
//	private WSRP_IntChromosome generateChromosome(){
//		WSRP_IntChromosome chromo = new WSRP_IntChromosome(taskNum * 3);
//		int[] taskSequence = generateRandomSequence();
//		ArrayList<double[]> pmResource = new ArrayList<double[]>();
//		ArrayList<int[]> vmTypesInPm = new ArrayList<int[]>();
//		int pmCount = 0;
//		boolean allocationSuccFlag = false;
//		pmResource.add(new double[]{pmCpu, pmMem});
//		vmTypesInPm.add(new int[vmTypes]);
//
//		for(int i = 0; i < taskNum; i++){
//			// find the suitable type for this task
//			int suitableType = suitableVM(taskSequence[i]);
//			for(int j = 0; j < pmCount; j++){
//				// check which VM is suitable for this task, randomly pick one from them
//				int suitableCount = 0;
//				// check If there is a suitable type of vm exists
//				for(int k = suitableType; k < vmTypes; k++){
//					if(vmTypesInPm.get(j)[k] > 0){
//						suitableCount += vmTypesInPm.get(j)[k];
//					}
//				}
//				if(suitableCount > 0){
//					int select = StdRandom.uniform(1, suitableCount + 1);
//					int selectType = 0;
//					for(int k = suitableType; k < vmTypes; k++){
//						if(vmTypesInPm.get(j)[k] > 0){
//							if(select - vmTypesInPm.get(j)[k] < 0){
//								select -= 1;
//								selectType = k;
//								break;
//							} else{
//								select -= vmTypesInPm.get(j)[k];
//							}
//						}
//					}
//					chromo.individual[i * 3] = taskSequence[i];
//					chromo.individual[i * 3 + 1] = selectType;
//					chromo.individual[i * 3 + 1] = select;
//				} else{
//					if(pmResource.get(j)[0] - vmCpu[suitableType] >= 0
//					&& pmResource.get(j)[1] - vmMem[suitableType] >= 0){
//						pmResource.get(j)[0] -= vmCpu[suitableType];
//						pmResource.get(j)[1] -= vmMem[suitableType];
//						chromo.individual[i * 3] = taskSequence[i];
//						chromo.individual[i * 3 + 1] = suitableType;
//						chromo.individual[i * 3 + 1] = 0;
//					}
//				}
//			}
//			// no suitable PM, launch a new PM
//			pmCount++;
//			pmResource.add(new double[]{pmCpu, pmMem});
//			vmTypesInPm.add(new int[vmTypes]);
//			pmResource.get(pmCount)[0] = pmResource.get(pmCount)[0] - vmCpu[suitableType];
//			pmResource.get(pmCount)[1] = pmResource.get(pmCount)[1] - vmMem[suitableType];
//			chromo.individual[i * 3] = taskSequence[i];
//			chromo.individual[i * 3 + 1] = suitableType;
//			vmTypesInPm.get(pmCount)[suitableType]++;
//			chromo.individual[i * 3 + 2] = 0;
//		}
//		return chromo;
//	}

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

//	private WSRP_IntChromosome generateChromosome(){
//		WSRP_IntChromosome chromo = new WSRP_IntChromosome(taskNum * 3);
//		int[] taskSequence = generateRandomSequence();
//		ArrayList<double[]> pmResource = new ArrayList<double[]>();
//		ArrayList<int[]> vmTypesInPm = new ArrayList<int[]>();
//		int pmCount = 0;
//		boolean allocationSuccFlag = false;
//		pmResource.add(new double[]{pmCpu, pmMem});
//		vmTypesInPm.add(new int[vmTypes]);
//
//
//		for(int i = 0; i < taskNum; i++){
//			int vmType = 0;
//			// Repeat generate vmType until the vm has the capacity to hold the current task
//			while(true) {
//				vmType = StdRandom.uniform(vmTypes);
//				if(vmCpu[vmType] - taskCpu[taskSequence[i]] * taskFreq[taskSequence[i]] >= 0
//				&& vmMem[vmType] - taskMem[taskSequence[i]] * taskFreq[taskSequence[i]] >= 0)
//					break;
//			}
//			for(int j = 0; j < pmCount; j++){
//				// First scenario is the current Pm has the capacity for the vm
//				// new code
//				// if this type of vm exists in the PM
////				if(vmTypesInPm.get(j)[vmType] != 0){
////					// check if there is enough room for this type of vm, if yes, generate the index from original number + 1
////					if(pmResource.get(j)[0] - vmCpu[vmType] >= 0
////					&& pmResource.get(j)[1] - vmMem[vmType] >= 0){
////						int indexSelect = StdRandom.uniform(vmTypesInPm.get(j)[vmType] + 1);
////						if(indexSelect == vmTypesInPm.get(j)[vmType] + 1){
////						// generate a new vm
////							pmResource.get(j)[0] -= vmCpu[vmType];
////							pmResource.get(j)[1] -= vmMem[vmType];
////							chromo.individual[i * 3] = taskSequence[i];
////							chromo.individual[i * 3 + 1] = vmType;
////							chromo.individual[i * 3 + 1] = indexSelect;
////							vmTypesInPm.get(j)[vmType] += 1;
////
////						} else{
////						// allocate in an old vm
////							chromo.individual[i * 3] = taskSequence[i];
////							chromo.individual[i * 3 + 1] = vmType;
////							chromo.individual[i * 3 + 1] = indexSelect;
////						}
////
////					} else{
////					// allocate in an old vm
////						int indexSelect = StdRandom.uniform(vmTypesInPm.get(j)[vmType]);
////						chromo.individual[i * 3] = taskSequence[i];
////						chromo.individual[i * 3 + 1] = vmType;
////						chromo.individual[i * 3 + 1] = indexSelect;
////					}
////					// If there is same type of vm exists, allocation is definitely successful
////					allocationSuccFlag = true;
////					break;
////				} else{
////					// else, there is no this type of vm exists, check if there is enough room for a new vm
////					if(pmResource.get(j)[0] - vmCpu[vmType] >= 0
////					&& pmResource.get(j)[1] - vmMem[vmType] >= 0){
////					// If yes, generate a new vm here
////						pmResource.get(j)[0] -= vmCpu[vmType];
////						pmResource.get(j)[1] -= vmMem[vmType];
////						chromo.individual[i * 3] = taskSequence[i];
////						chromo.individual[i * 3 + 1] = vmType;
////						chromo.individual[i * 3 + 2] = 0;
////						vmTypesInPm.get(j)[vmType] = 1;
////						// If there is no existing vm, only the new allocation is successful
////						allocationSuccFlag = true;
////						break;
////					}
////				}
//				// new code
//
//				if(
//					pmResource.get(j)[0] - vmCpu[vmType] >= 0
//				&&	pmResource.get(j)[1] - vmMem[vmType] >= 0){
//					pmResource.get(j)[0] -= vmCpu[vmType];
//					pmResource.get(j)[1] -= vmMem[vmType];
//					chromo.individual[i * 3] = taskSequence[i];
//					chromo.individual[i * 3 + 1] = vmType;
//					if(vmTypesInPm.get(j)[vmType] == 0) {
//						vmTypesInPm.get(j)[vmType]++;
//						chromo.individual[i * 3 + 2] = 0;
//					} else {
//						int indexSelect = StdRandom.uniform(vmTypesInPm.get(j)[vmType] + 1);
//						if(indexSelect == vmTypesInPm.get(j)[vmType] + 1){
//							vmTypesInPm.get(j)[vmType]++;
//							chromo.individual[i * 3 + 2] = indexSelect;
//						} else {
//							chromo.individual[i * 3 + 2] = indexSelect;
//						}
//					}
//					allocationSuccFlag = true;
//					break;
//
//				} // End If
//			} // End for
//
//
//			// There is no suitable pm for the current vm, therefore, we
//			// need to create a new pm.
//			if(!allocationSuccFlag){
//				pmCount++;
//				pmResource.add(new double[]{pmCpu, pmMem});
//				vmTypesInPm.add(new int[vmTypes]);
//				pmResource.get(pmCount)[0] = pmResource.get(pmCount)[0] - vmCpu[vmType];
//				pmResource.get(pmCount)[1] = pmResource.get(pmCount)[1] - vmMem[vmType];
//				chromo.individual[i * 3] = taskSequence[i];
//				chromo.individual[i * 3 + 1] = vmType;
//				vmTypesInPm.get(pmCount)[vmType]++;
//				chromo.individual[i * 3 + 2] = 0;
//			}
//			allocationSuccFlag = false;
//		}
//		return chromo;
//	}

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
