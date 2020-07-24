package alnsFF;

import cloudResourceUnit.VM;

import java.util.ArrayList;

public class ALNSChromosome implements Cloneable{
    private int numOfServices;
    private int[] replicas;
    private ArrayList<VM> vmList;
    private double fitness;

    private double[] serviceCpu;
    private double[] serviceMem;
    private double[] serviceId;
    private double[] appId;


    // generated after the replicas was being set
    private int numOfContainers;
    private double[] containerCpu;
    private double[] containerMem;
    private double[] containerServiceId;
    private double[] containerAppId;
    private double[] containerReplicaId;


    public ALNSChromosome(int numOfServices,
                          double[] serviceCpu,
                          double[] serviceMem,
                          double[] serviceId,
                          double[] appId){
        this.numOfServices = numOfServices;
        this.serviceCpu = serviceCpu;
        this.serviceMem = serviceMem;
        this.serviceId= serviceId;
        this.appId = appId;
    }

    public int getNumOfContainers() {
        return numOfContainers;
    }

    public int[] getReplicas() {
        return replicas;
    }

    public void setReplicas(int[] replicas) {
        this.replicas = replicas;
        generateContainer();
    }

    public ArrayList<VM> getVmList() {
        return vmList;
    }

    public void setVmList(ArrayList<VM> vmList) {
        this.vmList = vmList;
    }

    public int getNumOfServices() {
        return numOfServices;
    }

    public double[] getServiceCpu() {
        return serviceCpu;
    }

    public double[] getServiceMem() {
        return serviceMem;
    }

    public double[] getServiceId() {
        return serviceId;
    }

    public double[] getAppId() {
        return appId;
    }

    public double[] getContainerCpu() {
        return containerCpu;
    }

    public double[] getContainerMem() {
        return containerMem;
    }

    public double[] getContainerServiceId() {
        return containerServiceId;
    }

    public double[] getContainerAppId() {
        return containerAppId;
    }

    public double[] getContainerReplicaId() {
        return containerReplicaId;
    }

    @Override
    public ALNSChromosome clone(){
        ALNSChromosome newChromosome = new ALNSChromosome(
                numOfServices,
                serviceCpu,
                serviceMem,
                serviceId,
                appId
        );
        ArrayList<VM> newVmList = new ArrayList();
        int[] newReplicas = new int[numOfServices];
        for(VM vm:vmList) newVmList.add(vm.clone());
        for(int i = 0; i < numOfServices; i++) newReplicas[i] = replicas[i];

        newChromosome.setReplicas(newReplicas);
        newChromosome.setVmList(newVmList);

        return newChromosome;
    }

    // generate container Data
    private void generateContainer(){
        numOfContainers = calNumOfContainers();
        containerCpu = new double[numOfContainers];
        containerMem = new double[numOfContainers];
        containerReplicaId = new double[numOfContainers];
        containerServiceId = new double[numOfContainers];
        containerAppId = new double[numOfContainers];

        int containerCount = 0;
        for(int i = 0; i < numOfServices; i++){
            for(int j = 1; j <= replicas[i]; j++){
                containerCpu[containerCount] = serviceCpu[i];
                containerMem[containerCount] = serviceMem[i];
                containerReplicaId[containerCount] = j;
                containerServiceId[containerCount] = serviceId[i];
                containerAppId[containerCount] = appId[i];
                containerCount++;
            }
        }
    }

    private int calNumOfContainers(){
        int numOfContainers = 0;
        for(int i = 0; i < numOfServices; i++) numOfContainers += replicas[i];
        return numOfContainers;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }
}
