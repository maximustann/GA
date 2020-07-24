package variableReplicasFirstStepGA;

import algorithms.InitPop;
import algorithms.StdRandom;

import java.util.ArrayList;

import cloudResourceUnit.*;
public class FirstStepGAInitialization implements InitPop {

    private double ubound;
    private double lbound;
    private int numOfApps;
    private int numOfMicroServices;
    private int permutationNum;

    // PM data
    private double pmCpu;
    private double pmMem;
    private double k;
    private double maxPmEnergy;
    private double crushPro;


    // VM data
    private double vmCpuOverheadRate;
    private double vmMemOverhead;
    private double[] vmCpu;
    private double[] vmMem;


    // service data
    private double[] serviceCpu;
    private double[] serviceMem;
    private double[] serviceId;
    private double[] applicationId;



    private ArrayList<Container> listOfContainers;


    public FirstStepGAInitialization(
                                int permutationNum,
                                int numOfApps,
                                int numOfMicroServices,
                                double lbound,
                                double ubound,
                                double pmCpu,
                                double pmMem,
                                double k,
                                double maxPmEnergy,
                                double crushPro,
                                double vmCpuOverheadRate,
                                double vmMemOverhead,
                                double[] vmCpu,
                                double[] vmMem,
                                double[] serviceCpu,
                                double[] serviceMem,
                                double[] serviceId,
                                double[] applicationId
                                ) {

        this.permutationNum = permutationNum;
        this.numOfApps = numOfApps;
        this.numOfMicroServices = numOfMicroServices;
        this.ubound = ubound;
        this.lbound = lbound;


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

        listOfContainers = new ArrayList<>();

    }

    public FirstStepGAChromosome[] init(
            int popSize,
            int maxVar,
            double lbound,
            double ubound
    ) {
        FirstStepGAChromosome[] popVar = new FirstStepGAChromosome[popSize];
        // initialize population
        for (int i = 0; i < popSize; i++) popVar[i] = generateChromosome();
        return popVar;
    }

    /**
     * @return chromosome
     */
    public FirstStepGAChromosome generateChromosome() {
        FirstStepGAChromosome chromosome = new FirstStepGAChromosome(
                permutationNum, numOfApps, numOfMicroServices,
                pmCpu, pmMem, k, maxPmEnergy, crushPro, vmCpuOverheadRate, vmMemOverhead,
                serviceCpu, serviceMem, serviceId, applicationId,
                vmCpu, vmMem);


        int[] individual = new int[numOfMicroServices];
        for(int i = 0; i < numOfMicroServices; i++){
            individual[i] = StdRandom.uniform((int) lbound, (int) ubound);
        }

        chromosome.setIndividual(individual, numOfMicroServices);
        return chromosome;
    }


}
