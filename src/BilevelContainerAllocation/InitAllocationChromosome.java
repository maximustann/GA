package BilevelContainerAllocation;

import algorithms.*;
import commonRepresentation.IntValueChromosome;

public class InitAllocationChromosome implements InitPop {
    private int containerNum;

    public InitAllocationChromosome(int containerNum){
        this.containerNum = containerNum;
    }

    public IntValueChromosome[] init(
            int popSize,
            int maxVar,
            double lbound,
            double ubound
    ){
        IntValueChromosome[] popVar = new IntValueChromosome[popSize];

        /** initialize population
         *
         * The key part of the initialization is that
         * for every row, there must be only one deployment
         *
         */
        for(int i = 0; i < popSize; i++){
            popVar[i] = generateChromosome();
        }
        return popVar;

    }

    private IntValueChromosome generateChromosome(){
        // the length of an individual equals the square of the number of containers
        IntValueChromosome chromo = new IntValueChromosome(containerNum * containerNum);

        // change the individual to matrix form
        chromo.toMatrix(containerNum);

        for(int rowNum = 0; rowNum < containerNum; rowNum++){
            // For every row, randomly change an index to 1
            int allocation = StdRandom.uniform(containerNum);
            chromo.matrixIndividual[rowNum][allocation] = 1;
            chromo.synchronizeMatrixToVector();
        }

        return chromo;
    }

}
