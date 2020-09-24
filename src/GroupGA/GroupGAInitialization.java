package GroupGA;

import algorithms.InitPop;
import algorithms.StdRandom;

import java.util.ArrayList;

public class GroupGAInitialization implements InitPop {

    private double vmCpuOverheadRate;
    private double vmMemOverhead;
    private int numOfContainer;
    private double[] vmMem;
    private double[] vmCpu;
    private double[] taskCpu;
    private double[] taskMem;
    private double pmCpu;
    private double pmMem;
    private double k;
    private double maxEnergy;
    private ArrayList<Container> listOfContainers;


    public GroupGAInitialization(int numOfContainer,
                                 int seed,
                                 double pmCpu,
                                 double pmMem,
                                 double k,
                                 double maxEnergy,
                                 double[] vmCpu,
                                 double[] vmMem,
                                 double[] taskCpu,
                                 double[] taskMem,
                                 double vmCpuOverheadRate,
                                 double vmMemOverhead
    ) {
        this.pmCpu = pmCpu;
        this.pmMem = pmMem;
        this.k = k;
        this.maxEnergy = maxEnergy;
        this.numOfContainer = numOfContainer;
        this.vmCpu = vmCpu;
        this.vmMem = vmMem;
        this.taskCpu = taskCpu;
        this.taskMem = taskMem;
        this.vmCpuOverheadRate = vmCpuOverheadRate;
        this.vmMemOverhead = vmMemOverhead;
        listOfContainers = new ArrayList<>();

        StdRandom.setSeed(seed);


        // create containers
        generateContainers();
    }


