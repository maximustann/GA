package LNS_FF;

import algorithms.StdRandom;
import cloudResourceUnit.Container;
import cloudResourceUnit.VM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class AllocationDestroy {

    private double allocationDestroyPercentage;
    private int seed;

    private double[] vmCpu;
    private double[] vmMem;
    private double vmCpuOverheadRate;
    private double vmMemOverhead;

    private double pmCpu;
    private double pmMem;
    private ArrayList<Container> leftOver;


    // Cheat data,
    // We have already known that type 7, 17, and 20 are the non-dominated VM types,
    // we only generate three VM types from these 3 types
//    private int[] nonDominatedVmTypes = {6, 16, 19};

    public AllocationDestroy(double allocationDestroyPercentage,
                             DataPack dataPack
                             ){
        this.seed = dataPack.getSeed();
        this.allocationDestroyPercentage = allocationDestroyPercentage;
        this.vmCpu = dataPack.getVmCpu();
        this.vmMem = dataPack.getVmMem();
        this.vmCpuOverheadRate = dataPack.getVmCpuOverheadRate();
        this.vmMemOverhead = dataPack.getVmMemOverhead();

        this.pmCpu = dataPack.getPmCpu();
        this.pmMem = dataPack.getPmMem();
        leftOver = new ArrayList<>();
    }

    // The destroy method will delete a set of VMs and add the containers inside these VMs to the
    // "leftOver" list.
    // two methods,
    // Random selects a set of VMs to destroy
    // Ranked the VMs and destroy the bottom set of VMs
    public void destroy(LNSChromosome chromosome){
        double u = StdRandom.uniform();
        if(u < 0.5) {
            randomDestroy(chromosome);
        } else {
            rankedDestroy(chromosome);
        }
    }

    public void localSearch(LNSChromosome chromosome){
        ArrayList<VM> vmList = chromosome.getVmList();
        ArrayList<VM> newVmList = new ArrayList<>();
        for(VM vm:vmList){
//            VM newVM = findLowWasteVM(vm);
            VM newVM = findLowVolumeVM(vm);
            newVmList.add(newVM);
        }
        chromosome.setVmList(newVmList);
    }


    private VM findLowVolumeVM(VM vm){
        double totalContainerCpu = 0;
        double totalContainerMem = 0;
        ArrayList<Container> containerList = vm.getContainerList();
        double volume = 1 / (1 - vm.getCpuRemain() / pmCpu) * 1 / (1 - vm.getMemRemain() / pmMem);
        int betterType = vm.getType();
        VM betterVm = vm;

        for(Container container:containerList){
            totalContainerCpu += container.getCpu();
            totalContainerMem += container.getMem();
        }
        for(int i = 0; i < vmCpu.length; i++){
            if(vmCpu[i] - vmCpu[i] * vmCpuOverheadRate >= totalContainerCpu &&
                    vmMem[i] - vmMemOverhead >= totalContainerMem){
                double currentCpuWaste = (vmCpu[i] - vmCpu[i] * vmCpuOverheadRate - totalContainerCpu) / pmCpu;
                double currentMemWaste = (vmMem[i] - vmMemOverhead - totalContainerMem) / pmMem;
                double currentVolume = 1 / (1 - currentCpuWaste) * 1 / (1 - currentMemWaste);
                if(currentVolume < volume) betterType = i;
            }
        }

        // found better VM
        if(betterType != vm.getType()){
            betterVm = new VM(betterType, vmCpu[betterType], vmMem[betterType], vmCpuOverheadRate, vmMemOverhead);
            for(Container container:containerList){
                betterVm.allocate(container);
            }
        }
        return betterVm;
    }

    private VM findLowWasteVM(VM vm){
        double totalContainerCpu = 0;
        double totalContainerMem = 0;
        ArrayList<Container> containerList = vm.getContainerList();
        double cpuWaste = (vm.getCpuRemain() / pmCpu);
        double memWaste = (vm.getMemRemain() / pmMem);
        double waste = cpuWaste < memWaste ? cpuWaste:memWaste;
        int betterType = vm.getType();
        VM betterVm = vm;

        for(Container container:containerList){
            totalContainerCpu += container.getCpu();
            totalContainerMem += container.getMem();
        }
        for(int i = 0; i < vmCpu.length; i++){
            if(vmCpu[i] - vmCpu[i] * vmCpuOverheadRate >= totalContainerCpu &&
            vmMem[i] - vmMemOverhead >= totalContainerMem){
                double currentCpuWaste = (vmCpu[i] - vmCpu[i] * vmCpuOverheadRate - totalContainerCpu) / pmCpu;
                double currentMemWaste = (vmMem[i] - vmMemOverhead - totalContainerMem) / pmMem;
                double currentWaste = currentCpuWaste < currentMemWaste ? currentCpuWaste:currentMemWaste;
                if(currentWaste < waste) betterType = i;
            }
        }

        // found better VM
        if(betterType != vm.getType()){
            betterVm = new VM(betterType, vmCpu[betterType], vmMem[betterType], vmCpuOverheadRate, vmMemOverhead);
            for(Container container:containerList){
                betterVm.allocate(container);
            }
        }
        return betterVm;
    }

    private void randomDestroy(LNSChromosome chromosome){
        ArrayList<VM> vmList = chromosome.getVmList();
        ArrayList<Integer> destroyVmId = new ArrayList<>();
        int vmListSize = vmList.size();
        for (VM vm:vmList){
            double u = StdRandom.uniform();
            if(u < allocationDestroyPercentage) destroyVmId.add(vm.getID());
        }

        for(Integer id:destroyVmId){
            for(VM vm:vmList){
                if(vm.getID() == id) {
                    ArrayList<Container> containerList = vm.getContainerList();
                    for(Container container:containerList) container.setMotherVm(null);
                    leftOver.addAll(containerList);
                    vmList.remove(vm);
                    break;
                }
            }
        }

    }

    private void rankedDestroy(LNSChromosome chromosome){
        ArrayList<VM> vmList = chromosome.getVmList();
        int vmListSize = vmList.size();
        int deleteSize = (int) Math.round(vmListSize * allocationDestroyPercentage);
        vmList.sort(new Comparator<VM>() {
            @Override
            public int compare(VM o1, VM o2) {
//                double vm1Resource = o1.getCpuRemain() / pmCpu  * o1.getMemRemain() / pmMem;
//                double vm2Resource = o2.getCpuRemain() / pmCpu  * o2.getMemRemain() / pmMem;
                double vm1wastedCpu = (o1.getCpuRemain() + o1.getConfigureCpu() * o1.getCpuOverheadRate()) / pmCpu;
                double vm1wastedMem = (o1.getMemRemain() + o1.getMemOverhead()) / pmMem;
                double vm1wastedR = vm1wastedCpu < vm1wastedMem ? vm1wastedCpu : vm1wastedMem;

                double vm2wastedCpu = (o2.getCpuRemain() + o2.getConfigureCpu() * o2.getCpuOverheadRate()) / pmCpu;
                double vm2wastedMem = (o2.getMemRemain() + o2.getMemOverhead()) / pmMem;
                double vm2wastedR = vm2wastedCpu < vm2wastedMem ? vm2wastedCpu : vm2wastedMem;

                if(vm1wastedR < vm2wastedR){
                    return -1;
                } else if(vm1wastedR == vm2wastedR){
                    return 0;
                } else {
                    return 1;
                }
            }
        });

        // delete the bottom "deleteSize" of VMs
        for(int i = 0; i < deleteSize; i++) {
            VM lastVm = vmList.get(vmList.size() - 1);
            ArrayList<Container> containerList = lastVm.getContainerList();
            for(Container container:containerList) container.setMotherVm(null);
            leftOver.addAll(containerList);
            vmList.remove(vmList.size() - 1);
        }

    }



    public void repair(LNSChromosome chromosome){
        double u = StdRandom.uniform();
        Collections.shuffle(leftOver, new Random(seed));
        if(u < 0.5){
            ffRandomCreateVm(chromosome);
        } else{
            ffVolumeCreateVm(chromosome);
        }
        leftOver.clear();
    }


    // First, randomly shuffle the list of leftOver containers
    // Second, insert the leftOver containers with FF
    // If no VM is available, we create new VMs with volume
    // volume = 1 / (1 - leftCpu) * 1 / (1 - leftMem)
    public void ffVolumeCreateVm(LNSChromosome chromosome){
        ArrayList<VM> vmList = chromosome.getVmList();
        for(Container container:leftOver){
            boolean allocated = false;
            for(VM vm:vmList){
                if(vm.getCpuRemain() >= container.getCpu() &&
                        vm.getMemRemain() >= container.getMem()){
                    vm.allocate(container);
                    allocated = true;
                    break;
                }
            }
            if(!allocated){
                int bestVM = bestFitVmType(container);
                VM vm = new VM(bestVM, vmCpu[bestVM], vmMem[bestVM], vmCpuOverheadRate, vmMemOverhead);
                vm.allocate(container);
                vmList.add(vm);
            }
        }
    }

    // Insert the leftOver containers with FirstFit
    // If the current VMs cannot host the container, uniformly create one and add to the vmList
    public void ffRandomCreateVm(LNSChromosome chromosome){

        ArrayList<VM> vmList = chromosome.getVmList();
        int numOfContainers = leftOver.size();
        for(int i = 0; i < numOfContainers; i++){
            Container container = leftOver.get(i);
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
                    int vmType = randomVmType(container);
                    VM vm = new VM(vmType, vmCpu[vmType], vmMem[vmType], vmCpuOverheadRate, vmMemOverhead);
                    vm.allocate(container);
                    vmList.add(vm);
                    allocated = true;
                }
            }
        }
    }

    private int bestFitVmType(Container container){
        int bestType = 0;
        double bestScore = 2.0;
        for(int i = 0; i < vmCpu.length; i++){
            double cpuRemain = vmCpu[i] - vmCpuOverheadRate * vmCpu[i];
            double memRemain = vmMem[i] - vmMemOverhead;
            if(cpuRemain >= container.getCpu() && memRemain >= container.getMem()){
                double normalizedCpuRemain = (cpuRemain - container.getCpu()) / pmCpu;
                double normalizedMemRemain = (memRemain - container.getMem()) / pmMem;
                double normalizedVolume = 1 / (1 - normalizedCpuRemain) * 1 / (1 - normalizedMemRemain);
                if(normalizedVolume < bestScore){
                    bestType = i;
                    bestScore = normalizedVolume;
                }
            }
        }
        return bestType;
    }

    private int randomVmType(Container container){
        int bestIndex = 0;

        ArrayList<Integer> possibleList = new ArrayList<>();
        for(int i = 0; i < vmCpu.length; i++){
            double cpuRemain = vmCpu[i] - vmCpuOverheadRate * vmCpu[i];
            double memRemain = vmMem[i] - vmMemOverhead;
            if(cpuRemain >= container.getCpu() && memRemain >= container.getMem()) possibleList.add(i);
        }
        bestIndex = StdRandom.uniform(possibleList.size());
        return possibleList.get(bestIndex);
    }

//    public void localSearch(ALNSChromosome chromosome){
//
//    }
}
