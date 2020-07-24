package variableReplicasFirstStepGA;

import algorithms.Chromosome;
import algorithms.Gene;
import algorithms.StdRandom;

import java.util.*;

import cloudResourceUnit.*;
public class FirstStepGAChromosome extends Chromosome {

    private int permutationNum;

    private int numOfApps;
    private int numOfServices;
    private int numOfContainers;
    private double energyFitness;
    private double availabilityFitness;


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

    private double[] serviceCpu;
    private double[] serviceMem;
    private double[] serviceId;
    private double[] applicationId;



    private double[] vmCpu;
    private double[] vmMem;



    private int[] individual;


    // Constructor
    public FirstStepGAChromosome(
            int permutationNum,
            int numOfApps,
            int numOfServices,
            double pmCpu,
            double pmMem,
            double k,
            double maxPmEnergy,
            double crushPro,
            double vmCpuOverheadRate,
            double vmMemOverhead,
            double[] serviceCpu,
            double[] serviceMem,
            double[] serviceId,
            double[] applicationId,
            double[] vmCpu,
            double[] vmMem){

        this.permutationNum = permutationNum;
        this.numOfApps = numOfApps;
        this.numOfServices = numOfServices;

        this.pmCpu = pmCpu;
        this.pmMem = pmMem;
        this.k = k;
        this.maxPmEnergy = maxPmEnergy;
        this.crushPro = crushPro;
        this.vmCpuOverheadRate = vmCpuOverheadRate;
        this.vmMemOverhead = vmMemOverhead;
        this.serviceCpu = serviceCpu;
        this.serviceMem = serviceMem;
        this.serviceId = serviceId;
        this.applicationId = applicationId;
        this.vmCpu = vmCpu;
        this.vmMem = vmMem;
//        appList = new ArrayList<>();
    }

    // Another type of constructor which is used in crossover
    public FirstStepGAChromosome(FirstStepGAChromosome father, FirstStepGAChromosome mother, int cutPoint){
        this.permutationNum = father.permutationNum;
        this.numOfServices = father.numOfServices;

        this.pmCpu = father.pmCpu;
        this.pmMem = father.pmMem;
        this.k = father.k;
        this.maxPmEnergy = father.maxPmEnergy;
        this.crushPro = father.crushPro;
        this.vmCpuOverheadRate = father.vmCpuOverheadRate;
        this.vmMemOverhead = father.vmMemOverhead;
        this.serviceCpu = father.serviceCpu;
        this.serviceMem = father.serviceMem;
        this.serviceId = father.serviceId;
        this.applicationId = father.applicationId;
        this.vmCpu = father.vmCpu;
        this.vmMem = father.vmMem;

//        appList = new ArrayList<>();
        inherit(father, mother, cutPoint);
    }


    public int getNumOfContainers(){
        numOfContainers = 0;
        for(int i = 0; i < numOfServices; ++i){
            numOfContainers += individual[i];
        }
        return numOfContainers;
    }

    @Override
    // size() acutally returns the total number of micro-services
    public int size() {
        return individual.length;
    }

    @Override
    public Gene cut(int cutPoint, int geneIndicator) {
        return null;
    }

    @Override
    public void print() {
        for(int i = 0; i < numOfServices; i++){
            System.out.print(individual[i] + " ");
        }
        System.out.println("numOfContainers = " + numOfContainers);
        System.out.println();
    }

    @Override
    public Chromosome clone() {
        FirstStepGAChromosome newChromosome = new FirstStepGAChromosome(
                permutationNum, numOfApps, numOfServices,
                pmCpu, pmMem, k, maxPmEnergy, crushPro, vmCpuOverheadRate, vmMemOverhead,
                serviceCpu, serviceMem, serviceId, applicationId,
                vmCpu, vmMem);
        int[] newIndividual = new int[numOfServices];
        System.arraycopy(individual,0,newIndividual,0,numOfServices);
        newChromosome.setIndividual(newIndividual, numOfServices);
        return newChromosome;
    }

    @Override
    public boolean equals(Chromosome target) {
        int[] targetIndividual = ((FirstStepGAChromosome) target).getIndividual();
        return Arrays.equals(individual, targetIndividual);
    }

    public int[] getIndividual() {
        return individual;
    }

    // Set the individual, this will be called by initialization, crossover, and mutation
    // At the time of setting individual, we also set the replicaId
    public void setIndividual(int[] individual, int numOfServices){
        this.individual = individual;
        this.numOfServices = numOfServices;

        this.numOfContainers = 0;
        for(int i = 0; i < individual.length; i++){
            numOfContainers += individual[i];
        }




        // Initialize, we need to new all of them because the number of containers might
        // be different with the previous one. Hence, all other parts need to be new
        containerAppId = new double[numOfContainers];
        containerMicroServiceId = new double[numOfContainers];
        containerCpu = new double[numOfContainers];
        containerMem = new double[numOfContainers];
        containerReplicaId = new double[numOfContainers];

        int counter = 0;
        for(int microIndex = 0; microIndex < numOfServices; ++microIndex){
            for(int replicaId = 1; replicaId <= individual[microIndex]; ++replicaId){
                containerAppId[counter] = applicationId[microIndex];
                containerCpu[counter] = serviceCpu[microIndex];
                containerMem[counter] = serviceMem[microIndex];
                containerMicroServiceId[counter] = serviceId[microIndex];
                containerReplicaId[counter] = replicaId;

                counter++;
            }
        }
    }



    public void setEnergyFitness(double fitness){
        this.energyFitness = fitness;
    }

