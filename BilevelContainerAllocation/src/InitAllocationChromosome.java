import algorithms.*;
import commonRepresentation.IntValueChromosome;
import java.lang.*;

public class InitAllocationChromosome implements InitPop{

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
        int containerNum = (int) Math.sqrt((double) maxVar);
        for(int i = 0; i < popSize; i++){
            popVar[i] = generateChromosome(containerNum);
        }
        return popVar;

    }

    private IntValueChromosome generateChromosome(int containerNum){
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
