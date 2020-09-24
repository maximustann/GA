package NSGAII_Spread;

import java.util.ArrayList;

public class ReplicaNumDecisionPack {
    private double[] containerCpu;
    private double[] containerMem;
    private double[] containerAppId;
    private double[] containerMicroServiceId;
    private double[] containerReplicaId;
    private ArrayList<Double> firstStepTime;

    public ReplicaNumDecisionPack(
            double[] containerCpu,
            double[] containerMem,
            double[] containerAppId,
            double[] containerMicroServiceId,
            double[] containerReplicaId,
            ArrayList<Double> firstStepTime
    ){
        this.containerCpu = containerCpu;
        this.containerMem = containerMem;
        this.containerAppId = containerAppId;
        this.containerMicroServiceId = containerMicroServiceId;
        this.containerReplicaId = containerReplicaId;
        this.firstStepTime = firstStepTime;
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

    public double[] getContainerCpu() {
        return containerCpu;
    }

    public double[] getContainerMem() {
        return containerMem;
    }

    public int getContainerSize(){
        return containerReplicaId.length;
    }

    public ArrayList<Double> getFirstStepTime() {
        return firstStepTime;
    }

    public void print(){
//        int numOfContainers = containerCpu.length;
        System.out.println("numOfContainers = " + getContainerSize());
//        for(int i = 0; i < numOfContainers; i++){
//            System.out.println(containerCpu[i] + ", " + containerMem[i] +
//                                ", " + containerAppId[i] + ", " +
//                                    containerMicroServiceId[i] + ", " +
//                                    containerReplicaId[i]);
//        }
    }
}
