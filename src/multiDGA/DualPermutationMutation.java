package multiDGA;

import algorithms.Chromosome;
import algorithms.Mutation;
import algorithms.StdRandom;

import java.util.ArrayList;

/**
 * For now, we implemented switch mutation operator and change VM types operator.
 */
public class DualPermutationMutation implements Mutation {

    private int vmTypes;
    private int numOfVm;
    private int numOfContainer;
    private double vmCpuOverheadRate;
    private double vmMemOverhead;
    private double[] vmCpu;
    private double[] vmMem;
    private double[] taskCpu;
    private double[] taskMem;

    public DualPermutationMutation(int vmTypes,
                                   int numOfVm,
                                   int numOfContainer,
                                   double[] vmCpu,
                                   double[] vmMem,
                                   double[] taskCpu,
                                   double[] taskMem,
                                   double vmCpuOverheadRate,
                                   double vmMemOverhead,
                                   int seed){
        this.vmTypes = vmTypes;
        this.numOfContainer = numOfContainer;
        this.numOfVm = numOfVm;
        this.vmCpu = vmCpu;
        this.vmMem = vmMem;
        this.taskCpu = taskCpu;
        this.taskMem = taskMem;
        this.vmCpuOverheadRate = vmCpuOverheadRate;
        this.vmMemOverhead = vmMemOverhead;
        StdRandom.setSeed(seed);
    }


    @Override
    public void update(Chromosome individual, double mutationRate){
        update((DualPermutationChromosome) individual, mutationRate);
//        repairEmptyVm((DualPermutationChromosome) individual);
//        repairUnAllocatedContainers((DualPermutationChromosome) individual);
    }

    public void update(DualPermutationChromosome individual, double mutationRate){
        // We switch two values in the container Permutation chromosome
        if(StdRandom.uniform() < mutationRate){
            switchTwoContainers(individual);
//            regenerateSequence(individual);
        }

        // We mutate the type of a VM
        changeVMTypesMutation(individual, mutationRate);
    }

    private void regenerateSequence(DualPermutationChromosome individual){
        int[] taskSequence = generateRandomSequence();
        for(int i = 0; i < numOfContainer; i++){
            individual.containerPermutation[i] = taskSequence[i];
        }
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
        ArrayList<Integer> dummySequence = new ArrayList<Integer>();
        for(int i = 0; i < numOfContainer; i++) dummySequence.add(i);
        while(!dummySequence.isEmpty()){
            int index = StdRandom.uniform(dummySequence.size());
            taskSequence[taskCount++] = dummySequence.get(index);
            dummySequence.remove(index);
        }
        return taskSequence;
    }

    private void changeVMTypesMutation(DualPermutationChromosome individual, double mutationRate){
        for(int i = 0; i < individual.vmTypes.length; ++i){
            if(StdRandom.uniform() < mutationRate){
//                individual.vmTypes[i] = StdRandom.uniform(vmTypes);
                individual.vmTypes[i] = StdRandom.uniform(vmTypes);
            }
        }
    }


    private void switchTwoContainers(DualPermutationChromosome individual){
        int point1, point2;
        point1 = StdRandom.uniform(0, individual.containerPermutation.length - 1);
        point2 = StdRandom.uniform(point1, individual.containerPermutation.length);
        int temp = individual.containerPermutation[point1];
        individual.containerPermutation[point1] = individual.containerPermutation[point2];
        individual.containerPermutation[point2] = temp;
    }

    private void repairUnAllocatedContainers(DualPermutationChromosome individual){

        // Now, we start to repair
        while(!checkAllContainerAllocated(individual)){
            int[] vmUsed = generateMapping(individual);
            for(int i = 0; i < vmUsed.length; ++i){
                // change the first VM that is not used
                if(vmUsed[i] == 0){
                    individual.vmTypes[i] += 1;
                    if(individual.vmTypes[i] > vmTypes - 1){
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
                    individual.vmTypes[i] += 1;
                    // search for a bigger size VM type
//                    for(int j = 0; j < vmTypes; j++){
//                        if(vmCpu[j] >= vmCpu[individual.vmTypes[i]] && vmMem[j] >= vmMem[individual.vmTypes[i]]){
//                            individual.vmTypes[i] = j;
//                            break;
//                        }
//                    }
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
//            System.out.println("individual.vmTypes[" + j + "] = " + individual.vmTypes[j]);
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
