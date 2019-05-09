import algorithms.Chromosome;
import algorithms.Mutation;
import algorithms.StdRandom;

import java.util.*;

public class CecGAMutation implements Mutation {
    private int vmTypes;
    private int numOfVm;
    private int numOfContainer;
    private double vmCpuOverheadRate;
    private double vmMemOverhead;
    private double[] vmCpu;
    private double[] vmMem;
    private double[] taskCpu;
    private double[] taskMem;
    private double consolidationFactor;

    public CecGAMutation(int vmTypes,
                         int numOfVm,
                         int numOfContainer,
                         double[] vmCpu,
                         double[] vmMem,
                         double[] taskCpu,
                         double[] taskMem,
                         double vmCpuOverheadRate,
                         double vmMemOverhead,
                         double consolidationFactor,
                         int seed){
        this.vmTypes = vmTypes;
        this.numOfContainer = numOfContainer;
        this.numOfVm = numOfVm;
        this.vmCpu = vmCpu;
        this.vmMem = vmMem;
        this.taskCpu = taskCpu;
        this.taskMem = taskMem;
        this.vmCpuOverheadRate = vmCpuOverheadRate;
        this.vmMemOverhead = vmMemOverhead;
        this.consolidationFactor = consolidationFactor;
        StdRandom.setSeed(seed);
    }

    public void update(Chromosome individual, double mutationRate){
        for(int i = 0; i < numOfContainer; i++){
            if(StdRandom.uniform() <= mutationRate){
                mutateContainerAllocation((CecGAChromosome) individual, i);
            }
        }

//        mutateVmAllocation((CecGAChromosome) individual);
//
//        repairIndex((CecGAChromosome) individual);

        if(!validation((CecGAChromosome) individual)){
            throw(new IllegalStateException());
        }
    }


    private boolean validation(CecGAChromosome chromo){
        HashSet<Integer> vmSet = new HashSet<Integer>();
        int topNum = 0;
        for(int i = 0; i < numOfContainer; i++){
            vmSet.add(chromo.individual[i * 2 + 1]);
            if(topNum < chromo.individual[i * 2 + 1])
                topNum = chromo.individual[i * 2 + 1];
        }
        if(vmSet.size() != topNum + 1){
//            System.out.println("Wrong");
//            chromo.print();
            return false;
        }
        return true;

    }



    // return a array of ArrayList,
    // each entry of the array is a type of vm
    // each entry of the ArrayList is the number of vm
    private ArrayList<Integer>[] findExistingVms(CecGAChromosome chromo){
        ArrayList<Integer>[] existingVMTypes = new ArrayList[vmTypes];
        for(int i = 0; i < vmTypes; i++) existingVMTypes[i] = new ArrayList<Integer>();
        for(int i = 0; i < numOfContainer; i++){
            int type = chromo.individual[i * 2];
            // If the there is no machine or the current list has no such vm number, then add the vm number in the list
            if(existingVMTypes[type].isEmpty())
                existingVMTypes[type].add(chromo.individual[i * 2 + 1]);
            if(!existingVMTypes[type].contains(chromo.individual[i * 2 + 1])){
                existingVMTypes[type].add(chromo.individual[i * 2 + 1]);
            }
        }

        return existingVMTypes;
    }


    // find a minimum
    private int suitableVM(int taskNo){
        int vmType = 0;
        for(int k = 0; k < vmTypes; k++){
            if(vmCpu[k] - vmCpu[k] * vmCpuOverheadRate - taskCpu[taskNo] >= 0
                    && vmMem[k] - vmMemOverhead - taskMem[taskNo] >= 0){
                vmType = k;
                break;
            }
        }
        return vmType;
    }

    // count the number of VMs
    private int countNumOfVm(CecGAChromosome chromo){
        int numOfVms = 0;
        for(int i = 0; i < numOfContainer; ++i){
            if(numOfVms < chromo.individual[i * 2 + 1]) numOfVms = chromo.individual[i * 2 + 1];
        }
        return numOfVms;
    }

    private void repairIndex(CecGAChromosome chromo){
        TreeMap<Integer, ArrayList<Integer>> vmList = new TreeMap<>();
        for(int i = 0; i < numOfContainer; ++i){
            if(vmList.containsKey(chromo.individual[i * 2 + 1])){
                ArrayList<Integer> vm = vmList.get(chromo.individual[i * 2 + 1]);
                vm.add(i);
            }else{
                ArrayList<Integer> vm = new ArrayList<>();
                vm.add(i);
                vmList.put(chromo.individual[i * 2 + 1], vm);
            }
        }
//        System.out.println("vmList.size() = " + vmList.size());

        int index = 0;
        for(Map.Entry entry:vmList.entrySet()){
            ArrayList<Integer> vm = (ArrayList<Integer>) entry.getValue();
            for(int i = 0; i < vm.size(); ++i){
                chromo.individual[vm.get(i) * 2 + 1] = index;
            }
            index += 1;
        }

    }

    private int findCloestIndex(CecGAChromosome chromo, int index){
        Integer gap = null;
        int myIndex = 0;
        for(int i = 0; i < numOfContainer; ++i){
            if(chromo.individual[i * 2 + 1] > index){
                if(gap == null) {
                    gap = chromo.individual[i * 2 + 1] - index;
                    continue;
                }

                if(gap < chromo.individual[i * 2 + 1] - index) {
                    gap = chromo.individual[i * 2 + 1] - index;
                    myIndex = i;
                }
            }
        }
        return myIndex;
    }


