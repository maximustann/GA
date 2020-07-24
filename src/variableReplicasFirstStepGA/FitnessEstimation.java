package variableReplicasFirstStepGA;

import algorithms.StdRandom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import cloudResourceUnit.*;
public class FitnessEstimation {

//    private ArrayList<PM> pmList;
    private ArrayList<Application> appList;
    private double pmCpu;
    private double pmMem;
    private double k;
    private double maxPmEnergy;
    private double crushPro;
    private double vmCpuOverheadRate;
    private double vmMemOverhead;
    private double[] containerCpu;
    private double[] containerMem;
    private double[] containerAppId;
    private double[] containerMicroServiceId;
    private double[] containerReplicaId;
    private double[] vmCpu;
    private double[] vmMem;



    public FitnessEstimation(
            double pmCpu,
            double pmMem,
            double k,
            double maxPmEnergy,
            double crushPro,
            double vmCpuOverheadRate,
            double vmMemOverhead,
            double[] containerCpu,
            double[] containerMem,
            double[] containerAppId,
            double[] containerMicroServiceId,
            double[] containerReplicaId,
            double[] vmCpu,
            double[] vmMem){

        this.pmCpu = pmCpu;
        this.pmMem = pmMem;
        this.k = k;
        this.maxPmEnergy = maxPmEnergy;
        this.crushPro = crushPro;
        this.vmCpuOverheadRate = vmCpuOverheadRate;
        this.vmMemOverhead = vmMemOverhead;
        this.containerCpu = containerCpu;
        this.containerMem = containerMem;
        this.containerAppId = containerAppId;
        this.containerMicroServiceId = containerMicroServiceId;
        this.containerReplicaId = containerReplicaId;
        this.vmCpu = vmCpu;
        this.vmMem = vmMem;
        appList = new ArrayList<>();
    }



    // estimate the fitness of energy and availability
    // 1. use 100 random generated permutation to estimate the average energy consumption
    // 2. evaluate the upper bound of availability
    public double[] estimate(int permutationNum){

        double energyFitnessValue = energyFitness(permutationNum);
        double availFitnessValue = availFitness();
        return new double[]{energyFitnessValue, availFitnessValue};
    }

    // 1. randomly create a number of permutations of containers
    // 2. and allocate them using FF/BF
    // 3. return the average of their fitness
    private double energyFitness(int permutationNum){

        double totalEnergy = 0;
        for(int i = 0; i < permutationNum; ++i) {
            int[] randomSequence = generateRandomSequence();
            ArrayList<PM> pmList = allocate(randomSequence);
            totalEnergy += energy(pmList);
        }
        return totalEnergy / permutationNum;
    }

    private void printArray(int[] array){
        for(int i = 0; i < array.length; ++i) System.out.print(array[i] + " ");
        System.out.println();
    }


    private double availFitness(){
        constructAppList();
        ArrayList<PM> pmList = new ArrayList<>();

        for(Application application:appList){
            ArrayList<MicroService> microServices = application.getMicroServiceList();
            for(MicroService microService:microServices){
                allocateToPms(microService, pmList);
            }
        }

        return updateAverageAvailability();
    }

    private void allocateToPms(MicroService microService, ArrayList<PM> pmList){
        ArrayList<Container> containers = microService.getContainerList();
        // each container must be allocate to a different PM
        ArrayList<PM> skipList = new ArrayList<>();
        for(Container container:containers) {
            // If fails, create a new PM
            if(!allocateContainerAvail(container, pmList, skipList)){
                PM pm = new PM(pmCpu, pmMem, k, maxPmEnergy, crushPro);
                VM vm = createVM(container, pm);
                pm.allocate(vm);
                pmList.add(pm);
            }

            skipList.add(container.getPm());
        }
    }

    private boolean allocateContainerAvail(Container container, ArrayList<PM> pmList, ArrayList<PM> skipList){
        for(PM pm:pmList){
            // skip PM
            if(skip(pm, skipList)) continue;
            ArrayList<VM> vmList = pm.getVmList();
            for(VM vm:vmList){
                if(
                        vm.getCpuRemain() >= container.getCpu() &&
                                vm.getMemRemain() >= container.getMem()){
                    vm.allocate(container);
                    return true;
                }
            }
        }

        return false;
    }

    private boolean skip(PM pm, ArrayList<PM> skipList){
        if(skipList.size() == 0) return false;
        for(PM skipPm:skipList){
            if(pm.getID() == skipPm.getID()) return true;
        }
        return false;
    }


    public double updateAverageAvailability(){
        double availability = 0;
        for(Application application:appList){
            availability += application.availability();
        }

        return availability / appList.size();
    }

    private boolean appExist(ArrayList<Application> appList, int applicationId){
        for(Application app:appList){
            if(app.getID() == applicationId) return true;
        }
        return false;
    }

    private Application getApp(ArrayList<Application> appList, int applicationId){
        for(Application app:appList){
            if(app.getID() == applicationId) return app;
        }
        return null;
    }

    private boolean microExist(ArrayList<MicroService> microList, int microId){
        for(MicroService microService:microList){
            if(microService.getID() == microId) return true;
        }
        return false;
    }

    private MicroService getMicroService(ArrayList<MicroService> microList, int microId){
        for(MicroService microService:microList){
            if(microService.getID() == microId) return microService;
        }
        return null;
    }

