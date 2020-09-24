package LNS_FF;

import cloudResourceUnit.VM;

import java.util.ArrayList;

public class LNSPostProcessingUnit {
    private ArrayList<LNSChromosome> solutions;
    private int generation;

    private ReplicaAllocationDecisionPack decisionPack;

    public LNSPostProcessingUnit(
            ArrayList<LNSChromosome> solutions,
            int generation){
        this.solutions = solutions;
        this.generation = generation;
    }

    public ReplicaAllocationDecisionPack getDecisionPack(){
        LNSChromosome chromosome = solutions.get(generation);
        double[] containerCpu = chromosome.getContainerCpu();
        double[] containerMem = chromosome.getContainerMem();
        double[] containerAppId = chromosome.getContainerAppId();
        double[] containerMicroServiceId = chromosome.getContainerServiceId();
        double[] containerReplicaId = chromosome.getContainerReplicaId();
        ArrayList<VM> vmList = chromosome.getVmList();

        ReplicaAllocationDecisionPack replicaNumDecisionPack = new ReplicaAllocationDecisionPack(
                containerCpu,
                containerMem,
                containerAppId,
                containerMicroServiceId,
                containerReplicaId,
                vmList
        );
//        replicaNumDecisionPack.print();
        return replicaNumDecisionPack;
    }

}
