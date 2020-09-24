package GroupGA;

import algorithms.Chromosome;
import algorithms.Mutation;
import algorithms.StdRandom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * In this version, the mutation operator simply decides which PMs to break down and reallocate their containers
 */
public class GroupGAMutation implements Mutation {

    private GroupGARearrangement rearrangement;
    private double[] containerCpu;
    private double[] containerMem;
    private double[] vmCpu;
    private double[] vmMem;
    private double pmCpu;
    private double pmMem;
    private double vmCpuOverheadRate;
    private double vmMemOverhead;


    public GroupGAMutation(GroupGARearrangement rearrangement,
                           double[] containerCpu,
                           double[] containerMem,
                           double[] vmCpu,
                           double[] vmMem,
                           double pmCpu,
                           double pmMem,
                           double vmCpuOverheadRate,
                           double vmMemOverhead){

        this.rearrangement = rearrangement;
        this.containerCpu = containerCpu;
        this.containerMem = containerMem;
        this.vmCpu = vmCpu;
        this.vmMem = vmMem;
        this.pmCpu = pmCpu;
        this.pmMem = pmMem;
        this.vmCpuOverheadRate = vmCpuOverheadRate;
        this.vmMemOverhead = vmMemOverhead;
    }


    @Override
    public void update(Chromosome individual, double mutationRate) {
        update((GroupGAChromosome) individual);
        mutateVmTypes((GroupGAChromosome) individual);
    }



