package multiobjectiveMicroService;

import algorithms.Chromosome;
import algorithms.StdRandom;
import algorithms.TwoParentsCrossover;

import cloudResourceUnit.*;
import java.util.ArrayList;
import java.util.Arrays;


public class MultiGroupGACrossover implements TwoParentsCrossover{

    private MultiGroupGARearrangement rearrangement;
    private MultiGroupGAInitialization initialization;

    public MultiGroupGACrossover(MultiGroupGARearrangement rearrangement,
                                 MultiGroupGAInitialization initialization){
        this.initialization = initialization;
        this.rearrangement = rearrangement;
    }


    @Override
    public Chromosome[] update(Chromosome father, Chromosome mother, double crossoverRate) {

        int cpu = 0;
        int mem = 1;
        MultiGroupGAChromosome children1 = update((MultiGroupGAChromosome)father, (MultiGroupGAChromosome) mother, crossoverRate, cpu);
        MultiGroupGAChromosome children2 = update((MultiGroupGAChromosome)mother, (MultiGroupGAChromosome) father, crossoverRate, mem);
//        FirstStepGAChromosome children2 = newChromo();
        MultiGroupGAChromosome[] children = new MultiGroupGAChromosome[2];
        children[0] = children1;
        children[1] = children2;
        return children;
    }

    private MultiGroupGAChromosome newChromo(){
        return initialization.generateChromosome();
    }

    private MultiGroupGAChromosome update(MultiGroupGAChromosome father, MultiGroupGAChromosome mother, double crossoverRate, int res){
        double u = StdRandom.uniform();
        if(u < crossoverRate) {
//            father.sortPms();
//            mother.sortPms();
            if(res == 0) {
                father.sortPmsCpuUtil();
                mother.sortPmsCpuUtil();
            } else {
                father.sortPmAvailability();
                mother.sortPmAvailability();

            }
            ArrayList<PM> fatherList = father.getPmList();
            ArrayList<PM> motherList = mother.getPmList();

            ArrayList<PM> childList = new ArrayList<>();

            int[] containers = new int[father.getNumOfContainers()];
            // 0: Not allocated, 1: allocated
            Arrays.fill(containers, 0);

            // Compare PMs pair by pair
            for(int i = 0; i < fatherList.size(); i++){

                // copy the structure of the PM including the VM types inside the PM
                // check if the containers in the winner PM has been allocated, and allocate the containers to the original VMs
                PM copiedPm;
                PM winnerPm;
                if(i >= motherList.size()){
                    copiedPm = fatherList.get(i).copyStructure();
                    winnerPm = fatherList.get(i);
                } else {
                    if (res == 0){
                        if (fatherList.get(i).getCpuUtil() <= motherList.get(i).getCpuUtil()) {
                            copiedPm = fatherList.get(i).copyStructure();
                            winnerPm = fatherList.get(i);
                        } else {
                            copiedPm = motherList.get(i).copyStructure();
                            winnerPm = motherList.get(i);
                        }
                    } else{
//                        if (fatherList.get(i).getMemUtil() <= motherList.get(i).getMemUtil()) {
                        if(fatherList.get(i).getReplicaNum() <= motherList.get(i).getReplicaNum()){
                            copiedPm = fatherList.get(i).copyStructure();
                            winnerPm = fatherList.get(i);
                        } else {
                            copiedPm = motherList.get(i).copyStructure();
                            winnerPm = motherList.get(i);
                        }

                    }
                }


                // Check if the container has been allocated
                ArrayList<Integer> containerIdList = winnerPm.getContainerIdList();
                for(int j = 0; j < containerIdList.size(); j++){
                    Integer containerId = containerIdList.get(j);

                    // If we have not allocated this container, then allocate this container
                    if(containers[containerId] == 0){
                        // find out which vm owns the container
                        Integer vmIndex = winnerPm.findOwnerVmIndex(containerId);
                        VM vm = winnerPm.findOwnerVm(vmIndex);
                        if(vm == null){
                            throw new IllegalStateException("Cannot find this container in the PM");
                        }

                        // find out this unallocated container
                        Container container = vm.getContainerList().get(vm.getContainerIdList().indexOf(containerId));
                        copiedPm.getVmList().get(vmIndex).allocate(new Container(containerId,
                                                                                container.getCpu(),
                                                                                container.getMem(),
                                                                                container.getApplicationId(),
                                                                                container.getMicroServiceId(),
                                                                                container.getReplicaId()));
                        containers[containerId] = 1;
                    }
                }
                // add to the child's list
                childList.add(copiedPm);

            } // Now the repeated containers have been released from PMs


//            for(PM pm:childList){
//                ArrayList<VM> vmList = pm.getVmList();
//                for(VM vm:vmList){
//                    ArrayList<Container> containerList = vm.getContainerList();
//                    for(Container container : containerList){
//                        if(container.getMotherVm() == null){
//                            System.out.println("Before alert");
//                        }
//                    }
//                }
//            }
            // update the appList for each PM
           for(PM pm:childList) {
                pm.updateAppTable();
           }
//            System.out.println("1" + childList);

            // It is time to allocate the rest of the containers back to the current PMs,
            // We use the rearrangement operator to do this task.
            for(int i = 0; i < containers.length; i++){
                if(containers[i] == 0){
                    rearrangement.insert(childList, i);
                    containers[i] = 1;
                }
            }

            cleanEmptyPmsVms(childList);
            MultiGroupGAChromosome child = new MultiGroupGAChromosome(father.getNumOfContainers());
            child.setPmList(childList);
            child.updateAppList();



            return child;
        } else {
            return (MultiGroupGAChromosome) father.clone();
        }
    }

    /**
     * First, we delete all the empty VMs,
     * Second, we delete all the empty PMs
     * @param childList
     */
    public void cleanEmptyPmsVms(ArrayList<PM> childList){


        // delete all the empty VMs in the PMs
        for(PM pm:childList) {
            ArrayList<VM> vmList = pm.getVmList();
            boolean flag = false;
            while (!flag) {
                flag = true;
                for (int i = 0; i < vmList.size(); i++) {
                    if (vmList.get(i).getContainerList().size() == 0) {
                        pm.release(i);
                        flag = false;
                        break;
                    }
                }
            }
        }

        boolean flag = false;
        while(!flag){
            flag = true;
            for(int i = 0; i < childList.size(); i++){
                if(childList.get(i).getVmList().size() == 0){
                    childList.remove(i);
                    flag = false;
                    break;
                }
            }
        } // end of removing empty Pms
    }



}
