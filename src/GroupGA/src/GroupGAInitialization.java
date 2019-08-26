package GroupGA.src;

import algorithms.InitPop;
import algorithms.StdRandom;

import java.util.ArrayList;
import java.util.HashSet;

public class GroupGAInitialization implements InitPop {

    private double vmCpuOverheadRate;
    private double vmMemOverhead;
    private int numOfContainer;
    private int numOfVm;
    private int vmTypes;
    private double[] vmMem;
    private double[] vmCpu;
    private double[] taskCpu;
    private double[] taskMem;
    private ArrayList<Container> listOfContainers;


    public GroupGAInitialization(int numOfContainer,
                                 int numOfVm,
                                 int vmTypes,
                                 int seed,
                                 double[] vmCpu,
                                 double[] vmMem,
                                 double[] taskCpu,
                                 double[] taskMem,
                                 double vmCpuOverheadRate,
                                 double vmMemOverhead
    ) {
        this.numOfContainer = numOfContainer;
        this.numOfVm = numOfVm;
        this.vmTypes = vmTypes;
        this.vmCpu = vmCpu;
        this.vmMem = vmMem;
        this.taskCpu = taskCpu;
        this.taskMem = taskMem;
        this.vmCpuOverheadRate = vmCpuOverheadRate;
        this.vmMemOverhead = vmMemOverhead;

        StdRandom.setSeed(seed);


        // create containers
        generateContainers();
    }


    private void generateContainers() {
        for (int i = 0; i < numOfContainer; i++) {
            Container container = new Container(taskCpu[i], taskMem[i]);
            listOfContainers.add(container);
        }
    }


    @Override
    public GroupGAChromosome[] init(
            int popSize,
            int maxVar,
            double lbound,
            double ubound
    ) {
        GroupGAChromosome[] popVar = new GroupGAChromosome[popSize];
        // initialize population
        for (int i = 0; i < popSize; i++) {
            popVar[i] = generateChromosome();
        }
        return popVar;
    }


    /**
     * Generate a random sequence of task
     * Basic idea is, first initialize a sequence which has a length of task
     * then, randomly draw one each time until the array list is empty.
     *
     * @return a random sequence
     */
    private int[] generateRandomSequence() {
        int taskCount = 0;
        int[] taskSequence = new int[numOfContainer];
        ArrayList<Integer> dummySequence = new ArrayList<Integer>();
        for (int i = 0; i < numOfContainer; i++) dummySequence.add(i);
        while (!dummySequence.isEmpty()) {
            int index = StdRandom.uniform(dummySequence.size());
            taskSequence[taskCount++] = dummySequence.get(index);
            dummySequence.remove(index);
        }
        return taskSequence;
    }


    // Allocate containers to VMs
    private ArrayList<VM> allocateToVms(int[] containerSequence) {

        ArrayList<double[]> vmStatus = new ArrayList<>();
        ArrayList<VM> vmList = new ArrayList<>();

        // allocate all the containers
        for (int i = 0; i < numOfContainer; i++) {
            int containerNo = containerSequence[i];

            // If the allocation fails, create a new VM and allocate the container
            if (!allocateContainer(containerNo, vmStatus, vmList)) {
                vmStatus.add(createVM(
                        vmList,
                        containerNo,
                        vmCpu, vmMem,
                        vmCpuOverheadRate,
                        vmMemOverhead));
            }
        }
        return vmList;
    }

    private Boolean allocateContainer(int containerNo,
                                      ArrayList<double[]> vmStatus,
                                      ArrayList<VM> vmList) {

        for (int i = 0; i < vmStatus.size(); i++) {
            double[] vmS = vmStatus.get(i);
            VM vm = vmList.get(i);
            Container container = listOfContainers.get(containerNo);
            double containerCpu = container.getCpu();
            double containerMem = container.getMem();

            // Check if it possible to allocate
            if (vmS[0] >= containerCpu && vmS[1] >= containerMem) {
                vmS[0] -= containerCpu;
                vmS[1] -= containerMem;
                vm.allocate(container);
                return true;
            }
        }


        // If no available VM, return false
        return false;
    }

    // We apply the minimum resource or just-Fit heuristic to create VM
    // Choose a VM with minimum(CPU_v - CPU_c or Mem_v - Mem_c) in the VM list
    private double[] createVM(
            ArrayList<VM> vmList,
            int containerNo,
            double[] vmCpu,
            double[] vmMem,
            double vmCpuOverheadRate,
            double vmMemOverhead) {
        double[] vmRemainResource;

        vmRemainResource = bestFitCreation(
                vmList,
                containerNo,
                vmCpu,
                vmMem,
                vmCpuOverheadRate,
                vmMemOverhead
        );

        return vmRemainResource;
    }


    // We are using a BestFit approach to estimate the VM types that a container need
    private double[] bestFitCreation(
            ArrayList<VM> vmList,
            int containerNo,
            double[] vmCpu,
            double[] vmMem,
            double vmCpuOverheadRate,
            double vmMemOverhead) {

        Container container = listOfContainers.get(containerNo);
        double containerCpu = container.getCpu();
        double containerMem = container.getMem();

        double[] vmRemainResource = new double[2];
        int numOfVm = vmCpu.length;
        double minimumRemainResource = 1;
        int bestVm = 0;

        // find the VM with minimum remaining resources to host this container
        for (int i = 0; i < numOfVm; i++) {
            double normalizedCpuRemain = (vmCpu[i] - containerCpu - vmCpu[i] * vmCpuOverheadRate) / vmCpu[i];
            double normalizedMemRemain = (vmMem[i] - containerMem - vmMemOverhead) / vmMem[i];

            // find which resource is smaller
            double remain = normalizedCpuRemain > normalizedMemRemain ? normalizedCpuRemain : normalizedMemRemain;
//            double volume =  1 / (1 - normalizedCpuRemain) * 1 / (1 - normalizedMemRemain);

            // update the choice of VM
            if (minimumRemainResource > remain) {
                bestVm = i;
                minimumRemainResource = remain;
            }
//            if(maximumVolume < volume){
//                maximumVolume = volume;
//                bestVm = i;
//            }
        }

        // calculate the remaining resources after allocating the container to the chosen VM
        vmRemainResource[0] = vmCpu[bestVm] - containerCpu - vmCpu[bestVm] * vmCpuOverheadRate; // remaining cpu
        vmRemainResource[1] = vmMem[bestVm] - containerMem - vmMemOverhead; // remaining mem

        // create a VM
        VM vm = new VM(bestVm, vmCpu[bestVm], vmMem[bestVm]);

        // allocate the container to this new VM
        vm.allocate(container);

        // update the vmList
        vmList.add(vm);

        return vmRemainResource;
    }


    private GroupGAChromosome generateChromosome() {
        GroupGAChromosome chromo = new GroupGAChromosome();
        // We first generate a random permutation of containers
        int[] containerSequence = generateRandomSequence();

        // We use the heuristic to allocate them into VMs
        ArrayList<VM> vmList = allocateToVms(containerSequence);

        // Now we allocate all the VMs to PMs using FF


        return chromo;
    }

    private ArrayList<PM> allocateToPms(ArrayList<VM> vmList){

        ArrayList<double[]> pmStatus = new ArrayList<>();
        for(int i = 0; i < vmList.size(); i++){
            VM vm = vmList.get(i);
            if(!allocateVms(vm, pmStatus)){
                createPM();
            }

        }


    }

    private Boolean allocateVms(VM vm, ArrayList<double[]> pmStatus){


    }


}