    // switch two groups of containers' vm indexes and types
    private void mutateVmAllocation(CecGAChromosome chromo){
        int numOfVms = countNumOfVm(chromo);
        int chosenVm1 = StdRandom.uniform(numOfVms);
        int typeOfVm1 = findType(chromo, chosenVm1);
        int chosenVm2 = StdRandom.uniform(numOfVms);
        int typeOfVm2 = findType(chromo, chosenVm2);
        ArrayList<Integer> vm2Containers = new ArrayList<>();

        for(int i = 0; i < numOfContainer; ++i){
            if(chromo.individual[i * 2 + 1] == chosenVm2){
                vm2Containers.add(i);
            }
        }

        for(int i = 0; i < numOfContainer; ++i){
            if(chromo.individual[i * 2 + 1] == chosenVm1){
                chromo.individual[i * 2] = typeOfVm2;
                chromo.individual[i * 2 + 1] = chosenVm2;
            }
        }

        for(int i = 0; i < vm2Containers.size(); ++i){
            chromo.individual[vm2Containers.get(i) * 2 + 1] = chosenVm1;
            chromo.individual[vm2Containers.get(i) * 2] = typeOfVm1;
        }
    }

    // Since now, the VM types are not linear increasing, we can only find suitable types one by one.
    private int randomChooseStrongerVmType(int minimumType){
        ArrayList<Integer> suitableTypes = new ArrayList<>();
        for(int i = 0; i < vmCpu.length; i++){
            if(vmCpu[i] >= vmCpu[minimumType] && vmMem[i] >= vmMem[minimumType])
                suitableTypes.add(i);
        }

        int chosenIndex = StdRandom.uniform(suitableTypes.size());
        return suitableTypes.get(chosenIndex);
    }

    private void mutateContainerAllocation(CecGAChromosome chromo, int index){
        // First step, find out how many VMs we have used
        int numOfVms = countNumOfVm(chromo) + 1;

        // randomly select a vm to allocate, [0, numOfVms + 1)
        int chosenVm = StdRandom.uniform(numOfVms + 1);

        int typeOfChosenVm;
        // if we create a new VM to host this container
        if(chosenVm == numOfVms){
            int minimumVm = suitableVM(index);
//            typeOfChosenVm = StdRandom.uniform(minimumVm, vmTypes);
            typeOfChosenVm = randomChooseStrongerVmType(minimumVm);
        // else, we check if this VM is suitable for the container, if not, we just return without changing anything
        } else{
            typeOfChosenVm = findType(chromo, chosenVm);
//            if(vmCpu[typeOfChosenVm] - vmCpuOverheadRate * vmCpu[typeOfChosenVm] < taskCpu[index] ||
//                    vmMem[typeOfChosenVm] - vmMemOverhead < taskMem[index]) return;
            double[] vm = buildVmTable(chromo, chosenVm, typeOfChosenVm);
            if(vm[0] < taskCpu[index] || vm[1] < taskMem[index]) return;
        }
        chromo.individual[index * 2 + 1] = chosenVm;
        chromo.individual[index * 2] = typeOfChosenVm;
        // If it is overloaded, then we must repair it,
        // and we know we can fix this problem by removing containers from this VM
//        while(checkOverloadingVm(chromo, chosenVm)){
//            fix(chromo, chosenVm);
//        }
        if(!validation(chromo)){
            repairIndex(chromo);
        }
    }

    private double[] buildVmTable(CecGAChromosome chromo, int chosenVm, int typeOfChosenVm){
        double[] vm = new double[2];
        vm[0] = vmCpu[typeOfChosenVm] - vmCpu[typeOfChosenVm] * vmCpuOverheadRate;
        vm[1] = vmMem[typeOfChosenVm] - vmMemOverhead;
        for(int i = 0; i < numOfContainer; ++i){
            if(chromo.individual[i * 2 + 1] == chosenVm){
                vm[0] -= taskCpu[i];
                vm[1] -= taskMem[i];
            }
        }
        return vm;
    }

//    private void fix(CecGAChromosome chromo, int chosenVm){
//        for(int i = 0; i < numOfContainer; ++i){
//            // Start from the first one, move it to other feasible VM
//            if(chromo.individual[i * 2 + 1] == chosenVm){
//
//            }
//        }
//    }

    private boolean checkOverloadingVm(CecGAChromosome chromo, int chosenVm){
        int type = findType(chromo, chosenVm);
        double[] vm = new double[2];
        vm[0] = vmCpu[type] - vmCpu[type] * vmCpuOverheadRate;
        vm[1] = vmMem[type] - vmMemOverhead;
        for(int i = 0; i < numOfContainer; ++i){
            if(chromo.individual[i * 2 + 1] == chosenVm){
                vm[0] -= taskCpu[i];
                vm[1] -= taskMem[i];
                if(vm[0] < 0 || vm[1] < 0)  return true;
            }
        }

        return false;
    }

    // find the type of the chosen VM
    private Integer findType(CecGAChromosome chromo, int chosenVm){
        Integer vmType = null;
        for(int i = 0; i < numOfContainer; ++i){
            if(chromo.individual[i * 2 + 1] == chosenVm){
                vmType = chromo.individual[i * 2];
                break;
            }
        }
        return vmType;
    }

}
