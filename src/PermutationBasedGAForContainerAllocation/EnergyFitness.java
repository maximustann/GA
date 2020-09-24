package PermutationBasedGAForContainerAllocation;

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
        ArrayList<VM> vmList = new ArrayList<>();
        // Create an empty list of PMs
        ArrayList<PM> pmList = new ArrayList<>();
        // Check which VM has been used, mark with 1, else 0,
        // the side effect is to fill the vmList and vmStatusList
        allocateContainers(vmList);
        allocateVMs(vmList, pmList);

        // Finally, we may calculate the energy based on the full solution of pmList
        energyConsumption = energy(pmList);
//        System.out.println("energy Consumption = " + energyConsumption);

        ((DualPermutationChromosome) individual).setFitness(energyConsumption);
        fit[0] = energyConsumption;
        fit[1] = 0;

        return fit;
    }

    private void allocateContainers(ArrayList<VM> vmList){
        int[] containers = ((DualPermutationChromosome)individual).containerPermutation;
        int[] vms = ((DualPermutationChromosome)individual).vmTypes;
        int containerCounter = 0;

        int numOfContainers = containers.length;
        for(int j = 0; j < vms.length; j++) {
            int vmType = vms[j];
            VM vm = new VM(vmType, vmCpu[vmType], vmMem[vmType], vmCpuOverheadRate, vmMemOverhead);
            boolean empty = true;
            for (; containerCounter < numOfContainers; containerCounter++) {
                int containerIndex = containers[containerCounter];
                // check if this container can be allocated to this VM
                if(vm.getCpuRemain() >= taskCpu[containerIndex] &&
                        vm.getMemRemain() >= taskMem[containerIndex]){
                    Container container = new Container(containerIndex, taskCpu[containerIndex], taskMem[containerIndex]);
                    vm.allocate(container);
                    empty = false;
                } else {
                    break;
                }
            }
            if(!empty){
                vmList.add(vm);
            }
            if(containerCounter == numOfContainers){
                break;
            }
        }
    }



    /**
     * This function should calculate the energy consumption of all pms
     * @param pmList
     * @return
     */
    private double energy(ArrayList<PM> pmList){
        double totalEnergy = 0;
        for(PM pm:pmList){
            totalEnergy += pm.calEnergy();
        }
        return totalEnergy;
//        System.out.println("pmSize = " + pmList.size() + ", totalEnergy = " + totalEnergy);
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
                PM pm = new PM(pmCpu, pmMem, k, pmMaxEnergy);
                pm.allocate(vm);
                pmList.add(pm);
            }

        }

        ((DualPermutationChromosome)individual).setPmList(pmList);
    }


}
