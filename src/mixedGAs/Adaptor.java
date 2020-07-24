package mixedGAs;

import GroupGA.src.*;
import PermutationBasedGAForContainerAllocation.DualPermutationChromosome;

import java.util.ArrayList;
import java.util.Arrays;


// This adaptor is used for decoding the output from dual-permutation GA and then return the decoded solution as the input
// for group-GA
// It is essentially a FF decoding process
public class Adaptor {

    private int numOfContainer;
    private int numOfVm;
    private double k;
    private double pmCpu;
    private double pmMem;
    private double pmMaxEnergy;
    private double[] vmCpu;
    private double[] vmMem;
    private double[] taskCpu;
    private double[] taskMem;
    private double vmCpuOverheadRate;
    private double vmMemOverhead;


    public Adaptor(int numOfContainer,
                   int numOfVm,
                   double k,
                   double pmCpu,
                   double pmMem,
                   double pmMaxEnergy,
                   double[] vmCpu,
                   double[] vmMem,
                   double[] taskCpu,
                   double[] taskMem,
                   double vmCpuOverheadRate,
                   double vmMemOverhead){

        this.numOfContainer = numOfContainer;
        this.numOfVm = numOfVm;
        this.k = k;
        this.pmCpu = pmCpu;
        this.pmMem = pmMem;
        this.pmMaxEnergy = pmMaxEnergy;
        this.vmCpu = vmCpu;
        this.vmMem = vmMem;
        this.taskCpu = taskCpu;
        this.taskMem = taskMem;
        this.vmCpuOverheadRate = vmCpuOverheadRate;
        this.vmMemOverhead = vmMemOverhead;

    }


    public GroupGAChromosome[] convert(DualPermutationChromosome[] finalGen){
        GroupGAChromosome[] targetGen = new GroupGAChromosome[finalGen.length];

        for(int i = 0; i < finalGen.length; i++){
            targetGen[i] = decoding(finalGen[i]);
        }
        return targetGen;
    }

    private GroupGAChromosome decoding(DualPermutationChromosome individual){
        GroupGAChromosome groupGAChromosome = new GroupGAChromosome(numOfContainer);
        // Create an empty list of VMs
        ArrayList<VM> vmList = new ArrayList<>();
        // Create an empty list of PMs
        ArrayList<PM> pmList = new ArrayList<>();
        // Check which VM has been used, mark with 1, else 0,
        // the side effect is to fill the vmList and vmStatusList
        allocateContainers(individual, vmList);
        allocateVMsFF(pmList, vmList);

        groupGAChromosome.setPmList(pmList);
        return groupGAChromosome;
    }


    private void allocateVMsFF(ArrayList<PM> pmList, ArrayList<VM> vmList){

        int vmGlobalCounter = 0;
        int[] allocatedVm = new int[vmList.size()];
        Boolean vmAllocated = false;
        while(!vmAllocated){
            PM pm = new PM(pmCpu, pmMem, k, pmMaxEnergy);
            pmList.add(pm);

            for(int i = 0; i < vmList.size(); i++){
                if(allocatedVm[i] == 1) continue;
                VM vm = vmList.get(i);
                if(pm.getCpuRemain() >= vm.getConfigureCpu() &&
                        pm.getMemRemain() >= vm.getConfigureMem()) {
                    pm.allocate(vm);
                    allocatedVm[i] = 1;
                    vmGlobalCounter++;
                }
            }

            if(vmGlobalCounter == vmList.size())
                vmAllocated = true;
        }
    }




    private void allocateContainers(DualPermutationChromosome individual, ArrayList<VM> vmList){

        int numOfContainers = individual.getNumOfContainer();
        int[] allocatedContainers = new int[numOfContainers];

        int containerGlobalCounter = 0;
        for(int i = 0; i < numOfContainers; i++){
            VM vm = new VM(individual.vmTypes[i], vmCpu[individual.vmTypes[i]], vmMem[individual.vmTypes[i]]
                            ,vmCpuOverheadRate, vmMemOverhead);
            vmList.add(vm);

            for(int containerCounter = 0;containerCounter < numOfContainers; containerCounter++){

                // If this container has been allocated, skip
                if(allocatedContainers[containerCounter] == 1)
                    continue;

                // first, we find out the real index of the container
                int indexOfContainer = individual.containerPermutation[containerCounter];
                Container container = new Container(indexOfContainer,
                                    taskCpu[indexOfContainer], taskMem[indexOfContainer]);
                if(vm.getCpuRemain() >= container.getCpu() && vm.getMemRemain() >= container.getMem()){
                    vm.allocate(container);
                    allocatedContainers[containerCounter] = 1;
                    containerGlobalCounter++;
                }
            }

            if(containerGlobalCounter == numOfContainers)
                break;
        }
    }


}
