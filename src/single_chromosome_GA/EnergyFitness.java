package single_chromosome_GA;

import algorithms.Chromosome;
import algorithms.UnNormalizedFit;

import java.util.ArrayList;

public class EnergyFitness extends UnNormalizedFit {

    private static double k;
    private static int numOfContainers;
    private static double pmCpu;
    private static double pmMem;
    private static double pmEnergy;
    private static int vmTypes;
    private static double[] vmCpu;
    private static double[] vmMem;
    private static double[] taskCpu;
    private static double[] taskMem;
    private static double vmCpuOverheadRate;
    private static double vmMemOverhead;


    public EnergyFitness(
            int numOfContainers,
            int vmTypes,
            double k,
            double pmCpu,
            double pmMem,
            double pmEnergy,
            double[] vmCpu,
            double[] vmMem,
            double[] taskCpu,
            double[] taskMem,
            double vmCpuOverheadRate,
            double vmMemOverhead
    ){
        super(null);
        this.k = k;
        this.vmTypes = vmTypes;
        this.numOfContainers = numOfContainers;
        this.pmCpu = pmCpu;
        this.pmMem = pmMem;
        this.pmEnergy = pmEnergy;
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

    public Object call() throws Exception {
        double [] fit = new double[2];
        fit[0] = decoding((SingleChromosome) individual);
        fit[1] = 0;
        ((SingleChromosome) individual).setFitness(fit[0]);
        return fit;
    }

    public double decoding(SingleChromosome individual){
        double energy = 0;

        int previousVMIndex = 0;
        int[] allocationVector = individual.getAllocationVector();
        ArrayList<VM> vmList = new ArrayList<>();
        ArrayList<PM> pmList = new ArrayList<>();

        for(int i = 0; i < numOfContainers; i++){
            Container container = new Container(i, taskCpu[i], taskMem[i]);

            int vmType = allocationVector[i * 2];
            int vmIndex = allocationVector[i * 2 + 1];

            // we haven't start allocating
            if(vmList.isEmpty()){
                VM vm = new VM(vmType, vmCpu[vmType], vmMem[vmType],
                                vmCpuOverheadRate, vmMemOverhead);
                PM pm = new PM(pmCpu, pmMem, k, pmEnergy);

                vmList.add(vm);
                pmList.add(pm);
                pm.allocate(vm);
                vm.allocate(container);
                continue;
            }

            // we have allocated this VM.
            if(vmIndex == previousVMIndex){
                // Find the last VM and allocate the container to it.
                VM vm = vmList.get(vmList.size() - 1);
                vm.allocate(container);
                continue;
            }else{
                // create the VM
                VM vm = new VM(vmType, vmCpu[vmType], vmMem[vmType],
                        vmCpuOverheadRate, vmMemOverhead);
                PM pm = pmList.get(pmList.size() - 1);
                if(pm.getCpuRemain() >= vmCpu[vmType]
                        && pm.getMemRemain() >= vmMem[vmType]){
                    pm.allocate(vm);
                } else{
                  pm = new PM(pmCpu, pmMem, k, pmEnergy);
                  pm.allocate(vm);
                  pmList.add(pm);
                }
                vmList.add(vm);
            }

        }

        double cpuUtil = 0;
        double memUtil = 0;

        for(PM pm:pmList){
            energy += pm.calEnergy();
            cpuUtil += pm.getCpuUtil();
            memUtil += pm.getMemUtil();
        }

        cpuUtil /= pmList.size();
        memUtil /= pmList.size();


        double totalWasted = 0;
        for(PM pm:pmList){
            totalWasted += pm.getWastedResource();
        }

        System.out.println("pmList.size: " + pmList.size());
        individual.setAveWasted(totalWasted / pmList.size());
        individual.setNumOfPM(pmList.size());
        individual.setNumOfVM(vmList.size());
        individual.setAveCpuUtil(cpuUtil);
        individual.setAveMemUtil(memUtil);
        return energy;
    }

    /**
     *
     * keep 5 decimal points
     * @param number
     * @return
     */
    private double round5(double number){
        number = Math.floor(number * 100000) / 100000;
        return number;
    }

}
