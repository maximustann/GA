import algorithms.Chromosome;
import algorithms.UnNormalizedFit;

import java.util.ArrayList;

public class EnergyFitness extends UnNormalizedFit{

    private static int numOfContainer;
    private static int numOfVm;
    private static double k;
    private static double pmCpu;
    private static double pmMem;
    private static double pmMaxEnergy;
    private static double[] vmCpu;
    private static double[] vmMem;
    private static double[] taskCpu;
    private static double[] taskMem;
    private static double vmCpuOverheadRate;
    private static double vmMemOverhead;

    // Constructor 1
    public EnergyFitness(int numOfContainer,
                         int numOfVm,
                         double k,
                         double pmCpu,
                         double pmMem,
                         double pmEnergy,
                         double vmCpuOverheadRate,
                         double vmMemOverhead,
                         double[] vmCpu,
                         double[] vmMem,
                         double[] taskCpu,
                         double[] taskMem){
        super(null);

        this.numOfContainer = numOfContainer;
        this.numOfVm = numOfVm;
        this.k = k;
        this.pmCpu = pmCpu;
        this.pmMem = pmMem;
        this.pmMaxEnergy = pmEnergy;
        this.vmCpu = vmCpu;
        this.vmMem = vmMem;
        this.taskCpu = taskCpu;
        this.taskMem = taskMem;
        this.vmCpuOverheadRate = vmCpuOverheadRate;
        this.vmMemOverhead = vmMemOverhead;

    }


    public EnergyFitness(Chromosome individual){
        super(individual);
    }
    @Override
    public Object call() throws Exception {
        double[] fit = new double[2];

        Double energyConsumption = 0.0;
        // Create an empty list of VMs
        ArrayList<ArrayList<Integer>> vmList = new ArrayList<>();
        // Create an empty list of PMs
        ArrayList<ArrayList<ArrayList<Integer>>> pmList = new ArrayList<>();
        // Check which VM has been used, mark with 1, else 0,
        // the side effect is to fill the vmList and vmStatusList
        int[] usedVm = generateMapping((DualPermutationChromosome) individual, vmList);

        // count the used VMs
        int actualUsedVm = countUsedVm(usedVm);

        // Map the used VM types to actualUsageVm
        int[] actualUsageVm = getRealUsageOfVms((DualPermutationChromosome) individual, usedVm, actualUsedVm);

        // Now, allocate VMs to PMs to construct the full solution
        allocateVMs(actualUsageVm, vmList, pmList, actualUsedVm);

        // Finally, we may calculate the energy based on the full solution of pmList
        energyConsumption = energy(pmList, actualUsageVm);
//        System.out.println("energy Consumption = " + energyConsumption);

        ((DualPermutationChromosome) individual).setFitness(energyConsumption);
        fit[0] = energyConsumption;
        fit[1] = 0;

        return fit;
    }

    // turn arrayList into array, and assign to an individual
    private void setVmListToIndividual(DualPermutationChromosome individual, ArrayList<ArrayList<Integer>> vmList){
        ArrayList<int[]> subVmList = new ArrayList<>();
        for(int i = 0; i < vmList.size(); i++){
            ArrayList<Integer> vm = vmList.get(i);
            int[] vmContainer = new int[vm.size()];
            for(int j = 0; j < vm.size(); j++){
                vmContainer[j] = vm.get(j);
            }
            subVmList.add(vmContainer);
        }

        individual.setVmList(subVmList);
    }

    private int countUsedVm(int[] usedVm){
        int numOfUsedVm = 0;
        for(int i = 0; i < usedVm.length; i++){
            if(usedVm[i] == 1)
                numOfUsedVm++;
        }
//        System.out.println("used VM number: " + numOfUsedVm);
        return numOfUsedVm;
    }


    /**
     * This function should calculate the energy consumption of all pms
     * @param pmList
     * @return
     */
    private double energy(ArrayList<ArrayList<ArrayList<Integer>>> pmList, int[] actualUsedVm){
        double totalEnergy = 0;
        int globalVmCounter = 0;
        ArrayList<double[]> pmStatusList = new ArrayList<>();

        for(int pmCount = 0; pmCount < pmList.size(); ++pmCount){
            ArrayList<ArrayList<Integer>> pm = pmList.get(pmCount);
            double[] pmStatus = new double[3];
            double pmEnergy = 0;
            double pmCpuUsage = 0;
            double pmMemUsage = 0;
            double pmCpuUtil = 0;
            for(int vmCount = 0; vmCount < pm.size(); ++vmCount, ++globalVmCounter){
                ArrayList<Integer> vm = pm.get(vmCount);
                double vmCpuUsage = vmCpuOverheadRate * vmCpu[actualUsedVm[globalVmCounter]];
                double vmMemUsage = vmMemOverhead;
                for(int containerCount = 0; containerCount < vm.size(); ++containerCount){
                    vmCpuUsage += taskCpu[vm.get(containerCount)];
                    vmMemUsage += taskMem[vm.get(containerCount)];
                }
                pmCpuUsage += vmCpuUsage;
                pmMemUsage += vmMemUsage;
            }

            pmCpuUtil = pmCpuUsage / pmCpu;
//            //---------------------Debug------------------------------
//            System.out.println("pmCpuUtil = " + pmCpuUtil);
//            System.out.println("pmMaxEnergy = " + pmMaxEnergy);
//            System.out.println("k = " + k);
//            //---------------------Debug------------------------------

            pmEnergy = k * pmMaxEnergy + (1 - k) * pmMaxEnergy * pmCpuUtil;
//            System.out.println("pm " + pmCount + ", energy = " + pmEnergy);
//            //---------------------Debug------------------------------
//            System.out.println("pmEnergy = " + pmEnergy);
//            //---------------------Debug------------------------------
            pmStatus[0] = pmCpuUsage;
            pmStatus[1] = pmMemUsage;
            pmStatus[2] = pmEnergy;
            pmStatusList.add(pmStatus);
            totalEnergy += pmEnergy;
        }
        ((DualPermutationChromosome)individual).setPmStatusList(pmStatusList);
//        System.out.println("pmSize = " + pmList.size() + ", totalEnergy = " + totalEnergy);
        return totalEnergy;

    }

