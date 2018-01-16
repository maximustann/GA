import algorithms.InitPop;
import algorithms.Mutation;
import commonOperators.BinaryFlipCoinMutation;
import commonOperators.InitIntChromosomes;
import commonOperators.IntReverseSequenceMutation;
import dataCollector.DataCollector;
import gaFactory.IntCoGAFactory;

public class BilevelFactory extends IntCoGAFactory{

    public BilevelFactory(DataCollector collector, int numOfSubPop){
        super(collector, numOfSubPop);
    }

    @Override
    public InitPop[] getInitPopMethod(){
        InitPop initContainerVM = new InitAllocationChromosome();
        InitPop initVMPM = new InitAllocationChromosome();
        InitPop initVmTypes = new InitIntChromosomes();
        InitPop[] initPops = {initContainerVM, initVMPM, initVmTypes};
        return initPops;
    }

    @Override
    public Mutation[] getMutation() {
        Mutation mutateContainerVm = new BinaryFlipCoinMutation();
        Mutation mutateVMPM = new BinaryFlipCoinMutation();
        Mutation mutateTypes = new IntReverseSequenceMutation();
        Mutation[] mutations = {mutateContainerVm, mutateVMPM, mutateTypes};
        return mutations;
    }
}
