package single_chromosome_GA;

import algorithms.InitPop;
import algorithms.StdRandom;

public class SingleGAInitialization implements InitPop {

    private double vmCpuOverheadRate;
    private double vmMemOverhead;
    private int numOfContainer;
    private int vmTypes;
    private double[] vmMem;
    private double[] vmCpu;
    private double[] taskCpu;
    private double[] taskMem;

    public SingleGAInitialization(
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
    public SingleChromosome[] init(
            int popSize,
            int maxVar,
            double lbound,
            double ubound
    ){
        SingleChromosome[] popVar = new SingleChromosome[popSize];
        for(int i = 0; i < popSize; i++){
            popVar[i] = generateChromosome();
        }
        return popVar;
    }


    private SingleChromosome generateChromosome(){
        SingleChromosome individual = new SingleChromosome(numOfContainer);
        int[] vector = new int[numOfContainer * 2];

        int previousVMIndex = -1;
        VM previousVM = null;
        for(int i = 0; i < numOfContainer; i++){
            Container container = new Container(i, taskCpu[i], taskMem[i]);
            if(previousVM == null
                    || previousVM.getCpuRemain() < taskCpu[i]
                    || previousVM.getMemRemain() < taskMem[i]){
                int vmtype = generateVMType(i);
                vector[i * 2] = vmtype;
                VM vm = new VM(vmtype, vmCpu[vmtype], vmMem[vmtype],
                        vmCpuOverheadRate, vmMemOverhead);
                vm.allocate(container);
                previousVM = vm;
                previousVMIndex = ++previousVMIndex;
            } else {
                previousVM.allocate(container);
            }
            // Check if the container can be allocated to previous VM without any violation
            vector[i * 2] = previousVM.getType();
            vector[i * 2 + 1] = previousVMIndex;
        }
        individual.setAllocationVector(vector);
        return individual;
    }

    private int generateVMType(int containerIndex){
        // randomly generate a vm type
        int vmtype = StdRandom.uniform(vmTypes);
        // if the vm type is not big enough for the container do the following things, else return
        if(vmCpu[vmtype] - vmCpuOverheadRate * vmCpu[vmtype] < taskCpu[containerIndex]
                || vmMem[vmtype] - vmMemOverhead < taskMem[containerIndex]){
            // If the CPU is not big enough
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
                                || vmMem[i] - vmMemOverhead < taskMem[containerIndex]){
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
                        if(vmCpu[i] - vmCpuOverheadRate * vmCpu[vmtype] < taskCpu[containerIndex]
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
