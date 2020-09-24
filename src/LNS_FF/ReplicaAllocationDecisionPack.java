package LNS_FF;

import cloudResourceUnit.VM;

import java.util.ArrayList;

public class ReplicaAllocationDecisionPack {
    private double[] containerCpu;
    private double[] containerMem;
    private double[] containerAppId;
    private double[] containerMicroServiceId;
    private double[] containerReplicaId;
    private ArrayList<VM> vmList;

    public ReplicaAllocationDecisionPack(
            double[] containerCpu,
            double[] containerMem,
            double[] containerAppId,
            double[] containerMicroServiceId,
            double[] containerReplicaId,
            ArrayList<VM> vmList
    ){
        this.containerCpu = containerCpu;
        this.containerMem = containerMem;
        this.containerAppId = containerAppId;
        this.containerMicroServiceId = containerMicroServiceId;
        this.containerReplicaId = containerReplicaId;
        this.vmList = vmList;
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

    public ArrayList<VM> getVmList() {
        return vmList;
    }

    public void print(){
//        int numOfContainers = containerCpu.length;
//        System.out.println("numOfContainers = " + numOfContainers);
//        for(int i = 0; i < numOfContainers; i++){
//            System.out.println(containerCpu[i] + ", " + containerMem[i] +
//                                ", " + containerAppId[i] + ", " +
//                                    containerMicroServiceId[i] + ", " +
//                                    containerReplicaId[i]);
//        }
    }
}
