package cecGA.src;

import algorithms.InitPop;
import algorithms.StdRandom;

import java.util.ArrayList;
import java.util.HashSet;

public class CecGAInitialization implements InitPop{

    private double vmCpuOverheadRate;
    private double vmMemOverhead;
    private int numOfContainer;
    private int numOfVm;
    private int vmTypes;
    private double[] vmMem;
    private double[] vmCpu;
    private double[] taskCpu;
    private double[] taskMem;
    private double consolidationFactor;



    public CecGAInitialization(int numOfContainer,
                               int numOfVm,
                               int vmTypes,
                               int seed,
                               double[] vmCpu,
                               double[] vmMem,
                               double[] taskCpu,
                               double[] taskMem,
                               double vmCpuOverheadRate,
                               double vmMemOverhead,
                               double consolidationFactor
                                ){
        this.numOfContainer = numOfContainer;
        this.numOfVm = numOfVm;
        this.vmTypes = vmTypes;
        this.vmCpu = vmCpu;
        this.vmMem = vmMem;
        this.taskCpu = taskCpu;
        this.taskMem = taskMem;
        this.vmCpuOverheadRate = vmCpuOverheadRate;
        this.vmMemOverhead = vmMemOverhead;
        this.consolidationFactor = consolidationFactor;

        StdRandom.setSeed(seed);
    }


    @Override
    public CecGAChromosome[] init(
            int popSize,
            int maxVar,
            double lbound,
            double ubound
    ) {
        CecGAChromosome[] popVar = new CecGAChromosome[popSize];
        // initialize population
        for(int i = 0; i < popSize; i++){
            popVar[i] = generateChromosome();
        }
        return popVar;
    }

    // return the first VM that satisfied the container from existing VMs
    private Integer firstFit(ArrayList<double[]> vmList, double taskCpu, double taskMem){
        Integer suitableIndex = null;
        if(vmList.isEmpty()) return suitableIndex;
        for(int i = 0; i < vmList.size(); ++i){
            double[] vm = vmList.get(i);
            if(vm[0] >= taskCpu && vm[1] >= taskMem) return i;
        }
        return suitableIndex;
    }


    // return the first VM that statisfied the resource requirement
    private int minimumVm(double taskCpu, double taskMem){
        int index = 0;
        for(; index < vmTypes; ++index){
            if(vmCpu[index] - vmCpu[index] * vmCpuOverheadRate >= taskCpu &&
               vmMem[index] - vmMemOverhead >= taskMem) return index;
        }
        return index;
    }


    // Generate one individual
    private CecGAChromosome generateChromosome(){
        CecGAChromosome chromo = new CecGAChromosome(numOfContainer * 2);

        // double[0]: leftCpu, double[1]: leftMem, double[2]: vm type
        ArrayList<double[]> vmList = new ArrayList<>();

        // for each container
        for(int i = 0; i < numOfContainer; ++i){
            // First step, find the first VM that statisfied its requirement
            Integer suitableVm = firstFit(vmList, taskCpu[i], taskMem[i]);

            // allocate this container to this VM
            if(suitableVm != null){
                double[] vm = vmList.get(suitableVm);
                vm[0] -= taskCpu[i];
                vm[1] -= taskMem[i];
                chromo.individual[i * 2] = (int) vm[2];
                chromo.individual[i * 2 + 1] = suitableVm;
            // Else, no suitable VM exists, we create a new VM
            // First, we find a minimum VM type that can host this container
            // Second, we randomly generate a stronger type to host this container
            } else{
                suitableVm = minimumVm(taskCpu[i], taskMem[i]);
//                int generateVmType = StdRandom.uniform(suitableVm, vmTypes);
//                int generateVmType = StdRandom.uniform(suitableVm, vmTypes);
                int generateVmType = randomChooseStrongerVmType(suitableVm);

                double[] vm = new double[3];
                vm[0] = vmCpu[generateVmType] - vmCpu[generateVmType] * vmCpuOverheadRate - taskCpu[i];
                vm[1] = vmMem[generateVmType] - vmMemOverhead - taskMem[i];
                vm[2] = (double) generateVmType;
                vmList.add(vm);
                chromo.individual[i * 2] = (int) vm[2];
                chromo.individual[i * 2 + 1] = vmList.size() - 1;
            }
        }

        if(!validation(chromo)){
            throw(new IllegalStateException());
        }
        return chromo;
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


    private boolean validation(CecGAChromosome chromo){
        HashSet<Integer> vmSet = new HashSet<Integer>();
        int topNum = 0;
        for(int i = 0; i < numOfContainer; i++){
            vmSet.add(chromo.individual[i * 2 + 1]);
            if(topNum < chromo.individual[i * 2 + 1])
                topNum = chromo.individual[i * 2 + 1];
        }
        if(vmSet.size() != topNum + 1){
//            chromo.print();
            return false;
        }
        return true;

    }

}
