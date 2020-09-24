package LNS_NSGGA;

import LNS_FF.LNSChromosome;
import cloudResourceUnit.VM;

import java.util.ArrayList;

public class LNSPostProcessingUnit {
    private ArrayList<LNSChromosome> solutions;
    private ArrayList<Double> time;
    private int generation;

    private ReplicaNumDecisionPack decisionPack;

    public LNSPostProcessingUnit(
            ArrayList<LNSChromosome> solutions,
            ArrayList<Double> time,
            int generation){
        this.time = time;
        this.solutions = solutions;
        this.generation = generation;
    }

    public ReplicaNumDecisionPack getDecisionPack(){
        LNSChromosome chromosome = solutions.get(generation);
        double[] containerCpu = chromosome.getContainerCpu();
        double[] containerMem = chromosome.getContainerMem();
        double[] containerAppId = chromosome.getContainerAppId();
        double[] containerMicroServiceId = chromosome.getContainerServiceId();
        double[] containerReplicaId = chromosome.getContainerReplicaId();
        ArrayList<Double> firstStepTime = time;

        ReplicaNumDecisionPack replicaNumDecisionPack = new ReplicaNumDecisionPack(
                containerCpu,
                containerMem,
                containerAppId,
                containerMicroServiceId,
                containerReplicaId,
                time
        );
        return replicaNumDecisionPack;
    }

}