    private void generateContainers() {
        for (int i = 0; i < numOfContainer; i++) {
            Container container = new Container(i, taskCpu[i], taskMem[i]);
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
     *
     * @return a random sequence
     */
    private int[] generateRandomSequence() {
        int taskCount = 0;
        int[] taskSequence = new int[numOfContainer];
        for (int i = 0; i < numOfContainer; i++) taskSequence[i] = i;
        StdRandom.shuffle(taskSequence);
        return taskSequence;
    }


    // Allocate containers to VMs
    private ArrayList<VM> allocateToVms(int[] containerSequence) {

//        ArrayList<double[]> vmStatus = new ArrayList<>();
        ArrayList<VM> vmList = new ArrayList<>();

        // allocate all the containers
        for (int i = 0; i < numOfContainer; i++) {
            int containerNo = containerSequence[i];

            // If the allocation fails, create a new VM and allocate the container
            if (!allocateContainer(containerNo, vmList)) {
                createVM(vmList,
                        containerNo,
                        vmCpu, vmMem,
                        vmCpuOverheadRate,
                        vmMemOverhead);
            }
        }
        return vmList;
    }

    private Boolean allocateContainer(int containerNo,
                                      ArrayList<VM> vmList) {

        for (int i = 0; i < vmList.size(); i++) {
            VM vm = vmList.get(i);
            Container container = listOfContainers.get(containerNo);
            double containerCpu = container.getCpu();
            double containerMem = container.getMem();

            // Check if it possible to allocate
            if (vm.getCpuRemain() >= containerCpu && vm.getMemRemain() >= containerMem) {
                vm.allocate(container.clone());
                return true;
            }
        }


        // If no available VM, return false
        return false;
    }

    // We apply the minimum resource or just-Fit heuristic to create VM
    // Choose a VM with minimum(CPU_v - CPU_c or Mem_v - Mem_c) in the VM list
    private void createVM(
            ArrayList<VM> vmList,
            int containerNo,
            double[] vmCpu,
            double[] vmMem,
            double vmCpuOverheadRate,
            double vmMemOverhead) {

        randomCreation(
                    vmList,
                    containerNo,
                    vmCpu,
                    vmMem,
                    vmCpuOverheadRate,
                    vmMemOverhead);

//        bestFitCreation(
//                vmList,
//                containerNo,
//                vmCpu,
//                vmMem,
//                vmCpuOverheadRate,
//                vmMemOverhead
//        );

    }

    // We randomly create a VM type
    private void randomCreation(ArrayList<VM> vmList,
                                    int containerNo,
                                    double[] vmCpu,
                                    double[] vmMem,
                                    double vmCpuOverheadRate,
                                    double vmMemOverhead){

        // We first filter the VMs that is possible to allocate this container
        ArrayList<Integer> validVmTypes = new ArrayList<>();
        for(int i = 0; i < vmCpu.length; i++){
            double containerCpu = taskCpu[containerNo];
            double containerMem = taskMem[containerNo];
            if(vmCpu[i] >= vmCpuOverheadRate * vmCpu[i] + containerCpu && vmMem[i] >= vmMemOverhead + containerMem)
                validVmTypes.add(i);
        }

        // check
        if(validVmTypes.size() == 0){
            throw new IllegalStateException("No valid VM can be created");
        }

        // Randomly select one VM type to allocate the container
        int index = StdRandom.uniform(validVmTypes.size());
        int chosenVM = validVmTypes.get(index);

        // create the VM and allocate the container
        VM vm = new VM(chosenVM, vmCpu[chosenVM], vmMem[chosenVM], vmCpuOverheadRate, vmMemOverhead);
        vm.allocate(new Container(containerNo, taskCpu[containerNo], taskMem[containerNo]));
        vmList.add(vm);

    }


    // We are using a BestFit approach to estimate the VM types that a container need
    private void bestFitCreation(
            ArrayList<VM> vmList,
            int containerNo,
            double[] vmCpu,
            double[] vmMem,
            double vmCpuOverheadRate,
            double vmMemOverhead) {

        Container container = listOfContainers.get(containerNo);
        double containerCpu = container.getCpu();
        double containerMem = container.getMem();

        int numOfVm = vmCpu.length;
        double minimumRemainResource = 1;
        Integer bestVm = null;

        // find the VM with minimum remaining resources to host this container
        for (int i = 0; i < numOfVm; i++) {
            // If this VM cannot satisfy the requirement of the container
            if(vmCpu[i] < containerCpu + vmCpu[i] * vmCpuOverheadRate ||
                    vmMem[i] < containerMem + vmMemOverhead) continue;

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

        // check
        if(bestVm == null){
            throw new IllegalStateException("No valid VM to be created");
        }

        // create a VM
        VM vm = new VM(bestVm, vmCpu[bestVm], vmMem[bestVm], vmCpuOverheadRate, vmMemOverhead);

        // allocate the container to this new VM
        vm.allocate(container.clone());

        // update the vmList
        vmList.add(vm);

    }


    /**
     * 3 steps:
     *  1. generate a permutation sequence of containers
     *  2. use heuristic to create a type of VMs
     *  3. allocate containers to VMs using FF
     *  4. allocate VMs to PMs using FF
     *
     * @return chromosome
     */
    public GroupGAChromosome generateChromosome() {
        GroupGAChromosome chromosome = new GroupGAChromosome(numOfContainer);
        // We first generate a random permutation of containers
        int[] containerSequence = generateRandomSequence();

        // We use the heuristic to allocate them into VMs
        ArrayList<VM> vmList = allocateToVms(containerSequence);

        // Now we allocate all the VMs to PMs using FF
        ArrayList<PM> pmList = allocateToPms(vmList);

        chromosome.setPmList(pmList);

        return chromosome;
    }

    private ArrayList<PM> allocateToPms(ArrayList<VM> vmList){

        ArrayList<double[]> pmStatus = new ArrayList<>();
        ArrayList<PM> pmList = new ArrayList<>();

        for(VM vm:vmList){
            if(!allocateVms(vm, pmStatus, pmList)){
                pmStatus.add(createPM(pmList, vm));
            }
        }
        return pmList;
    }

    private double[] createPM(ArrayList<PM> pmList, VM vm){
        PM pm = new PM(pmCpu, pmMem, k, maxEnergy);
        pm.allocate(vm);
        pmList.add(pm);
        double[] pmStatus = new double[2];
        pmStatus[0] = pm.getCpuRemain();
        pmStatus[1] = pm.getMemRemain();
        return pmStatus;
    }

    private Boolean allocateVms(VM vm, ArrayList<double[]> pmStatus, ArrayList<PM> pmList){

        double vmCpu = vm.getConfigureCpu();
        double vmMem = vm.getConfigureMem();


        for(int i = 0; i < pmStatus.size(); i++){
            double[] pmS = pmStatus.get(i);
            PM pm = pmList.get(i);

            // Simply use FF to allocate the VM
            if(pmS[0] >= vmCpu && pmS[1] >= vmMem){
                pmS[0] -= vmCpu;
                pmS[1] -= vmMem;
                pm.allocate(vm);
                return true;
            }
        }

        return false;

    }


}
