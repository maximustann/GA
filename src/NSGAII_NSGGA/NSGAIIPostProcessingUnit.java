package NSGAII_NSGGA;

import algorithms.StdRandom;
import variableReplicasFirstStepGA.FirstStepGAChromosome;

import java.util.ArrayList;

public class NSGAIIPostProcessingUnit {
    private ArrayList<ArrayList<FirstStepGAChromosome>> dominantSetList;
    private ArrayList<double[]> genTime;
    private ArrayList<Double> time;
    private int generation;

    private ReplicaNumDecisionPack decisionPack;

    public NSGAIIPostProcessingUnit(
            ArrayList<ArrayList<FirstStepGAChromosome>> dominantSetList,
            ArrayList<double[]> genTime, ArrayList<Double> time,
            int generation){
        this.dominantSetList = dominantSetList;
        this.genTime = genTime;
        this.time = time;
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
                containerReplicaId,
                getTime()
        );
        return replicaNumDecisionPack;
    }

    public FirstStepGAChromosome randomSolution(){
        ArrayList<FirstStepGAChromosome> paretoFront = dominantSetList.get(generation - 1);
        int numSolutionPareto = paretoFront.size();
        int solutionIndex = StdRandom.uniform(numSolutionPareto);
        return paretoFront.get(solutionIndex);
    }

    public ArrayList<Double> getTime() {
        return time;
    }
}