    public double calEnergyFitness(){
        double totalEnergy = 0;
        for(int i = 0; i < permutationNum; ++i) {
            int[] randomSequence = generateRandomSequence();
            ArrayList<PM> pmList = allocate(randomSequence);
            totalEnergy += energy(pmList);
        }
        this.energyFitness = totalEnergy / permutationNum;
        return this.energyFitness;
    }


    public double calAvailFitness(){
        double totalAvailability = 0;
        for(int i = 0; i < permutationNum; ++i){
            int[] randomSequence = generateRandomSequence();
            ArrayList<PM> pmList = allocate(randomSequence);
            totalAvailability += avail(pmList);
        }
        availabilityFitness = totalAvailability / permutationNum;
        return availabilityFitness;
    }

    private double avail(ArrayList<PM> pmList){
        ArrayList<Application> appList = constructAppList(pmList);
//        for(Application application:appList){
//            ArrayList<MicroService> microServices = application.getMicroServiceList();
//            for(MicroService microService:microServices){
//                allocateToPms(microService, pmList);
//            }
//        }

        return updateAverageAvailability(appList);
    }

    private void inherit(FirstStepGAChromosome father, FirstStepGAChromosome mother, int cutPoint){
        individual = new int[father.numOfServices];

        System.arraycopy(father.individual, 0, individual, 0, cutPoint + 1);
        System.arraycopy(mother.individual, cutPoint + 1, individual, cutPoint + 1,
                numOfServices - cutPoint - 1);
        setIndividual(this.individual, numOfServices); // to create an int[] containerReplicaId.
    }

    /**
     * Generate a random sequence of task
     * Basic idea is, first initialize a sequence which has a length of task
     * then, randomly draw one each time until the array list is empty.
     * @return a random sequence
     */
    private int[] generateRandomSequence(){
        int taskCount = 0;
        int[] containerSequence = new int[numOfContainers];
        for (int i = 0; i < numOfContainers; i++) containerSequence[i] = i;
        StdRandom.shuffle(containerSequence);
        return containerSequence;
    }

    // return a full list of PMs
    // int[] sequence contains a random sequence of containers' indexes
    private ArrayList<PM> allocate(int[] sequence){

//        ArrayList<double[]> vmStatus = new ArrayList();
        ArrayList<PM> pmList = new ArrayList<>();


        // allocate containers to VMs
        for(int i = 0; i < numOfContainers; ++i){
            int serviceIndex = sequence[i];

            Container container = new Container(
                    i,
                    containerCpu[serviceIndex],
                    containerMem[serviceIndex],
                    (int) containerAppId[serviceIndex],
                    (int) containerMicroServiceId[serviceIndex],
                    (int) containerReplicaId[serviceIndex]
            );

            // allocate this container, if not success, create one VM and allocate the container
            if(!allocateContainer(container, pmList)) {
                PM pm = new PM(pmCpu, pmMem, k, maxPmEnergy, crushPro);
                VM vm = createVM(container, pm);
                pm.allocate(vm);
                pmList.add(pm);
            }
        }

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

    // We apply the BestFit heuristic
    private VM createVM(Container container, PM pm){
        return bestFitCreation(container, pm);
    }

    // We apply the volume method
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

    private double energy(ArrayList<PM> pmList){
        double totalEnergy = 0;
        for(PM pm:pmList){
            totalEnergy += pm.calEnergy();
        }
        return totalEnergy;
    }



    private double updateAverageAvailability(ArrayList<Application> appList){
        double availability = 0;
        for(Application application:appList){
            availability += application.availability();
        }

        return availability / appList.size();
    }

    // loop through all the containers to renew the applications on all PMs
    private ArrayList<Application> constructAppList(ArrayList<PM> pmList) {
        ArrayList<Application> appList = new ArrayList<>();
        for (PM pm : pmList) {
            ArrayList<VM> vmList = pm.getVmList();
            for (VM vm : vmList) {
                ArrayList<Container> containerList = vm.getContainerList();
                for (Container container : containerList) {
                    int applicationId = container.getApplicationId();
                    int microId = container.getMicroServiceId();
                    Application app;
                    MicroService microService;
                    // If the application list contains this application
                    if (appExist(appList, applicationId)) {
                        app = getApp(appList, applicationId);
                    } else {
                        app = new Application(applicationId);
                        appList.add(app);
                    }
                    ArrayList<MicroService> microServiceList = app.getMicroServiceList();
                    // If the application contains this micro service
                    if (microExist(microServiceList, microId)) {
                        microService = getMicroService(microServiceList, microId);
                    } else {
                        microService = new MicroService(microId);
                        microServiceList.add(microService);
                    }
                    microService.addContainer(container);
                }
            }
//            pm.updateAppTable();
        }
        return appList;
    }


    private boolean appExist(ArrayList<Application> appList, int applicationId){
        for(Application app:appList){
            if(app.getID() == applicationId) return true;
        }
        return false;
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

    private Application getApp(ArrayList<Application> appList, int applicationId){
        for(Application app:appList){
            if(app.getID() == applicationId) return app;
        }
        return null;
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

    public double[] getContainerCpu() {
        return containerCpu;
    }

    public double[] getContainerMem() {
        return containerMem;
    }

    public double[] getContainerAppId() {
        return containerAppId;
    }

    public double[] getContainerMicroServiceId() {
        return containerMicroServiceId;
    }

    public double[] getContainerReplicaId() {
        return containerReplicaId;
    }

    public int getNumOfApps(){
        return numOfApps;
    }


}
