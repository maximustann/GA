package variableReplicasFirstStepGA;

import algorithms.Chromosome;
import algorithms.StdRandom;
import algorithms.TwoParentsCrossover;

import java.util.ArrayList;
import java.util.Arrays;


public class FirstStepGACrossover implements TwoParentsCrossover{


    public FirstStepGACrossover(){
    }


    @Override
    public Chromosome[] update(Chromosome father, Chromosome mother, double crossoverRate) {

        FirstStepGAChromosome children1 = update((FirstStepGAChromosome)father, (FirstStepGAChromosome) mother, crossoverRate);
        FirstStepGAChromosome children2 = update((FirstStepGAChromosome)mother, (FirstStepGAChromosome) father, crossoverRate);
        FirstStepGAChromosome[] children = new FirstStepGAChromosome[2];
        children[0] = children1;
        children[1] = children2;
        return children;
    }

    private FirstStepGAChromosome update(FirstStepGAChromosome father, FirstStepGAChromosome mother, double crossoverRate){
        double u = StdRandom.uniform();
        if(u <= crossoverRate) {
            int cutPoint = StdRandom.uniform(father.size() - 1);
            FirstStepGAChromosome child = new FirstStepGAChromosome(father, mother, cutPoint);
            return child;
        }
        return (FirstStepGAChromosome) father.clone();
    }
}
