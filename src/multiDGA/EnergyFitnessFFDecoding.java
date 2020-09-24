package multiDGA;
import algorithms.Chromosome;
import algorithms.UnNormalizedFit;

import java.util.ArrayList;
import cloudResourceUnit.*;
public class EnergyFitnessFFDecoding extends UnNormalizedFit{

    private static int numOfContainer;
    private static int numOfVm;
    private static double k;
    private static double pmCpu;
    private static double pmMem;
    private static double pmMaxEnergy;
    private static double crushPro;
    private static double[] vmCpu;
    private static double[] vmMem;
    private static double[] taskCpu;
    private static double[] taskMem;
    private static double[] taskAppId;
    private static double[] taskMicroId;
    private static double[] taskReplicaId;
    private static double vmCpuOverheadRate;
    private static double vmMemOverhead;

    // Constructor 1
    public EnergyFitnessFFDecoding(int numOfContainer,
                                   int numOfVm,
                                   double k,
                                   double pmCpu,
                                   double pmMem,
                                   double pmEnergy,
                                   double crushPro,
                                   double vmCpuOverheadRate,
                                   double vmMemOverhead,
                                   double[] vmCpu,
                                   double[] vmMem,
                                   double[] taskCpu,
                                   double[] taskMem,
                                   double[] taskAppId,
                                   double[] taskMicroId,
                                   double[] taskReplicaId){
        super(null);

        this.numOfContainer = numOfContainer;
        this.numOfVm = numOfVm;
        this.k = k;
        this.pmCpu = pmCpu;
        this.pmMem = pmMem;
        this.pmMaxEnergy = pmEnergy;
        this.crushPro = crushPro;
        this.vmCpu = vmCpu;
        this.vmMem = vmMem;
        this.taskCpu = taskCpu;
        this.taskMem = taskMem;
        this.taskAppId = taskAppId;
        this.taskMicroId = taskMicroId;
        this.taskReplicaId = taskReplicaId;
        this.vmCpuOverheadRate = vmCpuOverheadRate;
        this.vmMemOverhead = vmMemOverhead;

    }


    public EnergyFitnessFFDecoding(Chromosome individual){
        super(individual);
    }
    @Override
    public Object call() throws Exception {
        double[] fit = new double[2];

        Double energyConsumption = 0.0;
        // Create an empty list of VMs
        ArrayList<VM> vmList = new ArrayList<>();
        // Create an empty list of PMs
        ArrayList<PM> pmList = new ArrayList<>();
        // Check which VM has been used, mark with 1, else 0,
        // the side effect is to fill the vmList and vmStatusList
//        int[] usedVm = generateMapping((DualPermutationChromosome) individual, vmList);
//        generateMappingFF((DualPermutationChromosome) individual, vmList);
        allocateContainers(vmList);
        allocateVMs(vmList, pmList);

        // Now, allocate VMs to PMs to construct the full solution
//        allocateVMsFF(vmList, pmList, ((DualPermutationChromosome) individual).vmTypes);

        // Finally, we may calculate the energy based on the full solution of pmList
        energyConsumption = energy(vmList, pmList);
//        System.out.println("energy Consumption = " + energyConsumption);

        ((DualPermutationChromosome) individual).setFitness(energyConsumption);
        ((DualPermutationChromosome) individual).averagePmCpuUtil();
        ((DualPermutationChromosome) individual).averagePmMemUtil();
        fit[0] = energyConsumption;
        fit[1] = 0;

        return fit;
    }

    // turn arrayList into array, and assign to an individual
//    private void setVmListToIndividual(DualPermutationChromosome individual, ArrayList<ArrayList<Integer>> vmList){
//        ArrayList<int[]> subVmList = new ArrayList<>();
//        for(int i = 0; i < vmList.size(); i++){
//            ArrayList<Integer> vm = vmList.get(i);
//            int[] vmContainer = new int[vm.size()];
//            for(int j = 0; j < vm.size(); j++){
//                vmContainer[j] = vm.get(j);
//            }
//            subVmList.add(vmContainer);
//        }
//
//        individual.setVmList(subVmList);
//    }


    private double energy(ArrayList<VM> vmList, ArrayList<PM> pmList){
        double totalEnergy = 0;
        for(PM pm:pmList){
            totalEnergy += pm.calEnergy();
        }
        return totalEnergy;
    }


