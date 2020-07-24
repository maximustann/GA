package FirstStepALNS;

import algorithms.Chromosome;
import algorithms.Gene;
import algorithms.StdRandom;

import java.util.ArrayList;
import java.util.Arrays;

public class ALNSChromosome extends Chromosome {

//    private int permutationNum;

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
    public ALNSChromosome(
//            int permutationNum,
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

//        this.permutationNum = permutationNum;
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
//    public ALNSChromosome(ALNSChromosome father, ALNSChromosome mother, int cutPoint){
////        this.permutationNum = father.permutationNum;
//        this.numOfServices = father.numOfServices;
//
//        this.pmCpu = father.pmCpu;
//        this.pmMem = father.pmMem;
//        this.k = father.k;
//        this.maxPmEnergy = father.maxPmEnergy;
//        this.crushPro = father.crushPro;
//        this.vmCpuOverheadRate = father.vmCpuOverheadRate;
//        this.vmMemOverhead = father.vmMemOverhead;
//        this.serviceCpu = father.serviceCpu;
//        this.serviceMem = father.serviceMem;
//        this.serviceId = father.serviceId;
//        this.applicationId = father.applicationId;
//        this.vmCpu = father.vmCpu;
//        this.vmMem = father.vmMem;
//
////        appList = new ArrayList<>();
//        inherit(father, mother, cutPoint);
//    }


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
        ALNSChromosome newChromosome = new ALNSChromosome(
                numOfApps, numOfServices,
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
        int[] targetIndividual = ((ALNSChromosome) target).getIndividual();
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


    private double energy(ArrayList<PM> pmList){
        double totalEnergy = 0;
        for(PM pm:pmList){
            totalEnergy += pm.calEnergy();
        }
        return totalEnergy;
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
