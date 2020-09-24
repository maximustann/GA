package multiDGA;

import GAProcedure.CommonGA;
import ProblemDefine.ParameterSettings;
import ProblemDefine.ProblemParameterSettings;
import algorithms.Chromosome;
import algorithms.TwoParentsCrossover;
import gaFactory.GAFactory;

import java.util.ArrayList;

public class DualPermutationGA extends CommonGA{
    GAFactory factory;
    ParameterSettings pars;
    ProblemParameterSettings proSet;

    /**
     * Constructor
     */

    public DualPermutationGA(ParameterSettings pars, ProblemParameterSettings proSet, GAFactory factory){
        this.factory = factory;
        this.pars = pars;
        this.proSet = proSet;
        prepare();
    }


    @Override
    public void run(int seed){
        collector.collectTime(0);
        initializeRand(seed);
        popVar = initPop.init(popSize, maxVar, lbound, ubound);

        ((ResultsCollector) collector).collectGenTime(0, 0);
        for(int i = 0; i < maxGen; i++) {
            int childrenCount = elitism.getSize();
            DualPermutationChromosome[] newPop = new DualPermutationChromosome[popSize];
            try {
                evaluate.evaluate(popVar, popFit);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            sort.sort(popVar, popFit);
            elitism.carryover(popVar, newPop);
            collector.collect(popVar[0].clone());


            while (true) {
                int exitFlag = 0;
                Chromosome father = popVar[selection.selected(popVar, popFit)];
                Chromosome mother = popVar[selection.selected(popVar, popFit)];

                Chromosome[] children = ((TwoParentsCrossover) crossover)
                        .update(father, mother, crossoverRate);


                for (int j = 0; j < children.length; j++) {
                    mutation.update(children[j], mutationRate);
                    newPop[childrenCount] = (DualPermutationChromosome) children[j];
                    childrenCount++;
                    if (childrenCount == popSize) {
                        exitFlag = 1;
                        break;
                    }
                }
                if (exitFlag == 1) break;
            }
            popVar = newPop;
            ((ResultsCollector) collector).collectGenTime(1, popFit.get(0)[0]);
        }
//        ((ResultsCollector) collector).collectFinalGen((DualPermutationChromosome[]) popVar);
        collector.collectTime(1);
    }

    @Override
    protected void prepare(){
        maxGen = pars.getMaxGen();
        maxVar = pars.getMaxVar();
        popSize = pars.getPopSize();
        mutationRate = pars.getMutationRate();
        crossoverRate = pars.getCrossoverRate();
        lbound = pars.getLbound();
        ubound = pars.getUbound();
        optimization = pars.getOptimization();
        tournamentSize = pars.getTournamentSize();
        popFit = new ArrayList<double[]>();

        initPop = factory.getInitPopMethod();
        mutation = factory.getMutation();
        crossover = factory.getCrossover();
        selection = factory.getSelection(tournamentSize, optimization);
        elitism = factory.getElitism(pars.getElitSize(), optimization);
        evaluate = proSet.getEvaluate();
        collector = factory.getDataCollector();
        sort = factory.getSort();
    }


}
