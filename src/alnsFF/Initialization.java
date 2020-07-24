package alnsFF;

import java.util.ArrayList;
import algorithms.StdRandom;
import cloudResourceUnit.Container;
import cloudResourceUnit.VM;

public class Initialization {

    private int numOfServices;
    private int lbound;
    private int ubound;


    // VM data
    private double vmCpuOverheadRate;
    private double vmMemOverhead;
    private double[] vmCpu;
    private double[] vmMem;

    private double[] serviceCpu;
    private double[] serviceMem;
    private double[] serviceId;
    private double[] appId;


    // Cheat data,
    // We have already known that type 7, 17, and 20 are the non-dominated VM types,
    // we only generate three VM types from these 3 types
    private int[] nonDominatedVmTypes = {6, 16, 19};


    public Initialization(int numOfServices,
                          int lbound,
                          int ubound,
                          DataPack dataPack){
        this.numOfServices = numOfServices;
        this.lbound = lbound;
        this.ubound = ubound;

        this.vmCpuOverheadRate = dataPack.getVmCpuOverheadRate();
        this.vmMemOverhead = dataPack.getVmMemOverhead();
        this.vmCpu = dataPack.getVmCpu();
        this.vmMem = dataPack.getVmMem();

        this.serviceCpu = dataPack.getServiceCpu();
        this.serviceMem = dataPack.getServiceMem();
        this.serviceId = dataPack.getServiceId();
        this.appId = dataPack.getApplicationId();
    }

    public ALNSChromosome init(){
        ALNSChromosome chromosome = generateChromosome();
        return chromosome;
    }

    private ALNSChromosome generateChromosome(){
        ALNSChromosome chromosome = new ALNSChromosome(
                numOfServices,
                serviceCpu,
                serviceMem,
                serviceId,
                appId);
        int[] replicas = generateReplicaPattern();
        chromosome.setReplicas(replicas);

        int numOfContainers = chromosome.getNumOfContainers();

        //
        double[] containerCpu = chromosome.getContainerCpu();
        double[] containerMem = chromosome.getContainerMem();
        double[] containerReplicaId = chromosome.getContainerReplicaId();
        double[] containerServiceId = chromosome.getContainerServiceId();
        double[] containerAppId = chromosome.getContainerAppId();
        int[] generatedVmTypes = generateVmTypes(numOfContainers);


        ArrayList<VM> vmList = allocateContainersToVm(
                numOfContainers,
                containerCpu, containerMem, containerReplicaId, containerServiceId, containerAppId,
                generatedVmTypes);


        chromosome.setVmList(vmList);
        return chromosome;
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

    // uniformly generate VM types from the nonDominatedVmTypes
    //
    private int[] generateVmTypes(int numOfContainers){
        int[] generatedVmTypes = new int[numOfContainers];
        int numOfVmTypes = nonDominatedVmTypes.length;

        for(int i = 0; i < numOfContainers; i++)
            generatedVmTypes[i] = nonDominatedVmTypes[StdRandom.uniform(numOfVmTypes)];
        return generatedVmTypes;
    }

    // uniformly generate number of replicas
    private int[] generateReplicaPattern(){
        int[] replicas = new int[numOfServices];
        for(int i = 0; i < numOfServices; i++)
            replicas[i] = StdRandom.uniform(lbound, ubound);
        return replicas;
    }


}
