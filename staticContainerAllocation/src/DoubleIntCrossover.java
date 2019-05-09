import algorithms.Chromosome;
import algorithms.StdRandom;
import algorithms.TwoParentsCrossover;
import constants.Parts;

public class DoubleIntCrossover implements TwoParentsCrossover {

    @Override
    public Chromosome[] update(Chromosome father, Chromosome mother, double crossoverRate){
        return update((DoubleIntChromosome) father, (DoubleIntChromosome) mother, crossoverRate);
    }

    public DoubleIntChromosome[] update(
                                    DoubleIntChromosome father,
                                    DoubleIntChromosome mother,
                                    double crossoverRate){
        DoubleIntChromosome[] children = new DoubleIntChromosome[2];

        /* If random number greater than crossover rate. Do not crossover. */
        if(StdRandom.uniform() > crossoverRate) {
            children[0] = father.clone();
            children[1] = mother.clone();
            return children;
        }
        /* randomly choose cutPoints */
        int cutPointForContainer = StdRandom.uniform(father.getNumOfContainers());
        int cutPointForVm = StdRandom.uniform(father.getNumOfVms());


        /*
            Construct two bear-children
         */
        DoubleIntChromosome child1 = new DoubleIntChromosome(
                father.cut(cutPointForContainer, cutPointForVm, Parts.FIRST.getNum()),
                mother.cut(cutPointForContainer, cutPointForVm, Parts.SECOND.getNum()));
        DoubleIntChromosome child2 = new DoubleIntChromosome(
                mother.cut(cutPointForContainer, cutPointForVm, Parts.FIRST.getNum()),
                father.cut(cutPointForContainer, cutPointForVm, Parts.SECOND.getNum())
                );

        children[0] = child1;
        children[1] = child2;

        return children;
    }
}
