package multiobjectiveMicroService;

import algorithms.Constraint;
import algorithms.StdRandom;

import cloudResourceUnit.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class MultiGroupGARearrangement {
    private double[] containerCpu;
    private double[] containerMem;
    private double[] containerAppId;
    private double[] containerMicroId;
    private double[] containerReplicaId;
    private double[] vmCpu;
    private double[] vmMem;
    private double vmCpuOverheadRate;
    private double vmMemOverhead;
    private double pmCpu;
    private double pmMem;
    private double k;
    private double maxEnergy;
    private double crushPro;

    public MultiGroupGARearrangement(double[] containerCpu,
                                     double[] containerMem,
                                     double[] containerAppId,
                                     double[] containerMicroId,
                                     double[] containerReplicaId,
                                     double[] vmCpu,
                                     double[] vmMem,
                                     double vmCpuOverheadRate,
                                     double vmMemOverhead,
                                     double pmCpu,
                                     double pmMem,
                                     double k,
                                     double maxEnergy,
                                     double crushPro){
        this.containerCpu = containerCpu;
        this.containerMem = containerMem;
        this.containerAppId = containerAppId;
        this.containerMicroId = containerMicroId;
        this.containerReplicaId = containerReplicaId;
        this.vmCpu = vmCpu;
        this.vmMem = vmMem;
        this.vmCpuOverheadRate = vmCpuOverheadRate;
        this.vmMemOverhead = vmMemOverhead;
        this.pmCpu = pmCpu;
        this.pmMem = pmMem;
        this.k = k;
        this.maxEnergy = maxEnergy;
        this.crushPro = crushPro;
    }

    public void insert(ArrayList<PM> pmList, int containerId){
//                for(PM pm:pmList){
//                ArrayList<VM> vmList = pm.getVmList();
//                for(VM vm:vmList){
//                    ArrayList<Container> containerList = vm.getContainerList();
//                    for(Container container : containerList){
//                        if(container.getMotherVm() == null){
//                            System.out.println("before");
//                        }
//                    }
//                }
//            }
        double u = StdRandom.uniform();
        if(u > 0.5){
            energyBasedInsert(pmList, containerId);
        } else {
//            energyBasedInsert(pmList, containerId);
            availabilityBaseInsert(pmList, containerId);
        }

//        for(PM pm:pmList){
//                ArrayList<VM> vmList = pm.getVmList();
//                for(VM vm:vmList){
//                    ArrayList<Container> containerList = vm.getContainerList();
//                    for(Container container : containerList){
//                        if(container.getMotherVm() == null){
//                            System.out.println("after");
//                        }
//                    }
//                }
//            }

    }

    private void availabilityBaseInsert(ArrayList<PM> pmList, int containerId){


        Integer switchOutContainer = null;
        // 1. Check whether this pm has replicas (its availability need to be improved)
        for(PM pm : pmList){
            if(pm.getReplicaNum() > 0){
                // 2. Check whether the current container will causes more replicas in the first PM
                if(checkCausesMoreReplicas(pm, containerId)) continue;
                switchOutContainer = replaceAndAllocate(pm, containerId);
//                pm.updateAppTable();
                // If equals, it means no replicas can be replaced
                if(switchOutContainer == containerId) continue;
                // else, we have replaced a container, break out from the loop
                else break;
            }
        }

        if(!FirstFitToVm(pmList, containerId)){
            VM vm = vmCreation(containerId);
            vm.allocate(new Container(
                    containerId,
                    containerCpu[containerId],
                    containerMem[containerId],
                    (int) containerAppId[containerId],
                    (int) containerMicroId[containerId],
                    (int) containerReplicaId[containerId])
            );

            // allocate this new VM to existing PMs, if no available PM, create a new PM
            if(!FirstFitToPm(pmList, vm)){
                PM pm = new PM(pmCpu, pmMem, k, maxEnergy, crushPro);
                pm.allocate(vm);
                pmList.add(pm);
            }
        }

    }

    private boolean checkCausesMoreReplicas(PM pm, int containerId){
        ArrayList<VM> vmList = pm.getVmList();
        for(VM vm:vmList){
            ArrayList<Container> containerList = vm.getContainerList();
            for(Container container:containerList){
                if(container.getApplicationId() == containerAppId[containerId] &&
                   container.getMicroServiceId() == containerMicroId[containerId]) return true;
            }
        }
        return false;
    }

    // return containerId if there is no replica can be replaced
    private Integer replaceAndAllocate(PM pm, int containerId){
        pm.updateAppTable();
        ArrayList<Object[]> appList = pm.getAppList();
        for(Object[] app:appList){
            ArrayList<Object[]> microList = (ArrayList<Object[]>) app[1];
            for(Object[] micro:microList){
                ArrayList<Container> containerList = (ArrayList<Container>) micro[1];
                if(containerList.size() > 2){
                    for(Container container:containerList){
                        VM vm = container.getMotherVm();
                        double availCpu = vm.getCpuRemain() + container.getCpu();
                        double availMem = vm.getMemRemain() + container.getMem();
                        if(availCpu >= containerCpu[containerId] && availMem >= containerMem[containerId]){
                            vm.releaseById(container.getId());
                            vm.allocate(new Container(containerId,
                                                    containerCpu[containerId],
                                                    containerMem[containerId],
                                                (int) containerAppId[containerId],
                                                (int) containerMicroId[containerId],
                                                (int) containerReplicaId[containerId]));

                            return container.getId();
                        }
                    }
                }
            }
        }
        return  containerId;
    }

    private void energyBasedInsert(ArrayList<PM> pmList, int containerId){
        // We first find and replace the larger container with two smaller containers
        ArrayList<Integer> smallContainers = findAndReleaseAndAllocate(pmList, containerId);
        if(smallContainers == null){
            if(!FirstFitToVm(pmList, containerId)){
                VM vm = vmCreation(containerId);
                vm.allocate(new Container(
                        containerId,
                        containerCpu[containerId],
                        containerMem[containerId],
                        (int) containerAppId[containerId],
                        (int) containerMicroId[containerId],
                        (int) containerReplicaId[containerId]));

                // allocate this new VM to existing PMs, if no available PM, create a new PM
                if(!FirstFitToPm(pmList, vm)){
                    PM pm = new PM(pmCpu, pmMem, k, maxEnergy, crushPro);
                    pm.allocate(vm);
                    pmList.add(pm);
                }
            }
        } else {
            for(Integer smallCon:smallContainers){
                if(!FirstFitToVm(pmList, smallCon)){
                    VM vm = vmCreation(smallCon);
                    vm.allocate(new Container(
                            smallCon,
                            containerCpu[smallCon],
                            containerMem[smallCon],
                            (int) containerAppId[smallCon],
                            (int) containerMicroId[smallCon],
                            (int) containerReplicaId[smallCon])
                    );

                    // allocate this new VM to existing PMs, if no available PM, create a new PM
                    if(!FirstFitToPm(pmList, vm)){
                        PM pm = new PM(pmCpu, pmMem, k, maxEnergy, crushPro);
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
                                    containerMem[containerId],
                                    (int) containerAppId[containerId],
                                    (int) containerMicroId[containerId],
                                    (int) containerReplicaId[containerId]));
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
                if(vm.getCpuRemain() >= containerCpu[containerIndex] &&
                        vm.getMemRemain() >= containerMem[containerIndex]){
                    vm.allocate(new Container(
                                            containerIndex,
                                            containerCpu[containerIndex],
                                            containerMem[containerIndex],
                                            (int) containerAppId[containerIndex],
                                            (int) containerMicroId[containerIndex],
                                            (int) containerReplicaId[containerIndex]));
                    return true;
                }
            }
        }
        return false;
    }


}
