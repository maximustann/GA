package PermutationBasedGAForContainerAllocation;

import algorithms.Chromosome;
import algorithms.StdRandom;
import algorithms.TwoParentsCrossover;

public class DualPermutationPhenoCrossover implements TwoParentsCrossover{

    public DualPermutationPhenoCrossover(int seed){
        StdRandom.setSeed(seed);
    }


    @Override
    public Chromosome[] update(
            Chromosome father,
            Chromosome mother,
            double crossoverRate
            ){

        return update((DualPermutationChromosome) father, (DualPermutationChromosome) mother, crossoverRate);
    }


    public DualPermutationChromosome[] update(DualPermutationChromosome father,
                                              DualPermutationChromosome mother,
                                              double crossoverRate){

        int numOfContainer = father.getNumOfContainer();
        int numOfVm = father.getNumOfVm();
        DualPermutationChromosome[] children = new DualPermutationChromosome[2];

        // We implement two types of crossover for container permutation and VM types
        // The order 1 is designed special for permutation representation and a single point crossover is a common
        // operator
//        orderOneCrossover(father, mother, children, crossoverRate, numOfContainer, numOfVm);
//        singlePointCrossover(father, mother, children, crossoverRate, numOfContainer, numOfVm)

        if(StdRandom.uniform() > crossoverRate){
            children[0] = father.clone();
            children[1] = mother.clone();
            return children;
        }

        int startPoint = StdRandom.uniform(numOfContainer - 1);
        int endPoint = StdRandom.uniform(startPoint + 1, numOfContainer + 1);
        int cutPointOnVm = StdRandom.uniform(numOfVm);


        children[0] = new DualPermutationChromosome(father.cut(startPoint, endPoint, cutPointOnVm),
                                                    mother.cut(startPoint, endPoint, cutPointOnVm),
                                                    startPoint, endPoint, cutPointOnVm, numOfContainer, numOfVm);

        children[1] = new DualPermutationChromosome(mother.cut(startPoint, endPoint, cutPointOnVm),
                                                    father.cut(startPoint, endPoint, cutPointOnVm),
                                                    startPoint, endPoint, cutPointOnVm, numOfContainer, numOfVm);
        return children;
    }


}
