package GroupGA.src;

import algorithms.StdRandom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GroupGARearrangement {
    private double[] containerCpu;
    private double[] containerMem;
    private double[] vmCpu;
    private double[] vmMem;
    private double vmCpuOverheadRate;
    private double vmMemOverhead;
    private double pmCpu;
    private double pmMem;
    private double k;
    private double maxEnergy;

    public GroupGARearrangement(double[] containerCpu,
                                double[] containerMem,
                                double[] vmCpu,
                                double[] vmMem,
                                double vmCpuOverheadRate,
                                double vmMemOverhead,
                                double pmCpu,
                                double pmMem,
                                double k,
                                double maxEnergy){
        this.containerCpu = containerCpu;
        this.containerMem = containerMem;
        this.vmCpu = vmCpu;
        this.vmMem = vmMem;
        this.vmCpuOverheadRate = vmCpuOverheadRate;
        this.vmMemOverhead = vmMemOverhead;
        this.pmCpu = pmCpu;
        this.pmMem = pmMem;
        this.k = k;
        this.maxEnergy = maxEnergy;
    }

    public void insert(ArrayList<PM> pmList, int containerId){
        // We first try FF, otherwise, create new VM
//        if(!FirstFitToVm(pmList, containerId)){
//            VM vm = vmCreation(containerId);
//            vm.allocate(new Container(containerId, containerCpu[containerId], containerMem[containerId]));
//
//            // allocate this new VM to existing PMs, if no available PM, create a new PM
//            if(!FirstFitToPm(pmList, vm)){
//                PM pm = new PM(pmCpu, pmMem, k, maxEnergy);
//                pm.allocate(vm);
//                pmList.add(pm);
//            }
//        }
        // We first find and replace the larger container with two smaller containers
        ArrayList<Integer> smallContainers = findAndReleaseAndAllocate(pmList, containerId);
        if(smallContainers == null){
            if(!FirstFitToVm(pmList, containerId)){
            VM vm = vmCreation(containerId);
            vm.allocate(new Container(containerId, containerCpu[containerId], containerMem[containerId]));

            // allocate this new VM to existing PMs, if no available PM, create a new PM
            if(!FirstFitToPm(pmList, vm)){
                PM pm = new PM(pmCpu, pmMem, k, maxEnergy);
                pm.allocate(vm);
                pmList.add(pm);
                }
            }
        } else {
            for(Integer smallCon:smallContainers){
                if(!FirstFitToVm(pmList, smallCon)){
                    VM vm = vmCreation(smallCon);
                    vm.allocate(new Container(smallCon, containerCpu[smallCon], containerMem[smallCon]));

                    // allocate this new VM to existing PMs, if no available PM, create a new PM
                    if(!FirstFitToPm(pmList, vm)){
                        PM pm = new PM(pmCpu, pmMem, k, maxEnergy);
                        pm.allocate(vm);
                        pmList.add(pm);
                    }
                }
            }

        }
    }

    private ArrayList<Integer> findAndReleaseAndAllocate(ArrayList<PM> pmList, int containerId){
        ArrayList<Integer> selectedContainers = new ArrayList<>();

        for(PM pm:pmList){
            ArrayList<VM> vmList = pm.getVmList();
            for(VM vm:vmList){
                ArrayList<Container> conList = (ArrayList<Container>) vm.getContainerList().clone();
                // ascending order
                Collections.sort(conList, new Comparator<Container>() {
                    @Override
                    public int compare(Container o1, Container o2) {
                        double totalRes1, totalRes2;
                        totalRes1 = o1.getCpu() / pmCpu * o1.getMem() / pmMem;
                        totalRes2 = o2.getCpu() / pmCpu * o2.getMem() / pmMem;

                        if (totalRes1 < totalRes2) {
                            return -1;
                        } else if (totalRes1 == totalRes2) {
                            return 0;
                        } else {
                            return 1;
                        }
                    }
                });

                // Look for the smaller containers
                if(conList.size() < 2){
                    return null;
                }
                Container container1 = conList.get(0);
                Container container2 = conList.get(1);
                double vmRemainCpu = container1.getCpu() + vm.getCpuRemain() + container2.getCpu();
                double vmRemainMem = container1.getMem() + container2.getMem() + vm.getMemRemain();
                // if we find them, release them and add them into the list
                if(containerCpu[containerId] >= container1.getCpu() + container2.getCpu()
                        && containerMem[containerId] >= container1.getMem() + container2.getMem()
                        && vmRemainCpu >= containerCpu[containerId]
                        && vmRemainMem >= containerMem[containerId]){

                        selectedContainers.add(container1.getId());
                        selectedContainers.add(container2.getId());
                        vm.releaseById(container1.getId());
                        vm.releaseById(container2.getId());

                        vm.allocate(new Container(containerId,
                                    containerCpu[containerId],
                                    containerMem[containerId]));
                        return selectedContainers;
                }
            }
        }
        return null;
    }


    // Random type
    private VM vmCreation(int containerIndex){
//        int bestVm = 0;
//        double minimumWaste = 1;
//        for(int i = 0; i < vmCpu.length; i++){
//            if(vmCpu[i] > containerCpu[containerIndex] + vmCpuOverheadRate * vmCpu[i] &&
//                    vmMem[i] > containerMem[containerIndex] + vmMemOverhead){
//                double normalizedCpuWaste = (vmCpu[i] - containerCpu[containerIndex] - vmCpuOverheadRate * vmCpu[i]) / vmCpu[i];
//                double normalizedMemWaste = (vmMem[i] - containerMem[containerIndex] - vmMemOverhead) / vmMem[i];
//                double normalizedWaste = normalizedCpuWaste < normalizedMemWaste ? normalizedCpuWaste:normalizedMemWaste;
//                if(normalizedWaste < minimumWaste){
//                    bestVm = i;
//                    minimumWaste = normalizedWaste;
//                }
//            }
//        }
        int chosenIndex = 0;
//        boolean suitable = false;
//        do{
//            chosenType = StdRandom.uniform(vmCpu.length);
//            if(vmCpu[chosenType] > containerCpu[containerIndex] + vmCpuOverheadRate * vmCpu[chosenType] &&
//                    vmMem[chosenType] > containerMem[containerIndex] + vmMemOverhead){
//                suitable = true;
//            }
//
//        } while(suitable);
        ArrayList<Integer> suitableVmTypes = new ArrayList<>();
        for(int i = 0; i < vmCpu.length; i++){
            if(vmCpu[i] > containerCpu[containerIndex] + vmCpuOverheadRate * vmCpu[i] &&
                    vmMem[i] > containerMem[containerIndex] + vmMemOverhead){
                suitableVmTypes.add(i);
            }
        }
        chosenIndex = StdRandom.uniform(suitableVmTypes.size());
        int chosenType = suitableVmTypes.get(chosenIndex);
        VM vm = new VM(chosenType, vmCpu[chosenType], vmMem[chosenType], vmCpuOverheadRate, vmMemOverhead);
        return vm;
    }
    // allocate a VM to a PM using FF
    private Boolean FirstFitToPm(ArrayList<PM> pmList, VM vm){
        for(PM pm:pmList){
            if(pm.getCpuRemain() >= vm.getConfigureCpu() && pm.getMemRemain() >= vm.getConfigureMem()){
                pm.allocate(vm);
                return true;
            }
        }
        return false;
    }

    // allocate a container to a VM using FF
    private Boolean FirstFitToVm(ArrayList<PM> pmList, int containerIndex){
        // loop through all the PMs
        for(PM pm:pmList){
            ArrayList<VM> vmList = pm.getVmList();

            // loop through all the VMs
            for(VM vm:vmList){
                if(vm.getCpuRemain() > containerCpu[containerIndex] &&
                        vm.getMemRemain() > containerMem[containerIndex]){
                    vm.allocate(new Container(containerIndex,
                                            containerCpu[containerIndex],
                                            containerMem[containerIndex]));
                    return true;
                }
            }
        }
        return false;
    }


}
