package twoStepGA;

import algorithms.StdRandom;
import variableReplicasFirstStepGA.FirstStepGAChromosome;

import java.util.ArrayList;

public class NSGAIIPostProcessingUnit {
    private ArrayList<ArrayList<FirstStepGAChromosome>> dominantSetList;
    private ArrayList<double[]> genTime;
    private int[][] microService;
    private int generation;
    private int maximumServiceNum;

    private ReplicaNumDecisionPack decisionPack;

    public NSGAIIPostProcessingUnit(
            ArrayList<ArrayList<FirstStepGAChromosome>> dominantSetList,
            ArrayList<double[]> genTime,
            int[][] microService,
            int maximumServiceNum,
            int generation){
        this.dominantSetList = dominantSetList;
        this.microService = microService;
        this.genTime = genTime;
        this.maximumServiceNum = maximumServiceNum;
        this.generation = generation;
    }

    public ReplicaNumDecisionPack getDecisionPack(){
        FirstStepGAChromosome chromosome = randomSolution();

        double[] containerCpu = chromosome.getContainerCpu();
        double[] containerMem = chromosome.getContainerMem();
        double[] containerAppId = chromosome.getContainerAppId();
        double[] containerMicroServiceId = chromosome.getContainerMicroServiceId();
        double[] containerReplicaId = chromosome.getContainerReplicaId();


        ReplicaNumDecisionPack replicaNumDecisionPack = new ReplicaNumDecisionPack(
                containerCpu,
                containerMem,
                containerAppId,
                containerMicroServiceId,
                containerReplicaId
        );
        replicaNumDecisionPack.print();
        return replicaNumDecisionPack;
    }

    public FirstStepGAChromosome randomSolution(){
        ArrayList<FirstStepGAChromosome> paretoFront = dominantSetList.get(generation - 1);
        int numSolutionPareto = paretoFront.size();
        int solutionIndex = StdRandom.uniform(numSolutionPareto);
        return paretoFront.get(solutionIndex);
    }




}
