package twoStepGA;

public class ReplicaNumDecisionPack {
    private double[] containerCpu;
    private double[] containerMem;
    private double[] containerAppId;
    private double[] containerMicroServiceId;
    private double[] containerReplicaId;

    public ReplicaNumDecisionPack(
            double[] containerCpu,
            double[] containerMem,
            double[] containerAppId,
            double[] containerMicroServiceId,
            double[] containerReplicaId
    ){
        this.containerCpu = containerCpu;
        this.containerMem = containerMem;
        this.containerAppId = containerAppId;
        this.containerMicroServiceId = containerMicroServiceId;
        this.containerReplicaId = containerReplicaId;
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
