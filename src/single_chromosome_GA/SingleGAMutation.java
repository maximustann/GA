package single_chromosome_GA;

import algorithms.Chromosome;
import algorithms.Mutation;
import algorithms.StdRandom;

public class SingleGAMutation implements Mutation {


    private double vmCpuOverheadRate;
    private double vmMemOverhead;
    private int numOfContainer;
    private int vmTypes;
    private double[] vmMem;
    private double[] vmCpu;
    private double[] taskCpu;
    private double[] taskMem;
    public SingleGAMutation(
            int numOfContainer,
            int vmTypes,
            int seed,
            double[] vmCpu,
            double[] vmMem,
            double[] taskCpu,
            double[] taskMem,
            double vmCpuOverheadRate,
            double vmMemOverhead
    ){
        this.numOfContainer = numOfContainer;
        this.vmTypes = vmTypes;
        this.vmCpu = vmCpu;
        this.vmMem = vmMem;
        this.taskCpu = taskCpu;
        this.taskMem = taskMem;
        this.vmCpuOverheadRate = vmCpuOverheadRate;
        this.vmMemOverhead = vmMemOverhead;

        StdRandom.setSeed(seed);
    }

    @Override
    public void update(Chromosome individual, double mutationRate) {
        for(int i = 0; i < numOfContainer; i++){
            if(StdRandom.uniform() <= mutationRate){
                mutateVMTypes((SingleChromosome) individual, i);
            }
        }
        adjustVMIndexes((SingleChromosome) individual);
    }

    private void mutateVMTypes(SingleChromosome individual, int index){
        int[] vector = individual.getAllocationVector();
        vector[index * 2] = generateVMType(index);
    }
    private void adjustVMIndexes(SingleChromosome individual){
        int[] vector = individual.getAllocationVector();
        int previousVMIndex = 0;
        VM previousVM = new VM(vector[1], vmCpu[vector[1]], vmMem[vector[1]],
                vmCpuOverheadRate, vmMemOverhead);
        for(int i = 0; i < numOfContainer; i++){
            Container container = new Container(i, taskCpu[i], taskMem[i]);
            // If the type is repeated
            if(previousVM.getType() == vector[i * 2]){
                // If we cannot allocate the container to this VM
                if(previousVM.getCpuRemain() < taskCpu[i] ||
                    previousVM.getMemRemain() < taskMem[i]){
                    vector[i * 2 + 1] = ++previousVMIndex;
                    previousVM = new VM(vector[i * 2], vmCpu[vector[i * 2]], vmMem[vector[i * 2]],
                                        vmCpuOverheadRate, vmMemOverhead);
                    previousVM.allocate(container);
                } else{
                    previousVM.allocate(container);
                    vector[i * 2 + 1] = previousVMIndex;
                }
                // else we simply create a new VM
            }else{
                previousVM = new VM(vector[i * 2], vmCpu[vector[i * 2]], vmMem[vector[i * 2]],
                                vmCpuOverheadRate, vmMemOverhead);
                vector[i * 2 + 1] = ++previousVMIndex;
                previousVM.allocate(container);
            }
        }
    }

    private int generateVMType(int containerIndex){
        // randomly generate a vm type
        int vmtype = StdRandom.uniform(vmTypes);
        // if the vm type is not big enough for the container, else return
        if(vmCpu[vmtype] - vmCpuOverheadRate * vmCpu[vmtype] < taskCpu[containerIndex]
                || vmMem[vmtype] - vmMemOverhead < taskMem[containerIndex]){
            if(vmCpu[vmtype] - vmCpuOverheadRate * vmCpu[vmtype] < taskCpu[containerIndex] &&
                    vmMem[vmtype] - vmMemOverhead >= taskMem[containerIndex]){

                // find a VM with larger CPU
                for(int i = 0; i < vmTypes; i++){
                    if(i == vmtype) continue;
                    if(vmCpu[i] > vmCpu[vmtype]){
                        // If we find a valid VM, return, otherwise, continue
                        if(vmCpu[i] - vmCpuOverheadRate * vmCpu[i] < taskCpu[containerIndex]
                                || vmMem[i] - vmMemOverhead < taskMem[containerIndex]){
                            continue;
                        }else{
                            return i;
                        }
                    }
                }
            }else if(vmCpu[vmtype] - vmCpuOverheadRate * vmCpu[vmtype] >= taskCpu[containerIndex] &&
                    vmMem[vmtype] - vmMemOverhead < taskMem[containerIndex]){
                // find a VM with larger memory
                for(int i = 0; i < vmTypes; i++){
                    if(i == vmtype) continue;
                    if(vmMem[i] > vmMem[vmtype]){
                        // If we find a valid VM, return, otherwise, continue
                        if(vmCpu[i] - vmCpuOverheadRate * vmCpu[i] < taskCpu[containerIndex]
                                || vmMem[i]- vmMemOverhead < taskMem[containerIndex]){
                            continue;
                        }else{
                            return i;
                        }
                    }
                }
            }else{
                // find a VM with larger resources
                for(int i = 0; i < vmTypes; i++){
                    if(i == vmtype) continue;
                    if(vmCpu[i] > vmCpu[vmtype] && vmMem[i] > vmMem[vmtype]){
                        // If we find a valid VM, return, otherwise, continue
                        if(vmCpu[i] - vmCpuOverheadRate * vmCpu[i] < taskCpu[containerIndex]
                                || vmMem[i] - vmMemOverhead < taskMem[containerIndex]){
                            continue;
                        }else{
                            return i;
                        }
                    }
                }
            }
        }
        return vmtype;
    }
}
