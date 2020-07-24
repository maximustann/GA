package alnsFF;

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
    private int[] nonDominatedVmTypes = {6, 16, 19};

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
    public void destroy(ALNSChromosome chromosome){
        double u = StdRandom.uniform();
        if(u < 0.5) {
            randomDestroy(chromosome);
        } else {
            rankedDestroy(chromosome);
        }
    }

    private void randomDestroy(ALNSChromosome chromosome){
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
                    leftOver.addAll(vm.getContainerList());
                    vmList.remove(vm);
                }
                break;
            }
        }

    }

    private void rankedDestroy(ALNSChromosome chromosome){
        ArrayList<VM> vmList = chromosome.getVmList();
        int vmListSize = vmList.size();
        int deleteSize = (int) Math.round(vmListSize * allocationDestroyPercentage);
        vmList.sort(new Comparator<VM>() {
            @Override
            public int compare(VM o1, VM o2) {
                double vm1Resource = o1.getCpuRemain() / pmCpu  * o1.getMemRemain() / pmMem;
                double vm2Resource = o2.getCpuRemain() / pmCpu  * o2.getMemRemain() / pmMem;
                if(vm1Resource < vm2Resource){
                    return 1;
                } else if(vm1Resource == vm2Resource){
                    return 0;
                } else {
                    return -1;
                }
            }
        });

        // delete the bottom "deleteSize" of VMs
        for(int i = 0; i < deleteSize; i++) {
            VM lastVm = vmList.get(vmList.size() - 1);
            leftOver.addAll(lastVm.getContainerList());
            vmList.remove(vmList.size() - 1);
        }

    }



    public void repair(ALNSChromosome chromosome){
        double u = StdRandom.uniform();
        if(u < 0.5){
            firstFitInsertion(chromosome);
        } else{
            bestFitInsertion(chromosome);
        }
    }


    // First, randomly shuffle the list of leftOver containers
    // Second, insert the leftOver containers with BestFit with volume
    // volume = 1 / (1 - leftCpu) * 1 / (1 - leftMem)
    // the VM with the lowest VM wins
    // If no current VM is available, randomly create a new VM to allocate.
    public void bestFitInsertion(ALNSChromosome chromosome){
        ArrayList<VM> vmList = chromosome.getVmList();
        Collections.shuffle(leftOver, new Random(seed));
        for(Container container:leftOver){
            double bestScore = 1.0;
            VM bestVM = null;
            for(VM vm:vmList){
                if(vm.getCpuRemain() >= container.getCpu() &&
                        vm.getMemRemain() >= container.getMem()){
                    double volume = 1 / (1 - (vm.getCpuRemain() - container.getCpu()) / pmCpu) *
                            1 / (1 - (vm.getMemRemain() - container.getMem()) / pmMem);
                    if(volume < bestScore){
                        bestScore = volume;
                        bestVM = vm;
                    }
                }
            }
            if(bestVM != null){
                bestVM.allocate(container);
            } else{
                boolean allocated = false;
                while (!allocated) {
                    int vmType = StdRandom.uniform(vmCpu.length);
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
    }


    // Insert the leftOver containers with FirstFit
    // If the current VMs cannot host the container, uniformly create one and add to the vmList
    public void firstFitInsertion(ALNSChromosome chromosome){

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
                    int vmType = StdRandom.uniform(vmCpu.length);
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
    }

//    public void localSearch(ALNSChromosome chromosome){
//
//    }
}
