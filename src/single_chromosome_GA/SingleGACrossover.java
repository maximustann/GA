package single_chromosome_GA;

import algorithms.Chromosome;
import algorithms.StdRandom;
import algorithms.TwoParentsCrossover;

public class SingleGACrossover implements TwoParentsCrossover {

    private double vmCpuOverheadRate;
    private double vmMemOverhead;
    private int numOfContainer;
    private int vmTypes;
    private double[] vmMem;
    private double[] vmCpu;
    private double[] taskCpu;
    private double[] taskMem;

    public SingleGACrossover(
            int numOfContainer,
            int vmTypes,
            int seed,
            double[] vmCpu,
            double[] vmMem,
            double[] taskCpu,
            double[] taskMem,
            double vmCpuOverheadRate,
            double vmMemOverhead
    ){
        this.numOfContainer = numOfContainer;
        this.vmTypes = vmTypes;
        this.vmCpu = vmCpu;
        this.vmMem = vmMem;
        this.taskCpu = taskCpu;
        this.taskMem = taskMem;
        this.vmCpuOverheadRate = vmCpuOverheadRate;
        this.vmMemOverhead = vmMemOverhead;
        StdRandom.setSeed(seed);
    }

    @Override
    public Chromosome[] update(
            Chromosome father,
            Chromosome mother,
            double crossoverRate
            ){
        Chromosome[] children = new Chromosome[2];
//        children[1] = mother.clone();
        if(StdRandom.uniform() > crossoverRate){
            children[0] = father.clone();
            children[1] = mother.clone();
            return children;
        }

        int cutPoint = StdRandom.uniform(father.size());
        SingleChromosome child1 = new SingleChromosome(father.size());
        SingleChromosome child2 = new SingleChromosome(father.size());


        int[] vec1 = new int[father.size() * 2];
        int[] vec2 = new int[father.size() * 2];
        int[] fatherVec = ((SingleChromosome) father).getAllocationVector();
        int[] motherVec = ((SingleChromosome) mother).getAllocationVector();
        for(int i = 0; i < cutPoint; i++){
            vec1[i * 2] = fatherVec[i * 2];
            vec1[i * 2 + 1] = fatherVec[i * 2 + 1];

            vec2[i * 2] = motherVec[i * 2];
            vec2[i * 2 + 1] = motherVec[i * 2 + 1];
        }
        for(int i = cutPoint; i < father.size(); i++){
            vec1[i * 2] = motherVec[i * 2];
            vec1[i * 2 + 1] = motherVec[i * 2 + 1];

            vec2[i * 2] = fatherVec[i * 2];
            vec2[i * 2 + 1] = fatherVec[i * 2 + 1];
        }

        child1.setAllocationVector(vec1);
        child2.setAllocationVector(vec2);

        children[0] = child1;
        children[1] = child2;

        adjustVMIndexes((SingleChromosome) children[0]);
        adjustVMIndexes((SingleChromosome) children[1]);

        return children;
    }
    private void adjustVMIndexes(SingleChromosome individual){
        int[] vector = individual.getAllocationVector();
        int previousVMIndex = 0;
        VM previousVM = new VM(vector[1], vmCpu[vector[1]], vmMem[vector[1]],
                vmCpuOverheadRate, vmMemOverhead);
        for(int i = 0; i < numOfContainer; i++){
            Container container = new Container(i, taskCpu[i], taskMem[i]);
            // If the type is repeated
            if(previousVM.getType() == vector[i * 2]){
                // If we cannot allocate the container to this VM
                if(previousVM.getCpuRemain() < taskCpu[i] ||
                        previousVM.getMemRemain() < taskMem[i]){
                    vector[i * 2 + 1] = ++previousVMIndex;
                    previousVM = new VM(vector[i * 2], vmCpu[vector[i * 2]], vmMem[vector[i * 2]],
                            vmCpuOverheadRate, vmMemOverhead);
                    previousVM.allocate(container);
                } else{
                    previousVM.allocate(container);
                    vector[i * 2 + 1] = previousVMIndex;
                }
                // else we simply create a new VM
            }else{
                previousVM = new VM(vector[i * 2], vmCpu[vector[i * 2]], vmMem[vector[i * 2]],
                        vmCpuOverheadRate, vmMemOverhead);
                vector[i * 2 + 1] = ++previousVMIndex;
                previousVM.allocate(container);
            }
        }
    }

}
