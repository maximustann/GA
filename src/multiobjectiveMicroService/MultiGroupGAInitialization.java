package multiobjectiveMicroService;

import algorithms.InitPop;
import algorithms.StdRandom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import cloudResourceUnit.*;
public class MultiGroupGAInitialization implements InitPop {

    private double vmCpuOverheadRate;
    private double vmMemOverhead;
    private int numOfContainer;
    private double[] vmMem;
    private double[] vmCpu;
    private double[] containerCpu;
    private double[] containerMem;
    private double[] containerAppId;
    private double[] containerMicroServiceId;
    private double[] containerReplicaId;
    private double pmCpu;
    private double pmMem;
    private double k;
    private double maxEnergy;
    private double crushPro;
    private ArrayList<Container> listOfContainers;


    public MultiGroupGAInitialization(
            int numOfContainer,
            double pmCpu,
            double pmMem,
            double k,
            double maxEnergy,
            double crushPro,
            double[] vmCpu,
            double[] vmMem,
            double[] containerCpu,
            double[] containerMem,
            double[] containerAppId,
            double[] containerMicroServiceId,
            double[] containerReplicaId,
            double vmCpuOverheadRate,
            double vmMemOverhead
    ) {
        this.pmCpu = pmCpu;
        this.pmMem = pmMem;
        this.k = k;
        this.maxEnergy = maxEnergy;
        this.crushPro = crushPro;
        this.numOfContainer = numOfContainer;
        this.vmCpu = vmCpu;
        this.vmMem = vmMem;
        this.containerCpu = containerCpu;
        this.containerMem = containerMem;
        this.containerAppId = containerAppId;
        this.containerMicroServiceId = containerMicroServiceId;
        this.containerReplicaId = containerReplicaId;
        this.vmCpuOverheadRate = vmCpuOverheadRate;
        this.vmMemOverhead = vmMemOverhead;
        listOfContainers = new ArrayList<>();


        // create containers
        generateContainers();
    }


    private void generateContainers() {
        for (int i = 0; i < numOfContainer; i++) {
            Container container = new Container(i,
                                                containerCpu[i],
                                                containerMem[i], (int)
                                                containerAppId[i], (int)
                                                containerMicroServiceId[i],(int)
                                                containerReplicaId[i]);
            listOfContainers.add(container);
        }
    }


    @Override
    public MultiGroupGAChromosome[] init(
            int popSize,
            int maxVar,
            double lbound,
            double ubound
    ) {
        MultiGroupGAChromosome[] popVar = new MultiGroupGAChromosome[popSize];
        // initialize population
        for (int i = 0; i < popSize; i++) {
            popVar[i] = generateChromosome();
        }
//        for(int i = 0; i < popSize; i++){
//            System.out.println(popVar[i].getAppList().size());
//            System.out.println(popVar[i].getNumOfContainers());
//        }
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
        int numOfContainer = listOfContainers.size();
        int[] taskSequence = new int[numOfContainer];
        for (int i = 0; i < numOfContainer; i++) taskSequence[i] = i;
        StdRandom.shuffle(taskSequence);
        return taskSequence;
    }


    // Allocate containers to VMs
    private ArrayList<VM> allocateToVms(int[] containerSequence) {
        int numOfContainer = listOfContainers.size();

//        ArrayList<double[]> vmStatus = new ArrayList<>();
        ArrayList<VM> vmList = new ArrayList<>();

        // allocate all the containers
        for (int i = 0; i < numOfContainer; i++) {
            int containerNo = containerSequence[i];

            // If the allocation fails, create a new VM and allocate the container
            if (!allocateContainer(containerNo, vmList)) {
                createVM(vmList,
                        containerNo,
                        (int) containerAppId[containerNo],
                        (int) containerMicroServiceId[containerNo],
                        (int) containerReplicaId[containerNo],
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
            int applicationId,
            int microServiceId,
            int replicaId,
            double[] vmCpu,
            double[] vmMem,
            double vmCpuOverheadRate,
            double vmMemOverhead) {

        randomCreation(
                    vmList,
                    containerNo,
                    applicationId,
                    microServiceId,
                    replicaId,
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
                                    int applicationId,
                                    int microServiceId,
                                    int replicaId,
                                    double[] vmCpu,
                                    double[] vmMem,
                                    double vmCpuOverheadRate,
                                    double vmMemOverhead){

        // We first filter the VMs that is possible to allocate this container
        ArrayList<Integer> validVmTypes = new ArrayList<>();
        for(int i = 0; i < vmCpu.length; i++){
            double myContainerCpu = containerCpu[containerNo];
            double myContainerMem = containerMem[containerNo];
            if(vmCpu[i] >= vmCpuOverheadRate * vmCpu[i] + myContainerCpu && vmMem[i] >= vmMemOverhead + myContainerMem)
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
        vm.allocate(new Container(
                                    containerNo,
                                    containerCpu[containerNo],
                                    containerMem[containerNo],
                                    applicationId,
                                    microServiceId,
                                    replicaId));
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
    public MultiGroupGAChromosome generateChromosome() {
        MultiGroupGAChromosome chromosome = new MultiGroupGAChromosome(numOfContainer);
        // We first generate a random permutation of containers
        int[] containerSequence = generateRandomSequence();

        // We use the heuristic to allocate them into VMs
        ArrayList<VM> vmList = allocateToVms(containerSequence);

        // Now we allocate all the VMs to PMs using FF
        ArrayList<PM> pmList = allocateToPms(vmList);

        // Now we generate an application List

        chromosome.setPmList(pmList);
//        chromosome.updateAppList();
//        chromosome.setAppList(appList);

//        chromosome.checkMotherVm();
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
        PM pm = new PM(pmCpu, pmMem, k, maxEnergy, crushPro);
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
