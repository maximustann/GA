package GroupGA;

import algorithms.Chromosome;
import algorithms.StdRandom;
import algorithms.TwoParentsCrossover;
//import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;
import java.util.Arrays;


public class GroupGACrossoverDiversity implements TwoParentsCrossover{

    private GroupGARearrangement rearrangement;
    private GroupGAInitialization initialization;

    public GroupGACrossoverDiversity(GroupGARearrangement rearrangement,
                                     GroupGAInitialization initialization){
        this.initialization = initialization;
        this.rearrangement = rearrangement;
    }


    @Override
    public Chromosome[] update(Chromosome father, Chromosome mother, double crossoverRate) {

        GroupGAChromosome children1 = update((GroupGAChromosome)father, (GroupGAChromosome) mother, crossoverRate);
        GroupGAChromosome children2 = update((GroupGAChromosome)mother, (GroupGAChromosome) father, crossoverRate);
//        GroupGAChromosome children2 = newChromo();
        GroupGAChromosome[] children = new GroupGAChromosome[2];
        children[0] = children1;
        children[1] = children2;
        return children;
    }

    private GroupGAChromosome newChromo(){
        return initialization.generateChromosome();
    }


    private Boolean checkDuplicatedCombinations(ArrayList<ArrayList<Integer>> existingList, ArrayList<Integer> target){
        for(ArrayList<Integer> pm:existingList){
            if(compareVmCombinations(pm, target))
                return true;
        }
        return false;
    }
    private Boolean compareVmCombinations(ArrayList<Integer> existing, ArrayList<Integer> target){
        return existing.equals(target);
    }

    private GroupGAChromosome update(GroupGAChromosome father, GroupGAChromosome mother, double crossoverRate){
        double u = StdRandom.uniform();
        if(u < crossoverRate) {
//            father.sortPms();
//            mother.sortPms();
//            father.sortPmsCpuUtil();
//            mother.sortPmsCpuUtil();

            ArrayList<PM> fatherList = father.getPmList();
            ArrayList<PM> motherList = mother.getPmList();
            ArrayList<ArrayList<Integer>> vmCombinations = new ArrayList<>();

            ArrayList<PM> childList = new ArrayList<>();

            int[] containers = new int[father.getNumOfContainers()];
            // 0: Not allocated, 1: allocated
            Arrays.fill(containers, 0);


            // First copy the structure of the first PM from the father
            vmCombinations.add(fatherList.get(0).getVmTypeList());

            // Compare PMs pair by pair
            for(int i = 1; i < fatherList.size(); i++){

                // copy the structure of the PM including the VM types inside the PM
                // check if the containers in the winner PM has been allocated, and allocate the containers to the original VMs
                PM copiedPm;
                PM winnerPm;
                if(i >= motherList.size()){
                    copiedPm = fatherList.get(i).copyStructure();
                    winnerPm = fatherList.get(i);
                } else {
//                    if(fatherList.get(i).getWastedResource() <= motherList.get(i).getWastedResource()){
//                    if(fatherList.get(i).getCpuUtil() <= motherList.get(i).getCpuUtil()){
//                        copiedPm = fatherList.get(i).copyStructure();
//                        winnerPm = fatherList.get(i);
//                    } else {
//                        copiedPm = motherList.get(i).copyStructure();
//                        winnerPm = motherList.get(i);
//                    }
                    ArrayList<Integer> fatherCombination = fatherList.get(i).getVmTypeList();
                    ArrayList<Integer> motherCombination = motherList.get(i).getVmTypeList();
                    if(checkDuplicatedCombinations(vmCombinations, fatherCombination)){
                        winnerPm = fatherList.get(i);
                        copiedPm = fatherList.get(i).copyStructure();
                        vmCombinations.add(fatherCombination);
                    } else if(checkDuplicatedCombinations(vmCombinations, motherCombination)){
                        winnerPm = motherList.get(i);
                        copiedPm = motherList.get(i).copyStructure();
                        vmCombinations.add(motherCombination);
                    } else {
                        winnerPm = fatherList.get(i);
                        copiedPm = fatherList.get(i).copyStructure();
                        vmCombinations.add(fatherCombination);
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
                        copiedPm.getVmList().get(vmIndex).allocate(new Container(containerId, container.getCpu(), container.getMem()));
                        containers[containerId] = 1;
                    }
                }
                // add to the child's list
                childList.add(copiedPm);

            } // Now the repeated containers have been released from PMs

            // It is time to allocate the rest of the containers back to the current PMs,
            // We use the rearrangement operator to do this task.
            for(int i = 0; i < containers.length; i++){
                if(containers[i] == 0){
                    rearrangement.insert(childList, i);
                }
            }

            cleanEmptyPmsVms(childList);
            GroupGAChromosome child = new GroupGAChromosome(father.getNumOfContainers());
            child.setPmList(childList);
            return child;
        } else {
            return (GroupGAChromosome) father.clone();
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