    // This mutation will first sort the PMs according to VM overheads in descending order
    // Then, we select the first PM to optimize
    // Sort the VM according to their memory
    // Select two smallest VMs
    private void mutateVmTypes(GroupGAChromosome individual){
        individual.sortPmsVmOverhead();
        PM pm = individual.getPmList().get(0);
        ArrayList<VM> vmList = pm.getVmList();

//        Boolean flag = true;
//        while(flag) {
            // ascending order
            Collections.sort(vmList, new Comparator<VM>() {
                @Override
                public int compare(VM o1, VM o2) {
                    double res1 = o1.getConfigureCpu() / pmCpu * o1.getConfigureMem() / pmMem;
                    double res2 = o2.getConfigureCpu() / pmCpu * o2.getConfigureMem() / pmMem;
                    if (res1 < res2) {
                        return -1;
                    } else if (res1 == res2) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            });

//            for (int j = 0; j < vmList.size(); j++) {
                VM vm1 = vmList.get(0);
                VM vm2 = vmList.get(1);
                ArrayList<Integer> possibleTypes = new ArrayList<>();

                for (int i = 0; i < vmCpu.length; i++) {

//                    double totalMem = vm1.getConfigureMem() + vm2.getConfigureMem();
                    double total = (vm1.getConfigureCpu() + vm2.getConfigureCpu()) / pmCpu * (vm1.getConfigureMem() + vm2.getConfigureMem()) / pmMem;
                    double targetVm = vmCpu[i] / pmCpu * vmMem[i] / pmMem;
                    // memory is larger
                    if (targetVm >= total) {

                        double pmCpuRemain = pm.getCpuRemain() + vm1.getConfigureCpu() + vm2.getConfigureCpu();
                        double pmMemRemain = pm.getMemRemain() + vm1.getConfigureMem() + vm2.getConfigureMem();

                        // PM has enough space
                        if (pmCpuRemain >= vmCpu[i] && pmMemRemain >= vmMem[i]) {

                            double totalContainerCpu = vm1.getCpuUsage() - vmCpu[vm1.getType()] * vmCpuOverheadRate
                                    + vm2.getCpuUsage() - vmCpu[vm2.getType()] * vmCpuOverheadRate;
                            double totalContainerMem = vm1.getMemUsage() + vm2.getMemUsage() - 2 * vmMemOverhead;

                            // has enough space for the current containers
                            if (vmCpu[i] >= totalContainerCpu + vmCpu[i] * vmCpuOverheadRate &&
                                    vmMem[i] >= totalContainerMem + vmMemOverhead) {
                                possibleTypes.add(i);


                            }
                        }
                    }
                }

                if(possibleTypes.size() != 0){

                    int randomSelectVmIndex = StdRandom.uniform(possibleTypes.size());
                    int selectedType = possibleTypes.get(randomSelectVmIndex);
                    VM newReplaceVm = new VM(selectedType, vmCpu[selectedType], vmMem[selectedType], vmCpuOverheadRate, vmMemOverhead);

                    // allocate containers to the new VM
                    ArrayList<Integer> containersList1 = vm1.getContainerIdList();
                    ArrayList<Integer> containersList2 = vm2.getContainerIdList();

//                if (containerList1 == null || containerList2 == null) {
//                    break;
//                }
                    for (Integer containerId : containersList1) {
                        newReplaceVm.allocate(new Container(containerId,
                                containerCpu[containerId],
                                containerMem[containerId]));
                    }
                    for (Integer containerId : containersList2) {
                        newReplaceVm.allocate(new Container(containerId,
                                containerCpu[containerId],
                                containerMem[containerId]));
                    }
                    pm.release(1);
                    pm.release(0);
                    pm.allocate(newReplaceVm);
                }
//                break;
//                if (j == vmList.size() - 2) {
//                    flag = false;
//                    break;
//                }
//            }  // end of two VMs
//        }
//            flag = true;
//            while (flag){
        // If no possible type of VMs can replace the two smallest VM
        if(possibleTypes.size() == 0) {
            // ascending order
            Collections.sort(vmList, new Comparator<VM>() {
                @Override
                public int compare(VM o1, VM o2) {
                    double res1 = o1.getConfigureCpu() / pmCpu * o1.getConfigureMem() / pmMem;
                    double res2 = o2.getConfigureCpu() / pmCpu * o2.getConfigureMem() / pmMem;
                    if (res1 < res2) {
                        return -1;
                    } else if (res1 == res2) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            });

            // Find a VM which can be replace with a larger one
            for (int j = 0; j < vmList.size(); j++) {
                vm1 = vmList.get(j);
                for (int i = 0; i < vmCpu.length; i++) {
                    double total = vmCpu[i] / pmCpu * vmMem[i] / pmMem;
                    double target = vm1.getConfigureCpu() / pmCpu * vm1.getConfigureMem() / pmMem;
                    // memory is larger
//                        if(vmMem[i] > vm1.getConfigureMem()){
                    if (target >= total) {
                        double pmCpuRemain = pm.getCpuRemain() + vm1.getConfigureCpu();
                        double pmMemRemain = pm.getMemRemain() + vm1.getConfigureMem();
                        // PM has enough space
                        if (pmCpuRemain >= vmCpu[i] && pmMemRemain >= vmMem[i]) {
                            double totalContainerCpu = vm1.getCpuUsage() - vmCpu[vm1.getType()] * vmCpuOverheadRate;
                            double totalContainerMem = vm1.getMemUsage() - vmMemOverhead;
                            // has enough space for the current containers
                            if (vmCpu[i] >= totalContainerCpu + vmCpu[i] * vmCpuOverheadRate &&
                                    vmMem[i] >= totalContainerMem + vmMemOverhead) {
                                VM newVm = new VM(i, vmCpu[i], vmMem[i], vmCpuOverheadRate, vmMemOverhead);
                                // allocate containers to the new VM
                                ArrayList<Integer> containerList1 = vm1.getContainerIdList();
                                if (containerList1 == null) {
                                    break;
                                }
                                for (Integer containerId : containerList1) {
                                    newVm.allocate(new Container(containerId,
                                            containerCpu[containerId],
                                            containerMem[containerId]));
                                }
                                pm.release(j);
                                pm.allocate(newVm);
                                break;
                            }
                        }

                    }
                }
            }
        }
//                    if(j == vmList.size() - 1){
//                        flag = false;
//                        break;
//                    }
//                }
//            } //end of enlarge
    }



    // 1. use Roulette wheel method to choose the PMs to be unpacked from the second part of PMs
    // 2. use rearrangement operator to reallocate the containers
    public void update(GroupGAChromosome individual){

        // Find the median PM, begin = medianPM
        ArrayList<PM> pmList = individual.getPmList();
        int numOfPms = individual.size();
//        int begin;
////        individual.sortPms();
//        if(numOfPms % 2 == 0){
//            begin = numOfPms / 2;
//        } else {
//            begin = (int) Math.ceil(numOfPms / 2.0);
//        }
//
//        // Make the probability table for selecting PMs to unpack
//        double[] prob = new double[numOfPms - begin];
//        double totalWaste = 0;
//        for(int i = begin; i < numOfPms; i++){
//            totalWaste += pmList.get(i).getWastedResource();
//        }
//        prob[0] = pmList.get(begin).getWastedResource() / totalWaste;
//        for(int i = 1; i < numOfPms - begin; i++){
//            prob[i] = prob[i - 1] + pmList.get(i + begin).getWastedResource() / totalWaste;
//        }

        // Make a probability table
        double[] prob = new double[numOfPms];
        double total = 0;
        for(int i = 0; i < numOfPms; i++){
            total += 1 - pmList.get(i).getCpuUtil();
        }
        prob[0] = (1 - pmList.get(0).getCpuUtil()) / total;
        for(int i = 1; i < numOfPms; i++){
            prob[i] = prob[i - 1] + (1 - pmList.get(i).getCpuUtil()) / total;
        }

        // choose the PM to unpack
        ArrayList<PM> chosenPM = new ArrayList<>();

        // From end to beginning
//        for(int i = numOfPms - 1; i >= begin; i--){
//            // uniformly generate a random number
//            double u = StdRandom.uniform();
//            if(i == begin){
//                if(u < prob[i - begin])
//                    chosenPM.add(pmList.get(i));
//            } else {
//                if (u < prob[i - begin] && u > prob[i - 1 - begin]) {
//                    chosenPM.add(pmList.get(i));
//                }
//            }
//        }
        for(int i = numOfPms - 1; i >= 0; i--){
            double u = StdRandom.uniform();
            if(i == 0){
                if(u < prob[0])
                    chosenPM.add(pmList.get(i));
            } else {
                if(u < prob[i] && u > prob[i - 1]){
                    chosenPM.add(pmList.get(i));
                }
            }
        }

        // unpack pms and add all their containers into the list
        ArrayList<Integer> containerIdToReallocate = new ArrayList<>();
        // add all the PM's containers into the list and remove the pm from the list
        for(PM pm:chosenPM){
            containerIdToReallocate.addAll(pm.getContainerIdList());
            pmList.remove(pm);
        }

        // use rearrangement operator to allocate these containers
        for(Integer containerId:containerIdToReallocate){
            rearrangement.insert(pmList, containerId);
        }

        // done
    }


}
