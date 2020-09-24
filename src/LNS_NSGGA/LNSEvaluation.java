package LNS_NSGGA;

import LNS_FF.LNSChromosome;
import cloudResourceUnit.VM;


public class LNSEvaluation {

    private double pmCpu;
    private double pmMem;

    public LNSEvaluation(DataPack dataPack){
        this.pmCpu = dataPack.getPmCpu();
        this.pmMem = dataPack.getPmMem();
    }

    public double evaluate(LNSChromosome solution){
        double fitness = 0;
//        fitness = solution.getVmList().size();
//        fitness = solution.getVmList()
        for(VM vm:solution.getVmList()){
//            fitness += vm.getWastedResource();
            double wastedCpu = vm.getCpuRemain() + vm.getCpuOverheadRate() * vm.getConfigureCpu();
            double wastedMem = (vm.getMemRemain() + vm.getMemOverhead()) / pmMem;
            double wastedResource = wastedCpu < wastedMem ? wastedCpu:wastedMem;
            fitness += wastedResource;
        }
        solution.setFitness(fitness);
        return fitness;
    }
}
