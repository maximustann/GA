package LNS_NSGGA;

import LNS_FF.LNSChromosome;
import algorithms.StdRandom;
import cloudResourceUnit.Container;
import cloudResourceUnit.VM;

import java.util.ArrayList;

public class ReplicaPatternDestroy {
    private double replicaDestroyPercentage;
    private int lbound;
    private int ubound;
    private double[] vmCpu;
    private double[] vmMem;
    private double vmCpuOverheadRate;
    private double vmMemOverhead;

    public ReplicaPatternDestroy(
            double replicaDestroyPercentage,
            int lbound,
            int ubound,
            DataPack dataPack){
        this.replicaDestroyPercentage = replicaDestroyPercentage;
        this.lbound = lbound;
        this.ubound = ubound;
        this.vmCpu = dataPack.getVmCpu();
        this.vmMem = dataPack.getVmMem();
        this.vmCpuOverheadRate = dataPack.getVmCpuOverheadRate();
        this.vmMemOverhead = dataPack.getVmMemOverhead();

    }
    public void destroy(LNSChromosome chromosome){
        int[] oldReplicaPattern = chromosome.getReplicas();
        int[] newReplicaPattern = new int[oldReplicaPattern.length];
        for(int i = 0; i < oldReplicaPattern.length; i++){
            double u = StdRandom.uniform();
            if(u <= replicaDestroyPercentage){
                newReplicaPattern[i] = StdRandom.uniform(lbound, ubound);
            } else {
                newReplicaPattern[i] = oldReplicaPattern[i];
            }
        }
        chromosome.setReplicas(newReplicaPattern);

        int numOfContainers = chromosome.getNumOfContainers();

        double[] containerCpu = chromosome.getContainerCpu();
        double[] containerMem = chromosome.getContainerMem();
        double[] containerReplicaId = chromosome.getContainerReplicaId();
        double[] containerServiceId = chromosome.getContainerServiceId();
        double[] containerAppId = chromosome.getContainerAppId();
        int[] generatedVmTypes = generateVmTypes(numOfContainers);

        ArrayList<VM> vmList = allocateContainersToVm(
                numOfContainers,
                containerCpu, containerMem, containerReplicaId,
                containerServiceId, containerAppId,
                generatedVmTypes);


        chromosome.setVmList(vmList);

    }

    // uniformly generate VM types from the nonDominatedVmTypes
    private int[] generateVmTypes(int numOfContainers){
        int[] generatedVmTypes = new int[numOfContainers];
//        int numOfVmTypes = nonDominatedVmTypes.length;

//        for(int i = 0; i < numOfContainers; i++)
//            generatedVmTypes[i] = nonDominatedVmTypes[StdRandom.uniform(numOfVmTypes)];
        for(int i = 0; i < numOfContainers; i++)
            generatedVmTypes[i] = StdRandom.uniform(vmCpu.length);
        return generatedVmTypes;
    }


    // We apply FF to allocate the set of containers into the set of VMs
    private ArrayList<VM> allocateContainersToVm(
            int numOfContainers,
            double[] containerCpu,
            double[] containerMem,
            double[] containerReplicaId,
            double[] containerServiceId,
            double[] containerAppId,
            int[] generatedVmTypes){
        ArrayList<VM> vmList = new ArrayList<>();
        int numOfVmTypes = vmCpu.length;

        int vmCounter = 0;
        for(int i = 0; i < numOfContainers; i++){
            Container container = new Container(i, containerCpu[i], containerMem[i],
                    (int) containerAppId[i], (int) containerServiceId[i],
                    (int) containerReplicaId[i]);
            boolean allocated = false;
            for(VM vm:vmList){
                if(vm.getCpuRemain() >= container.getCpu() &&
                        vm.getMemRemain() >= container.getMem()){
                    vm.allocate(container);
                    allocated = true;
                    break;
                }
            }
            if(!allocated) {
                while (!allocated) {
                    int vmType = generatedVmTypes[vmCounter];
                    vmCounter++;
                    VM vm = new VM(vmType, vmCpu[vmType], vmMem[vmType], vmCpuOverheadRate, vmMemOverhead);
                    if (vm.getCpuRemain() >= container.getCpu() &&
                            vm.getMemRemain() >= container.getMem()) {
                        vm.allocate(container);
                        vmList.add(vm);
                        allocated = true;
                    }
                }
            }
        }
        return vmList;
    }
}