    private void constructAppList(){
        appList = new ArrayList<>();
        int numOfContainers = containerCpu.length;
        for(int i = 0; i < numOfContainers; i++){
            int appId = (int) containerAppId[i];
            int microId = (int) containerMicroServiceId[i];
            Application app;
            MicroService microService;
            if(appExist(appList, appId)){
                app = getApp(appList, appId);
            } else {
                app = new Application(appId);
                appList.add(app);
            }

            ArrayList<MicroService> microServices = app.getMicroServiceList();
            if(microExist(microServices, microId)){
                microService = getMicroService(microServices, microId);
            } else {
                microService = new MicroService(microId);
                microServices.add(microService);
            }
            microService.addContainer(new Container(i, containerCpu[i], containerMem[i],
                    (int) containerAppId[i], (int) containerMicroServiceId[i],
                    (int) containerReplicaId[i]));
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
        int numOfContainer = containerCpu.length;
        int[] taskSequence = new int[numOfContainer];
        for (int i = 0; i < numOfContainer; i++) taskSequence[i] = i;
        StdRandom.shuffle(taskSequence);
        return taskSequence;
    }


    // return a full list of PMs
    // int[] sequence contains a random sequence of containers' indexes
    private ArrayList<PM> allocate(int[] sequence){

        int numOfContainer = sequence.length;
//        ArrayList<double[]> vmStatus = new ArrayList();
        ArrayList<PM> pmList = new ArrayList<>();


        // allocate containers to VMs
        for(int i = 0; i < numOfContainer; ++i){
            int containerIndex = sequence[i];

            Container container = new Container(
                                        i,
                                        containerCpu[containerIndex],
                                        containerMem[containerIndex],
                                        (int) containerAppId[containerIndex],
                                        (int) containerMicroServiceId[containerIndex],
                                        (int) containerReplicaId[containerIndex]
                                            );

            // allocate this container, if not success, create one VM and allocate the container
            if(!allocateContainer(container, pmList)) {
                PM pm = new PM(pmCpu, pmMem, k, maxPmEnergy, crushPro);
                VM vm = createVM(container, pm);
                pm.allocate(vm);
                pmList.add(pm);
            }
        }
//        for(PM pm:pmList){
//            pm.print();
//        }

        return pmList;
    }

    // Using FF to allocate containers, if success, return true, else false
    // The side effect is to update the vmStatus
    private Boolean allocateContainer(Container container, ArrayList<PM> pmList){
        for(PM pm:pmList){
            ArrayList<VM> vmList = pm.getVmList();
            for(VM vm:vmList){
                if(
                        vm.getCpuRemain() >= container.getCpu() &&
                        vm.getMemRemain() >= container.getMem()){
                    vm.allocate(container);
                    return true;
                }
            }
        }
        return false;
    }

    // We apply the minimum resource or just-Fit heuristic to create VM
    // Choose a VM with minimum(CPU_v - CPU_c or Mem_v - Mem_c) in the VM list
    private VM createVM(Container container, PM pm){
        return bestFitCreation(container, pm);
    }

    // bestFit approach
    private VM bestFitCreation(Container container, PM pm){

        int numOfVm = vmCpu.length;
        double minimumRemainResource = 1;
        double maximumVolume = 0;
        int bestVm = 0;

        // find the VM with minimum remaining resources to host this container
        for(int i = 0; i < numOfVm; i++){
            if(vmCpu[i] < container.getCpu() + vmCpu[i] * vmCpuOverheadRate ||
                    vmMem[i] < container.getMem() + vmMemOverhead) continue;


            double normalizedCpuRemain = (vmCpu[i] - container.getCpu() - vmCpu[i] * vmCpuOverheadRate) / vmCpu[i];
            double normalizedMemRemain = (vmMem[i] - container.getMem() - vmMemOverhead) / vmMem[i];

            // find which resource is smaller
//            double remain = normalizedCpuRemain > normalizedMemRemain ? normalizedCpuRemain:normalizedMemRemain;
            double volume =  1 / (1 - normalizedCpuRemain) * 1 / (1 - normalizedMemRemain);

            // update the choice of VM
//            if(minimumRemainResource > remain) {
//                bestVm = i;
//                minimumRemainResource = remain;
//            }
            if(maximumVolume < volume){
                maximumVolume = volume;
                bestVm = i;
            }
        }
        VM vm = new VM(bestVm, vmCpu[bestVm], vmMem[bestVm], vmCpuOverheadRate, vmMemOverhead);
        vm.allocate(container);

        return vm;
    }

    private PM createPM(VM vm){
        PM pm = new PM(pmCpu, pmMem, k, maxPmEnergy, crushPro);
        pm.allocate(vm);
        return pm;
    }

    // Using FF to allocate VM
    private Boolean allocateVM(VM vm, ArrayList<PM> pmList){
        for(PM pm:pmList){
            if(pm.getCpuRemain() >= vm.getConfigureCpu() && pm.getMemRemain() >= vm.getConfigureMem()){
                pm.allocate(vm);
                return true;
            }
        }
        return false;
    }

    public double energy(ArrayList<PM> pmList){
        double totalEnergy = 0;
        for(PM pm:pmList){
            totalEnergy += pm.calEnergy();
        }
        return totalEnergy;
    }

}