    private void allocateContainers(ArrayList<VM> vmList){
        int[] containers = ((DualPermutationChromosome)individual).containerPermutation;
        int[] vms = ((DualPermutationChromosome)individual).vmTypes;
        int vmCounter = 0;

        int numOfContainers = containers.length;
        for(int i = 0; i < numOfContainers; i++){
            int containerIndex = containers[i];
            Container container = new Container(containerIndex, taskCpu[containerIndex], taskMem[containerIndex],
                    (int) taskAppId[containerIndex], (int) taskMicroId[containerIndex], (int) taskReplicaId[containerIndex]);
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
                    int vmType = vms[vmCounter];
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
    }
    /**
     * Use FF to allocate VMs to PMs
     * @param vmList
     * @param pmList
     */
    private void allocateVMs(ArrayList<VM> vmList,
                             ArrayList<PM> pmList){

        for(VM vm:vmList){
            boolean allocated = false;
            for(PM pm:pmList){
                if(pm.getCpuRemain() >= vm.getConfigureCpu()
                        && pm.getMemRemain() >= vm.getConfigureMem()){
                    pm.allocate(vm);
                    allocated = true;
                    break;
                }
            }

            if(!allocated){
                PM pm = new PM(pmCpu, pmMem, k, pmMaxEnergy, crushPro);
                pm.allocate(vm);
                pmList.add(pm);
            }

        }

        ((DualPermutationChromosome)individual).setPmList(pmList);
    }

//    private void allocateVMsFF(ArrayList<ArrayList<Integer>> vmList,
//                             ArrayList<ArrayList<ArrayList<Integer>>> pmList, int[] vmTypes){
//
//
//        boolean noVmleft = false;
//
//        // the pmVmList includes PMs,
//        // each PM includes a list of VMs,
//        // each VM is represented as int[] with two attributes,
//        // [0] VM index, [1] VM type
//        ArrayList<ArrayList<int[]>> pmVmList = new ArrayList<>();
//        ArrayList<double[]> pmRemain = new ArrayList<>();
//        int[] vmAllocated = new int[vmList.size()];
//        int actualUsedVm = vmList.size();
//        int vmCounter = 0;
//
//        // When we still have VMs left
//        while(!noVmleft){
//            // create a new PM
//            double currentPmCpu = pmCpu;
//            double currentPmMem = pmMem;
//            double[] remain = new double[2];
//
//            // create a pm
//            ArrayList<ArrayList<Integer>> pm = new ArrayList<>();
//
//            // a list of VM
//            ArrayList<int[]> vms = new ArrayList<>();
//
//
//            // start to allocate VMs
//            for(int globalVmCounter = 0; globalVmCounter < actualUsedVm; globalVmCounter++){
//
//                // If the VM has been allocated
//                if(vmAllocated[globalVmCounter] == 1) continue;
//
//                // If we can allocate this VM to this PM, then add the vmList into PM
//                if(currentPmCpu >= vmCpu[vmTypes[globalVmCounter]] &&
//                        currentPmMem >= vmMem[vmTypes[globalVmCounter]]){
//                    currentPmCpu -= vmCpu[vmTypes[globalVmCounter]];
//                    currentPmMem -= vmMem[vmTypes[globalVmCounter]];
//                    pm.add(vmList.get(globalVmCounter));
//
//                    int[] vm = new int[2];
//                    vm[0] = globalVmCounter;
//                    vm[1] = vmTypes[globalVmCounter];
//                    vms.add(vm);
//                    vmCounter++;
//
//                    // mark the VM as allocated
//                    vmAllocated[globalVmCounter] = 1;
//                }
//            }
//
//            remain[0] = currentPmCpu;
//            remain[1] = currentPmMem;
//            pmVmList.add(vms);
//            pmList.add(pm);
//            pmRemain.add(remain);
//
//            if(vmCounter == actualUsedVm)
//                noVmleft = true;
//
//        } // end of While
//
//        ((DualPermutationChromosome)individual).setPmList(pmVmList);
//        ((DualPermutationChromosome)individual).setPmRemainList(pmRemain);
//    }


//    private void generateMappingFF(DualPermutationChromosome individual, ArrayList<ArrayList<Integer>> vmList){
//        int[] allocatedContainers = new int[numOfContainer];
//        int globalCounter = 0;
//        ArrayList<double[]> vmStatusList = new ArrayList<>();
//        ArrayList<double[]> vmUtilList = new ArrayList<>();
//        for(int i = 0; i < numOfVm; i++){
//            ArrayList<Integer> containerList = new ArrayList<>();
//            double currentVmCpuLeft = vmCpu[individual.vmTypes[i]] - vmCpu[individual.vmTypes[i]] * vmCpuOverheadRate;
//            double currentVmMemLeft = vmMem[individual.vmTypes[i]] - vmMemOverhead;
//            double[] vmStatus = new double[2];
//            double[] vmUtil = new double[2];
//
//            for(int j = 0; j < numOfContainer; j++){
//                if(allocatedContainers[j] == 1) continue;
//                int indexOfContainer = individual.containerPermutation[j];
//                if (currentVmCpuLeft >= taskCpu[indexOfContainer] && currentVmMemLeft >= taskMem[indexOfContainer]) {
//                    currentVmCpuLeft -= taskCpu[indexOfContainer];
//                    currentVmMemLeft -= taskMem[indexOfContainer];
//                    containerList.add(j);
//                    allocatedContainers[j] = 1;
//                    globalCounter++;
//                }
//           }
//           vmStatus[0] = vmCpu[individual.vmTypes[i]] - currentVmCpuLeft;
//           vmStatus[1] = vmMem[individual.vmTypes[i]] - currentVmMemLeft;
//           vmUtil[0] = vmStatus[0] / vmCpu[individual.vmTypes[i]];
//           vmUtil[1] = vmStatus[1] / vmMem[individual.vmTypes[i]];
//           vmStatusList.add(vmStatus);
//           vmUtilList.add(vmUtil);
//            // add this VM to the vmList
//           vmList.add(containerList);
//           if(globalCounter == numOfContainer)
//               break;
//        }
//
//        setVmListToIndividual(individual, vmList);
//        individual.setVmStatusList(vmStatusList);
//        individual.setVmUtilList(vmUtilList);
//    }
}