    /**
     * Use FF to allocate VMs to PMs
     * @param actualUsageVm
     * @param vmList
     * @param pmList
     */
    private void allocateVMs(int[] actualUsageVm, ArrayList<ArrayList<Integer>> vmList,
                             ArrayList<ArrayList<ArrayList<Integer>>> pmList, int actualUsedVm){


        int globalVmCounter = 0;
        boolean noVmleft = false;

        // the pmVmList includes PMs,
        // each PM includes a list of VMs,
        // each VM is represented as int[] with two attributes,
        // [0] VM index, [1] VM type
        ArrayList<ArrayList<int[]>> pmVmList = new ArrayList<>();

//        System.out.println("actualUsageVm = ");
//        for(int i = 0; i < actualUsageVm.length; i++){
//            System.out.print(actualUsageVm[i] + " ");
//        }
//        System.out.println();
//        System.out.println("actualUsedVm : " + actualUsedVm);

        // When we still have VMs left
        while(!noVmleft){
            // create a new PM
            double currentPmCpu = pmCpu;
            double currentPmMem = pmMem;

            // create a pm
            ArrayList<ArrayList<Integer>> pm = new ArrayList<>();

            // a list of VM
            ArrayList<int[]> vms = new ArrayList<>();


            // start to allocate VMs
            for(;globalVmCounter < actualUsedVm; globalVmCounter++){

                // If we can allocate this VM to this PM, then add the vmList into PM
                if(currentPmCpu >= vmCpu[actualUsageVm[globalVmCounter]] &&
                   currentPmMem >= vmMem[actualUsageVm[globalVmCounter]]){
                    currentPmCpu -= vmCpu[actualUsageVm[globalVmCounter]];
                    currentPmMem -= vmMem[actualUsageVm[globalVmCounter]];
                    pm.add(vmList.get(globalVmCounter));

                    int[] vm = new int[2];
                    vm[0] = globalVmCounter;
                    vm[1] = actualUsageVm[globalVmCounter];
                    vms.add(vm);
                // Else this PM has been filled, break and start a new PM
                } else{
                    break;
                }
            }

            pmVmList.add(vms);
            pmList.add(pm);

            // Have we allocated all the VMs to PMs?
            if(globalVmCounter == actualUsedVm) noVmleft = true;
        } // end of While

        ((DualPermutationChromosome)individual).setPmList(pmVmList);


    }

    /**
     * This function simply does an "and" operation over the individual.vmTypes chromosome and the usedVM
     * to find out the real usage of VMs
     * @param individual
     * @param usedVm
     * @return
     */
    private int[] getRealUsageOfVms(DualPermutationChromosome individual, int[] usedVm, int actualUsedVm){
        int[] realUsageOfVms = new int[actualUsedVm];


        for(int i = 0; i < actualUsedVm; ++i) {
            realUsageOfVms[i] = individual.vmTypes[i];
        }
        return realUsageOfVms;
    }

    private int[] generateMapping(DualPermutationChromosome individual, ArrayList<ArrayList<Integer>> vmList){
        // 1 means we have used this VM, otherwise it is empty
        int[] vmUsed = new int[numOfVm];
        int globalCounter = 0;
        ArrayList<double[]> vmStatusList = new ArrayList<>();

        // Loop through the VM type array
        for(int j = 0; j < numOfVm; ++j){
            ArrayList<Integer> containerList = new ArrayList<>();
            double currentVmCpuLeft = vmCpu[individual.vmTypes[j]] - vmCpu[individual.vmTypes[j]] * vmCpuOverheadRate;
            double currentVmMemLeft = vmMem[individual.vmTypes[j]] - vmMemOverhead;
            double[] vmStatus = new double[2];

            // Loop through the container Permutation array
            // Add all the containers that have been allocated on this VM
            for(;globalCounter < numOfContainer; ++globalCounter){
                // first, we find out the real index of the container
                int indexOfContainer = individual.containerPermutation[globalCounter];

                if(currentVmCpuLeft >= taskCpu[indexOfContainer]
                        && currentVmMemLeft >= taskMem[indexOfContainer]){
                    currentVmCpuLeft -= taskCpu[indexOfContainer];
                    currentVmMemLeft -= taskMem[indexOfContainer];

                    // add the value of the container into the container List
                    containerList.add(indexOfContainer);
                    vmUsed[j] = 1;
                // else this vm has been filled, break and start a new VM
                }else{
                    vmStatus[0] = vmCpu[individual.vmTypes[j]] - currentVmCpuLeft;
                    vmStatus[1] = vmMem[individual.vmTypes[j]] - currentVmMemLeft;
                    vmStatusList.add(vmStatus);
                    break;
                }
            }

            // add this VM to the vmList
            vmList.add(containerList);
            // if we have loop through all the containers, then break from the loop
            if(globalCounter == numOfContainer) break;
        }

        setVmListToIndividual(individual, vmList);
        individual.setVmStatusList(vmStatusList);

        return vmUsed;
    }
}
