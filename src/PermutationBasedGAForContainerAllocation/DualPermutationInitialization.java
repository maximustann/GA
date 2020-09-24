package PermutationBasedGAForContainerAllocation;
import algorithms.InitPop;
import algorithms.StdRandom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class DualPermutationInitialization implements InitPop {

    private double vmCpuOverheadRate;
    private double vmMemOverhead;
    private int numOfContainer;
    private int numOfVm;
    private int vmType;
    private double[] vmMem;
    private double[] vmCpu;
    private double[] taskCpu;
    private double[] taskMem;

    // Constructor
    public DualPermutationInitialization(int numOfContainer,
                                         int numOfVm,
                                         int vmType,
                                         int seed,
                                         double[] vmCpu,
                                         double[] vmMem,
                                         double[] taskCpu,
                                         double[] taskMem,
                                         double vmCpuOverheadRate,
                                         double vmMemOverhead
                                         ){
        this.numOfContainer = numOfContainer;
        this.numOfVm = numOfVm;
        this.vmType = vmType;
        this.vmCpu = vmCpu;
        this.vmMem = vmMem;
        this.taskCpu = taskCpu;
        this.taskMem = taskMem;
        this.vmCpuOverheadRate = vmCpuOverheadRate;
        this.vmMemOverhead = vmMemOverhead;

        StdRandom.setSeed(seed);
    }

    // Generate population
    public DualPermutationChromosome[] init(int popSize, int maxVar, double lbound, double ubound){
        DualPermutationChromosome[] pop = new DualPermutationChromosome[popSize];
        for(int i = 0; i < popSize; i++){
            DualPermutationChromosome temp = generateChromosome();
//            repairEmptyVm(temp);
//            repairUnAllocatedContainers(temp);
            pop[i] = temp;
        }
        return pop;
    }

    // Generate a single individual
    private DualPermutationChromosome generateChromosome(){
        DualPermutationChromosome individual = new DualPermutationChromosome(numOfContainer, numOfVm);
        individual.containerPermutation = generateRandomSequence();
        individual.vmTypes = generateRandomTypes();
        return individual;
    }


    private int[] generateRandomTypes(){
        int count = 0;
        int[] vmTypes = new int[numOfVm];
        while(count != numOfVm){
            vmTypes[count] = StdRandom.uniform(vmType);
//            System.out.println("vmTypes[" + count + "] = " + vmTypes[count]);
            count++;
        }
        return vmTypes;
    }

    /**
     * Generate a random sequence of task
     * Basic idea is, first initialize a sequence which has a length of task
     * then, randomly draw one each time until the array list is empty.
     * @return a random sequence
     */
	private int[] generateRandomSequence(){
		int taskCount = 0;
		int[] taskSequence = new int[numOfContainer];
        for (int i = 0; i < numOfContainer; i++) taskSequence[i] = i;
        StdRandom.shuffle(taskSequence);
		return taskSequence;
	}

	private void repairUnAllocatedContainers(DualPermutationChromosome individual){

        // Now, we start to repair
        while(!checkAllContainerAllocated(individual)){
            int[] vmUsed = generateMapping(individual);
            for(int i = 0; i < vmUsed.length; ++i){
                // change the first VM that is not used
                if(vmUsed[i] == 0){
                    individual.vmTypes[i] += 1;
                    if(individual.vmTypes[i] > vmType - 1){
                        System.out.println("Something Wrong!!!!");
                        return;
                    }
                    break;
                }
            }
        }

    }

    private boolean checkAllContainerAllocated(DualPermutationChromosome individual){
	    int globalCounter = 0;
	    int[] vmUsed = new int[numOfVm];

        // Loop through the VM type array
        for(int j = 0; j < numOfVm; ++j){
            double currentVmCpuLeft = vmCpu[individual.vmTypes[j]] - vmCpu[individual.vmTypes[j]] * vmCpuOverheadRate;
            double currentVmMemLeft = vmMem[individual.vmTypes[j]] - vmMemOverhead;

            // Loop through the container Permutation array
            for(;globalCounter < numOfContainer; ++globalCounter){
                if(currentVmCpuLeft >= taskCpu[individual.containerPermutation[globalCounter]]
                        && currentVmMemLeft >= taskMem[individual.containerPermutation[globalCounter]]){
                    currentVmCpuLeft -= taskCpu[individual.containerPermutation[globalCounter]];
                    currentVmMemLeft -= taskMem[individual.containerPermutation[globalCounter]];
                    vmUsed[j] = 1;
                }else{
                    break;
                }
            }
            // if we have loop through all the containers, then break from the loop
            if(globalCounter == numOfContainer) break;
        }

        if(globalCounter < numOfContainer) return false;
        else return true;
    }


	private void repairEmptyVm(DualPermutationChromosome individual){

	    // do we need to do a repairing
        boolean keepRepairing = true;

        // Now, we start to repair
        while(keepRepairing) {

            // did we find any repairing in this round
            boolean foundRepair = false;

            // check the usage of current VMs
            int[] vmUsed = generateMapping(individual);

            // this flag check if this VM is the first used VM check from the backwards
            boolean flag = false;

            // we check VM's usage from backwards
            for (int i = numOfVm - 1; i >= 0; --i) {
                // This step will change the flag to true when the first time found a "1"
                if (vmUsed[i] == 1) flag = true;

                // Now, we found an invalid vm type, adjust it to a bigger size
                if (flag == true && vmUsed[i] == 0) {
                    // search for a bigger size VM type
                    for(int j = 0; j < vmType; j++){
                        if(vmCpu[j] >= vmCpu[individual.vmTypes[i]] && vmMem[j] >= vmMem[individual.vmTypes[i]]){
                            individual.vmTypes[i] = j;
                            break;
                        }
                    }
                    foundRepair = true;
                }
                if(foundRepair) keepRepairing = true;
                else keepRepairing = false;
            }
        }
    }



    private int[] generateMapping(DualPermutationChromosome individual){
        // 1 means we have used this VM, otherwise it is empty
        int[] vmUsed = new int[numOfVm];
        int globalCounter = 0;

        // Loop through the VM type array
        for(int j = 0; j < numOfVm; ++j){
            double currentVmCpuLeft = vmCpu[individual.vmTypes[j]] - vmCpu[individual.vmTypes[j]] * vmCpuOverheadRate;
            double currentVmMemLeft = vmMem[individual.vmTypes[j]] - vmMemOverhead;

            // Loop through the container Permutation array
            for(;globalCounter < numOfContainer; ++globalCounter){
                if(currentVmCpuLeft >= taskCpu[individual.containerPermutation[globalCounter]]
                                        && currentVmMemLeft >= taskMem[individual.containerPermutation[globalCounter]]){
                    currentVmCpuLeft -= taskCpu[individual.containerPermutation[globalCounter]];
                    currentVmMemLeft -= taskMem[individual.containerPermutation[globalCounter]];
                    vmUsed[j] = 1;
                }else{
                    break;
                }
            }
            // if we have loop through all the containers, then break from the loop
            if(globalCounter == numOfContainer) break;
        }

        return vmUsed;
    }




}
